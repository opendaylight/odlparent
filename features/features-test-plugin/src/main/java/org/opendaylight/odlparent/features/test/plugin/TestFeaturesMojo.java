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
    name = "test-features",
    defaultPhase = LifecyclePhase.INTEGRATION_TEST,
    requiresProject = true,
    threadSafe = true)
public class TestFeaturesMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(TestFeaturesMojo.class);
    private static final String LOCK_CONTEXT_KEY = "GLOBAL LOCK";
    private static final PluginDescriptor DESCRIPTOR;

    static {
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        DESCRIPTOR = desc;
    }

    @Parameter(required = true, readonly = true)
    private MavenProject project;
    @Parameter(required = true)
    private MavenSession session;

    @Override
    public void execute() throws MojoExecutionException {
        final Object lock = establishLock();
        LOG.info("Using lock {}", lock);

        synchronized (lock) {

        }
    }

    // We create a plugin context in the top-level project of the build. There we store a single object which acts as
    // the global lock protecting execution.
    private Object establishLock() {
        final Map<String, Object> topContext;
        synchronized (session) {
            // This is as careful as we can be
            topContext = session.getPluginContext(DESCRIPTOR, session.getTopLevelProject());
        }
        return topContext.computeIfAbsent(LOCK_CONTEXT_KEY, key -> new Object());
    }
}
