/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A registry for {@link AutoCloseable} resources to close when a component is shut down. This class should be used via
 * {@link ResourceSupportModule}.
 */
@NonNullByDefault
final class DefaultResourceSupport extends ResourceSupport {
    private sealed interface State {
        // Marker interface
    }

    private static final class Closed implements State {
        static final Closed INSTANCE = new Closed();

        private Closed() {
            // Hidden on purpose
        }
    }

    private record Open(ArrayList<AutoCloseable> resources) implements State {
        Open {
            requireNonNull(resources);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(DefaultResourceSupport.class);

    private final UUID uuid = UUID.randomUUID();

    private State state = new Open(new ArrayList<>());

    DefaultResourceSupport() {
        // Hidden on purpose
    }

    /**
     * Register an {@link AutoCloseable} resource with this registry.
     *
     * @param <T> resource type
     * @param resource the resource
     * @return the resource
     * @throws IllegalStateException if this instance has been closed
     */
    @Override
    public <T extends AutoCloseable> T register(final T resource) {
        final var checked = requireNonNull(resource);

        synchronized (this) {
            switch (state) {
                case Closed close -> throw new IllegalStateException("Cannot register " + checked + " while closed");
                case Open(var resources) -> resources.add(checked);
            }
        }

        return checked;
    }

    @Override
    void close() {
        final Iterator<AutoCloseable> it;

        synchronized (this) {
            switch (state) {
                case Closed closed -> {
                    LOG.debug("Ignoring duplicate close() on {}", uuid);
                    return;
                }
                case Open(var resources) -> {
                    LOG.debug("Closing instance {} with {} resources", uuid, resources.size());
                    state = Closed.INSTANCE;
                    it = resources.reversed().iterator();
                }
            }
        }

        it.forEachRemaining(DefaultResourceSupport::closeDefensively);

        LOG.debug("Instance {} closed", uuid);
    }

    @SuppressWarnings("checkstyle:illegalCatch")
    private static void closeDefensively(final AutoCloseable resource) {
        try {
            resource.close();
        } catch (Exception e) {
            LOG.warn("Failed to close resource {}", resource, e);
        }
    }

    @Override
    public String toString() {
        final State local;
        synchronized (this) {
            local = state;
        }
        return "ResourceSupport [uuid=" + uuid + ", state=" + local.getClass().getSimpleName() + "]";
    }
}
