/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.net.URI;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.opendaylight.odlparent.bundles.diag.ContainerState;
import org.opendaylight.odlparent.bundles.diag.Diag;
import org.opendaylight.odlparent.bundles.diag.DiagProvider;
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
    enum CheckResult {
        SUCCESS,
        FAILURE,
        STOPPING,
        IN_PROGRESS;

        static CheckResult of(final String bundleName, final ContainerState state) {
            // Note we do not use a switch expression on purpose. Otherwise the probe installation will fail as we
            // cannot reference the inner class generated in that pattern.
            if (state == ContainerState.RESOLVED || state == ContainerState.ACTIVE
                || bundleName != null && state == ELIGIBLE_STATES.get(bundleName)) {
                return SUCCESS;
            } else if (state == ContainerState.FAILURE) {
                return FAILURE;
            } else if (state == ContainerState.STOPPING) {
                return STOPPING;
            } else {
                return IN_PROGRESS;
            }
        }
    }

    static final String FEATURE_FILE_URI_PROP = "feature.test.file.uri";
    static final String BUNDLE_CHECK_SKIP = "feature.test.bundle.check.skip";
    static final String BUNDLE_CHECK_TIMEOUT_SECONDS = "feature.test.bundle.check.timeout.seconds";
    static final String BUNDLE_CHECK_INTERVAL_SECONDS = "feature.test.bundle.check.interval.seconds";
    static final String DEFAULT_TIMEOUT = "300";
    static final String DEFAULT_INTERVAL = "1";

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);
    private static final Map<String, ContainerState> ELIGIBLE_STATES = Map.of(
        "slf4j.log4j12", ContainerState.INSTALLED,
        "org.apache.karaf.scr.management", ContainerState.WAITING);

    @Inject
    private FeaturesService featuresService;

    @Inject
    private DiagProvider diagProvider;

    /**
     * Default constructor.
     */
    public TestProbe() {
        // Exposed for javadoc
    }

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
        if (featuresService == null) {
            throw new IllegalStateException("featureService is not initialized");
        }
        if (diagProvider == null) {
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
        if (Boolean.getBoolean(BUNDLE_CHECK_SKIP)) {
            LOG.warn("SKIPPING bundle check because system property {} is set to true", BUNDLE_CHECK_SKIP);
            return;
        }

        final int timeout = Integer.parseInt(System.getProperty(BUNDLE_CHECK_TIMEOUT_SECONDS, DEFAULT_TIMEOUT));
        final int interval = Integer.parseInt(System.getProperty(BUNDLE_CHECK_INTERVAL_SECONDS, DEFAULT_INTERVAL));
        LOG.info("Bundle check started. Interval = {} second(s). Timeout = {} second(s).", interval, timeout);

        final var intervalNanos = SECONDS.toNanos(interval);
        final var maxNanos = SECONDS.toNanos(timeout);
        final var started = System.nanoTime();

        Diag prevDiag = null;
        while (true) {
            final var diag = diagProvider.currentDiag();
            LOG.info("Bundle check {}", diag.containerStateFrequencies());
            if (prevDiag != null) {
                diag.logDelta(LOG, prevDiag);
            }
            prevDiag = diag;

            final var checkResults = diag.bundles().stream()
                .map(bundle -> Map.entry(bundle,
                    CheckResult.of(bundle.symbolicName(), bundle.serviceState().containerState())))
                .collect(Collectors.toUnmodifiableList());

            LOG.info("Bundle states check results: total={}, byResult={}", checkResults.size(), checkResults.stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting())));
            final var elapsed = System.nanoTime() - started;

            for (var checkResult : checkResults) {
                if (checkResult.getValue() == CheckResult.FAILURE) {
                    LOG.error("Bundle check failed after {}s", NANOSECONDS.toSeconds(elapsed));
                    diag.logState(LOG);
                    throw new IllegalStateException("Bundle states check failure");
                }
            }
            for (var checkResult : checkResults) {
                if (checkResult.getValue() == CheckResult.STOPPING) {
                    LOG.error("Bundle check stopping after {}s", NANOSECONDS.toSeconds(elapsed));
                    diag.logState(LOG);
                    throw new IllegalStateException("Bundle states check stopping");
                }
            }

            final var inProgress = checkResults.stream()
                .filter(checkResult -> checkResult.getValue() == CheckResult.IN_PROGRESS)
                .collect(Collectors.toUnmodifiableList());
            if (inProgress.isEmpty()) {
                LOG.info("Bundle check completed after {}s", NANOSECONDS.toSeconds(elapsed));
                return;
            }

            final var elapsedSeconds = NANOSECONDS.toSeconds(elapsed);
            final var elapsedStr = elapsedSeconds > 0 ? "(after " + elapsedSeconds + "s) " : "";
            for (var checkResult : inProgress) {
                final var bundle = checkResult.getKey();
                final var serviceState = bundle.serviceState();
                LOG.info("Unresolved {}{}:{} {}/{}[{}]", elapsedStr, bundle.symbolicName(), bundle.version(),
                    bundle.frameworkState(), serviceState.containerState().reportingName(), serviceState.diag());
            }

            final var remainingNanos = maxNanos - elapsed;
            if (remainingNanos <= 0) {
                diag.logState(LOG);
                throw new IllegalStateException("Bundles states check timeout");
            }

            final var sleepNanos = Math.min(remainingNanos, intervalNanos);
            LOG.debug("Bundle check sleep {}ns", sleepNanos);
            NANOSECONDS.sleep(sleepNanos);
        }
    }
}