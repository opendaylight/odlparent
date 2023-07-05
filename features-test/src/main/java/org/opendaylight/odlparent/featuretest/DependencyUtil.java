/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class responsible for maven dependencies processing. Functionality relays on dependencies list populated
 * by {@code maven-dependency-plugin} on execution prior to SFT's {@code test} phase.
 */
final class DependencyUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DependencyUtil.class);
    private static final String TEST_DEPENDENCIES_LIST_PROP = "featureTest.dependencies.list";
    private static final String FEATURES = "features";
    private static final String XML = "xml";
    private static final String TEST = "test";

    private DependencyUtil() {
        // utility class
    }

    /**
     * Retrieves a list of maven artefacts classified as "features" defined in "test" scope for current project.
     *
     * <p>Path to dependencies list file is taken from system property variable {@value TEST_DEPENDENCIES_LIST_PROP}.
     *
     * @return features as PAX conditional composite option
     */
    static @NonNull Option testFeatures() {
        return testFeatures(new File(System.getProperty(TEST_DEPENDENCIES_LIST_PROP, "")));
    }

    /**
     * Retrieves a list of maven artefacts classified as "features" defined in "test" scope for current project.
     *
     * @param listFile the file containing dependencies list
     * @return features as PAX conditional composite option
     */
    static @NonNull Option testFeatures(final File listFile) {
        final var options = loadDependencyDescriptors(listFile).stream()
            .filter(depDesc -> depDesc.isFeature() && depDesc.isTestScope()
                && depDesc.featureNames().length > 0)
            .map(DependencyUtil::toFeatureOption).toArray(Option[]::new);
        return CoreOptions.when(options.length > 0).useOptions(options);
    }

    private static @NonNull Option toFeatureOption(final DependencyDescriptor depDesc) {
        final var mavenRef = CoreOptions.maven().groupId(depDesc.groupId).artifactId(depDesc.artifactId)
            .version(depDesc.version).classifier(FEATURES).type(XML);
        return KarafDistributionOption.features(mavenRef, depDesc.featureNames);
    }

    private static @NonNull Collection<DependencyDescriptor> loadDependencyDescriptors(final File listFile) {
        if (!listFile.exists()) {
            return List.of();
        }
        final var listBuilder = new ImmutableList.Builder<DependencyDescriptor>();
        try {
            // resolved dependencies are listed in following format
            // groupId:artifactId:type:classifier:version:scope:absoluteFilePath
            for (var line : Files.readAllLines(listFile.toPath(), Charset.defaultCharset())) {
                final var parts = line.trim().split(":");
                if (parts.length < 7) {
                    continue;
                }
                final var groupId = parts[0];
                final var artifactId = parts[1];
                final var isFeature = XML.equals(parts[2]) && FEATURES.equals(parts[3]);
                final var version = parts[4];
                final var isTestScope = TEST.equals(parts[5]);
                final var featureNames = isFeature ? getFeatureNames(new File(parts[6])) : new String[0];
                listBuilder.add(
                    new DependencyDescriptor(groupId, artifactId, version, isFeature, isTestScope, featureNames));
            }
        } catch (IOException e) {
            LOG.warn("Error reading dependencies list from {}", listFile, e);
        }
        return listBuilder.build();
    }

    private static @NonNull String[] getFeatureNames(final File featureFile) {
        if (featureFile.exists()) {
            try (var inputStream = new FileInputStream(featureFile)) {
                final Features feature = JaxbUtil.unmarshal(featureFile.toURI().toString(), inputStream, false);
                return feature.getFeature().stream().map(Feature::getName).toArray(String[]::new);
            } catch (IOException e) {
                LOG.warn("Error reading features from {}", featureFile, e);
            }
        }
        return new String[0];
    }

    private record DependencyDescriptor(String groupId, String artifactId, String version,
        boolean isFeature, boolean isTestScope, String[] featureNames) {
    }
}
