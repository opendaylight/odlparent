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
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.opendaylight.odlparent.karafutil.CustomBundleUrlStreamHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerRepoTestRunner extends ParentRunner<PerFeatureRunner> {

    private static final Logger LOG = LoggerFactory.getLogger(PerRepoTestRunner.class);

    private static final String REPO_RECURSE = "repo.recurse";
    private static final String[] FEATURES_FILENAMES = new String[] { "features.xml", "feature.xml" };

    private static boolean isURLStreamHandlerFactorySet = false;
    // Do NOT static {@code { URL.setURLStreamHandlerFactory(new CustomBundleUrlStreamHandlerFactory()); }}
    // This is VERY BAD practice, because it leads to VERY HARD TO TRACK errors in case ANYTHING goes wrong in this.
    // For example, we had a case where (following an upgrade of PAX Exam) a dependency was missing. This appeared
    // as a confusing error because the root cause of static initialization errors is typically lost in Java; so best is
    // NOT to use it!
    // ("NoClassDefFoundError: Could not initialize class ...PerRepoTestRunner")

    private final List<PerFeatureRunner> children = new ArrayList<>();

    /**
     * Create a runner.
     *
     * @param testClass The test class.
     * @throws InitializationError if an error occurs.
     */
    public PerRepoTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
        setURLStreamHandlerFactory();
        try {
            for (String filename : FEATURES_FILENAMES) {
                final URL repoUrl = getClass().getClassLoader().getResource(filename);
                if (repoUrl != null) {
                    final boolean recursive = Boolean.getBoolean(REPO_RECURSE);
                    LOG.info("Creating test runners for repoUrl {} recursive {}", repoUrl, recursive);
                    children.addAll(runnersFromRepoUrl(repoUrl, testClass, recursive));
                }
            }
            if (children.isEmpty()) {
                LOG.error("No features found to test; looked for {}", Arrays.toString(FEATURES_FILENAMES));
            }
        } catch (final IOException | JAXBException e) {
            throw new InitializationError(e);
        }
    }

    // We have to exceptionally suppress IllegalCatch just because URL.setURLStreamHandlerFactory stupidly throws Error
    @SuppressWarnings("checkstyle:IllegalCatch")
    // see doc on isURLStreamHandlerFactorySet for why we do NOT want to do this in a static block
    private static synchronized void setURLStreamHandlerFactory() {
        if (!isURLStreamHandlerFactorySet) {
            try {
                URL.setURLStreamHandlerFactory(new CustomBundleUrlStreamHandlerFactory());
                isURLStreamHandlerFactorySet = true;
            } catch (Error e) {
                LOG.warn("""
                    Failed to setURLStreamHandlerFactory to CustomBundleUrlStreamHandlerFactory (depending on which is
                    already set, this may or may not actually be a problem; e.g. Karaf 4 already registers
                    the neccessary handlers, so OK to ignore)""", e);
            }
        }
    }

    protected List<PerFeatureRunner> runnersFromRepoUrl(
            final URL repoUrl, final Class<?> testClass, final boolean recursive)
            throws JAXBException, IOException, InitializationError {
        if (recursive) {
            return recursiveRunnersFromRepoUrl(repoUrl, testClass);
        } else {
            return runnersFromRepoUrl(repoUrl, testClass);
        }
    }

    protected List<PerFeatureRunner> runnersFromRepoUrl(final URL repoUrl, final Class<?> testClass)
            throws JAXBException, IOException, InitializationError {
        return runnersFromFeatures(repoUrl, getFeatures(repoUrl), testClass);
    }

    protected List<PerFeatureRunner> recursiveRunnersFromRepoUrl(final URL repoUrl, final Class<?> testClass)
            throws JAXBException, IOException, InitializationError {
        final List<PerFeatureRunner> runners = new ArrayList<>();
        final Features features = getFeatures(repoUrl);
        runners.addAll(runnersFromRepoUrl(repoUrl, testClass));
        for (final String repoString : features.getRepository()) {
            final URL subRepoUrl = new URL(repoString);
            runners.addAll(recursiveRunnersFromRepoUrl(subRepoUrl, testClass));
        }
        return runners;
    }

    protected List<PerFeatureRunner> runnersFromFeatures(
            final URL repoUrl, final Features features, final Class<?> testClass) throws InitializationError {
        final var runners = new ArrayList<PerFeatureRunner>();
        final var featureList = features.getFeature();
        for (var f : featureList) {
            // If the features have more than one feature, ignore any feature with the same name as the
            // repository — these are the aggregator features generated by the Karaf Maven plugin, and
            // which are expensive to test
            if (featureList.size() == 1 || !f.getName().equals(features.getName())) {
                runners.add(new PerFeatureRunner(repoUrl, f.getName(), f.getVersion(), testClass));
            } else {
                LOG.warn("Skipping {}, it's an aggregator feature", f.getName());
            }
        }
        return runners;
    }

    protected Features getFeatures(final URL repoUrl) throws JAXBException, IOException {
        return JaxbUtil.unmarshal(repoUrl.toExternalForm(), false);
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
    @SuppressWarnings("checkstyle:RegexpSinglelineJava")
    protected void runChild(final PerFeatureRunner child, final RunNotifier notifier) {
        LOG.info("[LOG] About to run test: {}", child.getDescription());
        System.out.println("[sys.out] About to run test: " + child.getDescription());
        child.run(notifier);
    }

    @Override
    public int testCount() {
        return super.testCount() * children.size();
    }
}
