/*
 * Copyright (c) 2019 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.filter.manifest.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo processing filtering an input Manifest file into an output, retaining only selected entries.
 */
@Mojo(name = "filter-manifest")
public class FilterManifestMojo extends AbstractMojo {
    private static final Logger LOG = LoggerFactory.getLogger(FilterManifestMojo.class);

    /**
     * Input {@code MANIFEST.MF} file.
     */
    @Parameter(required = true)
    private File inputFile;

    /**
     * Output {@code MANIFEST.MF} file.
     */
    @Parameter(required = true)
    private File outputFile;

    /**
     * List of main attributes that should be copied from input to output. All other attributes (aside from version)
     * and entries are ignored. If an attribute is specified here and it does not exist in input, plugin execution will
     * fail.
     */
    @Parameter(required = true)
    private List<String> retainedAttributes;

    @Override
    public void execute() throws MojoExecutionException {
        LOG.debug("Filtering {} to {} retaining {}", inputFile, outputFile, retainedAttributes);

        final Manifest input;
        try (InputStream is = Files.newInputStream(inputFile.toPath())) {
            input = new Manifest(is);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read input " + inputFile, e);
        }

        // Keep only the entries we are instructed to keep
        final Attributes inputAttrs = input.getMainAttributes();
        LOG.debug("Input manifest has attributes {}", inputAttrs.keySet());

        final Manifest output = new Manifest();
        final Attributes outputAttrs = output.getMainAttributes();
        // We need to always emit version
        outputAttrs.putValue(Name.MANIFEST_VERSION.toString(), inputAttrs.getValue(Name.MANIFEST_VERSION));

        for (String attr : retainedAttributes) {
            final String value = inputAttrs.getValue(attr);
            if (value == null) {
                throw new MojoExecutionException("Attribute " + attr + " is not present in " + inputFile);
            }

            LOG.debug("Propagating attribute {} value {}", attr, value);
            outputAttrs.putValue(attr, value);
        }
        LOG.debug("Output manifest has attributes {}", outputAttrs.keySet());

        try (OutputStream os = Files.newOutputStream(outputFile.toPath())) {
            output.write(os);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write output " + outputFile, e);
        }
    }
}
