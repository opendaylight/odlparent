/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opendaylight.odlparent;

import com.google.common.annotations.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.opendaylight.odlparent.karafutil.CustomBundleUrlStreamHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo populating the local repository by delegating to Aether.
 */
@Mojo(name = "populate-local-repo", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
// URL.setURLStreamHandlerFactory throws an Error directly, so we can’t do any better than this...
@SuppressWarnings("checkstyle:IllegalCatch")
public class PopulateLocalRepoMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(PopulateLocalRepoMojo.class);

    static {
        // Static initialization, as we may be invoked multiple times
        // karaf-maven-plugin defines its own URLStreamHandlerFactory for install-kars, so we may find a factory
        // already defined (but it handles "mvn:" and "wrap:mvn:" so we should be OK)
        try {
            URL.setURLStreamHandlerFactory(new CustomBundleUrlStreamHandlerFactory());
        } catch (Error e) {
            LOG.warn("populate-local-repo: URL factory is already defined", e);
        }
    }

    /**
     * The Maven project being built.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * The entry point to Aether, i.e. the component doing all the work.
     */
    @Inject
    private RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     */
    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repoSession;

    /**
     * The project's remote repositories to use for the resolution of plugins and their dependencies.
     */
    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
    private List<RemoteRepository> remoteRepos;

    /**
     * The local repository to use for the resolution of plugins and their dependencies.
     */
    @Parameter
    private File localRepo;

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    public void execute() throws MojoExecutionException {

        AetherUtil aetherUtil = new AetherUtil(repoSystem, repoSession, remoteRepos, localRepo);
        FeatureUtil featureUtil = new FeatureUtil(aetherUtil, localRepo);
        try {
            final var startupArtifacts = readStartupProperties(aetherUtil);
            aetherUtil.installArtifacts(startupArtifacts);
            final var featureArtifacts = new LinkedHashSet<Artifact>();
            final var featureRepos = new LinkedHashSet<Features>();
            readFeatureCfg(aetherUtil, featureUtil, featureArtifacts, featureRepos);
            featureArtifacts.addAll(
                aetherUtil.resolveDependencies(MvnToAetherMapper.toAether(project.getDependencies()),
                    new KarafFeaturesDependencyFilter()));
            featureRepos.addAll(featureUtil.readFeatures(featureArtifacts));
            // Do not provide FeatureUtil.featuresRepositoryToCoords(features)) as existingCoords
            // to findAllFeaturesRecursively, as those coords are not resolved yet, and it would lead to Bug 6187.
            featureRepos.addAll(featureUtil.findAllFeaturesRecursively(featureRepos));

            featureRepos.stream().map(Features::getName).sorted().distinct()
                .forEach(name -> LOG.info("Feature repository discovered recursively: {}", name));

            // Remove blacklisted features
            final var blackListedFeatures = getBlackListedFeatures();
            final Set<Features> cleanedRepos;
            if (!blackListedFeatures.isEmpty()) {
                cleanedRepos = removeBlackListedFeatures(featureRepos, blackListedFeatures);
            } else {
                cleanedRepos = featureRepos;
            }
            final var artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(cleanedRepos));
            artifacts.addAll(featureArtifacts);
            featureUtil.removeLocalArtifacts(artifacts);

            final var gaceVersions = new HashMap<Gace, String>();
            for (var artifact : artifacts) {
                LOG.debug("Artifact to be installed: {}", artifact);
                Gace gace = new Gace(artifact);
                String duplicate = gaceVersions.putIfAbsent(gace, artifact.getVersion());
                if (duplicate != null && !duplicate.equals(artifact.getVersion())) {
                    LOG.warn("Duplicate versions for {}, {} and {}", gace, duplicate, artifact.getVersion());
                }
            }
            if (localRepo != null) {
                aetherUtil.installArtifacts(artifacts);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute", e);
        }
    }

    /**
     * Read blacklisted features
     *
     * <p>Read features which are enclosed by {@snippet <blacklisted>} tag in maven configuration.
     * Ignore those entries which are blank.
     *
     * @return {@code List} of non-blank blacklisted features
     */
    @VisibleForTesting
    List<String> getBlackListedFeatures() {
        final var plugin = project.getPlugin("org.apache.karaf.tooling:karaf-maven-plugin");
        if (plugin == null) {
            return List.of();
        }
        final var configuration = (Xpp3Dom) plugin.getConfiguration();
        if (configuration == null) {
            return List.of();
        }
        final var blacklistedFeatures = configuration.getChild("blacklistedFeatures");
        if (blacklistedFeatures == null) {
            return List.of();
        }
        return Stream.of(blacklistedFeatures.getChildren())
            .map(Xpp3Dom::getValue)
            .filter(blackListedFeature -> blackListedFeature != null && !blackListedFeature.isBlank())
            .toList();
    }

    /**
     * Remove blacklisted feature repositories and features
     *
     * <p>First filter feature repositories by removing those which are blacklisted.
     * Then clean features of all remaining repositories.
     *
     * @param featureRepos All feature repositories we depend on
     * @param blackListedFeatures List of blacklisted feature repos/features names
     * @return Filtered and cleaned collection of feature repositories
     */
    @VisibleForTesting
    Set<Features> removeBlackListedFeatures(final Set<Features> featureRepos, final List<String> blackListedFeatures) {
        final var featureComparators = blackListedFeatures.stream()
            .map(FeatureComparator::new)
            .toList();

        final var result = new HashSet<Features>();
        for (final var featureRepo : featureRepos) {
            if (!featureRepoMatch(featureComparators, featureRepo.getName())) {
                // Remove Feature objects from the feature list directly because there is no way to modify
                // the feature list or create Features as new objects.
                featureRepo.getFeature()
                    .removeIf(feature -> featureMatch(featureComparators, feature.getName(), featureRepo.getName()));
                result.add(featureRepo);
            }
        }
        return Set.copyOf(result);
    }

    private static boolean featureRepoMatch(final List<FeatureComparator> comparators, final String featureRepoName) {
        final var match = comparators.stream()
            .anyMatch(featureComparator -> featureComparator.compare(featureRepoName));
        if (match) {
            LOG.info("The whole feature repository: {} is blacklisted, selected for removal from local repo",
                featureRepoName);
        }
        return match;
    }

    private static boolean featureMatch(final List<FeatureComparator> comparators, final String featureName,
            final String featureRepoName) {
        final var match = comparators.stream()
            .anyMatch(featureComparator -> featureComparator.compare(featureName));
        if (match) {
            LOG.info("Feature: {} from repository: {} is blacklisted, selected for removal from local repo",
                featureName, featureRepoName);
        }
        return match;
    }

    private void readFeatureCfg(final AetherUtil aetherUtil, final FeatureUtil featureUtil,
            final Set<Artifact> artifacts, final Set<Features> features) {
        // Create file structure
        final var karafHome = localRepo.getParentFile().toPath();
        final var karafEtc = karafHome.resolve("etc");
        final var file = karafEtc.resolve("org.apache.karaf.features.cfg");

        final var prop = new Properties();
        try (var is = Files.newInputStream(file)) {
            prop.load(is);

            // Note this performs path seaparator translation
            final var karafHomePath = karafHome.toUri().getPath();
            final var karafEtcPath = karafEtc.toUri().getPath();
            final var featuresRepositories = prop.getProperty("featuresRepositories");
            for (var mvnUrl : featuresRepositories.split(",")) {
                final var fixedUrl = mvnUrl
                        .replace("${karaf.home}", karafHomePath)
                        .replace("${karaf.etc}", karafEtcPath);
                if (fixedUrl.startsWith("file:")) {
                    try {
                        // Local feature file
                        features.add(featureUtil.readFeature(Path.of(new URI(fixedUrl)).toFile()));
                    } catch (URISyntaxException e) {
                        throw new IllegalArgumentException("Could not resolve URI: " + fixedUrl, e);
                    }
                } else {
                    artifacts.add(aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(fixedUrl))));
                }
            }
        } catch (FileNotFoundException e) {
            LOG.info("Could not find properties file: {}", file.toAbsolutePath(), e);
        } catch (IOException e) {
            LOG.info("Could not read properties file: {}", file.toAbsolutePath(), e);
        }
    }

    private Set<Artifact> readStartupProperties(final AetherUtil aetherUtil) {
        final var artifacts = new LinkedHashSet<Artifact>();
        final var file = localRepo.toPath().resolveSibling("etc").resolve("startup.properties");
        final var prop = new Properties();
        try (var is = Files.newInputStream(file)) {
            prop.load(is);
            final var mvnUrls = prop.keys();
            while (mvnUrls.hasMoreElements()) {
                final var mvnUrl = (String) mvnUrls.nextElement();
                final var artifact = aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(mvnUrl)));
                artifacts.add(artifact);
            }
        } catch (FileNotFoundException e) {
            LOG.info("Could not find properties file: {}", file.toAbsolutePath(), e);
        } catch (IOException e) {
            LOG.info("Could not read properties file: {}", file.toAbsolutePath(), e);
        }

        return artifacts;
    }

    private static final class FeatureComparator {
        private final String name;
        private final Pattern pattern;
        private final VersionRange versionRange;

        private FeatureComparator(final String featureName) {
            String nameResult = featureName;
            VersionRange versionRangeResult = null;
            Pattern patternResult = null;
            try {
                if (featureName.contains("[")) {
                    nameResult = featureName.substring(0, featureName.indexOf('['));
                    final var featureRange = featureName.substring(featureName.indexOf('['));
                    versionRangeResult = VersionRange.createFromVersionSpec(featureRange);
                } else if (featureName.contains("(")) {
                    nameResult = featureName.substring(0, featureName.indexOf('('));
                    final var featureRange = featureName.substring(featureName.indexOf('('));
                    versionRangeResult = VersionRange.createFromVersionSpec(featureRange);
                } else if (featureName.contains("*")) {
                    patternResult = Pattern.compile(featureName.replace("*", ".*"));
                }
            } catch (InvalidVersionSpecificationException e) {
                // Ignored, FeatureComparator will use name instead.
                LOG.warn("Failed to do prepare version range with {}, ignoring it", featureName, e);
            }
            name = nameResult;
            pattern = patternResult;
            versionRange = versionRangeResult;
        }

        public boolean compare(final String value) {
            if (versionRange != null) {
                if (value.startsWith(name)) {
                    final var valueVersionRange = value.substring(name.length());
                    return versionRange.containsVersion(new DefaultArtifactVersion(valueVersionRange));
                } else {
                    return false;
                }
            } else if (pattern != null) {
                return pattern.matcher(value).matches();
            } else {
                return name.equals(value);
            }
        }
    }
}
