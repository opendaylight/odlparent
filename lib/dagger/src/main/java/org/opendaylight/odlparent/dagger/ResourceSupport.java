/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import com.google.errorprone.annotations.ThreadSafe;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * A registry for {@link AutoCloseable} resources to close when a component is shut down. This class should be used via
 * {@link ResourceSupportModule}.
 */
@ThreadSafe
@NonNullByDefault
public abstract sealed class ResourceSupport permits DefaultResourceSupport {
    /**
     * Default constructor.
     */
    ResourceSupport() {
        // Hidden onm purpose
    }

    /**
     * Register an {@link AutoCloseable} resource with this registry.
     *
     * @param <T> resource type
     * @param resource the resource
     * @return the resource
     * @throws IllegalStateException if this instance has been closed
     */
    public abstract <T extends AutoCloseable> T register(T resource);

    /**
     * Close this instance, calling {@link AutoCloseable#close()} on all registered resource in the reverse order
     * of registration.
     */
    abstract void close();

    @Override
    public abstract String toString();
}
