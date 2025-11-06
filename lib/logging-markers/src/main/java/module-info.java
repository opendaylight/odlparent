/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Common {@link org.slf4j.Marker}s. Individual markers are available via static methods exposed from
 * {@link org.opendaylight.odlparent.logging.markers.Markers}.
 */
@org.jspecify.annotations.NullMarked
module org.opendaylight.odlparent.logging.markers {
    exports org.opendaylight.odlparent.logging.markers;

    requires transitive org.slf4j;

    // Annotations
    requires static org.jspecify;
    requires static org.osgi.annotation.bundle;
}