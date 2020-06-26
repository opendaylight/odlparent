/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.runner.Description;

public final class Util {
    private static final String DEPENDENCIES_PROPERTIES = "/META-INF/maven/dependencies.properties";

    private Util() {

    }

    /**
     * Convert a Description to a Description that includes information about repoUrl, featureName, and
     * featureVersion. This is done so that when a test fails, we can get information about which repoUrl, featureName,
     * and featureVersion can come back with the Failure.
     *
     * @param repoUrl URL of the repository.
     * @param featureName the name of the feature.
     * @param featureVersion the version of the feature.
     * @param description original description of the feature.
     * @return the final description of the feature with the information of repoUrl, featureName, and featureVersion
     *         included.
     */
    public static Description convertDescription(
            final URL repoUrl, final String featureName,
            final String featureVersion, final Description description) {
        String delegateDisplayName = description.getDisplayName();
        delegateDisplayName =
                delegateDisplayName + "[repoUrl: " + repoUrl + ", Feature: " + featureName + " " + featureVersion + "]";
        Collection<Annotation> annotations = description.getAnnotations();
        Annotation[] annotationArray = annotations.toArray(new Annotation[annotations.size()]);
        return Description.createSuiteDescription(delegateDisplayName, annotationArray);
    }

    static List<MavenDependency> findTestDependencies() throws IOException {
        return findTestDependencies(Util.loadMavenDependencies()).stream()
                .filter(dep -> "xml".equals(dep.type()))
                .filter(dep -> "features".equals(dep.classifier()))
                .collect(Collectors.toList());
    }

    @VisibleForTesting
    static List<MavenDependency> findTestDependencies(final Properties dependencies) {
        final List<MavenDependency> ret = new ArrayList<>();

        for (Entry<Object, Object> entry : dependencies.entrySet()) {
            final String key = (String) entry.getKey();
            if (key.endsWith("/scope")) {
                if ("test".equals(entry.getValue())) {
                    ret.add(MavenDependency.create(dependencies, key));
                }
            }
        }

        return ret;
    }

    private static Properties loadMavenDependencies() throws IOException {
        final InputStream input = Util.class.getResourceAsStream(DEPENDENCIES_PROPERTIES);
        if (input == null) {
            throw new IOException("Failed to find " + DEPENDENCIES_PROPERTIES);
        }

        final Properties props = new Properties();
        props.load(input);
        input.close();
        return props;
    }
}
