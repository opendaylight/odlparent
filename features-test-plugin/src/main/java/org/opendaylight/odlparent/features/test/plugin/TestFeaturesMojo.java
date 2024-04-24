/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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

/**
 * Feature test plugin mojo.
 */
@Mojo(name = "test", defaultPhase = LifecyclePhase.INTEGRATION_TEST,
    requiresDependencyResolution = ResolutionScope.TEST,
    requiresDependencyCollection = ResolutionScope.TEST, threadSafe = true)
public final class TestFeaturesMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(TestFeaturesMojo.class);
    private static final String[] FEATURE_FILENAMES = {"feature.xml", "features.xml"};
    private static final PluginDescriptor STATIC_DESCRIPTOR;

    static {
        final var desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        STATIC_DESCRIPTOR = desc;
    }

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
    @Parameter(property = "skip.karaf.featureTest", defaultValue = "false")
    private boolean legacySkip;
    @Parameter(property = "sft.concurrent", defaultValue = "false")
    private boolean concurrent;

    // bundle state check probe settings
    @Parameter(property = "sft.diag.skip", defaultValue = "false")
    private boolean bundleStateCheckSkip;
    @Parameter(property = "sft.diag.timeout", defaultValue = TestProbe.DEFAULT_TIMEOUT)
    private int bundleStateCheckTimeout;
    @Parameter(property = "sft.diag.interval", defaultValue = TestProbe.DEFAULT_TIMEOUT)
    private int bundleStateCheckInterval;

    // vm and profile options for karaf container
    @Parameter(property = "sft.heap.max", defaultValue = "2g")
    private String maxHeap;
    @Parameter(property = "sft.heap.dump.path", defaultValue = "/dev/null")
    private String heapDumpPath;
    @Parameter(property = "karaf.featureTest.profile", defaultValue = "false")
    private boolean profile;

    // Backing distribution details
    @Parameter(property = "karaf.distro.groupId", defaultValue = "org.opendaylight.odlparent")
    private String distGroupId;
    @Parameter(property = "karaf.distro.artifactId", defaultValue = "opendaylight-karaf-empty")
    private String distArtifactId;
    @Parameter(property = "karaf.distro.type", defaultValue = "tar.gz")
    private String distType;
    @Parameter(property = "karaf.keep.unpack", defaultValue = "false")
    private boolean keepUnpack;

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

        final var buildDir = project.getBuild().getDirectory();
        final var featureFile = getFeatureFile(new File(buildDir + File.separator + "feature"));

        // resolve dependencies (ensure all are in local repository)
        final var resolver = new DependencyResolver(repoSystem, repoSession, repositories);
        resolver.resolveFeatureFile(featureFile);

        // using file:* url instead of mvn:* to avoid MalformedUrlException (unknown protocol 'mvn');
        // no reason to involve external maven resolver (pax-exam-aether-url) to fetch distro artifact via maven,
        // while we can use local repository file directly (URL has built-in handler for 'file' protocol)
        final var karafDistroUrl = resolver.resolveKarafDistroUrl(distGroupId, distArtifactId, distType);
        LOG.debug("Distro URL resolved: {}", karafDistroUrl);

        // dependency features (incl test scope) to be pre-installed
        final var pluginDependencyFeatures = resolver.resolvePluginFeatures();
        final var projectDependencyFeatures = resolver.resolveFeatures(project.getArtifacts());
        LOG.info("Project dependency features detected: {}", projectDependencyFeatures);

        // pax exam options
        final var options = concat(
            vmOptions(maxHeap, heapDumpPath),
            profileOptions(profile),
            karafDistroOptions(karafDistroUrl, keepUnpack, buildDir),
            karafConfigOptions(buildDir, localRepository),
            dependencyFeaturesOptions(pluginDependencyFeatures),
            dependencyFeaturesOptions(projectDependencyFeatures),
            probePropertiesOptions(),
            miscOptions()
        );

        // probe parameters
        System.setProperty(TestProbe.FEATURE_FILE_URI_PROP, featureFile.toURI().toString());
        System.setProperty(TestProbe.BUNDLE_CHECK_SKIP, String.valueOf(bundleStateCheckSkip));
        System.setProperty(TestProbe.BUNDLE_CHECK_TIMEOUT_SECONDS, String.valueOf(bundleStateCheckTimeout));
        System.setProperty(TestProbe.BUNDLE_CHECK_INTERVAL_SECONDS, String.valueOf(bundleStateCheckInterval));

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

        // Note: we are using a fair lock to get first-come, first-serve rather than some unpredictable order
        final var lock = (Lock) topContext.computeIfAbsent("lock", key -> new ReentrantLock(true));
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

    static File getFeatureFile(final File dir) throws MojoExecutionException {
        for (var filename : FEATURE_FILENAMES) {
            final File file = new File(dir, filename);
            if (file.exists()) {
                return file;
            }
        }
        throw new MojoExecutionException("No feature XML file found in " + dir);
    }
}