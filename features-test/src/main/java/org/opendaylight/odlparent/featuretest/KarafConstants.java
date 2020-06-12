/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class KarafConstants {
    private static final Logger LOG = LoggerFactory.getLogger(KarafConstants.class);

    /**
     * Property file used to store the Karaf distribution version.
     */
    private static final String PROPERTIES_FILENAME = "/singlefeaturetest.properties";
    private static final String KARAF_DISTRO_VERSION_PROP = "karaf.distro.version";
    private static final String KARAF_RELEASE_VERSION_PROP = "karaf.release.version";

    private static final @NonNull String KARAF_DISTRO_VERSION;
    private static final @NonNull String KARAF_RELEASE_VERSION;

    static {
        final InputStream input = KarafConstants.class.getResourceAsStream(PROPERTIES_FILENAME);
        if (input == null) {
            throw new IllegalStateException("Failed to open resource \"" + PROPERTIES_FILENAME + "\"");
        }

        final Properties props = new Properties();
        try {
            // We use a properties file to retrieve Karaf's version, instead of .versionAsInProject()
            // This avoids forcing all users to depend on Karaf in their POMs
            props.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        KARAF_DISTRO_VERSION = nonnullProp(props, KARAF_DISTRO_VERSION_PROP);
        KARAF_RELEASE_VERSION = nonnullProp(props, KARAF_RELEASE_VERSION_PROP);
    }

    private KarafConstants() {
        // Hidden on purpose
    }

    /**
     * Return Karaf distribution version. This defaults to this project version, as we use opendaylight-karaf-empty
     * from this project. It can be overridden via a system property.
     *
     * @return Distribution version
     */
    static @NonNull String karafDistroVersion() {
        return systemOrFile(KARAF_DISTRO_VERSION_PROP, KARAF_DISTRO_VERSION);
    }

    /**
     * Return Karaf release version. This is upstream karaf release which is contained within the distribution. Note
     * this is distinct from {@link #karafDistroVersion()}.
     *
     * @return Karaf version
     */
    static @NonNull String karafReleaseVersion() {
        return systemOrFile(KARAF_RELEASE_VERSION_PROP, KARAF_RELEASE_VERSION);
    }

    private static @NonNull String systemOrFile(final String key, final @NonNull String fileVal) {
        String ret = System.getProperty(key);
        if (ret == null) {
            ret = fileVal;
            LOG.info("Retrieved {} value {} from properties file {}", key, ret, key);
        } else {
            LOG.info("Retrieved {} value {} from system properties", key, ret);
        }
        return ret;
    }

    private static @NonNull String nonnullProp(final Properties props, final String key) {
        final String ret = props.getProperty(requireNonNull(key));
        if (ret == null) {
            throw new IllegalStateException("Property \"" + key + "\" not found");
        }
        return ret;
    }
}
