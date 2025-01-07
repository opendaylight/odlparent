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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
import org.checkerframework.checker.regex.qual.Regex;
import org.eclipse.aether.artifact.Artifact;
import org.ops4j.pax.url.mvn.internal.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FeatureUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FeatureUtil.class);

    private static final Pattern MVN_PATTERN = Pattern.compile("mvn:", Pattern.LITERAL);
    private static final Pattern WRAP_PATTERN = Pattern.compile("wrap:", Pattern.LITERAL);

    @Regex
    private static final String VERSION_STRIP_PATTERN_STR = "\\$.*$";
    private static final Pattern VERSION_STRIP_PATTERN = Pattern.compile(VERSION_STRIP_PATTERN_STR);

    private final AetherUtil aetherUtil;
    private final File localRepo;

    public FeatureUtil(final AetherUtil aetherUtil, final File localRepo) {
        this.aetherUtil = aetherUtil;
        this.localRepo = localRepo;
    }

    /**
     * Converts the given list of URLs to artifact coordinates.
     *
     * @param urls The URLs.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static List<String> toCoord(final List<URL> urls) throws MalformedURLException {
        List<String> result = new ArrayList<>();
        for (URL url : urls) {
            result.add(toCoord(url));
        }
        LOG.trace("toCoord({}) returns {}", urls, result);
        return result;
    }

    /**
     * Converts the given URL to artifact coordinates.
     *
     * @param url The URL.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if the URL is malformed.
     */
    public static String toCoord(final URL url) throws MalformedURLException {
        String repository = url.toString();
        String unwrappedRepo = WRAP_PATTERN.matcher(repository).replaceFirst("");

        Parser parser = new Parser(unwrappedRepo);
        String coord = MVN_PATTERN.matcher(parser.getGroup()).replaceFirst("") + ":" + parser.getArtifact();
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
     * @param repository The repository (list of URLs)
     * @return The corresponding artifact coordinates
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> mvnUrlsToCoord(final List<String> repository)
            throws MalformedURLException, URISyntaxException {
        final var result = new LinkedHashSet<String>();
        for (var url : repository) {
            result.add(toCoord(new URI(url).toURL()));
        }
        LOG.trace("mvnUrlsToCoord({}) returns {}", repository, result);
        return result;
    }

    /**
     * Converts the given features' repository to artifact coordinates.
     *
     * @param features The features.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> featuresRepositoryToCoords(final Features features)
            throws MalformedURLException, URISyntaxException {
        return mvnUrlsToCoord(features.getRepository());
    }

    /**
     * Converts all the given features' repositories to artifact coordinates.
     *
     * @param features The features.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> featuresRepositoryToCoords(final Set<Features> features)
            throws MalformedURLException, URISyntaxException {
        final var result = new LinkedHashSet<String>();
        for (var feature : features) {
            result.addAll(featuresRepositoryToCoords(feature));
        }
        LOG.trace("featuresRepositoryToCoords({}) returns {}", features, result);
        return result;
    }

    /**
     * Lists the artifact coordinates of the given feature's bundles and configuration files.
     *
     * @param feature The feature.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> featureToCoords(final Feature feature) throws MalformedURLException, URISyntaxException {
        final var result = new LinkedHashSet<String>();
        final var bundle = feature.getBundle();
        if (bundle != null) {
            result.addAll(bundlesToCoords(bundle));
        }
        final var conditionals = feature.getConditional();
        if (conditionals != null) {
            for (var conditional : conditionals) {
                final var bundles = conditional.getBundles();
                if (bundles != null) {
                    for (var bundleInfo : bundles) {
                        result.add(toCoord(new URI(bundleInfo.getLocation()).toURL()));
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
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> configFilesToCoords(final List<ConfigFile> configfiles)
            throws MalformedURLException, URISyntaxException {
        final var result = new LinkedHashSet<String>();
        for (var configFile : configfiles) {
            result.add(toCoord(new URI(configFile.getLocation()).toURL()));
        }
        LOG.trace("configFilesToCoords({}) returns {}", configfiles, result);
        return result;
    }

    /**
     * Lists the artifact coordinates of the given bundles.
     *
     * @param bundles The bundles.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     */
    public static Set<String> bundlesToCoords(final List<Bundle> bundles)
            throws MalformedURLException, URISyntaxException {
        final var result = new LinkedHashSet<String>();
        for (var bundle : bundles) {
            try {
                result.add(toCoord(new URI(bundle.getLocation()).toURL()));
            } catch (MalformedURLException | URISyntaxException e) {
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
    public static Set<String> featuresToCoords(final Features features) throws MojoExecutionException {
        final var result = new LinkedHashSet<String>();
        if (features.getRepository() != null) {
            try {
                result.addAll(featuresRepositoryToCoords(features));
            } catch (MalformedURLException | URISyntaxException e) {
                throw new MojoExecutionException("Feature " + features.getName() + " has an invalid repository URL", e);
            }
        }

        final var featureList = features.getFeature();
        if (featureList != null) {
            for (var feature : featureList) {
                try {
                    result.addAll(featureToCoords(feature));
                } catch (MalformedURLException | URISyntaxException e) {
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
    public static Set<String> featuresToCoords(final Set<Features> features) throws MojoExecutionException {
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
    public Set<Features> readFeatures(final Set<Artifact> featureArtifacts) throws IOException {
        final var result = new LinkedHashSet<Features>();
        for (var artifact : featureArtifacts) {
            result.add(readFeature(artifact));
        }
        LOG.trace("readFeatures({}) returns {}", featureArtifacts, result);
        return result;
    }

    /**
     * Unmarshal the features in the given artifact.
     *
     * @param artifact The artifact.
     * @return The features.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    public Features readFeature(final Artifact artifact) throws IOException {
        return readFeature(artifact.getFile());
    }

    /**
     * Unmarshal the features in the given file.
     *
     * @param file The file.
     * @return The features.
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    public Features readFeature(final File file) throws IOException {
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
    public Features readFeature(final String coords) throws IOException {
        final var result = readFeature(aetherUtil.resolveArtifact(coords));
        LOG.trace("readFeature({}) returns {} after resolving first", coords, result.getName());
        return result;
    }

    /**
     * Unmarshals all the features starting from the given feature.
     *
     * @param features The starting features.
     * @param existingCoords The artifact coordinates which have already been unmarshalled.
     * @return The features.
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    public Set<Features> findAllFeaturesRecursively(final Features features, final Set<String> existingCoords)
            throws MalformedURLException, IOException, URISyntaxException {
        LOG.debug("findAllFeaturesRecursively({}) starts", features.getName());
        LOG.trace("findAllFeaturesRecursively knows about these coords: {}", existingCoords);
        final var result = new LinkedHashSet<Features>();
        final var coords = FeatureUtil.featuresRepositoryToCoords(features);
        for (var coord : coords) {
            if (!existingCoords.contains(coord)) {
                LOG.trace("findAllFeaturesRecursively() going to add {}", coord);
                existingCoords.add(coord);
                Features feature = readFeature(coord);
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
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    public Set<Features> findAllFeaturesRecursively(final Set<Features> features, final Set<String> existingCoords)
            throws MalformedURLException, IOException, URISyntaxException {
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
     * @throws MalformedURLException if a URL cannot be used
     * @throws URISyntaxException if a URL is malformed
     * @throws IOException if a file cannot be read
     * @throws FileNotFoundException if a file is missing.
     */
    public Set<Features> findAllFeaturesRecursively(final Set<Features> features)
            throws MalformedURLException, IOException, URISyntaxException {
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
        Path filePath = file.toPath();
        Path parent = filePath.getParent();
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
