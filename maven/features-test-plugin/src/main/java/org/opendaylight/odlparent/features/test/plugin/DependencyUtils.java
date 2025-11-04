/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.karaf.features.BundleInfo;
import org.apache.karaf.features.internal.model.Bundle;
import org.apache.karaf.features.internal.model.ConfigFile;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.util.maven.Parser;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility artifact responsible for maven dependencies extraction and conversion.
 */
final class DependencyUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DependencyUtils.class);
    private static final String MVN_PREFIX = "mvn:";
    private static final int MVN_CUT_INDEX = MVN_PREFIX.length();
    private static final String WRAP_PREFIX = "wrap:mvn:";
    private static final int WRAP_CUT_INDEX = WRAP_PREFIX.length();

    static final String XML = "xml";
    static final String TEST = "test";
    static final String FEATURES = "features";

    static final String RELEASE_VERSION;
    static final String KARAF_VERSION;
    static final String PAX_EXAM_VERSION;

    static {
        try (var in = DependencyUtils.class.getClassLoader().getResourceAsStream("versions")) {
            if (in == null) {
                throw new ExceptionInInitializerError("Cannot read from 'versions' resource");
            }
            final var props = new Properties();
            props.load(in);
            RELEASE_VERSION = nonnullValue(props, "release.version");
            KARAF_VERSION = nonnullValue(props, "karaf.version");
            PAX_EXAM_VERSION = nonnullValue(props, "pax.exam.version");
        } catch (IOException | IllegalStateException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DependencyUtils() {
        // utility class
    }

    static Collection<Artifact> extractDependencies(final Features features) {
        final var urls = new LinkedHashSet<String>();
        urls.addAll(features.getRepository());
        for (var feature : features.getFeature()) {
            if (feature.getBundle() != null) {
                urls.addAll(feature.getBundle().stream().map(Bundle::getLocation).toList());
            }
            if (feature.getConditional() != null) {
                urls.addAll(feature.getConditional().stream()
                    .filter(conditional -> conditional.getBundles() != null)
                    .flatMap(conditional -> conditional.getBundles().stream())
                    .map(BundleInfo::getLocation).toList());
            }
            if (feature.getConfigfile() != null) {
                urls.addAll(feature.getConfigfile().stream().map(ConfigFile::getLocation).toList());
            }
        }
        final var artifacts = new LinkedList<Artifact>();
        urls.forEach(url -> collectArtifact(url, artifacts));
        return artifacts;
    }

    private static void collectArtifact(final String url, final Collection<Artifact> collection) {
        final var filteredUrl = url == null ? null : filterMvnUrl(url);
        if (filteredUrl != null) {
            final Parser parser;
            try {
                parser = new Parser(filteredUrl);
            } catch (MalformedURLException e) {
                LOG.warn("Error parsing url {} -> dependency omitted", url, e);
                return;
            }
            collection.add(new DefaultArtifact(parser.getGroup(), parser.getArtifact(),
                parser.getClassifier(), parser.getType(), parser.getVersion()));
        }
    }

    private static String filterMvnUrl(final String url) {
        if (url.startsWith(MVN_PREFIX)) {
            return url.substring(MVN_CUT_INDEX);
        }
        if (url.startsWith(WRAP_PREFIX)) {
            final var endIndex = url.indexOf('$');
            return endIndex > WRAP_CUT_INDEX ? url.substring(WRAP_CUT_INDEX, endIndex) : url.substring(WRAP_CUT_INDEX);
        }
        return null;
    }

    static Artifact toAetherArtifact(final org.apache.maven.artifact.Artifact mavenArtifact) {
        return new DefaultArtifact(mavenArtifact.getGroupId(), mavenArtifact.getArtifactId(),
            mavenArtifact.getClassifier(), mavenArtifact.getType(), mavenArtifact.getVersion());
    }

    static String identifierOf(final Artifact artifact) {
        return String.join(":", List.of(artifact.getGroupId(), artifact.getArtifactId(),
            artifact.getVersion(), artifact.getClassifier(), artifact.getExtension()));
    }

    static boolean isFeature(final Artifact artifact) {
        return FEATURES.equals(artifact.getClassifier()) && XML.equals(artifact.getExtension());
    }

    private static String nonnullValue(final Properties props, final String key) {
        final var value = props.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("no property value found for key " + key);
        }
        return value;
    }
}