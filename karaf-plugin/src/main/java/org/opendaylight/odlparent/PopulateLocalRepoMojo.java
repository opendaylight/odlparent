/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent;

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
import org.apache.karaf.tooling.url.CustomBundleURLStreamHandlerFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResolutionException;

/**
 * @goal populate-local-repo
 * @phase prepare-package
 */
public class PopulateLocalRepoMojo
    extends AbstractMojo {

    static {
        // Static initialization, as we may be invoked multiple times
        URL.setURLStreamHandlerFactory(new CustomBundleURLStreamHandlerFactory());
    }

    /**
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
     * @parameter
     */
    private File localRepo;

    private AetherUtil aetherUtil;

    public void execute() throws MojoExecutionException, MojoFailureException {

        aetherUtil = new AetherUtil(repoSystem, repoSession, remoteRepos,localRepo,getLog());
        try {
            Set<Artifact> featureArtifacts = readFeatureCfg();
            featureArtifacts.addAll(aetherUtil.resolveDependencies(MvnToAetherMapper.toAether(project.getDependencies()),
                    new KarafFeaturesDependencyFilter()));
            Set<Features> features = FeatureUtil.readFeatures(featureArtifacts);
            features.addAll(FeatureUtil.findAllFeaturesRecursively(aetherUtil, features, FeatureUtil.featuresRepositoryToCoords(features)));
            for(Features feature: features) {
                getLog().info("Features Repos  discovered recursively: " + feature.getName());
            }
            Set<Artifact> artifacts = aetherUtil.resolveArtifacts(FeatureUtil.featuresToCoords(features));
            artifacts.addAll(featureArtifacts);

            for(Artifact artifact: artifacts) {
                getLog().info("Artifacts to be installed: " + artifact.toString());
            }
            if(localRepo != null) {
                aetherUtil.installArtifacts(artifacts);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failure: ", e);
        }
    }

    private Set<Artifact> readFeatureCfg() throws ArtifactResolutionException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        File file = new File(localRepo.getParentFile().toString() + "/etc/org.apache.karaf.features.cfg");
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(file));
            String featuresRepositories = prop.getProperty("featuresRepositories");
            List<String> result = Arrays.asList(featuresRepositories.split(","));
            for(String mvnUrl: result) {
                artifacts.add(aetherUtil.resolveArtifact(FeatureUtil.toCoord(new URL(mvnUrl))));
            }
        } catch (FileNotFoundException e) {
            getLog().info("Could not find properties file: " + file.getAbsolutePath());
        } catch (IOException e) {
            getLog().info("Could not read properties file: " + file.getAbsolutePath());
        }

        return artifacts;
    }
}
