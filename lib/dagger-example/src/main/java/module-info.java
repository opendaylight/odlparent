/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Example module showcasing dual OSGI and Dagger support.
 */
module org.opendaylight.odlparent.dagger.example {
    exports org.opendaylight.odlparent.dagger.example.api;
    exports org.opendaylight.odlparent.dagger.example.dagger;

    requires transitive org.opendaylight.odlparent.dagger;

    // Annotations
    requires static transitive org.eclipse.jdt.annotation;
    requires static org.osgi.service.component.annotations;
    requires static org.osgi.annotation.bundle;
}
