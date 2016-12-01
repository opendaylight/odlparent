/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import static org.opendaylight.odlparent.bundlestest.SystemState.Active;
import static org.opendaylight.odlparent.bundlestest.SystemState.Failure;
import static org.opendaylight.odlparent.bundlestest.SystemState.Stopping;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Hamcrest Matcher for {@link BundleDiagInfos}.
 *
 * @author Michael Vorburger.ch
 */
class BundleServiceSummaryMatcher extends BaseMatcher<BundleDiagInfos> {

    @Override
    public boolean matches(Object item) {
        SystemState systemState = ((BundleDiagInfos) item).getSystemState();
        return systemState.equals(Active) || systemState.equals(Stopping) || systemState.equals(Failure);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("system either ready with all bundles active, "
                + "or stopping or failed (but not still booting)");
    }
}
