/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.karaf.features.internal.model.Bundle;
import org.apache.karaf.features.internal.model.ConfigFile;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.aether.artifact.Artifact;
import org.ops4j.pax.url.mvn.internal.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FeatureUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureUtil.class);

    private static final Pattern MVN_PATTERN = Pattern.compile("mvn:", Pattern.LITERAL);
    private static final Pattern WRAP_PATTERN = Pattern.compile("wrap:", Pattern.LITERAL);
    private static final Pattern VERSION_STRIP_PATTERN = Pattern.compile("\\$.*$");

    private final AetherUtil aetherUtil;
    private final File localRepo;

    FeatureUtil(final AetherUtil aetherUtil, final File localRepo) {
        this.aetherUtil = aetherUtil;
        this.localRepo = localRepo;
    }

    /**
     * Converts the given URL to artifact coordinates.
     *
     * @param url The URL.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if the URL is malformed.
     */
    static String toCoord(final URL url) throws MalformedURLException {
        final var repository = url.toString();
        final var unwrappedRepo = WRAP_PATTERN.matcher(repository).replaceFirst("");

        final var parser = new Parser(unwrappedRepo);
        var coord = MVN_PATTERN.matcher(parser.getGroup()).replaceFirst("") + ":" + parser.getArtifact();
        if (parser.getType() != null) {
            coord = coord + ":" + parser.getType();
        }
        if (parser.getClassifier() != null) {
            coord = coord + ":" + parser.getClassifier();
        }
        coord = coord + ":" + VERSION_STRIP_PATTERN.matcher(parser.getVersion()).replaceAll("");
        LOG.trace("toCoord({}) returns {}", url, coord);
        return coord;
    }

    /**
     * Parses the given repository as URLs and converts them to artifact coordinates.
     *
     * @param repository The repository (list of URLs).
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    private static Set<String> mvnUrlsToCoord(final List<String> repository) throws MalformedURLException {
        final var result = new LinkedHashSet<String>();
        for (var url : repository) {
            result.add(toCoord(new URL(url)));
        }
        LOG.trace("mvnUrlsToCoord({}) returns {}", repository, result);
        return result;
    }

    /**
     * Converts the given features' repository to artifact coordinates.
     *
     * @param features The features.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    private static Set<String> featuresRepositoryToCoords(final Features features) throws MalformedURLException {
        return mvnUrlsToCoord(features.getRepository());
    }

    /**
     * Lists the artifact coordinates of the given feature's bundles and configuration files.
     *
     * @param feature The feature.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    private static Set<String> featureToCoords(final Feature feature) throws MalformedURLException {
        final var result = new LinkedHashSet<String>();
        if (feature.getBundle() != null) {
            result.addAll(bundlesToCoords(feature.getBundle()));
        }
        final var conditionals = feature.getConditional();
        if (conditionals != null) {
            for (var conditional : conditionals) {
                final var bundles = conditional.getBundles();
                if (bundles != null) {
                    for (var bundleInfo : bundles) {
                        result.add(toCoord(new URL(bundleInfo.getLocation())));
                    }
                }
            }
            // TODO Dependencies
        }
        final var configFile = feature.getConfigfile();
        if (configFile != null) {
            result.addAll(configFilesToCoords(configFile));
        }
        LOG.trace("featureToCoords({}) returns {}", feature.getName(), result);
        return result;
    }

    /**
     * Lists the artifact coordinates of the given configuration files.
     *
     * @param configfiles The configuration files.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    private static Set<String> configFilesToCoords(final List<ConfigFile> configfiles) throws MalformedURLException {
        final var result = new LinkedHashSet<String>();
        for (var configFile : configfiles) {
            result.add(toCoord(new URL(configFile.getLocation())));
        }
        LOG.trace("configFilesToCoords({}) returns {}", configfiles, result);
        return result;
    }

    /**
     * Lists the artifact coordinates of the given bundles.
     *
     * @param bundles The bundles.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    private static Set<String> bundlesToCoords(final List<Bundle> bundles) throws MalformedURLException {
        final var result = new LinkedHashSet<String>();
        for (var bundle : bundles) {
            try {
                result.add(toCoord(new URL(bundle.getLocation())));
            } catch (MalformedURLException e) {
                LOG.error("Invalid URL {}", bundle.getLocation(), e);
                throw e;
            }
        }
        LOG.trace("bundlesToCoords({}) returns {}", bundles, result);
        return result;
    }

    /**
     * Extracts all the artifact coordinates for the given features (repositories, bundles, configuration files).
     *
     * @param features The feature.
     * @return The artifact coordinates.
     * @throws MojoExecutionException if an error occurs during processing.
     */
    private static Set<String> featuresToCoords(final Features features) throws MojoExecutionException {
        final var result = new LinkedHashSet<String>();
        if (features.getRepository() != null) {
            try {
                result.addAll(featuresRepositoryToCoords(features));
            } catch (MalformedURLException e) {
                throw new MojoExecutionException("Feature " + features.getName() + " has an invalid repository URL", e);
            }
        }
        if (features.getFeature() != null) {
            for (var feature : features.getFeature()) {
                try {
                    result.addAll(featureToCoords(feature));
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException("Feature " + feature.getName() + " in " + features.getName()
                            + " contains an invalid or missing URL", e);
                }
            }
        }
        LOG.trace("featuresToCoords({}) returns {}", features.getName(), result);
        return result;
    }

    /**
     * Extracts all the artifact coordinates for the given set of features (repositories, bundles, configuration
     * files).
     *
     * @param features The features.
     * @return The artifact coordinates.
     * @throws MojoExecutionException if an error occurs during processing.
     */
    static Set<String> featuresToCoords(final Set<Features> features) throws MojoExecutionException {
        final var result = new LinkedHashSet<String>();
        for (var feature : features) {
            result.addAll(featuresToCoords(feature));
        }
        LOG.trace("featuresToCoords({}) returns {}", features, result);
        return result;
    }

    /**
     * Unmarshal all the features in the given artifacts.
     *
     * @param featureArtifacts The artifacts.
     * @return The features.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    Set<Features> readFeatures(final Set<Artifact> featureArtifacts) throws IOException {
        final var result = new LinkedHashSet<Features>();
        for (var artifact : featureArtifacts) {
            result.add(readFeature(artifact.getFile()));
        }
        LOG.trace("readFeatures({}) returns {}", featureArtifacts, result);
        return result;
    }

    /**
     * Unmarshal the features in the given file.
     *
     * @param file The file.
     * @return The features.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    Features readFeature(final File file) throws IOException {
        final var localFile = getFileInLocalRepo(file);
        final var stream = Files.newInputStream(localFile != null ? localFile : file.toPath());
        final var result = JaxbUtil.unmarshal(file.toURI().toString(), stream, false);
        LOG.trace("readFeature({}) returns {} without resolving first", file, result.getName());
        return result;
    }

    /**
     * Unmarshal the features matching the given artifact coordinates.
     *
     * @param coords The artifact coordinates.
     * @return The features.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    private Features readFeature(final String coords) throws IOException {
        final var result = readFeature(aetherUtil.resolveArtifact(coords).getFile());
        LOG.trace("readFeature({}) returns {} after resolving first", coords, result.getName());
        return result;
    }

    /**
     * Unmarshals all the features starting from the given feature.
     *
     * @param features The starting features.
     * @param existingCoords The artifact coordinates which have already been unmarshalled.
     * @return The features.
     * @throws MalformedURLException if a URL is malformed.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    private Set<Features> findAllFeaturesRecursively(final Features features,
            final LinkedHashSet<String> existingCoords) throws IOException {
        LOG.debug("findAllFeaturesRecursively({}) starts", features.getName());
        LOG.trace("findAllFeaturesRecursively knows about these coords: {}", existingCoords);
        final var result = new LinkedHashSet<Features>();
        for (var coord : featuresRepositoryToCoords(features)) {
            if (!existingCoords.contains(coord)) {
                LOG.trace("findAllFeaturesRecursively() going to add {}", coord);
                existingCoords.add(coord);
                final var feature = readFeature(coord);
                result.add(feature);
                LOG.debug("findAllFeaturesRecursively() added {}", coord);
                result.addAll(findAllFeaturesRecursively(feature, existingCoords));
            } else {
                LOG.trace("findAllFeaturesRecursively() skips known {}", coord);
            }
        }
        return result;
    }

    /**
     * Unmarshals all the features starting from the given features.
     *
     * @param features The starting features.
     * @param existingCoords The artifact coordinates which have already been unmarshalled.
     * @return The features.
     * @throws MalformedURLException if a URL is malformed.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    private Set<Features> findAllFeaturesRecursively(final Set<Features> features,
            final LinkedHashSet<String> existingCoords) throws IOException {
        final var result = new LinkedHashSet<Features>();
        for (var feature : features) {
            result.addAll(findAllFeaturesRecursively(feature, existingCoords));
        }
        return result;
    }

    /**
     * Unmarshals all the features (including known ones) starting from the given features.
     *
     * @param features The starting features.
     * @return The features.
     * @throws MalformedURLException if a URL is malformed.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    Set<Features> findAllFeaturesRecursively(final Set<Features> features) throws IOException {
        return findAllFeaturesRecursively(features, new LinkedHashSet<>());
    }

    void removeLocalArtifacts(final Set<Artifact> artifacts) {
        if (localRepo != null) {
            final var it = artifacts.iterator();
            while (it.hasNext()) {
                final var artifact = it.next();
                if (getFileInLocalRepo(artifact.getFile()) != null) {
                    LOG.trace("Removing artifact {}", artifact);
                    it.remove();
                }
            }
        }
    }

    private Path getFileInLocalRepo(final File file) {
        final var filePath = file.toPath();
        var parent = filePath.getParent();
        while (parent != null) {
            final var candidate = localRepo.toPath().resolve(parent.relativize(filePath));
            if (Files.exists(candidate)) {
                return candidate;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
