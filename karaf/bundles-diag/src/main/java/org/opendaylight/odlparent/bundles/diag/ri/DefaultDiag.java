/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag.ri;

import static java.util.Objects.requireNonNull;

import com.google.errorprone.annotations.Var;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.odlparent.bundles.diag.ContainerState;
import org.opendaylight.odlparent.bundles.diag.Diag;
import org.opendaylight.odlparent.bundles.diag.DiagBundle;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;

/**
 * The default {@link Diag} implementation.
 */
@NonNullByDefault
record DefaultDiag(BundleContext bundleContext, List<DiagBundle> bundles) implements Diag {
    private static final Map<String, ContainerState> ALLOWED_STATES = Map.of(
        "slf4j.log4j12", ContainerState.INSTALLED,
        // ODLPARENT-144
        "org.apache.karaf.scr.management", ContainerState.WAITING);

    DefaultDiag {
        requireNonNull(bundleContext);
        requireNonNull(bundles);
    }

    @Override
    public void logDelta(Logger logger, Diag prevDiag) {
        var prevBundles = new ArrayDeque<>(prevDiag.bundles());
        // Log current state ...
        for (var bundle : bundles) {
            if (!bundle.equals(find(logger, prevBundles, bundle.bundleId()))) {
                var serviceState = bundle.serviceState();
                logger.debug("Updated {}:{} {}/{}[{}]", bundle.symbolicName(), bundle.version(),
                    bundle.frameworkState(), serviceState.containerState().reportingName(), serviceState.diag());
            }
        }

        // everything else is not present
        prevBundles.forEach(bundle -> logger.info("{} no longer present", bundle));
    }

    static @Nullable DiagBundle find(Logger logger, ArrayDeque<DiagBundle> bundles, long bundleId) {
        for (var bundle = bundles.poll(); bundle != null; bundle = bundles.poll()) {
            var id = bundle.bundleId();
            if (id == bundleId) {
                return bundle;
            } else if (id > bundleId) {
                bundles.addFirst(bundle);
                break;
            }
            logger.info("{} no longer present", bundle);
        }
        return null;
    }

    @Override
    public void logServices(Logger logger) {
        logger.info("""
            Now going to log all known services, to help diagnose root cause of diag failure BundleService reported \
            bundle(s) which are not active""");
        try {
            for (var serviceRef : bundleContext.getAllServiceReferences(null, null)) {
                var bundle = serviceRef.getBundle();
                // serviceRef.getBundle() can return null if the bundle was destroyed
                if (bundle != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("{} defines OSGi Service {} used by {}", bundle.getSymbolicName(),
                            getProperties(serviceRef), getUsingBundleSymbolicNames(serviceRef));
                    }
                } else {
                    logger.trace("skipping reporting service reference as the underlying bundle is null");
                }
            }
        } catch (InvalidSyntaxException e) {
            logger.error("Failed due to InvalidSyntaxException", e);
        }
    }

    // Visible for testing
    static Map<String, Object> getProperties(ServiceReference<?> serviceRef) {
        var propertyKeys = serviceRef.getPropertyKeys();
        var properties = new HashMap<String, Object>(propertyKeys.length);
        for (var propertyKey : propertyKeys) {
            @Var var propertyValue = serviceRef.getProperty(propertyKey);
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

    // Visible for testing
    static List<String> getUsingBundleSymbolicNames(ServiceReference<?> serviceRef) {
        var usingBundles = serviceRef.getUsingBundles();
        return usingBundles == null ? List.of()
            : Arrays.stream(usingBundles).map(Bundle::getSymbolicName).collect(Collectors.toList());
    }

    @Override
    public void logState(Logger logger) {
        try {
            logServices(logger);
        } catch (IllegalStateException e) {
            logger.warn("logOSGiServices() failed (never mind); too late during shutdown already?", e);
        }

        var okBundles = new ArrayList<DiagBundle>();
        var allowedBundles = new ArrayList<DiagBundle>();
        var nokBundles = new ArrayList<DiagBundle>();

        for (var bundle : bundles) {
            var serviceState = bundle.serviceState();
            // BundleState comparison as in Karaf's "diag" command, see
            // https://github.com/apache/karaf/blob/master/bundle/core/src/main/java/org/apache/karaf/bundle/command/Diag.java
            // but we intentionally, got a little further than Karaf's "diag" command, and instead of only checking some
            // states, we check what's really Active, but accept that some remain just Resolved:
            var containerState = serviceState.containerState();

            var list = switch (containerState) {
                case ACTIVE, RESOLVED -> okBundles;
                default -> {
                    var symbolicName = bundle.symbolicName();
                    yield symbolicName != null && containerState.equals(ALLOWED_STATES.get(symbolicName))
                        ? allowedBundles : nokBundles;
                }
            };
            list.add(bundle);
        }

        if (logger.isInfoEnabled()) {
            for (var bundle : okBundles) {
                logger.info("OK {}:{} {}/{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    bundle.serviceState().containerState().reportingName());
            }
        }
        if (logger.isWarnEnabled()) {
            for (var bundle : allowedBundles) {
                logger.warn("WHITELISTED {}:{} {}/{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    bundle.serviceState().containerState().reportingName());
            }
        }
        if (logger.isErrorEnabled()) {
            for (var bundle : nokBundles) {
                var serviceState = bundle.serviceState();
                var diag = serviceState.diag();
                var diagStr = diag.isBlank() ? "" : ", due to: " + diag;
                logger.error("NOK {}:{} {}/{}{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    bundle.serviceState().containerState().reportingName(), diagStr);
            }
        }
    }
}
