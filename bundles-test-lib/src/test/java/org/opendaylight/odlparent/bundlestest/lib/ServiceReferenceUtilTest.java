/*
 * SPDX-License-Identifier: EPL-1.0
 *
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
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
        assertThat(new ServiceReferenceUtil().getUsingBundleSymbolicNames(getServiceReference())).isEmpty();
    }

    @Test
    public void testGetProperties() {
        assertThat(new ServiceReferenceUtil().getProperties(getServiceReference())).containsExactly(
                "property1", "value1",
                "property2", Arrays.asList(new String[] { "value2.1", "value2.2" }),
                "property3", null);
    }

    private static ServiceReference<?> getServiceReference() {
        return new TestServiceReference();
    }

    private static final class TestServiceReference implements ServiceReference<Object> {

        private final Map<String, Object> properties = Maps.newHashMap();

        TestServiceReference() {
            properties.put("property1", "value1");
            properties.put("property2", Lists.newArrayList("value2.1", "value2.2"));
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
    }

}
