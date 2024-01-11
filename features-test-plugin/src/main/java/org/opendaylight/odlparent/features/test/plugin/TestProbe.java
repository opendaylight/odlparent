/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import java.io.File;
import java.net.URI;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.bundle.core.BundleState;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.osgi.framework.BundleContext;
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

    static final String FEATURE_FILE_URI_PROP = "feature.test.file.uri";
    static final String BUNDLE_CHECK_SKIP = "feature.test.bundle.check.skip";
    static final String BUNDLE_CHECK_TIMEOUT_SECONDS = "feature.test.bundle.check.timeout.seconds";
    static final String[] ALL_PROPERTY_KEYS =
        {FEATURE_FILE_URI_PROP, BUNDLE_CHECK_SKIP, BUNDLE_CHECK_TIMEOUT_SECONDS};

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);

    private final Map<Long, CheckResult> bundleCheckResults = new ConcurrentHashMap<>();
    private final AtomicReference<CompletableFuture<CheckResult>> checkFutureRef = new AtomicReference<>();

    @Inject
    private BundleContext bundleContext;

    @Inject
    private FeaturesService featuresService;

    @Inject
    private BundleService bundleService;

    /**
     * Performs the project feature installation on karaf requirement with subsequent state check.
     *
     * @throws Exception on probe failure
     */
    @Test
    @SuppressWarnings("IllegalCatch")
    public void testFeature() throws Exception {
        validateServices();
        setBundleListener();
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
        if (featuresService == null) {
            throw new IllegalStateException("featureService is not initialized");
        }
        if (bundleService == null) {
            throw new IllegalStateException("bundleService is not initialized");
        }
    }

    private void setBundleListener() {
        // event based bundle states collection
        bundleContext.addBundleListener(event -> {
            final var bundle = event.getBundle();
            if (bundle != null) {
                final var info = bundleService.getInfo(bundle);
                LOG.info("Bundle state updated: {} -> {}", info.getName(), info.getState());
                bundleCheckResults.put(bundle.getBundleId(), CheckResult.from(info.getState()));
                updateCheckResults();
            }
        });
    }

    private void installFeatures() throws Exception {
        final var featureUri = URI.create(System.getProperty(FEATURE_FILE_URI_PROP));
        if (! new File(featureUri).exists()) {
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

    private void checkBundleStates() throws InterruptedException, ExecutionException {
        if ("true".equals(System.getProperty(BUNDLE_CHECK_SKIP))) {
            return;
        }
        final int timeout = Integer.parseInt(System.getProperty(BUNDLE_CHECK_TIMEOUT_SECONDS, "600"));
        LOG.info("Checking bundle states. Timeout is {} seconds.", timeout);

        final var future = new CompletableFuture<CheckResult>();
        checkFutureRef.set(future);
        updateCheckResults();
        final CheckResult result;
        try {
            result = future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logBundlesStatesDetails();
            throw new IllegalStateException("Bundles states check is not completed in " + timeout + "seconds", e);
        }
        LOG.info("Bundle state check completed with result {}", result);
        if (result != CheckResult.SUCCESS) {
            logBundlesStatesDetails();
            throw new IllegalStateException("Bundle states check failed");
        }
    }

    private void updateCheckResults() {
        if (checkFutureRef.get() == null || checkFutureRef.get().isDone()) {
            // don't check stats if results are not expected or already delivered
            return;
        }
        final var resultStats = bundleCheckResults.entrySet().stream()
            .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        LOG.info("Bundle states check results: total={}, byResult={}", bundleCheckResults.size(), resultStats);

        if (resultStats.getOrDefault(CheckResult.FAILURE, 0L) > 0) {
            checkFutureRef.get().complete(CheckResult.FAILURE);
        } else if (resultStats.getOrDefault(CheckResult.STOPPING, 0L) > 0) {
            checkFutureRef.get().complete(CheckResult.STOPPING);
        } else if (resultStats.getOrDefault(CheckResult.WAITING, 0L) == 0) {
            checkFutureRef.get().complete(CheckResult.SUCCESS);
        }
    }

    private void logBundlesStatesDetails() {
        LOG.error("Bundle state check failed -> TBD");
    }

    enum CheckResult {
        SUCCESS, FAILURE, STOPPING, WAITING;

        static CheckResult from(final BundleState state) {
            if (state == BundleState.Stopping) {
                return STOPPING;
            }
            if (state == BundleState.Failure) {
                return FAILURE;
            }
            if (state == BundleState.Resolved || state == BundleState.Active) {
                return SUCCESS;
            }
            return WAITING;
        }
    }
}