/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.logging.markers;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Common markers for SLF4J. See individual methods for available markers and their use.
 */
public final class Markers {
    private Markers() {
        // Hidden on purpose
    }

    /**
     * Return the SLF4J marker for reporting messages which may contain sensitive information, such as credentials
     * used with an external system. This marker should be used by code components when dumping protocol data, such
     * as NETCONF messages, which may contain user passwords and similar. Logging provider should be configured to
     * redirect messages marked with this marker to a separate location which enjoys a higher level of confidentiality
     * and protection than other messages.
     *
     * @return The confidential marker.
     */
    public static Marker confidential() {
        return Confidential.MARKER;
    }

    // https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    private static final class Confidential {
        static final Marker MARKER = MarkerFactory.getMarker("CONFIDENTIAL");
    }
}
