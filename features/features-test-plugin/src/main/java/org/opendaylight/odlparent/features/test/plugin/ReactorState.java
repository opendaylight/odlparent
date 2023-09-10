/*
 * Copyright (c) 2023 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ReactorState {
    @FunctionalInterface
    interface Execution {
        /**
         * Run with specified ReactorState.
         *
         * @param state A {@link ReactorState}
         * @throws MojoExecutionException
         */
        void execute(@NonNull ReactorState state) throws MojoExecutionException;
    }

    private static final Logger LOG = LoggerFactory.getLogger(TestFeaturesMojo.class);
    private static final PluginDescriptor DESCRIPTOR;

    static {
        final var desc = new PluginDescriptor();
        desc.setGroupId("org.opendaylight.odlparent");
        desc.setArtifactId("features-test-plugin");
        DESCRIPTOR = desc;
    }

    private final ReentrantLock lock = new ReentrantLock();

    private ReactorState() {
        // Hidden on purpose
    }

    // We create a plugin context in the top-level project of the build. There we store a single object which acts as
    // the global lock protecting execution.
    static void execute(final MavenSession session, final Execution execution) throws MojoExecutionException {
        final Map<String, Object> topContext;
        synchronized (session) {
            // This is as careful as we can be
            topContext = session.getPluginContext(DESCRIPTOR, session.getTopLevelProject());
        }

        ((ReactorState) topContext.computeIfAbsent("state", key -> new ReactorState())).execute(execution);
    }

    private void execute(final Execution execution) throws MojoExecutionException {
        LOG.debug("Using lock {}", lock);
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MojoExecutionException("Interrupted while acquiring lock", e);
        }

        LOG.debug("Acquired lock {}", lock);
        try {
            execution.execute(this);
        } finally {
            lock.unlock();
            LOG.debug("Released lock {}", lock);
        }
    }
}