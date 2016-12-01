/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * Unit test for ServiceReferenceUtil.
 *
 * @author Michael Vorburger
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
                "property2", Arrays.asList(new String[] { "value2.1", "value2.2" }));
    }

    private ServiceReference<?> getServiceReference() {
        return new ServiceReference<Object>() {

            @Override
            public Object getProperty(String key) {
                if ("property1".equals(key)) {
                    return "value1";
                } else if ("property2".equals(key)) {
                    return new String[] { "value2.1", "value2.2" };
                } else {
                    return null;
                }
            }

            @Override
            public String[] getPropertyKeys() {
                return new String[] { "property1", "property2"};
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
        };
    }

}
