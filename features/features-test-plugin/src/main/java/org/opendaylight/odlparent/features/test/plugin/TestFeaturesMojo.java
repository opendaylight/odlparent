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

    static {
        final var desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        DESCRIPTOR = desc;
    }

    @Parameter(required = true, readonly = true)
    private MavenProject project;
    @Parameter(required = true)
    private MavenSession session;
    @Parameter(property = "karaf.featureTest.skip", defaultValue = "false")
    private boolean skip;
    @Parameter(property = "karaf.featureTest.concurrent", defaultValue = "false")
    private boolean concurrent;
    @Parameter(defaultValue = "${settings.localRepository}")
    private String localRepository;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            LOG.debug("Skipping execution");
            return;
        }

        final var execution = new PaxExamExecution(project.getBuild().getDirectory() + "/feature", localRepository);
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
