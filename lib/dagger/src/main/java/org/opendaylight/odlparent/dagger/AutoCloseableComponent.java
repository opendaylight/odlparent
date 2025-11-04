/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import dagger.Component;
import jakarta.inject.Singleton;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * A base interface for {@link Component}s contain one or more {@link AutoCloseable} resources that need cleaning up.
 * Invoking {@link #close()} on the component. Users are expected to combine this interface with
 * {@link ResourceSupportModule}.
 */
@NonNullByDefault
public interface AutoCloseableComponent extends AutoCloseable {
    /**
     * {@return the {@link ResourceSupport} that handles this component's resources}
     */
    @Singleton ResourceSupport resourceSupport();

    @Override
    default void close() {
        resourceSupport().close();
    }
}
