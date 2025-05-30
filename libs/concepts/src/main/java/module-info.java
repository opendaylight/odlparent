/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Concepts used widely across OpenDaylight code base.
 */
module org.opendaylight.odlparentt.concepts {
    exports org.opendaylight.odlparent.concepts;

    requires transitive com.google.common;
    requires static transitive org.osgi.framework;
    requires org.slf4j;

    // Annotations
    requires static transitive org.eclipse.jdt.annotation;
    requires static org.osgi.annotation.bundle;
}
