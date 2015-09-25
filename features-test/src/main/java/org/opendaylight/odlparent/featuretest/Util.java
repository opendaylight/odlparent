/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent.featuretest;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;

import org.junit.runner.Description;

public final class Util {

    private Util() {
        // Noop constructor
    }

    /**
     * <p>Convert a Description to a Description that includes information about repoUrl, featureName, and
     * featureVersion</p>
     * <p>This is done so that when a test fails, we can get information about which repoUrl, featureName, and
     * featureVersion can come back with the Failure.</p>
     *
     * @param repoUrl URL of the repository.
     * @param featureName the name of the feature.
     * @param featureVersion the version of the feature.
     * @param description original description of the feature.
     * @return the final description of the feature with the information of repoUrl, featureName, and featureVersion
     *     included.
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
}
