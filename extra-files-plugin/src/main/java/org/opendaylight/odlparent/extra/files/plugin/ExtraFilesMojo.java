/*
 * Copyright (c) 2019 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.extra.files.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo processing filtering an input Manifest file into an output, retaining only selected entries.
 */
@Mojo(name = "extra-files", threadSafe = true)
public class ExtraFilesMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(ExtraFilesMojo.class);

    /**
     * File glob patterns to match.
     */
    @Parameter(required = true)
    private List<String> fileGlobs;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        File root = project.getBasedir();
        while (!new File(root, ".git").exists()) {
            final File parent = root.getParentFile();
            if (parent == null) {
                LOG.info("Could not find project root directory, not copying any files");
                return;
            }
            root = parent;
        }

        final File target = new File(project.getBuild().getOutputDirectory());
        try {
            Files.createDirectories(target.toPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create " + target, e);
        }

        for (String glob : fileGlobs) {
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(root.toPath(), glob)) {
                for (Path path : dirStream) {
                    LOG.debug("Copying {})", path);
                    Files.copy(path, new File(target, path.toFile().getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy files", e);
            }
        }
    }
}
