/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

import java.util.concurrent.TimeUnit;
import org.apache.karaf.bundle.core.BundleService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Utility for OSGi bundles' diagnostics.
 *
 * @deprecated use the non-static {@link TestBundleDiag} instead of this.
 *
 * @author Michael Vorburger.ch
 */
@Deprecated
public final class TestBundleDiag {

    // TODO This @Deprecated class is about to be removed, it's just kept here so not to cause a compilation error
    // in infrautils.ready, which already uses this, but which will be changed ASAP

    private TestBundleDiag() { }

    public static void checkBundleDiagInfos(BundleContext bundleContext, long timeout, TimeUnit timeoutUnit)
            throws SystemStateFailureException {
        ServiceReference<BundleService> bundleServiceReference = bundleContext.getServiceReference(BundleService.class);
        try {
            BundleService bundleService = bundleContext.getService(bundleServiceReference);
            org.opendaylight.odlparent.bundlestest.lib.TestBundleDiag diag =
                    new org.opendaylight.odlparent.bundlestest.lib.TestBundleDiag(bundleContext, bundleService);
            diag.checkBundleDiagInfos(timeout, timeoutUnit);
        } catch (org.opendaylight.odlparent.bundlestest.lib.SystemStateFailureException e) {
            throw new SystemStateFailureException(e.getMessage(), e.getBundleDiagInfos(), e.getCause());
        } finally {
            if (bundleServiceReference != null) {
                bundleContext.ungetService(bundleServiceReference);
            }
        }
    }

}
