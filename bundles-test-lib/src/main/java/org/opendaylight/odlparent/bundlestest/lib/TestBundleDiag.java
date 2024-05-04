/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.opendaylight.odlparent.bundles.diag.DiagProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to verify bundle diagnostic state.
 *
 * @author Michael Vorburger.ch, based on guidance from Christian Schneider
 */
public class TestBundleDiag {
    private static final Logger LOG = LoggerFactory.getLogger(TestBundleDiag.class);

    private final DiagProvider diagProvider;

    public TestBundleDiag(final DiagProvider diagProvider) {
        this.diagProvider = requireNonNull(diagProvider);
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
    public void checkBundleDiagInfos(final long timeout, final TimeUnit timeoutUnit)
            throws SystemStateFailureException {
        checkBundleDiagInfos(timeout, timeoutUnit, (timeInfo, bundleDiagInfos) ->
            LOG.info("checkBundleDiagInfos: Elapsed time {}s, remaining time {}s, {}",
                timeInfo.elapsedTimeInMS() / 1000, timeInfo.remainingTimeInMS() / 1000,
                bundleDiagInfos.getFullDiagnosticText()));
    }

    public void checkBundleDiagInfos(final long timeout, final TimeUnit timeoutUnit,
            final BiConsumer<TimeInfo, BundleDiagInfos> awaitingListener) throws SystemStateFailureException {
        LOG.info("checkBundleDiagInfos() started...");
        try {
            Awaitility.await("checkBundleDiagInfos")
                .pollDelay(0, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(timeout, timeoutUnit)
                    .conditionEvaluationListener(condition -> awaitingListener.accept(
                        new TimeInfo(condition.getElapsedTimeInMS(), condition.getRemainingTimeInMS()),
                        (BundleDiagInfosImpl) condition.getValue()))
                    .until(
                        () -> BundleDiagInfosImpl.ofDiag(diagProvider.currentDiag()),
                        new BundleServiceSummaryMatcher());

            // If we're here then either BundleServiceSummaryMatcher quit because of Active, Failure or Stopping..
            final var diag = diagProvider.currentDiag();
            final var bundleInfos = BundleDiagInfosImpl.ofDiag(diag);
            final var systemState = bundleInfos.getSystemState();
            if (systemState.equals(SystemState.Failure) || systemState.equals(SystemState.Stopping)) {
                LOG.error("""
                    diag failure; BundleService reports bundle(s) which failed or are already stopping (details in \
                    following INFO and ERROR log messages...)""");
                diag.logState(LOG);
                throw new SystemStateFailureException("diag failed; some bundles failed to start", bundleInfos);

            } else {
                // Inform the developer of the green SystemState.Active
                LOG.info("diag successful; system state active ({})", bundleInfos.getFullDiagnosticText());
            }

        } catch (ConditionTimeoutException e) {
            // If this happens then it got stuck waiting in SystemState.Booting,
            // typically due to bundles still in BundleState GracePeriod or Waiting
            LOG.error("""
                diag failure; BundleService reports bundle(s) which are still not active (details in following INFO \
                and ERROR log messages...)""");
            final var diag = diagProvider.currentDiag();
            diag.logState(LOG);
            throw new SystemStateFailureException("diag timeout; some bundles are still not active:",
                BundleDiagInfosImpl.ofDiag(diag), e);
        }
    }
}
