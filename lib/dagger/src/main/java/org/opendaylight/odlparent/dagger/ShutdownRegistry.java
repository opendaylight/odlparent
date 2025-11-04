/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A registry for {@link AutoCloseable} objects to close when a component is shut down. This class should be used via
 * {@link ShutdownModule}.
 */
@NonNullByDefault
public final class ShutdownRegistry implements AutoCloseable {
    private sealed interface State {
        // Marker interface
    }

    private static final class Closed implements State {
        static final Closed INSTANCE = new Closed();

        private Closed() {
            // Hidden on purpose
        }
    }

    private record Open(ArrayDeque<AutoCloseable> registrants) implements State {
        Open {
            requireNonNull(registrants);
        }

        Open() {
            this(new ArrayDeque<>());
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownRegistry.class);

    private final UUID uuid = UUID.randomUUID();

    private State state = new Open();

    ShutdownRegistry() {
        // Hidden on purpose
    }

    /**
     * Register a new {@link AutoCloseable} with this registry.
     *
     * @param <T> registrant type
     * @param registrant the registrant
     * @return the registrant
     * @throws IllegalStateException if this registry has been closed
     */
    public <T extends AutoCloseable> T register(final T registrant) {
        final var checked = requireNonNull(registrant);

        synchronized (this) {
            switch (state) {
                case Closed close -> throw new IllegalStateException("Cannot register " + checked + " while closed");
                case Open(var resources) -> resources.add(checked);
            }
        }

        return checked;
    }

    @Override
    public void close() {
        final Iterator<AutoCloseable> resources;

        synchronized (this) {
            resources = switch (state) {
                case Closed closed -> {
                    LOG.debug("Ignoring duplicate close() on {}", uuid);
                    yield Collections.emptyIterator();
                }
                case Open open -> {
                    LOG.debug("Closing instance {} with {} registrants", uuid, open.registrants.size());
                    state = Closed.INSTANCE;
                    yield open.registrants.descendingIterator();
                }
            };
        }

        closeAll(resources);
    }

    @SuppressWarnings("checkstyle:illegalCatch")
    private static void closeAll(final Iterator<AutoCloseable> it) {
        it.forEachRemaining(resource -> {
            try {
                resource.close();
            } catch (Exception e) {
                LOG.warn("Failed to close {}", resource, e);
            }
        });
    }

    @Override
    public String toString() {
        final State local;
        synchronized (this) {
            local = state;
        }
        return "ShutdownRegistry [uuid=" + uuid + ", state=" + local.getClass().getSimpleName() + "]";
    }
}
