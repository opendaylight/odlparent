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

public class FeatureUtil {

    public static  List<String> toCoord(List<URL> urls) throws MalformedURLException {
        List<String> result = new ArrayList<String>();
        for(URL url:urls) {
            result.add(toCoord(url));
        }
        return result;
    }

    public static String toCoord(URL url) throws MalformedURLException {
        String repository = url.toString();
        String unwrappedRepo = repository.replaceFirst("wrap:", "");
        Parser parser = new Parser(unwrappedRepo);
        String coord = parser.getGroup().replace("mvn:","") + ":" + parser.getArtifact();
        if(parser.getType() != null) {
            coord = coord + ":" + parser.getType();
        }
        if(parser.getClassifier() != null) {
            coord = coord + ":" + parser.getClassifier();
        }
        coord = coord + ":" + parser.getVersion().replaceAll("\\$.*$", "");
        return coord;
    }

    public static Set<String> mvnUrlsToCoord(List<String> repository) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        for(String url:repository) {
            result.add(toCoord(new URL(url)));
        }
        return result;
    }

    public static Set<String> featuresRepositoryToCoords(Features features) throws MalformedURLException {
        return mvnUrlsToCoord(features.getRepository());
    }

    public static Set<String> featuresRepositoryToCoords(Set<Features> features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        for(Features feature:features) {
            result.addAll(featuresRepositoryToCoords(feature));
        }
        return result;
    }

    public static Set<String> featureToCoords(Feature feature) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        if(feature.getBundle() != null) {
            result.addAll(bundlesToCoords(feature.getBundle()));
        }
        if(feature.getConfigfile() != null) {
            result.addAll(configFilesToCoords(feature.getConfigfile()));
        }
        return result;
    }

    public static Set<String> configFilesToCoords(List<ConfigFile> configfiles) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        for(ConfigFile configFile: configfiles) {
            result.add(toCoord(new URL(configFile.getLocation())));
        }
        return result;
    }

    public static Set<String> bundlesToCoords(List<Bundle> bundles) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        for(Bundle bundle: bundles) {
            result.add(toCoord(new URL(bundle.getLocation())));
        }
        return result;
    }

    public static Set<String> featuresToCoords(Features features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        if(features.getRepository() != null) {
            result.addAll(featuresRepositoryToCoords(features));
        }
        if(features.getFeature() != null) {
            for(Feature feature: features.getFeature() ) {
                result.addAll(featureToCoords(feature));
            }
        }
        return result;
    }

    public static Set<String> featuresToCoords(Set<Features> features) throws MalformedURLException {
        Set<String> result = new LinkedHashSet<String>();
        for(Features feature:features) {
            result.addAll(featuresToCoords(feature));
        }
        return result;
    }

    public static LinkedHashSet<Features> readFeatures(Set<Artifact> featureArtifacts) throws FileNotFoundException {
        LinkedHashSet<Features> result = new LinkedHashSet<Features>();
        for(Artifact artifact: featureArtifacts) {
            result.add(readFeature(artifact));
        }
        return result;
    }

    public static Features readFeature(Artifact artifact) throws FileNotFoundException {
        Features result;
        File file = artifact.getFile();
        FileInputStream stream = new FileInputStream(file);
        Features features = JaxbUtil.unmarshal(stream, false);
        result = features;
        return result;
    }

    public static Features readFeature(AetherUtil aetherUtil,String coords) throws ArtifactResolutionException, FileNotFoundException {
        Artifact artifact = aetherUtil.resolveArtifact(coords);
        return readFeature(artifact);
    }

    public static Set<Features> findAllFeaturesRecursively(AetherUtil aetherUtil,Features features,Set<String> existingCoords) throws MalformedURLException, FileNotFoundException, ArtifactResolutionException {
        Set<Features> result = new LinkedHashSet<Features>();
        Set<String> coords = FeatureUtil.featuresRepositoryToCoords(features);
        for(String coord : coords) {
            if(!existingCoords.contains(coord)) {
                existingCoords.add(coord);
                Features f = FeatureUtil.readFeature(aetherUtil,coord);
                result.add(f);
                result.addAll(findAllFeaturesRecursively(aetherUtil,FeatureUtil.readFeature(aetherUtil,coord), existingCoords));
            }
        }
        return result;
    }

    public static Set<Features> findAllFeaturesRecursively(AetherUtil aetherUtil,Set<Features> features,Set<String> existingCoords) throws MalformedURLException, FileNotFoundException, ArtifactResolutionException {
        Set<Features> result = new LinkedHashSet<Features>();
        for(Features feature: features) {
            result.addAll(findAllFeaturesRecursively(aetherUtil, feature, existingCoords));
        }
        return result;
    }

}
