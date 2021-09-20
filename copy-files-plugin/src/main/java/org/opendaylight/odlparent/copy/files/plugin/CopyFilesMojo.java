/*
 * Copyright (c) 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.copy.files.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "copy-files", threadSafe = true)
public class CopyFilesMojo extends AbstractMojo {
    @Parameter(required = true, defaultValue = "${project.basedir}", readonly = true)
    private File gitRepoRootDir;

    @Parameter(required = true, defaultValue = "${project.build.outputDirectory}", readonly = true)
    private File target;

    @Parameter(required = true)
    private List<String> globs;

    @Override
    public void execute() throws MojoExecutionException {
        var srcDir = gitRepoRootDir.toPath();
        while (!Files.isDirectory(srcDir.resolve(".git"))) {
            final var parent = srcDir.getParent();
            if (parent == null) {
                // Remain in last directory
                break;
            }
            srcDir = parent;
        }

        final var dstDir = target.toPath();
        try {
            Files.createDirectories(dstDir);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create directory " + target, e);
        }

        for (var glob : globs) {
            try (var dirStream = Files.newDirectoryStream(srcDir, glob)) {
                for (var src : dirStream) {
                    if (Files.isRegularFile(src, LinkOption.NOFOLLOW_LINKS)) {
                        Files.copy(src, dstDir.resolve(src.getFileName()),
                            StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to perform filesystem operation", e);
            }
        }
    }
}
