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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "copy-files")
public class CopyFilesMojo extends AbstractMojo {
    @Parameter(required = true)
    private File gitRepoRootDir;

    @Parameter(required = true)
    private File target;

    @Override
    public void execute() throws MojoExecutionException {
        while (!new File(gitRepoRootDir, ".git").exists() && gitRepoRootDir.getParentFile() != null) {
            gitRepoRootDir = gitRepoRootDir.getParentFile();
        }

        if (target.exists() || target.mkdirs()) {
            copy(gitRepoRootDir, "README*", target);
            copy(gitRepoRootDir, "CONTRIBUTING*", target);
            copy(gitRepoRootDir, "INFO.yaml", target);
        } else {
            throw new MojoExecutionException("Failed to create the target file " + target);
        }
    }

    private static void copy(File root, String glob, File target) throws MojoExecutionException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(root.toPath(), glob)) {
            for (Path path : dirStream) {
                Files.copy(path, new File(target, path.toFile().getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read a file", e);
        }
    }


}
