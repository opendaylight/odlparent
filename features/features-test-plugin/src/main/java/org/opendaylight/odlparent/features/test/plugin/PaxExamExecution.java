/*
 * Copyright (c) 2023 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.apache.maven.plugin.MojoExecutionException;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestContainerFactory;
import org.ops4j.pax.exam.spi.DefaultExamSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PaxExamExecution {
    private static final Logger LOG = LoggerFactory.getLogger(PaxExamExecution.class);

    private final List<TestContainer> containers;
    private final String localRepository;
    private final String featureDir;

    PaxExamExecution(final String featureDir, final String localRepository) throws MojoExecutionException {
        this.featureDir = requireNonNull(featureDir);
        this.localRepository = requireNonNull(localRepository);

        final var containerFactory = ServiceLoader.load(TestContainerFactory.class).findFirst()
            .orElseThrow(() -> new MojoExecutionException("Cannot find a TestContainerFactory"));

        final var options = new ArrayList<Option>();

        // FIXME: fill in other options

        final ExamSystem system;
        try {
            system = DefaultExamSystem.create(options.toArray(new Option[0]));
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot create pax-exam system", e);
        }

        containers = List.of(containerFactory.create(system));
    }

    void execute() throws MojoExecutionException {
        // Use the same repository for Pax Exam as is used for Maven
        System.setProperty("org.ops4j.pax.url.mvn.localRepository", localRepository);
        LOG.debug("Running features in {} with local repository at {} with {}", featureDir, localRepository,
            containers);

        // FIXME: implement this
        throw new MojoExecutionException("Not implemented yet");
    }
}