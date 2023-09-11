/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import java.util.Arrays;
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
final class ServiceReferenceUtil {
    private ServiceReferenceUtil() {
        // Hidden on purpose
    }

    static Map<String, Object> getProperties(final ServiceReference<?> serviceRef) {
        final var propertyKeys = serviceRef.getPropertyKeys();
        final var properties = new HashMap<String, Object>(propertyKeys.length);
        for (var propertyKey : propertyKeys) {
            var propertyValue = serviceRef.getProperty(propertyKey);
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

    static List<String> getUsingBundleSymbolicNames(final ServiceReference<?> serviceRef) {
        final var usingBundles = serviceRef.getUsingBundles();
        return usingBundles == null ? List.of()
            : Arrays.stream(usingBundles).map(Bundle::getSymbolicName).collect(Collectors.toList());
    }
}
