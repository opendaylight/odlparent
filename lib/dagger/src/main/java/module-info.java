/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Common modules for {@code Dagger} integration.
 */
module org.opendaylight.odlparent.dagger {
    exports org.opendaylight.odlparent.dagger;

    requires transitive dagger;
    requires org.slf4j;

    // Annotations
    requires transitive jakarta.inject;
    requires transitive java.compiler;
    requires static transitive org.eclipse.jdt.annotation;
    // Workaround for Eclipse failing
    requires static javax.inject;
}
