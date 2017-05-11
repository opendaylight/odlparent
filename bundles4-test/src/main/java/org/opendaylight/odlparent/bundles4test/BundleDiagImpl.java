/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.karaf.bundle.core.BundleService;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.opendaylight.odlparent.bundlestest.BundleDiag;
import org.opendaylight.odlparent.bundlestest.BundleDiagInfos;
import org.opendaylight.odlparent.bundlestest.DiagUpdatesListener;
import org.opendaylight.odlparent.bundlestest.SystemState;
import org.opendaylight.odlparent.bundlestest.SystemStateFailureException;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link BundleDiag}.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class BundleDiagImpl implements BundleDiag {

    private static final Logger LOG = LoggerFactory.getLogger(BundleDiagImpl.class);

    private final BundleContext bundleContext;
    private final BundleService bundleService;

    @Inject
    public BundleDiagImpl(BundleContext bundleContext, @OsgiService BundleService bundleService) {
        this.bundleContext = bundleContext;
        this.bundleService = bundleService;
    }

    @Override
    public void checkBundleDiagInfos(long timeout, TimeUnit timeoutUnit, @Nullable DiagUpdatesListener listener)
            throws SystemStateFailureException {
        LOG.info("checkBundleDiagInfos() started...");
        try {
            Awaitility.await("checkBundleDiagInfos")
                .pollDelay(0, MILLISECONDS)
                .pollInterval(1, SECONDS)
                .atMost(timeout, timeoutUnit)
                    .conditionEvaluationListener(
                        condition -> {
                            final BundleDiagInfos bundleDiagInfos = (BundleDiagInfosImpl) condition.getValue();
                            LOG.info("checkBundleDiagInfos: Elapsed time {}s, remaining time {}s, {}",
                                condition.getElapsedTimeInMS() / 1000, condition.getRemainingTimeInMS() / 1000,
                                bundleDiagInfos.getFullDiagnosticText());
                            if (listener != null) {
                                listener.onUpdate(bundleDiagInfos,
                                        condition.getElapsedTimeInMS(), condition.getRemainingTimeInMS());
                            }
                        })
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
                LOG.info(bundleInfos.getFullDiagnosticText());
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

    private BundleDiagInfosImpl getBundleDiagInfos() {
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
