/*
 * Copyright (c) 2019 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
module org.opendaylight.odlparent.test.immutables.jpms {
    exports org.opendaylight.odlparent.test.immutables.jpms;

    requires static com.github.spotbugs.annotations;
    requires static transitive org.immutables.value.annotations;
}
