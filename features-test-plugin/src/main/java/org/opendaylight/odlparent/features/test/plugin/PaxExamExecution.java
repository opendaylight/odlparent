/*
 * Copyright (c) 2023 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static java.util.Objects.requireNonNull;

import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.ops4j.pax.exam.TestContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PaxExamExecution {
    private static final Logger LOG = LoggerFactory.getLogger(PaxExamExecution.class);

    private final List<TestContainer> containers;
    private final String localRepository;
    private final String featureDir;

    PaxExamExecution(final String featureDir, final String localRepository, final List<TestContainer> containers)
        throws MojoExecutionException {
        this.featureDir = requireNonNull(featureDir);
        this.localRepository = requireNonNull(localRepository);
        this.containers = List.copyOf(containers);

    }

    @SuppressWarnings("IllegalCatch")
    void execute() throws MojoExecutionException {

        // Use the same repository for Pax Exam as is used for Maven
        System.setProperty("org.ops4j.pax.url.mvn.localRepository", localRepository);

        LOG.debug("Running features in {} with local repository at {} with {}", featureDir, localRepository,
            containers);

        for (var container : containers) {
            // FIXME: also install probe and all that
            try {
                container.start();
                LOG.debug("Karaf container started");

            } catch (RuntimeException e) {
                LOG.error("Error", e);
                throw new MojoExecutionException(e);
            } finally {
                container.stop();
            }
        }
    }
}