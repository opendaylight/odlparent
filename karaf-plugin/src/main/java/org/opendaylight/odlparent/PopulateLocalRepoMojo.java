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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.opendaylight.odlparent.karafutil.CustomBundleUrlStreamHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo populating the local repository by delegating to Aether.
 */
@Mojo(name = "populate-local-repo", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class PopulateLocalRepoMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(PopulateLocalRepoMojo.class);

    static {
        initStreamHandlerFactory();
    }

    // URL.setURLStreamHandlerFactory throws an Error directly, so we can’t do any better than this...
    @SuppressWarnings("checkstyle:IllegalCatch")
    private static void initStreamHandlerFactory() {
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
    public void execute() throws MojoExecutionException {
        final var aetherUtil = new AetherUtil(repoSystem, repoSession, remoteRepos, localRepo);
        final var featureUtil = new FeatureUtil(aetherUtil, localRepo);

        final var startupArtifacts = readStartupProperties(aetherUtil);
        try {
            aetherUtil.installArtifacts(startupArtifacts);
        } catch (InstallationException e) {
            throw new MojoExecutionException("Failed to install startup artifacts", e);
        }

        final var featureArtifacts = new LinkedHashSet<Artifact>();
        final var features = new LinkedHashSet<Features>();
        readFeatureCfg(aetherUtil, featureUtil, featureArtifacts, features);

        try {
            featureArtifacts.addAll(aetherUtil.resolveDependencies(
                MvnToAetherMapper.toAether(project.getDependencies()),
                (node, ignored) -> node != null
                    && node.getArtifact() != null
                    && node.getDependency() != null
                    && node.getDependency().getScope() != null
                    && node.getArtifact().getClassifier().equals("features")
                    && node.getArtifact().getExtension().equals("xml")));
        } catch (DependencyResolutionException e) {
            throw new MojoExecutionException("Failed to resolve dependencies", e);
        }

        try {
            features.addAll(featureUtil.readFeatures(featureArtifacts));
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("Failed to read features", e);
        }

        try {
            // Do not provide FeatureUtil.featuresRepositoryToCoords(features)) as existingCoords
            // to findAllFeaturesRecursively, as those coords are not resolved yet, and it would lead to Bug 6187.
            features.addAll(featureUtil.findAllFeaturesRecursively(features));
        } catch (FileNotFoundException | MalformedURLException e) {
            throw new MojoExecutionException("Failed to discover features", e);
        }

        for (var feature : features) {
            LOG.info("Feature repository discovered recursively: {}", feature.getName());
        }
        final var artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(features));
        artifacts.addAll(featureArtifacts);
        featureUtil.removeLocalArtifacts(artifacts);

        final var gaceVersions = new HashMap<Gace, String>();
        for (var artifact : artifacts) {
            LOG.debug("Artifact to be installed: {}", artifact);
            final var gace = new Gace(artifact);
            final var duplicate = gaceVersions.putIfAbsent(gace, artifact.getVersion());
            if (duplicate != null && !duplicate.equals(artifact.getVersion())) {
                LOG.warn("Duplicate versions for {}, {} and {}", gace, duplicate, artifact.getVersion());
            }
        }
        if (localRepo != null) {
            try {
                aetherUtil.installArtifacts(artifacts);
            } catch (InstallationException e) {
                throw new MojoExecutionException("Failed to execute", e);
            }
        }
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
        final var artifacts = new LinkedHashSet<Artifact>();
        final var file = new File(new File(localRepo.getParentFile(), "etc"), "startup.properties");
        try (var is = new FileInputStream(file)) {
            final var prop = new Properties();
            prop.load(is);
            final var mvnUrls = prop.keys();
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
