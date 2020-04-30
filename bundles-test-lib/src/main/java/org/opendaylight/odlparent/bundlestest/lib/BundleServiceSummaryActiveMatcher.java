/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static org.opendaylight.odlparent.bundlestest.lib.SystemState.Active;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class BundleServiceSummaryActiveMatcher extends BaseMatcher<BundleDiagInfos> {

    @Override
    public boolean matches(Object item) {
        SystemState systemState = ((BundleDiagInfos) item).getSystemState();
        return systemState.equals(Active);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("System ready with all bundles Active");
    }
}