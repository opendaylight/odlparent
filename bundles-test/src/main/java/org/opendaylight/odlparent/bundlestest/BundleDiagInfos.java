/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.karaf.bundle.core.BundleState;

/**
 * System readyness diagnostic summary information.
 *
 * @author Michael Vorburger
 */
public class BundleDiagInfos {

    // TODO private, final, getters, more in constructor than in TestBundleDiag

    boolean systemIsReady;
    String summaryText;
    List<String> okBundleStateInfoTexts = new ArrayList<>();
    List<String> nokBundleStateInfoTexts = new ArrayList<>();
    Map<BundleState, Integer> bundleStatesCounters = new EnumMap<>(BundleState.class);

    public BundleDiagInfos() {
        for (BundleState bundleState : BundleState.values()) {
            bundleStatesCounters.put(bundleState, 0);
        }
    }
}
