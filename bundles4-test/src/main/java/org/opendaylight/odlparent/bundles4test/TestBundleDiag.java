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
import org.osgi.framework.BundleContext;

/**
 * Utility.
 *
 * @deprecated use the non-static {@link BundleDiag} instead of this.
 *
 * @author Michael Vorburger.ch
 */
@Deprecated
public class TestBundleDiag {

    public static void checkBundleDiagInfos(BundleContext bundleContext, long timeout, TimeUnit timeoutUnit) {
        BundleDiagImpl diag = new BundleDiagImpl(bundleContext);
        try {
            diag.checkBundleDiagInfos(timeout, timeoutUnit);
        } finally {
            diag.close();
        }
    }

}
