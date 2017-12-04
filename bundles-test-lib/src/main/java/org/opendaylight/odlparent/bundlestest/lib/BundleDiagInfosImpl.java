/*
 * Copyright (c) 2016, 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static org.apache.karaf.bundle.core.BundleState.Active;
import static org.apache.karaf.bundle.core.BundleState.Installed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * {@link BundleDiagInfos} implementation.
 *
 * @author Michael Vorburger.ch
 */
// intentionally just package-local
final class BundleDiagInfosImpl implements BundleDiagInfos {
    private static final long serialVersionUID = 1L;

    private static final Map<String, BundleState> WHITELISTED_BUNDLES;

    static {
        WHITELISTED_BUNDLES = new HashMap<>();
        WHITELISTED_BUNDLES.put("slf4j.log4j12", Installed);
    }

    private final List<String> okBundleStateInfoTexts;
    private final List<String> nokBundleStateInfoTexts;
    private final List<String> whitelistedBundleStateInfoTexts;
    private final Map<BundleState, Integer> bundleStatesCounters;
    private final Map<BundleSymbolicNameWithVersion, BundleState> bundlesStateMap;

    private BundleDiagInfosImpl(List<String> okBundleStateInfoTexts, List<String> nokBundleStateInfoTexts,
            List<String> whitelistedBundleStateInfoTexts, Map<BundleState, Integer> bundleStatesCounters,
            Map<BundleSymbolicNameWithVersion, BundleState> bundlesStateMap) {
        this.okBundleStateInfoTexts = immutableCopyOf(okBundleStateInfoTexts);
        this.nokBundleStateInfoTexts = immutableCopyOf(nokBundleStateInfoTexts);
        this.whitelistedBundleStateInfoTexts = immutableCopyOf(whitelistedBundleStateInfoTexts);
        this.bundleStatesCounters = immutableCopyOf(bundleStatesCounters);
        this.bundlesStateMap = immutableCopyOf(bundlesStateMap);
    }

    public static BundleDiagInfos forContext(BundleContext bundleContext, BundleService bundleService) {
        List<String> okBundleStateInfoTexts = new ArrayList<>();
        List<String> nokBundleStateInfoTexts = new ArrayList<>();
        List<String> whitelistedBundleStateInfoTexts = new ArrayList<>();
        Map<BundleSymbolicNameWithVersion, BundleState> bundlesStateMap = new HashMap<>();
        Map<BundleState, Integer> bundleStatesCounters = new EnumMap<>(BundleState.class);
        for (BundleState bundleState : BundleState.values()) {
            bundleStatesCounters.put(bundleState, 0);
        }

        for (Bundle bundle : bundleContext.getBundles()) {
            String bundleSymbolicName = bundle.getSymbolicName();
            BundleSymbolicNameWithVersion bundleSymbolicNameWithVersion
                = new BundleSymbolicNameWithVersion(bundleSymbolicName, bundle.getVersion().toString());

            BundleInfo karafBundleInfo = bundleService.getInfo(bundle);
            BundleState karafBundleState = karafBundleInfo.getState();
            bundlesStateMap.put(bundleSymbolicNameWithVersion, karafBundleState);

            String bundleStateDiagText = "OSGi state = " + bundleStateToText(bundle.getState())
                + ", Karaf bundleState = " + karafBundleState;
            String diagText = bundleService.getDiag(bundle);
            if (!diagText.isEmpty()) {
                bundleStateDiagText += ", due to: " + diagText;
            }

            if (WHITELISTED_BUNDLES.get(bundleSymbolicName) != null) {
                if (WHITELISTED_BUNDLES.get(bundleSymbolicName).equals(karafBundleState)) {
                    String msg = "WHITELISTED " + bundleSymbolicNameWithVersion + ": " + bundleStateDiagText;
                    whitelistedBundleStateInfoTexts.add(msg);
                    continue;
                }
            }

            bundleStatesCounters.compute(karafBundleState, (key, counter) -> counter + 1);

            // BundleState comparison as in Karaf's "diag" command,
            // see https://github.com/apache/karaf/blob/master/bundle/core/src/main/java/org/apache/karaf/bundle/command/Diag.java
            // but we intentionally, got a little further than Karaf's "diag" command,
            // and instead of only checking some states, we check what's really Active,
            // but accept that some remain just Resolved:
            if (karafBundleState != Active && !(karafBundleState == BundleState.Resolved)) {
                String msg = "NOK " + bundleSymbolicNameWithVersion + ": " + bundleStateDiagText;
                nokBundleStateInfoTexts.add(msg);
            } else {
                String msg = "OK " + bundleSymbolicNameWithVersion + ": " + bundleStateDiagText;
                okBundleStateInfoTexts.add(msg);
            }
        }

        return new BundleDiagInfosImpl(okBundleStateInfoTexts, nokBundleStateInfoTexts,
                whitelistedBundleStateInfoTexts, bundleStatesCounters, bundlesStateMap);
    }

    private static String bundleStateToText(int state) {
        switch (state) {
            case Bundle.INSTALLED:
                return "Installed";
            case Bundle.RESOLVED:
                return "Resolved";
            case Bundle.STARTING:
                return "Starting";
            case Bundle.ACTIVE:
                return "Active";
            case Bundle.STOPPING:
                return "Stopping";
            case Bundle.UNINSTALLED:
                return "Uninstalled";
            default:
                return state + "???";
        }
    }

    @Override
    public SystemState getSystemState() {
        if (bundleStatesCounters.get(BundleState.Failure) > 0) {
            return SystemState.Failure;
        } else if (bundleStatesCounters.get(BundleState.Stopping) > 0) {
            return SystemState.Stopping;
        } else if (bundleStatesCounters.get(BundleState.Installed) == 0
                // No, just Resolved is OK, so do not: && bundleStatesCounters.get(BundleState.Resolved) == 0
                && bundleStatesCounters.get(BundleState.Unknown) == 0
                && bundleStatesCounters.get(BundleState.GracePeriod) == 0
                && bundleStatesCounters.get(BundleState.Waiting) == 0
                && bundleStatesCounters.get(BundleState.Starting) == 0
                // BundleState.Active *should* be ~= total # of bundles (minus Resolved, and whitelisted installed)
                && bundleStatesCounters.get(BundleState.Stopping) == 0
                && bundleStatesCounters.get(BundleState.Failure) == 0) {
            return SystemState.Active;
        } else {
            return SystemState.Booting;
        }
    }

    @Override
    public String getFullDiagnosticText() {
        StringBuilder sb = new StringBuilder(getSummaryText());
        int failureNumber = 1;
        for (String nokBundleStateInfoText : getNokBundleStateInfoTexts()) {
            sb.append('\n');
            sb.append(failureNumber++);
            sb.append(". ");
            sb.append(nokBundleStateInfoText);
        }
        return sb.toString();
    }

    @Override
    public String getSummaryText() {
        return "diag: " + getSystemState() + " " + bundleStatesCounters.toString();
    }

    @Override
    public Map<BundleSymbolicNameWithVersion, BundleState> getBundlesStateMap() {
        return bundlesStateMap;
    }

    @Override
    public List<String> getNokBundleStateInfoTexts() {
        return nokBundleStateInfoTexts;
    }

    @Override
    public List<String> getOkBundleStateInfoTexts() {
        return okBundleStateInfoTexts;
    }

    @Override
    public List<String> getWhitelistedBundleStateInfoTexts() {
        return whitelistedBundleStateInfoTexts;
    }

    @Override
    public String toString() {
        return getFullDiagnosticText();
    }

    private List<String> immutableCopyOf(List<String> stringList) {
        return Collections.unmodifiableList(new ArrayList<>(stringList));
    }

    private <K, V> Map<K, V> immutableCopyOf(Map<K, V> map) {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }

}
