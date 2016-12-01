/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

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
        return systemState.equals(SystemState.Active);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("ready system with all bundles active");
    }
}
