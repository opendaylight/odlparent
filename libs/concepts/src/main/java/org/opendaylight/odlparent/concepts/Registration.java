/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.concepts;

import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;
import java.lang.ref.Reference;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Class representing a registration. Such a registration is a proper resource and should be cleaned up when no longer
 * required.
 */
@NonNullByDefault
public interface Registration extends AutoCloseable {
    /**
     * Unregisters the object. This operation is required not to invoke blocking operations. Implementations which
     * require interaction with outside world must provide guarantees that any work is done behind the scenes and
     * the unregistration process looks as if it has already succeeded once this method returns.
     *
     * <p>The above requirement does not necessarily mean that all interactions with the registered entity seize before
     * this method returns, but they should complete within a reasonable time frame.
     *
     * <p>While the interface contract allows an implementation to ignore the occurrence of RuntimeExceptions,
     * implementations are strongly encouraged to deal with such exceptions internally and to ensure invocations of
     * this method do not fail in such circumstances.
     */
    @Override
    void close();

    static BaseRegistration of(final AutoCloseable autoCloseable) {
        // Note: we do not check for the argument being BaseRegistration on purpose because we guarantee identity-based
        //       equality. That implies the argument to this method and the result of this method could be stored in
        //       the same Set -- in which case they need to be treated as two separate objects.
        return new ResourceRegistration(autoCloseable);
    }

    static BaseRegistration of(final Cleanable cleanable) {
        return new CleanableRegistration(cleanable);
    }

    static BaseRegistration of(final Cleaner cleaner, final Object obj, final Runnable action) {
        return of(cleaner.register(obj, action));
    }

    static BaseRegistration of(final Reference<?> reference) {
        return new ReferenceRegistration(reference);
    }
}
