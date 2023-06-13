/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

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
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mojo(name = "populate-test-features", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class PopulateTestFeaturesMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(PopulateTestFeaturesMojo.class);

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
    public void execute() throws MojoExecutionException, MojoFailureException {
        AetherUtil aetherUtil = new AetherUtil(repoSystem, repoSession, remoteRepos, localRepo);
        FeatureUtil featureUtil = new FeatureUtil(aetherUtil, localRepo);
        try {
            Set<Artifact> startupArtifacts = readStartupProperties(aetherUtil);
            aetherUtil.installArtifacts(startupArtifacts);
            Set<Artifact> featureArtifacts = new LinkedHashSet<>();
            Set<Features> features = new LinkedHashSet<>();
            readFeatureCfg(aetherUtil, featureUtil, featureArtifacts, features);
            featureArtifacts.addAll(
                aetherUtil.resolveDependencies(MvnToAetherMapper.toAether(project.getDependencies()),
                    new KarafTestFeaturesDependencyFilter()));
            features.addAll(featureUtil.readFeatures(featureArtifacts));
            features.addAll(featureUtil.findAllFeaturesRecursively(features));
            Set<Artifact> artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(features));
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


    private void readFeatureCfg(AetherUtil aetherUtil, FeatureUtil featureUtil, Set<Artifact> artifacts,
        Set<Features> features) {
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

    private Set<Artifact> readStartupProperties(AetherUtil aetherUtil) {
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
