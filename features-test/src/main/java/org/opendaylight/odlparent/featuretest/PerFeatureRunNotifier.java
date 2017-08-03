/*
 * SPDX-License-Identifier: EPL-1.0
 *
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import java.net.URL;
import java.rmi.NoSuchObjectException;
import java.util.Objects;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class PerFeatureRunNotifier extends RunNotifier {

    private final RunNotifier delegate;
    private final URL repoUrl;
    private final String featureName;
    private final String featureVersion;

    /**
     * Create a delegating notifier.
     *
     * @param repoUrl The repository URL.
     * @param featureName The feature name.
     * @param featureVersion The feature version.
     * @param delegate The notification delegate.
     */
    public PerFeatureRunNotifier(
            final URL repoUrl, final String featureName, final String featureVersion, final RunNotifier delegate) {
        this.repoUrl = Objects.requireNonNull(repoUrl);
        this.featureName = Objects.requireNonNull(featureName);
        this.featureVersion = Objects.requireNonNull(featureVersion);
        this.delegate = Objects.requireNonNull(delegate);
    }

    private Failure convertFailure(final Failure failure) {
        return new Failure(Util.convertDescription(repoUrl, featureName, featureVersion, failure.getDescription()),
                failure.getException());
    }

    @Override
    public void addListener(final RunListener listener) {
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(final RunListener listener) {
        delegate.removeListener(listener);
    }

    /**
     * Calculates the hash code (delegated).
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public void fireTestRunStarted(final Description description) {
        delegate.fireTestRunStarted(Util.convertDescription(repoUrl, featureName, featureVersion, description));
    }

    @Override
    public void fireTestRunFinished(final Result result) {
        delegate.fireTestRunFinished(result);
    }

    @Override
    public void fireTestStarted(final Description description) {
        delegate.fireTestStarted(Util.convertDescription(repoUrl, featureName, featureVersion, description));
    }

    @Override
    public boolean equals(final Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public void fireTestFailure(final Failure failure) {
        if (!(failure.getException() instanceof NoSuchObjectException)) {
            delegate.fireTestFailure(convertFailure(failure));
        }
    }

    @Override
    public void fireTestAssumptionFailed(final Failure failure) {
        delegate.fireTestAssumptionFailed(convertFailure(failure));
    }

    @Override
    public void fireTestIgnored(final Description description) {
        delegate.fireTestIgnored(Util.convertDescription(repoUrl, featureName, featureVersion, description));
    }

    @Override
    public void fireTestFinished(final Description description) {
        delegate.fireTestFinished(Util.convertDescription(repoUrl, featureName, featureVersion, description));
    }

    @Override
    public void pleaseStop() {
        delegate.pleaseStop();
    }

    @Override
    public void addFirstListener(final RunListener listener) {
        delegate.addFirstListener(listener);
    }

    /**
     * Value of the delegate as a String.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return delegate.toString();
    }
}
