/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent.featuretest;

import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP;

import java.net.URL;

import com.google.common.base.Preconditions;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.ops4j.pax.exam.junit.PaxExam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerFeatureRunner extends Runner implements Filterable, Sortable {
    private static final Logger LOG = LoggerFactory.getLogger(PerFeatureRunner.class);
    private final String featureVersion;
    private final String featureName;
    private final PaxExam delegate;
    private final URL repoUrl;

    /**
     * Create a runner.
     *
     * @param repoUrl        The repository URL.
     * @param featureName    The feature name.
     * @param featureVersion The feature version.
     * @param testClass      The test class.
     * @throws InitializationError if an error occurs.
     */
    public PerFeatureRunner(
            final URL repoUrl, final String featureName, final String featureVersion, final Class<?> testClass)
            throws InitializationError {
        this.repoUrl = Preconditions.checkNotNull(repoUrl);
        this.featureName = Preconditions.checkNotNull(featureName);
        this.featureVersion = Preconditions.checkNotNull(featureVersion);

        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP, repoUrl.toString());
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP, featureName);
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP, featureVersion);
        this.delegate = new PaxExam(Preconditions.checkNotNull(testClass));
    }

    @Override
    public Description getDescription() {
        return Util.convertDescription(repoUrl, featureName, featureVersion, delegate.getDescription());
    }

    @Override
    public void run(final RunNotifier notifier) {
        LOG.info("About to run test for feature: {} {}", featureName, featureVersion);
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP, repoUrl.toString());
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP, featureName);
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP, featureVersion);
        delegate.run(new PerFeatureRunNotifier(repoUrl, featureName, featureVersion, notifier));
    }

    @Override
    public int testCount() {
        return delegate.testCount();
    }

    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        delegate.filter(filter);
    }

    @Override
    public void sort(final Sorter sorter) {
        delegate.sort(sorter);
    }

    /**
     * Delegated implementation of {@link #toString()}.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return delegate.toString();
    }

    /**
     * Returns the repository URL.
     *
     * @return The repository URL.
     */
    public URL getRepoUrl() {
        return repoUrl;
    }

    /**
     * Returns the feature name.
     *
     * @return The feature name.
     */
    public String getFeatureName() {
        return featureName;
    }

    /**
     * Returns the feature version.
     *
     * @return The feature version.
     */
    public String getFeatureVersion() {
        return featureVersion;
    }
}
