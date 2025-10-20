/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag.ri;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.opendaylight.odlparent.bundles.diag.ContainerState;
import org.opendaylight.odlparent.bundles.diag.Diag;
import org.opendaylight.odlparent.bundles.diag.DiagBundle;
import org.opendaylight.odlparent.bundles.diag.DiagProvider;
import org.opendaylight.odlparent.bundles.diag.FrameworkState;
import org.opendaylight.odlparent.bundles.diag.ServiceState;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Default implementation of {@link DiagProvider}.
 */
@Component
@NonNullByDefault
public final class DefaultDiagProvider implements DiagProvider {
    private final BundleService bundleService;
    private final BundleContext bundleContext;

    @Activate
    public DefaultDiagProvider(@Reference BundleService bundleService, BundleContext bundleContext) {
        this.bundleService = requireNonNull(bundleService);
        this.bundleContext = requireNonNull(bundleContext);
    }

    @Override
    public Diag currentDiag() {
        return new DefaultDiag(bundleContext, Arrays.stream(bundleContext.getBundles())
            .map(bundle -> {
                var info = bundleService.getInfo(bundle);
                return new DiagBundle(bundle.getBundleId(), info.getName(), bundle.getSymbolicName(),
                    // Note: not info.getVersion() as that can return null
                    bundle.getVersion().toString(), frameworkStateOf(bundle),
                    new ServiceState(containerStateOf(info), bundleService.getDiag(bundle)));
            })
            .sorted(Comparator.comparingLong(DiagBundle::bundleId))
            .collect(Collectors.toList()));
    }

    /**
     * Return the {@link FrameworkState} for specified {@link Bundle}.
     *
     * @param bundle a {@link Bundle}
     * @return A {@link FrameworkState}
     */
    private static FrameworkState frameworkStateOf(Bundle bundle) {
        int state = bundle.getState();
        return switch (state) {
            case Bundle.INSTALLED -> FrameworkState.INSTALLED;
            case Bundle.RESOLVED -> FrameworkState.RESOLVED;
            case Bundle.STARTING -> FrameworkState.STARTING;
            case Bundle.ACTIVE -> FrameworkState.ACTIVE;
            case Bundle.STOPPING -> FrameworkState.STOPPING;
            case Bundle.UNINSTALLED -> FrameworkState.UNINSTALLED;
            default -> new FrameworkState(state + "???");
        };
    }

    /**
     * Return the {@link ContainerState} for specified {@link BundleInfo}.
     *
     * @param info a {@link BundleInfo}
     * @return A {@link ContainerState}
     */
    private static ContainerState containerStateOf(BundleInfo info) {
        return switch (info.getState()) {
            case Active -> ContainerState.ACTIVE;
            case Failure -> ContainerState.FAILURE;
            case GracePeriod -> ContainerState.GRACE_PERIOD;
            case Installed -> ContainerState.INSTALLED;
            case Resolved -> ContainerState.RESOLVED;
            case Starting -> ContainerState.STARTING;
            case Stopping -> ContainerState.STOPPING;
            case Unknown -> ContainerState.UNKNOWN;
            case Waiting -> ContainerState.WAITING;
        };
    }
}
