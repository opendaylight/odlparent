/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.maven.plugin.MojoExecutionException;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.TestContainer;

final class PaxExamExecution {
    private final TestContainer[] containers;
    private final ExamSystem examSystem;
    private final String localRepository;

    PaxExamExecution(final String localRepository, final ExamSystem examSystem, final TestContainer ... containers) {
        this.localRepository = requireNonNull(localRepository);
        this.containers = containers;
        this.examSystem = examSystem;
    }

    @SuppressWarnings({"IllegalCatch", "RegexpSinglelineJava"})
    void execute() throws MojoExecutionException {

        // Use the same repository for Pax Exam as is used for Maven
        System.setProperty("org.ops4j.pax.url.mvn.localRepository", localRepository);

        for (var container : containers) {
            // disable karaf stdout output to maven log
            final var stdout = System.out;
            System.setOut(new PrintStream(OutputStream.nullOutputStream(), true, StandardCharsets.UTF_8));

            final var containerStarted = new AtomicBoolean(false);
            try {
                container.start();
                containerStarted.set(true);

                // build probe
                final var probeBuilder = examSystem.createProbe();
                final var address = probeBuilder.addTest(TestProbe.class, "testFeature");
                probeBuilder.addTest(TestProbe.CheckResult.class);

                // install probe bundle
                container.install(probeBuilder.build().getStream());
                // execute probe testMethod
                container.call(address);

            } catch (RuntimeException | IOException e) {
                throw new MojoExecutionException(e);
            } finally {
                if (containerStarted.get()) {
                    container.stop();
                }
                // restore stdout
                System.setOut(stdout);
            }
        }
    }
}