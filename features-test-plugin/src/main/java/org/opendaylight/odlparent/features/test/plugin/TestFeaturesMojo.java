/*
 * Copyright 2021 PANTHEON.tech, s.r.o and others.
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
package org.opendaylight.odlparent.features.test.plugin;

import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.concat;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.dependencyFeaturesOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.karafConfigOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.karafDistroOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.miscOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.probePropertiesOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.profileOptions;
import static org.opendaylight.odlparent.features.test.plugin.PaxOptionUtils.vmOptions;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.karaf.container.internal.KarafTestContainerFactory;
import org.ops4j.pax.exam.spi.PaxExamRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mojo(name = "test", defaultPhase = LifecyclePhase.INTEGRATION_TEST,
    requiresDependencyResolution = ResolutionScope.TEST,
    requiresDependencyCollection = ResolutionScope.TEST, threadSafe = true)
public final class TestFeaturesMojo extends AbstractMojo {

    private static final Logger LOG = LoggerFactory.getLogger(TestFeaturesMojo.class);
    private static final String[] FEATURE_FILENAMES = {"feature.xml", "features.xml"};
    private static final PluginDescriptor STATIC_DESCRIPTOR = staticPluginDescriptor();

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;
    @Parameter(defaultValue = "${settings.localRepository}")
    private String localRepository;
    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
    private RepositorySystemSession repoSession;
    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
    private List<RemoteRepository> repositories;
    @Component
    private RepositorySystem repoSystem;

    @Parameter(property = "sft.skip", defaultValue = "false")
    private boolean skip;
    @Deprecated(since = "13.0.10", forRemoval = true)
    @Parameter(property = "karaf.featureTest.skip", defaultValue = "false")
    private boolean legacySkip;
    @Parameter(property = "karaf.featureTest.profile", defaultValue = "false")
    private boolean profile;
    @Parameter(property = "karaf.keep.unpack", defaultValue = "false")
    private boolean keepUnpack;
    @Parameter(property = "sft.concurrent", defaultValue = "false")
    private boolean concurrent;
    @Parameter(property = "sft.heap.max", defaultValue = "2g")
    private String maxHeap;
    @Parameter(property = "sft.heap.dump.path", defaultValue = "/dev/null")
    private String heapDumpPath;

    // Backing distribution details
    @Parameter(property = "karaf.distro.groupId", defaultValue = "org.opendaylight.odlparent")
    private String distGroupId;
    @Parameter(property = "karaf.distro.artifactId", defaultValue = "opendaylight-karaf-empty")
    private String distArtifactId;
    @Parameter(property = "karaf.distro.type", defaultValue = "tar.gz")
    private String distType;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            LOG.debug("Skipping execution");
            return;
        }
        if (legacySkip) {
            LOG.warn("Skipping execution due to legacy karaf.featureTest.skip, please migrate to sft.skip");
            return;
        }
        if (!"feature".equals(project.getPackaging())) {
            LOG.info("Project packaging is not 'feature', skipping execution");
            return;
        }

        LOG.info("Starting SFT for {}:{}", project.getGroupId(), project.getArtifactId());

        setUrlStreamHandler();

        final var buildDir = project.getBuild().getDirectory();
        final var featureFile = getFeatureFile(new File(buildDir + File.separator + "feature"));
        try {
            LOG.debug("Feature file URI: {}", featureFile.toURI());
            LOG.debug("Feature file URL: {}", featureFile.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new MojoExecutionException(e);
        }

        // resolve dependencies (ensure all are in local repository)
        final var resolver = new DependencyResolver(repoSystem, repoSession, repositories);
        resolver.resolveFeatureFile(featureFile);

        // dependency features (incl test scope) to be pre-installed
        final var pluginDependencyFeatures = resolver.resolvePluginFeatures();
        final var projectDependencyFeatures = resolver.resolveFeatures(project.getArtifacts());
        LOG.info("Project dependency features detected: {}", projectDependencyFeatures);

        // pax exam options
        final var options = concat(
            vmOptions(maxHeap, heapDumpPath),
            profileOptions(profile),
            karafDistroOptions(distGroupId, distArtifactId, distType, keepUnpack, buildDir),
            karafConfigOptions(buildDir, localRepository),
            dependencyFeaturesOptions(pluginDependencyFeatures),
            dependencyFeaturesOptions(projectDependencyFeatures),
            probePropertiesOptions(),
            miscOptions()
        );

        // probe parameters
        System.setProperty(TestProbe.FEATURE_FILE_URI_PROP, featureFile.toURI().toString());


        final ExamSystem system;
        try {
            system = PaxExamRuntime.createTestSystem(options);
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot create pax-exam system", e);
        }

        final var execution = new PaxExamExecution(localRepository, system,
            new KarafTestContainerFactory().create(system));
        if (concurrent) {
            execution.execute();
            return;
        }

        // We create a plugin context in the top-level project of the build. There we store a single object which acts
        // as the global lock protecting execution.
        final Map<String, Object> topContext;
        synchronized (session) {
            // This is as careful as we can be. We guard against concurrent executions on the same top-level project.
            topContext = session.getPluginContext(STATIC_DESCRIPTOR, session.getTopLevelProject());
        }

        final var lock = (Lock) topContext.computeIfAbsent("lock", key -> new ReentrantLock());
        LOG.debug("Using lock {}", lock);
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MojoExecutionException("Interrupted while acquiring lock", e);
        }

        LOG.debug("Acquired lock {}", lock);
        try {
            execution.execute();
        } finally {
            lock.unlock();
            LOG.debug("Released lock {}", lock);
        }
    }

    private static PluginDescriptor staticPluginDescriptor() {
        final var desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        return desc;
    }

    static File getFeatureFile(final File dir) throws MojoExecutionException {
        for (var filename : FEATURE_FILENAMES) {
            final File file = new File(dir, filename);
            if (file.exists()) {
                return file;
            }
        }
        throw new MojoExecutionException("No feature XML file found in " + dir);
    }

    private static void setUrlStreamHandler() {
        // required to properly handle mvn urls on getting karaf distribution for karaf container
        URL.setURLStreamHandlerFactory(protocol -> {
            try {
                final var handlerClass = Class.forName("org.ops4j.pax.url." + protocol + ".Handler");
                return URLStreamHandler.class.cast(handlerClass.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                return null;
            }
        });
    }
}