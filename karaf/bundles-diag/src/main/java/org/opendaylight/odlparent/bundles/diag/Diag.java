/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 * A snapshot of bundle diagnostics.
 */
public interface Diag {

    List<DiagBundle> bundles();

    // Iteration order guaranteed to be ordered by ContainerState.ordinal()
    default Map<ContainerState, Integer> containerStateFrequencies() {
        var frequencies = new EnumMap<ContainerState, Integer>(ContainerState.class);
        for (var bundleState : ContainerState.values()) {
            frequencies.put(bundleState, 0);
        }

        for (var bundle : bundles()) {
            frequencies.compute(bundle.serviceState().containerState(), (key, counter) -> counter + 1);
        }
        return frequencies;
    }

    // log a delta from some previous bundles
    void logDelta(Logger logger, Diag prevDiag);

    // log services
    void logServices(Logger logger);

    // log bundle state details for troubleshooting
    void logState(Logger logger);
}
