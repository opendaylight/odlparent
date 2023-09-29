/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import org.apache.karaf.bundle.core.BundleService;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to verify bundle diagnostic state.
 *
 * @author Michael Vorburger.ch, based on guidance from Christian Schneider
 */
@SuppressFBWarnings(value = "CRLF_INJECTION_LOGS",
        justification = "multi-line logs are internal, without input from untrusted external source")
public class TestBundleDiag {

    private static final Logger LOG = LoggerFactory.getLogger(TestBundleDiag.class);

    private final BundleContext bundleContext;
    private final BundleService bundleService;

    public TestBundleDiag(BundleContext bundleContext, BundleService bundleService) {
        this.bundleContext = bundleContext;
        this.bundleService = bundleService;
    }

    /**
     * Does something similar to Karaf's "diag" CLI command, and throws a {@link SystemStateFailureException} if
     * anything including bundle wiring is not OK.
     *
     * <p>The implementation is based on Karaf's BundleService, and not the BundleStateService, because each Karaf
     * supported DI system (such as Blueprint and Declarative Services, see String constants in BundleStateService),
     * will have a separate BundleStateService.  The BundleService however will contain the combined status of all
     * BundleStateServices.
     *
     * @param timeout maximum time to wait for bundles to settle
     * @param timeoutUnit time unit of timeout
     * @throws SystemStateFailureException if all bundles do not settle within the timeout period
     */
    public void checkBundleDiagInfos(long timeout, TimeUnit timeoutUnit) throws SystemStateFailureException {
        checkBundleDiagInfos(timeout, timeoutUnit, (timeInfo, bundleDiagInfos) ->
            LOG.info("checkBundleDiagInfos: Elapsed time {}s, remaining time {}s, {}",
                    timeInfo.getElapsedTimeInMS() / 1000, timeInfo.getRemainingTimeInMS() / 1000,
                    bundleDiagInfos.getFullDiagnosticText()));
    }

    public void checkBundleDiagInfos(long timeout, TimeUnit timeoutUnit,
            BiConsumer<TimeInfo, BundleDiagInfos> awaitingListener) throws SystemStateFailureException {
        LOG.info("checkBundleDiagInfos() started...");
        try {
            Awaitility.await("checkBundleDiagInfos")
                .pollDelay(0, MILLISECONDS)
                .pollInterval(1, SECONDS)
                .atMost(timeout, timeoutUnit)
                    .conditionEvaluationListener(condition -> awaitingListener.accept(
                            new TimeInfo(condition.getElapsedTimeInMS(), condition.getRemainingTimeInMS()),
                            (BundleDiagInfosImpl) condition.getValue()))
                    .until(this::getBundleDiagInfos, new BundleServiceSummaryMatcher());

            // If we're here then either BundleServiceSummaryMatcher quit because of Active, Failure or Stopping..
            BundleDiagInfos bundleInfos = getBundleDiagInfos();
            SystemState systemState = bundleInfos.getSystemState();
            if (systemState.equals(SystemState.Failure) || systemState.equals(SystemState.Stopping)) {
                LOG.error("diag failure; BundleService reports bundle(s) which failed or are already stopping"
                        + " (details in following INFO and ERROR log messages...)");
                logBundleDiagInfos(bundleInfos);
                throw new SystemStateFailureException("diag failed; some bundles failed to start", bundleInfos);

            } else {
                // Inform the developer of the green SystemState.Active
                LOG.info("diag successful; system state active ({})", bundleInfos.getFullDiagnosticText());
            }

        } catch (ConditionTimeoutException e) {
            // If this happens then it got stuck waiting in SystemState.Booting,
            // typically due to bundles still in BundleState GracePeriod or Waiting
            LOG.error("diag failure; BundleService reports bundle(s) which are still not active"
                    + " (details in following INFO and ERROR log messages...)");
            BundleDiagInfos bundleInfos = getBundleDiagInfos();
            logBundleDiagInfos(bundleInfos);
            throw new SystemStateFailureException("diag timeout; some bundles are still not active:", bundleInfos, e);
        }
    }

    private void logBundleDiagInfos(BundleDiagInfos bundleInfos) {
        try {
            logOSGiServices();
        } catch (IllegalStateException e) {
            LOG.warn("logOSGiServices() failed (never mind); too late during shutdown already?", e);
        }
        for (String okBundleStateInfo : bundleInfos.getOkBundleStateInfoTexts()) {
            LOG.info(okBundleStateInfo);
        }
        for (String whitelistedBundleStateInfo : bundleInfos.getWhitelistedBundleStateInfoTexts()) {
            LOG.warn(whitelistedBundleStateInfo);
        }
        for (String nokBundleStateInfo : bundleInfos.getNokBundleStateInfoTexts()) {
            LOG.error(nokBundleStateInfo);
        }
    }

    private BundleDiagInfos getBundleDiagInfos() {
        return BundleDiagInfosImpl.forContext(bundleContext, bundleService);
    }

    private void logOSGiServices() {
        ServiceReferenceUtil util = new ServiceReferenceUtil();
        LOG.info("Now going to log all known services, to help diagnose root cause of "
                + "diag failure BundleService reported bundle(s) which are not active");
        try {
            for (ServiceReference<?> serviceRef : bundleContext.getAllServiceReferences(null, null)) {
                Bundle bundle = serviceRef.getBundle();
                // serviceRef.getBundle() can return null if the bundle was destroyed
                if (bundle != null) {
                    LOG.info("{} defines OSGi Service {} used by {}", bundle.getSymbolicName(),
                            util.getProperties(serviceRef), util.getUsingBundleSymbolicNames(serviceRef));
                } else {
                    LOG.trace("skipping reporting service reference as the underlying bundle is null");
                }
            }
        } catch (InvalidSyntaxException e) {
            LOG.error("logOSGiServices() failed due to InvalidSyntaxException", e);
        }
    }

}
