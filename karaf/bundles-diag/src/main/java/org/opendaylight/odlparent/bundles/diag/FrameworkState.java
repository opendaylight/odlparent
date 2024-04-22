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
import org.osgi.framework.Bundle;

/**
 * The equivalent of {@link Bundle#getState()}, i.e. what
 * <a href="https://docs.osgi.org/specification/osgi.core/8.0.0/framework.api.html">OSGi Framework API</a> reports.
 */
public record FrameworkState(String symbolicName) implements Serializable {
    public static final FrameworkState INSTALLED = new FrameworkState("Installed");
    public static final FrameworkState RESOLVED = new FrameworkState("Resolved");
    public static final FrameworkState STARTING = new FrameworkState("Starting");
    public static final FrameworkState ACTIVE = new FrameworkState("Active");
    public static final FrameworkState STOPPING = new FrameworkState("Stopping");
    public static final FrameworkState UNINSTALLED = new FrameworkState("Uninstalled");

    // FIXME: remove once we have spotbugs-4.8.5+
    @java.io.Serial
    private static final long serialVersionUID = 0;

    public FrameworkState {
        requireNonNull(symbolicName);
    }
}
