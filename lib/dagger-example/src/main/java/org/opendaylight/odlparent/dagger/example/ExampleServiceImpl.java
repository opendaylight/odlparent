/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger.example;

/**
 * An {@link ExampleService} implementation that needs explicit cleanup.
 */
final class ExampleServiceImpl implements ExampleService, AutoCloseable {
    private int interactionCount;

    @Override
    public long interactionCount() {
        return interactionCount;
    }

    @Override
    public void close() {
        interactionCount++;
    }
}
