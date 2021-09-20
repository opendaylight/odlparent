/*
 * Copyright (c) 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.odlparent.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class CopyFilesScript {

    private CopyFilesScript() {
    }

    public static void main(String[] args) throws IOException {
        File gitRepoRootDir = new File(args[0]);
        while (!new File(gitRepoRootDir, ".git").exists() && gitRepoRootDir.getParentFile() != null) {
            gitRepoRootDir = gitRepoRootDir.getParentFile();
        }

        File target = new File(args[1]);
        if (target.exists() || target.mkdirs()) {
            copy(gitRepoRootDir, "README*", target);
            copy(gitRepoRootDir, "CONTRIBUTING*", target);
            copy(gitRepoRootDir, "INFO.yaml", target);
        } else {
            throw new IOException("It is not possible to create the target file");
        }
    }

    private static void copy(File root, String glob, File target) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(root.toPath(), glob)) {
            for (Path path : dirStream) {
                Files.copy(path, new File(target, path.toFile().getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }
}
