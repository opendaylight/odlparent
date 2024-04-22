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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.opendaylight.odlparent.bundles.diag.ContainerState;
import org.opendaylight.odlparent.bundles.diag.spi.DefaultDiagProvider;
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
    static final String BUNDLE_CHECK_INTERVAL_SECONDS = "feature.test.bundle.check.interval.seconds";
    static final String DEFAULT_TIMEOUT = "300";
    static final String DEFAULT_INTERVAL = "1";

    static final String[] ALL_PROPERTY_KEYS =
        {FEATURE_FILE_URI_PROP, BUNDLE_CHECK_SKIP, BUNDLE_CHECK_TIMEOUT_SECONDS, BUNDLE_CHECK_INTERVAL_SECONDS};

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);
    private static final Map<String, ContainerState> ELIGIBLE_STATES = Map.of(
        "slf4j.log4j12", ContainerState.INSTALLED,
        "org.apache.karaf.scr.management", ContainerState.WAITING);

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

    private void checkBundleStates() throws InterruptedException {
        if ("true".equals(System.getProperty(BUNDLE_CHECK_SKIP))) {
            return;
        }
        final int timeout = Integer.parseInt(System.getProperty(BUNDLE_CHECK_TIMEOUT_SECONDS, DEFAULT_TIMEOUT));
        final int interval = Integer.parseInt(System.getProperty(BUNDLE_CHECK_INTERVAL_SECONDS, DEFAULT_INTERVAL));
        LOG.info("Checking bundle states. Interval = {} second(s). Timeout = {} second(s).", interval, timeout);

        final var maxNanos = TimeUnit.SECONDS.toNanos(timeout);
        final var started = System.nanoTime();
        final var diagProvider = new DefaultDiagProvider(bundleService, bundleContext);

        // FIXME: this does not work well with bundles being uninstalled, see below
        final var bundleCheckResults = new HashMap<Long, CheckResult>();

        while (true) {
            final var diag = diagProvider.currentDiag();

            for (var bundle : diag.bundles()) {
                final var containerState = bundle.serviceState().containerState();
                final var checkResult = checkResultOf(bundle.symbolicName(), containerState);

                // FIXME: this does not account for bundleIds disappearing. We really should just index here and then
                //        compare observed state from previous run
                final var prev = bundleCheckResults.put(bundle.bundleId(), checkResult);
                if (prev != checkResult) {
                    LOG.info("Bundle {} -> State: {} ({})", bundle.symbolicName(), containerState, checkResult);
                }
            }

            final var result = aggregatedCheckResults(bundleCheckResults);

            if (result == CheckResult.IN_PROGRESS) {
                final var now = System.nanoTime();
                if (now - started >= maxNanos) {
                    diag.logState(LOG);
                    throw new IllegalStateException("Bundles states check timeout");
                }

                TimeUnit.SECONDS.sleep(interval);
                continue;
            }

            LOG.info("Bundle state check completed with result {}", result);
            if (result != CheckResult.SUCCESS) {
                diag.logState(LOG);
                throw new IllegalStateException("Bundle states check failure");
            }
            break;
        }
    }

    private static CheckResult aggregatedCheckResults(final Map<Long, CheckResult> bundleCheckResults) {
        final var resultStats = bundleCheckResults.entrySet().stream()
            .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()));
        LOG.info("Bundle states check results: total={}, byResult={}", bundleCheckResults.size(), resultStats);

        if (resultStats.getOrDefault(CheckResult.FAILURE, 0L) > 0) {
            return CheckResult.FAILURE;
        }
        if (resultStats.getOrDefault(CheckResult.STOPPING, 0L) > 0) {
            return CheckResult.STOPPING;
        }
        if (resultStats.getOrDefault(CheckResult.IN_PROGRESS, 0L) > 0) {
            return CheckResult.IN_PROGRESS;
        }
        return CheckResult.SUCCESS;
    }

    static CheckResult checkResultOf(final String bundleName, final ContainerState state) {
        if (bundleName != null && state == ELIGIBLE_STATES.get(bundleName)) {
            return CheckResult.SUCCESS;
        }
        if (state == ContainerState.STOPPING) {
            return CheckResult.STOPPING;
        }
        if (state == ContainerState.FAILURE) {
            return CheckResult.FAILURE;
        }
        if (state == ContainerState.RESOLVED || state == ContainerState.ACTIVE) {
            return CheckResult.SUCCESS;
        }
        return CheckResult.IN_PROGRESS;
    }

    enum CheckResult {
        SUCCESS, FAILURE, STOPPING, IN_PROGRESS;
    }
}