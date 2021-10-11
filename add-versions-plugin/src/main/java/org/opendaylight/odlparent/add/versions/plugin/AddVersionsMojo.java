/*
 * Copyright (c) 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.add.versions.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.TransformerFactoryImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "add-versions", threadSafe = true)
public class AddVersionsMojo extends AbstractMojo {

    private static final String FEATURE_FILE_PATH = "src/main/feature/feature.xml";

    @Parameter(required = true, defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(required = true, defaultValue = "${project.basedir}", readonly = true)
    private File projectBasedir;

    @Parameter(required = true, defaultValue = "${project.build.directory}", readonly = true)
    private File projectTargetDir;

    @Override
    public void execute() throws MojoExecutionException {
        final Source srcFeature = new StreamSource(new File(projectBasedir, FEATURE_FILE_PATH));
        final Source srcXslt = new StreamSource(AddVersionsMojo.class.getResourceAsStream("/feature.xsl"));

        final var dstDir = Path.of(projectTargetDir.getPath(), "generated-resources/feature");
        try {
            Files.createDirectories(dstDir);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create directory " + dstDir, e);
        }
        final Result resultFeature = new StreamResult(new File(dstDir.toFile(), "feature.xml"));

        try {
            final Transformer transformer = new TransformerFactoryImpl().newTransformer(srcXslt);
            transformer.setParameter("mavenProject", mavenProject);
            transformer.transform(srcFeature, resultFeature);
        } catch (TransformerException e) {
            throw new MojoExecutionException("Failed to perform transformation", e);
        }
    }
}
