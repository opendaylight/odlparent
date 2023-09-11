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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.container.internal.KarafTestContainerFactory;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.extra.VMOption;
import org.ops4j.pax.exam.spi.DefaultExamSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mojo(
    name = "test",
    defaultPhase = LifecyclePhase.INTEGRATION_TEST,
    requiresProject = true,
    threadSafe = true)
public final class TestFeaturesMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(TestFeaturesMojo.class);
    private static final PluginDescriptor DESCRIPTOR;
    private static final String DISTRO_VERSION;
    private static final String RELEASE_VERSION;

    static {
        final var desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        DESCRIPTOR = desc;

        try (var is = TestFeaturesMojo.class.getResourceAsStream("/distro.version")) {
            DISTRO_VERSION = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        try (var is = TestFeaturesMojo.class.getResourceAsStream("/release.version")) {
            RELEASE_VERSION = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;
    @Parameter(defaultValue = "${settings.localRepository}")
    private String localRepository;
    @Parameter(property = "sft.skip", defaultValue = "false")
    private boolean skip;
    @Deprecated(since = "13.0.5", forRemoval = true)
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
    // FIXME: use tar.gz instead
    @Parameter(property = "karaf.distro.type", defaultValue = "zip")
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

        final var buildDir = project.getBuild().getDirectory();

        final var options = new ArrayList<Option>();
        options.add(new VMOption("-Xmx" + maxHeap));
        options.add(new VMOption("-XX:+HeapDumpOnOutOfMemoryError"));
        options.add(new VMOption("-XX:HeapDumpPath=" + heapDumpPath));

        if ("Linux".equals(System.getProperty("os.name"))) {
            // This prevents low entropy issues on Linux to affect Java random numbers
            // which can block crypto such as the SSH server in netconf
            // see https://jira.opendaylight.org/browse/ODLPARENT-49
            options.add(new VMOption("-Djava.security.egd=file:/dev/./urandom"));
        }

        if (profile) {
            final Path jfrFile;
            try {
                jfrFile = Files.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr");
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to create JFR file", e);
            }
            options.add(new VMOption("-XX:StartFlightRecording=disk=true,settings=profile,dumponexit=true,filename="
                + jfrFile.toAbsolutePath().toString()));
        }

        // Baseline distribution
        options.add(KarafDistributionOption.karafDistributionConfiguration()
            .frameworkUrl(CoreOptions.maven()
                .groupId(distGroupId)
                .artifactId(distArtifactId)
                .type(distType)
                .version(DISTRO_VERSION))
            .name("OpenDaylight")
            .unpackDirectory(new File("target/pax"))
            .useDeployFolder(false));

        if (keepUnpack) {
            options.add(KarafDistributionOption.keepRuntimeFolder());
        }

        options.add(KarafDistributionOption.configureConsole().ignoreLocalConsole().ignoreRemoteShell());
        options.add(KarafDistributionOption.logLevel(LogLevel.INFO));


        // FIXME: fill in other options

        final var karafLogFile = new File("target/SFT/karaf.log").getAbsoluteFile();
        try {
            Files.createDirectories(karafLogFile.toPath().getParent());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create log directory", e);
        }

        final ExamSystem system;
        try {
            system = DefaultExamSystem.create(options.toArray(new Option[0]));
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot create pax-exam system", e);
        }

        final var execution = new PaxExamExecution(buildDir + "/feature", localRepository,
            List.of(new KarafTestContainerFactory().create(system)));
        if (concurrent) {
            execution.execute();
            return;
        }

        // We create a plugin context in the top-level project of the build. There we store a single object which acts
        // as the global lock protecting execution.
        final Map<String, Object> topContext;
        synchronized (session) {
            // This is as careful as we can be. We guard against concurrent executions on the same top-leven project.
            topContext = session.getPluginContext(DESCRIPTOR, session.getTopLevelProject());
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
}
