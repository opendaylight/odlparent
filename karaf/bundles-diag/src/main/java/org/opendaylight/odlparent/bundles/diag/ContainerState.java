/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag;

import static java.util.Objects.requireNonNull;

import org.apache.karaf.bundle.core.BundleState;

/**
 * Possible states of a container. Mirrors {@link BundleState}.
 */
public enum ContainerState {
    INSTALLED("Installed"),
    RESOLVED("Resolved"),
    UNKNOWN("Unknown"),
    GRACE_PERIOD("GracePeriod"),
    WAITING("Waiting"),
    STARTING("Starting"),
    ACTIVE("Active"),
    STOPPING("Stopping"),
    FAILURE("Failure");

    private final String reportingName;

    ContainerState(String reportingName) {
        this.reportingName = requireNonNull(reportingName);
    }

    /**
     * Return the reporting name, matching {@link BundleState#name()}.
     *
     * @return the reporting name
     */
    public String reportingName() {
        return reportingName;
    }
}
