/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafFeaturesOption;
import org.ops4j.pax.exam.options.OptionalCompositeOption;

class DependencyUtilTest {

    @TempDir
    File dir;

    @Test
    void testFeatures() throws IOException {
        final var listFile = new File(dir, "test-dependencies");
        final var artifactFile = new File(dir, "features-artifact.xml");
        writeFile(artifactFile, """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-test">
                <feature name="test-feature-name" version="1.2.3-SNAPSHOT">
                    <bundle>mvn:some.group/some-artifact/0.0.1</bundle>
                </feature>
            </features>
            """);
        writeFile(listFile, String.format("""
            The following files have been resolved:
                org.opendaylight.odlparent:odl-test:xml:features:1.2.3-SNAPSHOT:test:%s
            """, artifactFile.getAbsolutePath())
        );
        final var result = DependencyUtil.testFeatures(listFile);
        assertNotNull(result);
        final var composite = assertInstanceOf(OptionalCompositeOption.class, result);
        final var options = composite.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
        final var featureOpt = assertInstanceOf(KarafFeaturesOption.class, options[0]);
        assertEquals("mvn:org.opendaylight.odlparent/odl-test/1.2.3-SNAPSHOT/xml/features", featureOpt.getURL());
        assertArrayEquals(new String[]{"test-feature-name"}, featureOpt.getFeatures());
    }

    @Test
    void testDependenciesNoFeatures() throws IOException {
        final var listFile = new File(dir, "test-dependencies");
        writeFile(listFile, """
            The following files have been resolved:
               none
            """);
        assertEmpty(DependencyUtil.testFeatures(listFile));
    }

    @Test
    void testDependenciesNoListFile() {
        assertEmpty(DependencyUtil.testFeatures(new File("")));
    }

    private static void writeFile(final File targetFile, final String content) throws IOException {
        Files.write(targetFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }

    private static void assertEmpty(final Option result) {
        assertNotNull(result);
        final var composite = assertInstanceOf(OptionalCompositeOption.class, result);
        final var options = composite.getOptions();
        assertNotNull(options);
        assertEquals(0, options.length);
    }
}
