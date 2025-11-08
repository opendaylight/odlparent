/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceSupportTest {
    @Mock
    private AutoCloseable one;
    @Mock
    private AutoCloseable two;

    private final ResourceSupport registry = ResourceSupportModule.provideResourceSupport();

    @Test
    void toStringExposesState() {
        assertThat(registry.toString()).startsWith("ResourceSupport [uuid=").endsWith(" state=Open]");
        registry.close();
        assertThat(registry.toString()).startsWith("ResourceSupport [uuid=").endsWith(" state=Closed]");
    }

    @Test
    void closeOrdersRegistrants() throws Exception {
        assertSame(one, registry.register(one));
        assertSame(two, registry.register(two));

        doNothing().when(one).close();
        doThrow(Exception.class).when(two).close();

        registry.close();

        var inOrder = inOrder(one, two);
        inOrder.verify(two).close();
        inOrder.verify(one).close();
    }

    @Test
    void registerRejectedAfterClose() {
        registry.close();
        var ex = assertThrows(IllegalStateException.class, () -> registry.register(one));
        assertEquals("Cannot register one while closed", ex.getMessage());
    }

    @Test
    void closeIsIdempotent() throws Exception {
        doNothing().when(one).close();
        assertSame(one, registry.register(one));
        registry.close();
        verify(one).close();
        registry.close();
        assertThat(mockingDetails(one).getInvocations()).hasSize(1);
    }
}
