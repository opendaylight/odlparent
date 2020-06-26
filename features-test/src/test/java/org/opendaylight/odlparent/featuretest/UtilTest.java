/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testFindTestDependencies() {
        final Properties props = new Properties();
        props.setProperty("biz.aQute.bnd/biz.aQute.bndlib/version", "3.5.0");
        props.setProperty("biz.aQute.bnd/biz.aQute.bndlib/type", "jar");
        props.setProperty("biz.aQute.bnd/biz.aQute.bndlib/scope", "compile");

        props.setProperty("biz.aQute.bnd/bndlib/version", "2.4.0");
        props.setProperty("biz.aQute.bnd/bndlib/type", "jar");
        props.setProperty("biz.aQute.bnd/bndlib/scope", "test");

        props.setProperty("com.github.spotbugs/spotbugs-annotations/version", "3.1.12");
        props.setProperty("com.github.spotbugs/spotbugs-annotations/type", "jar");
        props.setProperty("com.github.spotbugs/spotbugs-annotations/scope", "provided");

        props.setProperty("com.google.errorprone/error_prone_annotations/version", "2.3.3");
        props.setProperty("com.google.errorprone/error_prone_annotations/type", "jar");

        final List<MavenDependency> deps = Util.findTestDependencies(props);
        assertEquals(1, deps.size());

        final MavenDependency dep = deps.get(0);
        assertEquals("biz.aQute.bnd", dep.groupId());
        assertEquals("bndlib", dep.artifactId());
        assertEquals("2.4.0", dep.version());
        assertEquals("jar", dep.type());
        assertEquals("test", dep.scope());
    }
}
