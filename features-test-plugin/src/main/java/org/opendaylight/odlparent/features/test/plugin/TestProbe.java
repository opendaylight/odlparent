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
import javax.inject.Inject;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestProbe {

    static final String FEATURE_FILE_URI_PROP = "feature.test.file.uri";
    static final String FEATURE_TIMEOUT_MILLIS = "feature.test.timeout.millis";
    static final String[] ALL_PROPERTY_KEYS = {FEATURE_FILE_URI_PROP, FEATURE_TIMEOUT_MILLIS};

    private static final Logger LOG = LoggerFactory.getLogger(TestProbe.class);

    @Inject
    BundleContext bundleContext;

    @Inject
    FeaturesService featuresService;

    @Test
    @SuppressWarnings("IllegalCatch")
    public void testFeature() throws Exception {
        try {
            validateServices();
            installFeatures();
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
    }

    private void installFeatures() throws Exception {
        final var featureUri = URI.create(System.getProperty(FEATURE_FILE_URI_PROP));
        if (! new File(featureUri).exists()) {
            throw new IllegalStateException("Feature file with URI " + featureUri + " does not exist");
        }

        // install repository the feature info can be read from
        featuresService.addRepository(featureUri);
        LOG.info("Feature repository with URI: {} initialized", featureUri);

        for (var feature : featuresService.getRepository(featureUri).getFeatures()) {
            final var name = feature.getName();
            final var version = feature.getVersion();
            LOG.info("Installing feature: {}, {}", name, version);
            featuresService.installFeature(name, version, EnumSet.of(FeaturesService.Option.Verbose));
            LOG.info("Feature state: {}", featuresService.getState(feature.getId()));
            LOG.info("Feature installed: {}", featuresService.isInstalled(feature));
        }
    }
}