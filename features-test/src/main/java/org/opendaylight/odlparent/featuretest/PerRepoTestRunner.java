/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.apache.karaf.tooling.url.CustomBundleURLStreamHandlerFactory;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerRepoTestRunner extends ParentRunner<PerFeatureRunner> {
    private static final String REPO_RECURSE = "repo.recurse";
    private static final Logger LOG = LoggerFactory.getLogger(PerRepoTestRunner.class);
    private static final String FEATURES_FILENAME = "features.xml";
    private final List<PerFeatureRunner> children = new ArrayList<>();

    static {
        // Static initialization, as we may be invoked multiple times
        URL.setURLStreamHandlerFactory(new CustomBundleURLStreamHandlerFactory());
    }

    public PerRepoTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
        try {
            final URL repoURL = getClass().getClassLoader().getResource(FEATURES_FILENAME);
            final boolean recursive = Boolean.getBoolean(REPO_RECURSE);
            LOG.info("Creating test runners for repoURL {} recursive {}",repoURL,recursive);
            children.addAll(runnersFromRepoURL(repoURL,testClass,recursive));
        } catch (final Exception e) {
            throw new InitializationError(e);
        }
    }

    protected List<PerFeatureRunner> runnersFromRepoURL(final URL repoURL,final Class<?> testClass,final boolean recursive) throws JAXBException, IOException, InitializationError {
        if(recursive) {
            return recursiveRunnersFromRepoURL(repoURL,testClass);
        } else {
            return runnersFromRepoURL(repoURL,testClass);
        }
    }

    protected List<PerFeatureRunner> runnersFromRepoURL(final URL repoURL,final Class<?> testClass) throws JAXBException, IOException, InitializationError {
        final List<PerFeatureRunner> runners = new ArrayList<>();
        final Features features = getFeatures(repoURL);
        runners.addAll(runnersFromFeatures(repoURL,features,testClass));
        return runners;
    }

    protected List<PerFeatureRunner> recursiveRunnersFromRepoURL(final URL repoURL,final Class<?> testClass) throws JAXBException, IOException, InitializationError {
        final List<PerFeatureRunner> runners = new ArrayList<>();
        final Features features = getFeatures(repoURL);
        runners.addAll(runnersFromRepoURL(repoURL,testClass));
        for(final String repoString: features.getRepository()) {
            final URL subRepoURL = new URL(repoString);
            runners.addAll(recursiveRunnersFromRepoURL(subRepoURL,testClass));
        }
        return runners;
    }

    protected List<PerFeatureRunner> runnersFromFeatures(final URL repoURL, final Features features,final Class<?> testClass) throws InitializationError {
        final List<PerFeatureRunner> runners = new ArrayList<>();
        final List<Feature> featureList = features.getFeature();
        for(final Feature f : featureList) {
            runners.add(new PerFeatureRunner(repoURL, f.getName(), f.getVersion(),testClass));
        }
        return runners;
    }

    /**
     * @param repoURL url of the repo
     * @return features
     * @throws JAXBException exception during unmarshalling.
     * @throws IOException IOException in getting the features.
     */
    protected Features getFeatures(final URL repoURL) throws JAXBException,
            IOException {
        return JaxbUtil.unmarshal(repoURL.openStream(), false);
    }

    @Override
    protected List<PerFeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(final PerFeatureRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(final PerFeatureRunner child, final RunNotifier notifier) {
        LOG.info("About to run test for {}", child.getRepoURL());
        child.run(notifier);
    }

    /* (non-Javadoc)
     * @see org.junit.runner.Runner#testCount()
     */
    @Override
    public int testCount() {
        return super.testCount() * children.size();
    }
}
