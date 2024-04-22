/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static org.apache.karaf.bundle.core.BundleState.Installed;
import static org.apache.karaf.bundle.core.BundleState.Waiting;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The feature test probe artifact.
 *
 * <p>
 * The class is being packaged and deployed to karaf environment on {@link PaxExamExecution#execute()} invocation.
 * All dependencies which are absent on target environment expected to be packaged using same
 * {@link org.ops4j.pax.exam.ProbeBuilder}. Input parameters are passed through system properties. in order to be
 * delivered properly all affected properties require explicit declaration using associated Pax options -- see
 * {@link PaxOptionUtils#probePropertiesOptions()}.
 *
 * <p>
 * Pax Exam module references:
 * <ul>
 *     <li>Probe bundle deployment handling is served by pax-exam-extender-service</li>
 *     <li>Service instances lookup and injection into probe instance is served by pax-exam-inject</li>
 *     <li>Test method invocation is served by pax-exam-invoker-junit, uses JUnitCore v.4, which requires @Test
 *     annotation for method to be eligible for invocation</li>
 * </ul>
 */
public final class TestProbe {
    private enum CheckResult {
        SUCCESS,
        FAILURE,
        STOPPING,
        IN_PROGRESS;

        static CheckResult of(final String bundleName, final BundleState state) {
            if (bundleName != null && state == ELIGIBLE_STATES.get(bundleName)) {
                return CheckResult.SUCCESS;
            }
            if (state == BundleState.Stopping) {
                return CheckResult.STOPPING;
            }
            if (state == BundleState.Failure) {
                return CheckResult.FAILURE;
            }
            if (state == BundleState.Resolved || state == BundleState.Active) {
                return CheckResult.SUCCESS;
            }
            return CheckResult.IN_PROGRESS;
        }
    }

    private final class BundleCheck implements BundleListener, AutoCloseable {
        private final AtomicReference<CompletableFuture<CheckResult>> checkFutureRef = new AtomicReference<>();
        private final Map<Long, CheckResult> bundleCheckResults;

        // We are being invoked outside of framework tread, hence things may be happening asynchronously. Let's tread
        // very carefully here
        BundleCheck() {
            // Populate initial bundle states...
            bundleCheckResults = Arrays.stream(bundleContext.getBundles())
                .collect(Collectors.toMap(Bundle::getBundleId, bundle -> {
                    final var info = bundleService.getInfo(bundle);
                    final var result = CheckResult.of(info.getSymbolicName(), info.getState());
                    LOG.info("Initial bundle state: [{}]{} -> {} ({})", bundle.getBundleId(), info.getSymbolicName(),
                        info.getState(), result);
                    return result;
                }));

            // ... register as listener, enabling bundleChanged() delivery, but be careful about completing the future
            synchronized (this) {
                // this may trigger bundleChanged() events being proceessed due to reentrant locking
                bundleContext.addBundleListener(this);

                // some time may have passed between initial state and listener seeing state, hence we need to re-run
                // updates by observing bundles again.
                for (var bundle : bundleContext.getBundles()) {
                    updateBundle(bundle);
                }

                // finally enable completion and perform first check
                final var checkFuture = new CompletableFuture<CheckResult>();
                checkFutureRef.set(checkFuture);
                updateCheckResults(checkFuture);
            }
        }

        void getResult(final int timeoutSeconds) {
            final CheckResult result;
            try {
                result = checkFutureRef.get().get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logNokBundleDetails();
                throw new IllegalStateException("Bundles states check was not completed", e);
            }

            LOG.info("Bundle state check completed with result {}", result);
            if (result != CheckResult.SUCCESS) {
                logNokBundleDetails();
                throw new IllegalStateException("Bundle states check failed");
            }
        }

        @Override
        public synchronized void close() {
            bundleContext.removeBundleListener(this);
        }

        @Override
        public synchronized void bundleChanged(final BundleEvent event) {
            LOG.info("Received {}", event);
            final var bundle = event.getBundle();
            if (bundle != null) {
                updateBundle(bundle);
                final var checkFuture = checkFutureRef.get();
                if (checkFuture != null && !checkFuture.isDone()) {
                    // update results if already initialized and check has not completed yet
                    updateCheckResults(checkFuture);
                }
            }
        }

        private void updateBundle(final Bundle bundle) {
            final var info = bundleService.getInfo(bundle);
            final var next = CheckResult.of(info.getSymbolicName(), info.getState());
            final Long bundleId = bundle.getBundleId();
            final var prev = bundleCheckResults.put(bundleId, next);
            LOG.info("Bundle state updated: [{}]{} -> {} ({} -> {})", bundleId, info.getSymbolicName(), info.getState(),
                prev, next);
        }

        private void updateCheckResults(final CompletableFuture<CheckResult> checkFuture) {
            final var results = bundleCheckResults.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
            LOG.info("Bundle states check results: total={}, byResult={}", bundleCheckResults.size(), results);

            final var result = computeResult(results);
            if (result != null) {
                checkFuture.complete(result);
            }
        }

        private static CheckResult computeResult(final Map<CheckResult, Long> results) {
            final var failures = results.get(CheckResult.FAILURE);
            if (failures != null && failures > 0) {
                LOG.info("Observed {} failured bundles, completing", failures);
                return CheckResult.FAILURE;
            }
            final var stopping = results.get(CheckResult.STOPPING);
            if (stopping != null && stopping > 0) {
                LOG.info("Observed {} stopping bundles, completing", failures);
                return CheckResult.STOPPING;
            }
            final var inProgress = results.get(CheckResult.IN_PROGRESS);
            if (inProgress != null && inProgress > 0) {
                LOG.info("Observed {} bundles in progress, waiting for more updates", inProgress);
                return null;
            }

            LOG.info("Observed {} successful bundles, completing", results.getOrDefault(CheckResult.SUCCESS, 0L));
            return CheckResult.SUCCESS;
        }

        private synchronized void logNokBundleDetails() {
            final var nokBundles = bundleCheckResults.entrySet().stream()
                .filter(entry -> CheckResult.SUCCESS != entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
            // log NOK Bundles
            for (var bundle : bundleContext.getBundles()) {
                if (nokBundles.contains(bundle.getBundleId())) {
                    final var info = bundleService.getInfo(bundle);
                    final var diag = bundleService.getDiag(bundle);
                    LOG.warn("NOK Bundle {} -> State: {}{}", info.getSymbolicName(), info.getState(),
                        diag.isEmpty() ? "" : ", due to: " + diag);
                }
            }
            // log services of NOK bundles
            try {
                for (var serviceRef : bundleContext.getAllServiceReferences(null, null)) {
                    final var bundle = serviceRef.getBundle();
                    if (bundle != null && nokBundles.contains(bundle.getBundleId())) {
                        final var usingBundles = serviceRef.getUsingBundles();
                        final var usingSymbolic = usingBundles == null ? List.of()
                            : Arrays.stream(usingBundles).map(Bundle::getSymbolicName).toList();
                        final var propKeys = serviceRef.getPropertyKeys();
                        final var serviceProps = Arrays.stream(propKeys)
                            .collect(Collectors.toMap(Function.identity(), serviceRef::getProperty));
                        LOG.warn("NOK Service {} -> of bundle: {}, using: {}, props: {}",
                            serviceRef.getClass().getName(), bundle.getSymbolicName(), usingSymbolic, serviceProps);
                    }
                }
            } catch (InvalidSyntaxException e) {
                LOG.warn("Error retrieving services", e);
            }
        }
    }

    static final String FEATURE_FILE_URI_PROP = "feature.test.file.uri";
    static final String BUNDLE_CHECK_SKIP = "feature.test.bundle.check.skip";
    static final String BUNDLE_CHECK_TIMEOUT_SECONDS = "feature.test.bundle.check.timeout.seconds";
    static final String[] ALL_PROPERTY_KEYS =
        {FEATURE_FILE_URI_PROP, BUNDLE_CHECK_SKIP, BUNDLE_CHECK_TIMEOUT_SECONDS};

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);
    private static final Map<String, BundleState> ELIGIBLE_STATES = Map.of(
        "slf4j.log4j12", Installed,
        // see https://jira.opendaylight.org/browse/ODLPARENT-144
        "org.apache.karaf.scr.management", Waiting);

    @Inject
    private BundleContext bundleContext;
    @Inject
    private FeaturesService featuresService;
    @Inject
    private BundleService bundleService;

    /**
     * Performs the project feature installation on karaf environment with subsequent state check of deployed bundles.
     *
     * @throws Exception on probe failure
     */
    @Test
    @SuppressWarnings("IllegalCatch")
    public void testFeature() throws Exception {
        validateServices();
        try {
            installFeatures();
            checkBundleStates();
        } catch (Exception e) {
            LOG.error("Exception executing feature test", e);
            throw e;
        }
    }

    private void validateServices() {
        if (bundleContext == null) {
            throw new IllegalStateException("bundleContext is not initialized");
        }
        // replace the probe's initial context which expires too fast
        bundleContext = bundleContext.getBundle(0).getBundleContext();

        if (featuresService == null) {
            throw new IllegalStateException("featureService is not initialized");
        }
        if (bundleService == null) {
            throw new IllegalStateException("bundleService is not initialized");
        }
    }

    private void installFeatures() throws Exception {
        final var featureUri = URI.create(System.getProperty(FEATURE_FILE_URI_PROP));
        if (!new File(featureUri).exists()) {
            throw new IllegalStateException("Feature file with URI " + featureUri + " does not exist");
        }

        // install repository the feature definition can be read from
        featuresService.addRepository(featureUri);
        LOG.info("Feature repository with URI: {} initialized", featureUri);

        // install features
        for (var feature : featuresService.getRepository(featureUri).getFeatures()) {
            final var name = feature.getName();
            final var version = feature.getVersion();
            LOG.info("Installing feature: {}, {}", name, version);
            featuresService.installFeature(name, version, EnumSet.of(FeaturesService.Option.Verbose));
            LOG.info("Feature is installed: {}, isInstalled()={}, getState()={}",
                name, featuresService.isInstalled(feature), featuresService.getState(feature.getId()));
        }
    }

    private void checkBundleStates() {
        if ("true".equals(System.getProperty(BUNDLE_CHECK_SKIP))) {
            return;
        }
        final int timeout = Integer.parseInt(System.getProperty(BUNDLE_CHECK_TIMEOUT_SECONDS, "600"));
        LOG.info("Checking bundle states. Timeout is {} seconds.", timeout);

        try (var check = new BundleCheck()) {
            check.getResult(timeout);
        }
    }
}