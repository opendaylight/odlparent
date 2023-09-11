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

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * Unit test for ServiceReferenceUtil.
 *
 * @author Michael Vorburger.ch
 */
public class ServiceReferenceUtilTest {

    @Test
    public void testGetUsingBundleSymbolicNames() {
        assertEquals(List.of(), ServiceReferenceUtil.getUsingBundleSymbolicNames(getServiceReference()));
    }

    @Test
    public void testGetProperties() {
        final var map = ServiceReferenceUtil.getProperties(getServiceReference());
        assertEquals(3, map.size());
        assertEquals("value1", map.get("property1"));
        assertEquals(List.of("value2.1", "value2.2"), map.get("property2"));
        // Unfortunate null;
        assertTrue(map.containsKey("property3"));
        assertNull(map.get("property3"));
    }

    private static ServiceReference<?> getServiceReference() {
        return new TestServiceReference();
    }

    private static final class TestServiceReference implements ServiceReference<Object> {

        private final Map<String, Object> properties = new HashMap<>();

        TestServiceReference() {
            properties.put("property1", "value1");
            properties.put("property2", List.of("value2.1", "value2.2"));
            properties.put("property3", null);
        }

        @Override
        public Object getProperty(String key) {
            return properties.get(key);
        }

        @Override
        public String[] getPropertyKeys() {
            return properties.keySet().stream().toArray(String[]::new);
        }

        @Override
        public Bundle getBundle() {
            return null;
        }

        @Override
        public Bundle[] getUsingBundles() {
            return null;
        }

        @Override
        public boolean isAssignableTo(Bundle bundle, String className) {
            return false;
        }

        @Override
        public int compareTo(Object reference) {
            return 0;
        }

        @Override
        public Dictionary<String, Object> getProperties() {
            return null;
        }

        @Override
        public <A> A adapt(Class<A> type) {
            throw new UnsupportedOperationException();
        }
    }

}
