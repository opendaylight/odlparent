/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.karaf.features.internal.model.Bundle;
import org.apache.karaf.features.internal.model.ConfigFile;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.ops4j.pax.url.mvn.Parser;

public final class FeatureUtil {
    private FeatureUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Converts the given list of URLs to artifact coordinates.
     *
     * @param urls The URLs.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static List<String> toCoord(List<URL> urls) throws MalformedURLException {
        List<String> result = new ArrayList<>();
        for (URL url : urls) {
            result.add(toCoord(url));
        }
        return result;
    }

    /**
     * Converts the given URL to artifact coordinates.
     *
     * @param url The URL.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if the URL is malformed.
     */
    public static String toCoord(URL url) throws MalformedURLException {
        String repository = url.toString();
        String unwrappedRepo = repository.replaceFirst("wrap:", "");
        Parser parser = new Parser(unwrappedRepo);
        String coord = parser.getGroup().replace("mvn:", "") + ":" + parser.getArtifact();
        if (parser.getType() != null) {
            coord = coord + ":" + parser.getType();
        }
        if (parser.getClassifier() != null) {
            coord = coord + ":" + parser.getClassifier();
        }
        coord = coord + ":" + parser.getVersion().replaceAll("\\$.*$", "");
        return coord;
    }

    /**
     * Parses the given repository as URLs and converts them to artifact coordinates.
     *
     * @param repository The repository (list of URLs).
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> mvnUrlsToCoord(List<String> repository) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        for (String url : repository) {
            result.add(toCoord(new URL(url)));
        }
        return result;
    }

    /**
     * Converts the given features' repository to artifact coordinates.
     *
     * @param features The features.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> featuresRepositoryToCoords(Features features) throws MalformedURLException {
        return mvnUrlsToCoord(features.getRepository());
    }

    /**
     * Converts all the given features' repositories to artifact coordinates.
     *
     * @param features The features.
     * @return The corresponding artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> featuresRepositoryToCoords(Set<Features> features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        for (Features feature : features) {
            result.addAll(featuresRepositoryToCoords(feature));
        }
        return result;
    }

    /**
     * Lists the artifact coordinates of the given feature's bundles and configuration files.
     *
     * @param feature The feature.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> featureToCoords(Feature feature) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        if (feature.getBundle() != null) {
            result.addAll(bundlesToCoords(feature.getBundle()));
        }
        if (feature.getConfigfile() != null) {
            result.addAll(configFilesToCoords(feature.getConfigfile()));
        }
        return result;
    }

    /**
     * Lists the artifact coordinates of the given configuration files.
     *
     * @param configfiles The configuration files.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> configFilesToCoords(List<ConfigFile> configfiles) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        for (ConfigFile configFile : configfiles) {
            result.add(toCoord(new URL(configFile.getLocation())));
        }
        return result;
    }

    /**
     * Lists the artifact coordinates of the given bundles.
     *
     * @param bundles The bundles.
     * @return The corresponding coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> bundlesToCoords(List<Bundle> bundles) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        for (Bundle bundle : bundles) {
            result.add(toCoord(new URL(bundle.getLocation())));
        }
        return result;
    }

    /**
     * Extracts all the artifact coordinates for the given features (repositories, bundles, configuration files).
     *
     * @param features The feature.
     * @return The artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> featuresToCoords(Features features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        if (features.getRepository() != null) {
            result.addAll(featuresRepositoryToCoords(features));
        }
        if (features.getFeature() != null) {
            for (Feature feature : features.getFeature()) {
                result.addAll(featureToCoords(feature));
            }
        }
        return result;
    }

    /**
     * Extracts all the artifact coordinates for the given set of features (repositories, bundles, configuration
     * files).
     *
     * @param features The features.
     * @return The artifact coordinates.
     * @throws MalformedURLException if a URL is malformed.
     */
    public static Set<String> featuresToCoords(Set<Features> features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<>();
        for (Features feature : features) {
            result.addAll(featuresToCoords(feature));
        }
        return result;
    }

    /**
     * Unmarshal all the features in the given artifacts.
     *
     * @param featureArtifacts The artifacts.
     * @return The features.
     * @throws FileNotFoundException if a file is missing.
     */
    public static Set<Features> readFeatures(Set<Artifact> featureArtifacts) throws FileNotFoundException {
        Set<Features> result = new LinkedHashSet<>();
        for (Artifact artifact : featureArtifacts) {
            result.add(readFeature(artifact));
        }
        return result;
    }

    /**
     * Unmarshal the features in the given artifact.
     *
     * @param artifact The artifact.
     * @return The features.
     * @throws FileNotFoundException if a file is missing.
     */
    public static Features readFeature(Artifact artifact) throws FileNotFoundException {
        File file = artifact.getFile();
        FileInputStream stream = new FileInputStream(file);
        return JaxbUtil.unmarshal(stream, false);
    }

    /**
     * Unmarshal the features matching the given artifact coordinates.
     *
     * @param aetherUtil The Aether resolver.
     * @param coords The artifact coordinates.
     * @return The features.
     * @throws ArtifactResolutionException if the coordinates can't be resolved.
     * @throws FileNotFoundException if a file is missing.
     */
    public static Features readFeature(AetherUtil aetherUtil, String coords)
            throws ArtifactResolutionException, FileNotFoundException {
        Artifact artifact = aetherUtil.resolveArtifact(coords);
        return readFeature(artifact);
    }

    /**
     * Unmarshals all the features starting from the given feature.
     *
     * @param aetherUtil The Aether resolver.
     * @param features The starting features.
     * @param existingCoords The artifact coordinates which have already been unmarshalled.
     * @return The features.
     * @throws MalformedURLException if a URL is malformed.
     * @throws FileNotFoundException if a file is missing.
     * @throws ArtifactResolutionException if artifact coordinates can't be resolved.
     */
    public static Set<Features> findAllFeaturesRecursively(
            AetherUtil aetherUtil, Features features, Set<String> existingCoords)
            throws MalformedURLException, FileNotFoundException, ArtifactResolutionException {
        Set<Features> result = new LinkedHashSet<>();
        Set<String> coords = FeatureUtil.featuresRepositoryToCoords(features);
        for (String coord : coords) {
            if (!existingCoords.contains(coord)) {
                existingCoords.add(coord);
                Features feature = FeatureUtil.readFeature(aetherUtil, coord);
                result.add(feature);
                result.addAll(findAllFeaturesRecursively(aetherUtil, FeatureUtil.readFeature(aetherUtil, coord),
                        existingCoords));
            }
        }
        return result;
    }

    /**
     * Unmarshals all the features starting from the given features.
     *
     * @param aetherUtil The Aether resolver.
     * @param features The starting features.
     * @param existingCoords The artifact coordinates which have already been unmarshalled.
     * @return The features.
     * @throws MalformedURLException if a URL is malformed.
     * @throws FileNotFoundException if a file is missing.
     * @throws ArtifactResolutionException if artifact coordinates can't be resolved.
     */
    public static Set<Features> findAllFeaturesRecursively(
            AetherUtil aetherUtil, Set<Features> features, Set<String> existingCoords)
            throws MalformedURLException, FileNotFoundException, ArtifactResolutionException {
        Set<Features> result = new LinkedHashSet<>();
        for (Features feature : features) {
            result.addAll(findAllFeaturesRecursively(aetherUtil, feature, existingCoords));
        }
        return result;
    }

}
