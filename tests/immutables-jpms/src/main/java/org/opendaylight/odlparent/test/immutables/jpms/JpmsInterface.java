/*
 * Copyright (c) 2019 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.test.immutables.jpms;

import org.immutables.value.Value.Immutable;

/**
 * Test interface.
 */
@Immutable
public interface JpmsInterface {
    /**
     * Get foo.
     *
     * @return An integer
     */
    int getFoo();
}
