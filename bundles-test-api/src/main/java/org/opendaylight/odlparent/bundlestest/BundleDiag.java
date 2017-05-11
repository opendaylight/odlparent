/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/**
 * Obtain OSGi bundles' diagnostics.
 *
 * @author Michael Vorburger.ch
 */
public interface BundleDiag {

    /**
     * Does the equivalent of the "diag" CLI command, and fails the test if anything incl. bundle wiring is NOK.
     *
     * <p>The implementation is based on Karaf's BundleService, and not the BundleStateService,
     * because each Karaf supported DI system (such as Blueprint and Declarative Services, see String constants
     * in BundleStateService), will have a separate BundleStateService.  The BundleService however will
     * contain the combined status of all BundleStateServices.
     *
     * @throws SystemStateFailureException if system has failed to converge on all bundles being up
     *
     * @author Michael Vorburger, based on guidance from Christian Schneider
     */
    void checkBundleDiagInfos(long timeout, TimeUnit timeoutUnit, @Nullable DiagUpdatesListener listener)
            throws SystemStateFailureException;

}
