/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

import java.util.concurrent.TimeUnit;
import org.opendaylight.odlparent.bundles4test.internal.BundleDiagImpl;
import org.opendaylight.odlparent.bundlestest.BundleDiag;
import org.opendaylight.odlparent.bundlestest.SystemStateFailureException;
import org.osgi.framework.BundleContext;

/**
 * Utility for OSGi bundles' diagnostics.
 *
 * @deprecated use the non-static {@link BundleDiag} instead of this.
 *
 * @author Michael Vorburger.ch
 */
@Deprecated
public final class TestBundleDiag {

    private TestBundleDiag() { }

    public static void checkBundleDiagInfos(BundleContext bundleContext, long timeout, TimeUnit timeoutUnit)
            throws SystemStateFailureException {
        try (BundleDiagImpl diag = new BundleDiagImpl(bundleContext)) {
            diag.checkBundleDiagInfos(timeout, timeoutUnit, null);
        }
    }

}
