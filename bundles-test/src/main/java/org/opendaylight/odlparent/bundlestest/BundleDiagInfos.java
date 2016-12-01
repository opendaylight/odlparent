/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import static org.apache.karaf.bundle.core.BundleState.Failure;
import static org.apache.karaf.bundle.core.BundleState.GracePeriod;
import static org.apache.karaf.bundle.core.BundleState.Installed;
import static org.apache.karaf.bundle.core.BundleState.Waiting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * System readyness diagnostic summary information.
 *
 * @author Michael Vorburger.ch
 */
public class BundleDiagInfos {

    private final List<String> okBundleStateInfoTexts = new ArrayList<>();
    private final List<String> nokBundleStateInfoTexts = new ArrayList<>();
    private final Map<BundleState, Integer> bundleStatesCounters = new EnumMap<>(BundleState.class);

    public static BundleDiagInfos getBundleDiagInfos(BundleContext bundleContext, BundleService bundleService) {
        BundleDiagInfos bundleInfos = new BundleDiagInfos();
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String bundleSymbolicName = bundle.getSymbolicName();
            BundleInfo karafBundleInfo = bundleService.getInfo(bundle);
            BundleState karafBundleState = karafBundleInfo.getState();

            bundleInfos.bundleStatesCounters.compute(karafBundleState, (key, counter) -> counter + 1);

            String bundleStateDiagText = "OSGi state = " + bundle.getState()
                + ", Karaf bundleState = " + karafBundleState;
            String diagText = bundleService.getDiag(bundle);
            if (diagText != null && !diagText.isEmpty()) {
                bundleStateDiagText += ", due to: " + diagText;
            }

            // BundleState comparison as in Karaf's "diag" command,
            // see https://github.com/apache/karaf/blob/master/bundle/core/src/main/java/org/apache/karaf/bundle/command/Diag.java
            // TODO Unknown? Starting, still, shouldn't be any?
            if (karafBundleState == Failure || karafBundleState == Waiting
                    || karafBundleState == GracePeriod || karafBundleState == Installed) {
                String msg = "NOK " + bundleSymbolicName + ": " + bundleStateDiagText;
                bundleInfos.nokBundleStateInfoTexts.add(msg);
            } else {
                String msg = "OK " + bundleSymbolicName + ": " + bundleStateDiagText;
                bundleInfos.okBundleStateInfoTexts.add(msg);
            }
        }
        return bundleInfos;
    }

    public BundleDiagInfos() {
        for (BundleState bundleState : BundleState.values()) {
            bundleStatesCounters.put(bundleState, 0);
        }
    }

    public SystemState getSystemState() {
        if (bundleStatesCounters.get(BundleState.Failure) > 0) {
            return SystemState.Failure;
        } else if (bundleStatesCounters.get(BundleState.Stopping) > 0) {
            return SystemState.Stopping;
        } else if (bundleStatesCounters.get(BundleState.Installed) == 0
                && bundleStatesCounters.get(BundleState.Resolved) == 0
                && bundleStatesCounters.get(BundleState.Unknown) == 0
                && bundleStatesCounters.get(BundleState.GracePeriod) == 0
                && bundleStatesCounters.get(BundleState.Waiting) == 0
                && bundleStatesCounters.get(BundleState.Starting) == 0
                // BundleState.Active *should* be == total # of bundles
                && bundleStatesCounters.get(BundleState.Stopping) == 0
                && bundleStatesCounters.get(BundleState.Failure) == 0) {
            return SystemState.Active;
        } else {
            return SystemState.Booting;
        }
    }

    public String getFullDiagnosticText() {
        StringBuilder sb = new StringBuilder(getSummaryText());
        for (String nokBundleStateInfoText : getNokBundleStateInfoTexts()) {
            sb.append('\n');
            sb.append(nokBundleStateInfoText);
        }
        return sb.toString();
    }

    public String getSummaryText() {
        return "diag: " + getSystemState() + " " + bundleStatesCounters.toString();
    }

    public List<String> getNokBundleStateInfoTexts() {
        return Collections.unmodifiableList(nokBundleStateInfoTexts);
    }

    public List<String> getOkBundleStateInfoTexts() {
        return Collections.unmodifiableList(okBundleStateInfoTexts);
    }

    @Override
    public String toString() {
        return getFullDiagnosticText();
    }
}
