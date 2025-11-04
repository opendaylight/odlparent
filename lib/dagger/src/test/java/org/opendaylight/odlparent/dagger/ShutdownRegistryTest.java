/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShutdownRegistryTest {
    @Mock
    private AutoCloseable one;
    @Mock
    private AutoCloseable two;

    private final ShutdownRegistry registry = new ShutdownRegistry();

    @Test
    void toStringExposesState() {
        assertThat(registry.toString()).startsWith("ShutdownRegistry [uuid=").endsWith(" state=Open]");
        registry.close();
        assertThat(registry.toString()).startsWith("ShutdownRegistry [uuid=").endsWith(" state=Closed]");
   }

    @Test
    void closeOrdersRegistrants() throws Exception {
        assertSame(one, registry.register(one));
        assertSame(two, registry.register(two));

        doNothing().when(one).close();
        doThrow(Exception.class).when(two).close();

        registry.close();

        final var inOrder = inOrder(one, two);
        inOrder.verify(two).close();
        inOrder.verify(one).close();
    }
}
