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
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;

/**
 * A {@link Bundle} that has been diagnosed.
 *
 * @param bundleId {@link Bundle#getBundleId()}
 * @param name bundle name
 * @param symbolicName bundle symbolic name
 * @param version bundle version, if {@code "0.0.0"} if not otherwise specified
 * @param frameworkState current {@link FrameworkState}
 * @param serviceState current {@link ServiceState}
 */
public record DiagBundle(
        long bundleId,
        String name,
        @Nullable String symbolicName,
        String version,
        FrameworkState frameworkState,
        ServiceState serviceState) implements Serializable {
    // FIXME: remove once we have spotbugs-4.8.5+
    @java.io.Serial
    private static final long serialVersionUID = 0;

    public DiagBundle {
        // FIXME: handle pax-exam's test probe bundle with null name and version
        // requireNonNull(name);
        requireNonNull(version);
        requireNonNull(frameworkState);
        requireNonNull(serviceState);
    }
}
