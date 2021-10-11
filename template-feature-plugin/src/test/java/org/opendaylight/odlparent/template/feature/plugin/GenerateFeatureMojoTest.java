/*
 * Copyright (c) 2022 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.template.feature.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GenerateFeatureMojoTest {

    @Test
    public void testSemVerRange() {
        assertEquals("[0,1)", GenerateFeatureMojo.semVerRange("0"));
        assertEquals("[0.1,1)", GenerateFeatureMojo.semVerRange("0.1"));
        assertEquals("[0.0.1,1)", GenerateFeatureMojo.semVerRange("0.0.1"));
        assertEquals("[0.1,1)", GenerateFeatureMojo.semVerRange("0.1.0"));
        assertEquals("[0.1.1,1)", GenerateFeatureMojo.semVerRange("0.1.1"));
        assertEquals("[1.2.3,2)", GenerateFeatureMojo.semVerRange("1.2.3"));
    }
}
