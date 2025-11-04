/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * A Dagger module wiring in {@link ResourceSupport}. A typical user would look like
 * {@snippet :
 *     @Singleton
 *     @Component(modules = {
 *         ResourceSupportModule.class,
 *         ...
 *     })
 *     interface ShutdownComponent extends AutoCloseableComponent {
 *
 *     }
 * }
 */
@Module
@NonNullByDefault
public interface ResourceSupportModule {
    /**
     * {@return a singleton {@link ResourceSupport}}
     */
    @Provides
    @Singleton
    static ResourceSupport provideResourceSupport() {
        return new ResourceSupport();
    }
}
