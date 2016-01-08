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
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.opendaylight.odlparent.featuretest.CustomBundleURLStreamHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo populating the local repository by delegating to Aether.
 *
 * @goal populate-local-repo
 * @phase prepare-package
 */
public class PopulateLocalRepoMojo
        extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(PopulateLocalRepoMojo.class);

    static {
        // Static initialization, as we may be invoked multiple times
        URL.setURLStreamHandlerFactory(new CustomBundleURLStreamHandlerFactory());
    }

    /**
     * The Maven project being built.
     *
     * @component
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The entry point to Aether, i.e. the component doing all the work.
     *
     * @component
     */
    private RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     *
     * @parameter default-value="${repositorySystemSession}"
     * @readonly
     */
    private RepositorySystemSession repoSession;

    /**
     * The project's remote repositories to use for the resolution of plugins and their dependencies.
     *
     * @parameter default-value="${project.remotePluginRepositories}"
     * @readonly
     */
    private List<RemoteRepository> remoteRepos;

    /**
     * The local repository to use for the resolution of plugins and their dependencies.
     *
     * @parameter
     */
    private File localRepo;

    private AetherUtil aetherUtil;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        aetherUtil = new AetherUtil(repoSystem, repoSession, remoteRepos, localRepo);
        try {
            Set<Artifact> featureArtifacts = readFeatureCfg();
            featureArtifacts.addAll(
                    aetherUtil.resolveDependencies(MvnToAetherMapper.toAether(project.getDependencies()),
                            new KarafFeaturesDependencyFilter()));
            Set<Features> features = FeatureUtil.readFeatures(featureArtifacts);
            features.addAll(FeatureUtil.findAllFeaturesRecursively(aetherUtil, features,
                    FeatureUtil.featuresRepositoryToCoords(features)));
            for (Features feature : features) {
                LOG.info("Features Repos  discovered recursively: {}", feature.getName());
            }
            Set<Artifact> artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(features));
            artifacts.addAll(featureArtifacts);

            for (Artifact artifact : artifacts) {
                LOG.info("Artifacts to be installed: {}", artifact.toString());
            }
            if (localRepo != null) {
                aetherUtil.installArtifacts(artifacts);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute", e);
        }
    }

    private Set<Artifact> readFeatureCfg() throws ArtifactResolutionException {
        Set<Artifact> artifacts = new LinkedHashSet<>();
        File file = new File(localRepo.getParentFile().toString() + "/etc/org.apache.karaf.features.cfg");
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(file));
            String featuresRepositories = prop.getProperty("featuresRepositories");
            List<String> result = Arrays.asList(featuresRepositories.split(","));
            for (String mvnUrl : result) {
                artifacts.add(aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(mvnUrl))));
            }
        } catch (FileNotFoundException e) {
            LOG.info("Could not find properties file: {}", file.getAbsolutePath(), e);
        } catch (IOException e) {
            LOG.info("Could not read properties file: {}", file.getAbsolutePath(), e);
        }

        return artifacts;
    }
}
