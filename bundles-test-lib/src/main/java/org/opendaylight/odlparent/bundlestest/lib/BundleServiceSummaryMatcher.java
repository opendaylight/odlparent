/*
 * SPDX-License-Identifier: EPL-1.0
 *
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static org.opendaylight.odlparent.bundlestest.lib.SystemState.Active;
import static org.opendaylight.odlparent.bundlestest.lib.SystemState.Failure;
import static org.opendaylight.odlparent.bundlestest.lib.SystemState.Stopping;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Hamcrest Matcher for BundleDiagInfosImpl.
 *
 * @author Michael Vorburger.ch
 */
// intentionally just package-local
class BundleServiceSummaryMatcher extends BaseMatcher<BundleDiagInfos> {

    @Override
    public boolean matches(Object item) {
        SystemState systemState = ((BundleDiagInfos) item).getSystemState();
        return systemState.equals(Active) || systemState.equals(Stopping) || systemState.equals(Failure);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("system either ready with all bundles Active, "
                + "or Stopping or Failure (but not still booting in GracePeriod, Waiting, Starting, Unknown;"
                + "but just Resolved and some exceptional Installed is OK)");
    }
}
