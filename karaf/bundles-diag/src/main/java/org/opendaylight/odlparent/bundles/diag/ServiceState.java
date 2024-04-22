/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles.diag;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

/**
 * Container state of a bundle.
 */
public record ServiceState(ContainerState containerState, String diag) implements Serializable {
    // FIXME: remove once we have spotbugs-4.8.5+
    @java.io.Serial
    private static final long serialVersionUID = 0;

    public ServiceState {
        requireNonNull(containerState);
        requireNonNull(diag);
    }
}
