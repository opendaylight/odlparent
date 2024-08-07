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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
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
    private static final String INCLUDE_JAR = ".include.jar";

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
    @Component
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
            Set<Artifact> startupArtifacts = readStartupProperties(aetherUtil);
            aetherUtil.installArtifacts(startupArtifacts);
            Set<Artifact> featureArtifacts = new LinkedHashSet<>();
            final Set<Features> featureRepos = new LinkedHashSet<>();
            readFeatureCfg(aetherUtil, featureUtil, featureArtifacts, featureRepos);
            featureArtifacts.addAll(
                aetherUtil.resolveDependencies(MvnToAetherMapper.toAether(project.getDependencies()),
                    new KarafFeaturesDependencyFilter()));
            featureRepos.addAll(featureUtil.readFeatures(featureArtifacts));
            // Do not provide FeatureUtil.featuresRepositoryToCoords(features)) as existingCoords
            // to findAllFeaturesRecursively, as those coords are not resolved yet, and it would lead to Bug 6187.
            featureRepos.addAll(featureUtil.findAllFeaturesRecursively(featureRepos));
            for (final Features featureRepo : featureRepos) {
                LOG.info("Feature repository discovered recursively: {}", featureRepo.getName());
            }
            final var includeJar = getIncludeJar();
            final Set<Features> cleanedRepos;
            if (includeJar != null && !includeJar.isEmpty()) {
                cleanedRepos = removeExcludedFeatures(featureRepos, includeJar);
            } else {
                cleanedRepos = featureRepos;
            }
            Set<Artifact> artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(cleanedRepos));
            artifacts.addAll(featureArtifacts);
            featureUtil.removeLocalArtifacts(artifacts);

            Map<Gace, String> gaceVersions = new HashMap<>();
            for (Artifact artifact : artifacts) {
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

    Map<String, Boolean> getIncludeJar() {
        final var ret = new HashMap<String, Boolean>();
        for (var entry : project.getProperties().entrySet()) {
            if (entry.getKey() instanceof String key && key.endsWith(INCLUDE_JAR)
                && entry.getValue() instanceof String value) {
                if ("true".equalsIgnoreCase(value)) {
                    ret.put(key, Boolean.TRUE);
                } else if ("false".equalsIgnoreCase(value)) {
                    ret.put(key, Boolean.FALSE);
                } else {
                    LOG.warn("Ignoring {} value {}", key, value);
                }
            }
        }
        return ret;
    }

    Set<Features> removeExcludedFeatures(final Set<Features> features, final Map<String, Boolean> includeJar) {
        return features.stream()
            .filter(feature -> {
                if (isFeatureExcluded(includeJar, feature.getName())) {
                    return false;
                }
                feature.getFeature().removeIf(innerFeature -> isFeatureExcluded(includeJar, innerFeature.getName()));
                return true;
            })
            .collect(Collectors.toSet());
    }

    private static boolean isFeatureExcluded(final Map<String, Boolean> includeJar, final String featureName) {
        if (featureName == null) {
            return false;
        }
        final var propName = featureName.replace('-', '.') + INCLUDE_JAR;
        // Return false as a default for any unset feature.
        return !includeJar.getOrDefault(propName, true);
    }

    private void readFeatureCfg(final AetherUtil aetherUtil, final FeatureUtil featureUtil,
            final Set<Artifact> artifacts, final Set<Features> features) {
        // Create file structure
        final File karafHome = localRepo.getParentFile();
        final File karafEtc = new File(karafHome, "etc");
        final File file = new File(karafEtc, "org.apache.karaf.features.cfg");

        final Properties prop = new Properties();
        try (InputStream is = new FileInputStream(file)) {
            prop.load(is);

            // Note this performs path seaparator translation
            final String karafHomePath = karafHome.toURI().getPath();
            final String karafEtcPath = karafEtc.toURI().getPath();
            final String featuresRepositories = prop.getProperty("featuresRepositories");
            for (String mvnUrl : featuresRepositories.split(",")) {
                final String fixedUrl = mvnUrl
                        .replace("${karaf.home}", karafHomePath)
                        .replace("${karaf.etc}", karafEtcPath);
                if (fixedUrl.startsWith("file:")) {
                    try {
                        // Local feature file
                        features.add(featureUtil.readFeature(new File(new URI(fixedUrl))));
                    } catch (URISyntaxException e) {
                        throw new IllegalArgumentException("Could not resolve URI: " + fixedUrl, e);
                    }
                } else {
                    artifacts.add(aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(fixedUrl))));
                }
            }
        } catch (FileNotFoundException e) {
            LOG.info("Could not find properties file: {}", file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOG.info("Could not read properties file: {}", file.getAbsolutePath(), e);
        }
    }

    private Set<Artifact> readStartupProperties(final AetherUtil aetherUtil) {
        Set<Artifact> artifacts = new LinkedHashSet<>();
        File file = new File(new File(localRepo.getParentFile(), "etc"), "startup.properties");
        Properties prop = new Properties();
        try (InputStream is = new FileInputStream(file)) {
            prop.load(is);
            Enumeration<Object> mvnUrls = prop.keys();
            while (mvnUrls.hasMoreElements()) {
                String mvnUrl = (String) mvnUrls.nextElement();
                Artifact artifact = aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(mvnUrl)));
                artifacts.add(artifact);
            }
        } catch (FileNotFoundException e) {
            LOG.info("Could not find properties file: {}", file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOG.info("Could not read properties file: {}", file.getAbsolutePath(), e);
        }

        return artifacts;
    }
}
