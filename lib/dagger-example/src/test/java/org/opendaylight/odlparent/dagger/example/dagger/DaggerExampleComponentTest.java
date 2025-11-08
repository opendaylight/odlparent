/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger.example.dagger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.opendaylight.odlparent.dagger.example.api.ExampleService;

class DaggerExampleComponentTest {
    @Test
    void resourceSupportIsConstant() {
        try (var component = DaggerExampleComponent.create()) {
            var first = component.resourceSupport();
            assertNotNull(first);
            assertSame(first, component.resourceSupport());
        }
    }

    @Test
    void componentCloseIsIdempotent() {
        ExampleService service;
        try (var component = DaggerExampleComponent.create()) {
            service = component.exampleResource();
            assertEquals(0, service.interactionCount());
            component.close();
            assertEquals(1, service.interactionCount());
        }

        assertEquals(1, service.interactionCount());
    }
}
