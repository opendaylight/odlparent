/*
 * Copyright (c) 2023 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static java.util.Objects.requireNonNull;

import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PaxExamExecution {
    private static final Logger LOG = LoggerFactory.getLogger(PaxExamExecution.class);

    private final String featureDir;
    private final String localRepository;

    PaxExamExecution(final String featureDir, final String localRepository) {
        this.featureDir = requireNonNull(featureDir);
        this.localRepository = requireNonNull(localRepository);
    }

    void execute() throws MojoExecutionException {
        // Use the same repository for Pax Exam as is used for Maven
        System.setProperty("org.ops4j.pax.url.mvn.localRepository", localRepository);
        LOG.debug("Running features in {} with local repository at {}", featureDir, localRepository);

        // FIXME: implement this
        throw new MojoExecutionException("Not implemented yet");
    }
}