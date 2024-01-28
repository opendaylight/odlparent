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
import java.util.concurrent.ConcurrentHashMap;
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

    static final String FEATURE_FILE_URI_PROP = "feature.test.file.uri";
    static final String BUNDLE_CHECK_SKIP = "feature.test.bundle.check.skip";
    static final String BUNDLE_CHECK_TIMEOUT_SECONDS = "feature.test.bundle.check.timeout.seconds";
    static final String[] ALL_PROPERTY_KEYS =
        {FEATURE_FILE_URI_PROP, BUNDLE_CHECK_SKIP, BUNDLE_CHECK_TIMEOUT_SECONDS};

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);
    private static final Map<String, BundleState> ELIGIBLE_STATES = Map.of(
        "slf4j.log4j12", Installed,
        "org.apache.karaf.scr.management", Waiting);

    private final Map<Long, CheckResult> bundleCheckResults = new ConcurrentHashMap<>();
    private final AtomicReference<CompletableFuture<CheckResult>> checkFutureRef = new AtomicReference<>();

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

    private void checkBundleStates() throws InterruptedException, ExecutionException {
        if ("true".equals(System.getProperty(BUNDLE_CHECK_SKIP))) {
            return;
        }
        final int timeout = Integer.parseInt(System.getProperty(BUNDLE_CHECK_TIMEOUT_SECONDS, "600"));
        LOG.info("Checking bundle states. Timeout is {} seconds.", timeout);

        // start event based states collection
        final BundleListener bundleListener = event -> {
            captureBundleState(event.getBundle());
            updateCheckResults();
        };
        bundleContext.addBundleListener(bundleListener);
        // init all bundles state data
        Arrays.stream(bundleContext.getBundles()).forEach(this::captureBundleState);
        // enable stats analysis
        checkFutureRef.set(new CompletableFuture<>());
        // perform stats analysis
        updateCheckResults();

        final CheckResult result;
        try {
            result = checkFutureRef.get().get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logNokBundleDetails();
            throw new IllegalStateException("Bundles states check was not completed in " + timeout + "seconds", e);
        } finally {
            bundleContext.removeBundleListener(bundleListener);
        }
        LOG.info("Bundle state check completed with result {}", result);
        if (result != CheckResult.SUCCESS) {
            logNokBundleDetails();
            throw new IllegalStateException("Bundle states check failed");
        }
    }

    private void captureBundleState(final Bundle bundle) {
        if (bundle != null) {
            final var info = bundleService.getInfo(bundle);
            final var checkResult = checkResultOf(info.getSymbolicName(), info.getState());
            LOG.info("Bundle state updated: {} -> {} ({})", info.getSymbolicName(), info.getState(), checkResult);
            bundleCheckResults.put(bundle.getBundleId(), checkResult);
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
        } else if (resultStats.getOrDefault(CheckResult.IN_PROGRESS, 0L) == 0) {
            checkFutureRef.get().complete(CheckResult.SUCCESS);
        }
    }

    private void logNokBundleDetails() {
        final var nokBundles = bundleCheckResults.entrySet().stream()
            .filter(entry -> CheckResult.SUCCESS != entry.getValue())
            .map(Map.Entry::getKey).collect(Collectors.toSet());
        // log NOK Bundles
        for (var bundle : bundleContext.getBundles()) {
            if (nokBundles.contains(bundle.getBundleId())) {
                final var info = bundleService.getInfo(bundle);
                LOG.warn("NOK Bundle {} -> State: {}", info.getSymbolicName(), info.getState());
            }
        }
        // log services of NOK bundles
        try {
            for (var serviceRef : bundleContext.getAllServiceReferences(null, null)) {
                if (serviceRef.getBundle() != null && nokBundles.contains(serviceRef.getBundle().getBundleId())) {
                    final var bundle = serviceRef.getBundle();
                    final var usingBundles = serviceRef.getUsingBundles() == null ? List.of() :
                        Arrays.stream(serviceRef.getUsingBundles()).map(Bundle::getSymbolicName).toList();
                    final var propKeys = serviceRef.getPropertyKeys();
                    final var serviceProps = Arrays.stream(propKeys)
                        .collect(Collectors.toMap(Function.identity(), serviceRef::getProperty));
                    LOG.warn("NOK Service {} -> of bundle: {}, using: {}, props: {}",
                        serviceRef.getClass().getName(), bundle.getSymbolicName(), usingBundles, serviceProps);
                }
            }
        } catch (InvalidSyntaxException e) {
            LOG.warn("Error retrieving services", e);
        }
    }

    static CheckResult checkResultOf(final String bundleName, final BundleState state) {
        if (bundleName != null && state == ELIGIBLE_STATES.get(bundleName)) {
            return CheckResult.SUCCESS;
        }
        return switch (state) {
            case Active, Resolved -> CheckResult.SUCCESS;
            case Failure -> CheckResult.FAILURE;
            case Stopping -> CheckResult.STOPPING;
            default -> CheckResult.IN_PROGRESS;
        };
    }

    enum CheckResult {
        SUCCESS, FAILURE, STOPPING, IN_PROGRESS;
    }
}