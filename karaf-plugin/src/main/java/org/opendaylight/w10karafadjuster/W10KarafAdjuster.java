/*
 * Copyright (c) 2015 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.w10karafadjuster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adjusts Karaf(Equinox) custom properties to allow running on Windows 10.
 * Basically automates a manual fix recommended in Opendaylight docs. Needs
 * OpenJDK version 8u60 or newer because of a resolved issue.
 *
 * @author martin.dindoffer
 * @see <a href="http://docs.opendaylight.org/en/latest/getting-started-guide/installing_opendaylight.html">Opendaylight docs</a>
 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8066504">OpenJDK issue</a>
 */
public class W10KarafAdjuster {

    private static final Logger LOG = LoggerFactory.getLogger(W10KarafAdjuster.class);

    public static final String WIN_10_OS_NAME = "Windows 10";
    public static final String KARAF_PROPERTY_DOC = "# Workaround for Karaf(equinox) not resolving Windows 10 os.name";
    public static final String KARAF_PROPERTY = "org.osgi.framework.os.name = Win32";
    public static final String PROPERTIES_FILE_PATH = "karaf/karaf-parent/target/assembly/etc/custom.properties";

    public static void main(String[] args) {
        if (System.getProperty("os.name").equals(WIN_10_OS_NAME)) {
            List<String> property = Arrays.asList(KARAF_PROPERTY_DOC, KARAF_PROPERTY);
            Path propertiesFile = Paths.get(PROPERTIES_FILE_PATH);
            try {
                Files.write(propertiesFile, property, StandardOpenOption.APPEND);
            } catch (IOException e) {
                LOG.warn("Failed to apply Windows 10 karaf workaround into custom.properties");
            }
        }
    }

}
