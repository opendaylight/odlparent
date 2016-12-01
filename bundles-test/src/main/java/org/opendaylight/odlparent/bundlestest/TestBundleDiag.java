/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import static org.apache.karaf.bundle.core.BundleState.Failure;
import static org.apache.karaf.bundle.core.BundleState.GracePeriod;
import static org.apache.karaf.bundle.core.BundleState.Installed;
import static org.apache.karaf.bundle.core.BundleState.Waiting;

import org.apache.karaf.bundle.core.BundleInfo;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to verify bundle diagnostic state from OSGi integration tests.
 *
 * @author Michael Vorburger
 */
public class TestBundleDiag {

    private static final Logger LOG = LoggerFactory.getLogger(TestBundleDiag.class);

    private final BundleContext bundleContext;
    private final BundleService bundleService;

    public TestBundleDiag(BundleContext bundleContext, BundleService bundleService) {
        this.bundleContext = bundleContext;
        this.bundleService = bundleService;
    }

    /**
     * Does the equivalent of the "diag" CLI command, and fails the test if anything incl. bundle wiring is NOK.
     *
     * <p>The implementation is based on Karaf's BundleService, and not the BundleStateService,
     * because each Karaf supported DI system (such as Blueprint and Declarative Services, see String constants
     * in BundleStateService), will have a separate BundleStateService.  The BundleService however will
     * contain the combined status of all BundleStateServices.
     *
     * @author Michael Vorburger, based on guidance from Christian Schneider
     */
    public void checkBundleDiagInfos() {
        try {
            Awaitility.await("checkBundleDiagInfos")
                .pollDelay(0, MILLISECONDS)
                .pollInterval(250, MILLISECONDS)
                .atMost(1, MINUTES)
                    .conditionEvaluationListener(
                        condition -> LOG.info("diag: {} (elapsed time {}ms, remaining time {}ms)",
                            ((BundleDiagInfos) condition.getValue()).summaryText,
                            condition.getElapsedTimeInMS(), condition.getRemainingTimeInMS()))
                    .until(() -> getBundleDiagInfos(), new BundleServiceSummaryMatcher());
        } catch (ConditionTimeoutException e) {
            LOG.error("diag failure; BundleService reports bundle(s) which are not active"
                    + " (details in following INFO and ERROR log messages...)");
            logOSGiServices();
            BundleDiagInfos bundleInfos = getBundleDiagInfos();
            for (String okBundleStateInfo : bundleInfos.okBundleStateInfoTexts) {
                LOG.info(okBundleStateInfo);
            }
            for (String nokBundleStateInfo : bundleInfos.nokBundleStateInfoTexts) {
                LOG.error(nokBundleStateInfo);
            }
            throw e; // fail the test!
        }
    }

    public BundleDiagInfos getBundleDiagInfos() {
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
        bundleInfos.systemIsReady = bundleInfos.nokBundleStateInfoTexts.isEmpty();
        bundleInfos.summaryText = bundleInfos.bundleStatesCounters.toString();
        return bundleInfos;
    }

    private void logOSGiServices() {
        ServiceReferenceUtil util = new ServiceReferenceUtil();
        LOG.info("Now going to log all known services, to help diagnose root cause of "
                + "diag failure BundleService reported bundle(s) which are not active");
        try {
            for (ServiceReference<?> serviceRef : bundleContext.getAllServiceReferences(null, null)) {
                LOG.info("{} defines OSGi Service {} used by {}", serviceRef.getBundle().getSymbolicName(),
                        util.getProperties(serviceRef), util.getUsingBundleSymbolicNames(serviceRef));
            }
        } catch (InvalidSyntaxException e) {
            LOG.error("logOSGiServices() failed due to InvalidSyntaxException", e);
        }
    }

}
