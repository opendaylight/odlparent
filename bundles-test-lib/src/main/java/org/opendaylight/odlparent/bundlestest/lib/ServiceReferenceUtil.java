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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * Utilities for OSGi's {@link ServiceReference}.
 *
 * @author Michael Vorburger.ch
 */
// intentionally just package-local
class ServiceReferenceUtil {

    public Map<String, Object> getProperties(ServiceReference<?> serviceRef) {
        String[] propertyKeys = serviceRef.getPropertyKeys();
        Map<String, Object> properties = new HashMap<>(propertyKeys.length);
        for (String propertyKey : propertyKeys) {
            Object propertyValue = serviceRef.getProperty(propertyKey);
            if (propertyValue != null) {
                if (propertyValue.getClass().isArray()) {
                    propertyValue = Arrays.asList((Object[]) propertyValue);
                }
            }
            // maintain the null value in the property map anyway
            properties.put(propertyKey, propertyValue);
        }
        return properties;
    }

    public List<String> getUsingBundleSymbolicNames(ServiceReference<?> serviceRef) {
        Bundle[] usingBundles = serviceRef.getUsingBundles();
        if (usingBundles == null) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(usingBundles).map(Bundle::getSymbolicName).collect(Collectors.toList());
        }
    }

}
