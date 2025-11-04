/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger.example.dagger;

import dagger.Component;
import jakarta.inject.Singleton;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.opendaylight.odlparent.dagger.AutoCloseableComponent;
import org.opendaylight.odlparent.dagger.ResourceSupportModule;
import org.opendaylight.odlparent.dagger.example.api.ExampleService;

/**
 * A full component extending {@link AutoCloseableComponent}.
 */
@Singleton
@Component(modules = {
    // be sure to include this module
    ResourceSupportModule.class,
    // ... and anything else
    ExampleServiceImplModule.class,
})
@NonNullByDefault
public interface ExampleComponent extends AutoCloseableComponent {
    /**
     * {@return the ExampleService wired into this component}
     */
    ExampleService exampleResource();
}
