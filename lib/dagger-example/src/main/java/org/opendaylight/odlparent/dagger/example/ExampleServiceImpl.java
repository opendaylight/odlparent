/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger.example;

import org.opendaylight.odlparent.dagger.example.api.ExampleService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * An {@link ExampleService} implementation that needs explicit cleanup. This class is public, but in reality it is only
 * accessible from within this module. Annotations used here are defined in
 * <a href="https://docs.osgi.org/specification/osgi.cmpn/8.0.0/service.component.html">OSGi Declarative Services</a>
 * and are processed at compile-time to generate component description. That description is used at run-time by
 * Service Component Runtime to affect instantiation.
 */
@Component
public final class ExampleServiceImpl implements AutoCloseable, ExampleService {
    private int interactionCount;

    @Activate
    public ExampleServiceImpl() {
        // Nothing else
    }

    @Deactivate
    @Override
    public void close() {
        interactionCount++;
    }

    @Override
    public long interactionCount() {
        return interactionCount;
    }
}
