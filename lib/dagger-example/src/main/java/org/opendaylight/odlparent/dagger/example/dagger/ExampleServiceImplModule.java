/*
 * Copyright (c) 2025 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.dagger.example.dagger;

import com.google.errorprone.annotations.DoNotMock;
import dagger.Module;
import dagger.Provides;
import jakarta.inject.Singleton;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.opendaylight.odlparent.dagger.ResourceSupport;
import org.opendaylight.odlparent.dagger.example.ExampleServiceImpl;
import org.opendaylight.odlparent.dagger.example.api.ExampleService;

/**
 * A Dagger module binding {@link ExampleServiceImpl}.
 */
@Module
@DoNotMock
@NonNullByDefault
public interface ExampleServiceImplModule {
    /**
     * Provide an {@link ExampleService}.
     *
     * @param resourceSupport the {@link ResourceSupport} to register with
     * @return an {@link ExampleService}
     */
    @Provides
    @Singleton
    static ExampleService provideExampleService(ResourceSupport resourceSupport) {
        // this is the magic incantation
        return resourceSupport.register(new ExampleServiceImpl());
    }
}
