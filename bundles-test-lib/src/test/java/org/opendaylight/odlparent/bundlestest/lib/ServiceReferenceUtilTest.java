/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.ServiceReference;

/**
 * Unit test for ServiceReferenceUtil.
 *
 * @author Michael Vorburger.ch
 */
@ExtendWith(MockitoExtension.class)
class ServiceReferenceUtilTest {
    @Mock
    private ServiceReference<?> serviceReference;

    @Test
    void testGetUsingBundleSymbolicNames() {
        doReturn(null).when(serviceReference).getUsingBundles();
        assertEquals(List.of(), ServiceReferenceUtil.getUsingBundleSymbolicNames(serviceReference));
    }

    @Test
    void testGetProperties() {
        doReturn(new String[] { "property1", "property2", "property3" }).when(serviceReference).getPropertyKeys();
        doReturn("value1").when(serviceReference).getProperty("property1");
        doReturn(List.of("value2.1", "value2.2")).when(serviceReference).getProperty("property2");
        doReturn(null).when(serviceReference).getProperty("property3");

        final var map = ServiceReferenceUtil.getProperties(serviceReference);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("property1"));
        assertEquals(List.of("value2.1", "value2.2"), map.get("property2"));
        // Unfortunate null;
        assertTrue(map.containsKey("property3"));
        assertNull(map.get("property3"));
    }
}
