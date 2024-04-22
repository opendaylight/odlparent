/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag.spi;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.opendaylight.odlparent.bundles.diag.ContainerState;
import org.opendaylight.odlparent.bundles.diag.Diag;
import org.opendaylight.odlparent.bundles.diag.DiagBundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default Diag implementation
 */
record DefaultDiag(BundleContext bundleContext, List<DiagBundle> bundles) implements Diag {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDiag.class);
    private static final Map<String, ContainerState> ALLOWED_STATES = Map.of(
        "slf4j.log4j12", ContainerState.INSTALLED,
        // ODLPARENT-144
        "org.apache.karaf.scr.management", ContainerState.WAITING);

    DefaultDiag {
        requireNonNull(bundleContext);
        requireNonNull(bundles);
    }

    @Override
    public void logState() {
        //  try {
        //      logOSGiServices();
        //  } catch (IllegalStateException e) {
        //      LOG.warn("logOSGiServices() failed (never mind); too late during shutdown already?", e);
        //  }

        final var okBundles = new ArrayList<DiagBundle>();
        final var allowedBundles = new ArrayList<DiagBundle>();
        final var nokBundles = new ArrayList<DiagBundle>();

        for (var bundle : bundles) {
            final var serviceState = bundle.serviceState();
            // BundleState comparison as in Karaf's "diag" command, see
            // https://github.com/apache/karaf/blob/master/bundle/core/src/main/java/org/apache/karaf/bundle/command/Diag.java
            // but we intentionally, got a little further than Karaf's "diag" command, and instead of only checking some
            // states, we check what's really Active, but accept that some remain just Resolved:
            final var containerState = serviceState.containerState();

            final var list = switch (containerState) {
                case ACTIVE, RESOLVED -> okBundles;
                default -> {
                    final var symbolicName = bundle.symbolicName();
                    yield symbolicName != null && containerState.equals(ALLOWED_STATES.get(symbolicName))
                        ? allowedBundles : nokBundles;
                }
            };
            list.add(bundle);
        }

        if (LOG.isInfoEnabled()) {
            for (var bundle : okBundles) {
                LOG.info("OK {}:{} {}/{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    serviceStateString(bundle));
            }
        }
        if (LOG.isWarnEnabled()) {
            for (var bundle : allowedBundles) {
                LOG.warn("ALLOW {}:{} {}/{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    serviceStateString(bundle));
            }
        }
        if (LOG.isErrorEnabled()) {
            for (var bundle : nokBundles) {
                LOG.error("NOK {}:{} {}/{}", bundle.symbolicName(), bundle.version(), bundle.frameworkState(),
                    serviceStateString(bundle));
            }
        }
    }

    private static String serviceStateString(final DiagBundle bundle) {
        final var serviceState = bundle.serviceState();
        final var diag = serviceState.diag();
        return serviceState.containerState().reportingName() + (diag.isBlank() ? "" : " with " + diag);
    }
}
