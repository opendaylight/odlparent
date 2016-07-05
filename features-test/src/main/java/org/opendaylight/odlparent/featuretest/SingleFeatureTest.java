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
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.features.Repository;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.extra.VMOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PerRepoTestRunner.class)
public class SingleFeatureTest {
    private static final String MAVEN_REPO_LOCAL = "maven.repo.local";
    private static final String ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY = "org.ops4j.pax.url.mvn.localRepository";
    private static final String ORG_OPS4J_PAX_URL_MVN_REPOSITORIES = "org.ops4j.pax.url.mvn.repositories";
    private static final String ETC_ORG_OPS4J_PAX_URL_MVN_CFG = "etc/org.ops4j.pax.url.mvn.cfg";
    private static final String ETC_ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";
    private static final String KEEP_UNPACK_DIRECTORY_PROP = "karaf.keep.unpack";
    private static final String LOG4J_LOGGER_ORG_OPENDAYLIGHT_YANGTOOLS_FEATURETEST =
            "log4j.logger.org.opendaylight.odlparent.featuretest";
    private static final Logger LOG = LoggerFactory.getLogger(SingleFeatureTest.class);

    /*
     * File name to add our logging config property too.
     */
    private static final String ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";

    /*
     * Default values for karaf distro type, groupId, and artifactId
     */
    private static final String KARAF_DISTRO_TYPE = "zip";
    private static final String KARAF_DISTRO_ARTIFACTID = "apache-karaf";
    private static final String KARAF_DISTRO_GROUPID = "org.apache.karaf";

    /*
     * Property names to override defaults for karaf distro artifactId, groupId, version, and type
     */
    private static final String KARAF_DISTRO_VERSION_PROP = "karaf.distro.version";
    private static final String KARAF_DISTRO_TYPE_PROP = "karaf.distro.type";
    private static final String KARAF_DISTRO_ARTIFACTID_PROP = "karaf.distro.artifactId";
    private static final String KARAF_DISTRO_GROUPID_PROP = "karaf.distro.groupId";

    /**
     * Property file used to store the Karaf distribution version.
     */
    private static final String PROPERTIES_FILENAME = "singlefeaturetest.properties";

    /**
     * <p>List of Karaf 3.0.4 default maven repositories with snapshot repositories excluded.</p>
     * <p>Unfortunately this must be hard-coded since declarative model which uses Options,
     * does not allow us to read value, parse it (properties has allways
     * problems with lists) and construct replacement string which does
     * not contains snapshots.</p>
     * <p>When updating Karaf, check this against org.ops4j.pax.url.mvn.cfg in the Karaf distribution.</p>
     */
    private static final String EXTERNAL_DEFAULT_REPOSITORIES = "http://repo1.maven.org/maven2@id=central, "
            + "http://repository.springsource.com/maven/bundles/release@id=spring.ebr.release, "
            + "http://repository.springsource.com/maven/bundles/external@id=spring.ebr.external, "
            + "http://zodiac.springsource.com/maven/bundles/release@id=gemini ";


    @Inject
    private FeaturesService featuresService;

    private String karafVersion;
    private String karafDistroVersion;

    /**
     * Returns the required configuration.
     *
     * @return The Pax Exam configuration.
     * @throws IOException if an error occurs.
     */
    @Configuration
    public Option[] config() throws IOException {
        return new Option[] {
            // TODO: Find a way to inherit memory limits from Maven options.
            new VMOption("-Xmx2g"),
            new VMOption("-XX:MaxPermSize=512m"),
            getKarafDistroOption(),
            when(Boolean.getBoolean(KEEP_UNPACK_DIRECTORY_PROP)).useOptions(keepRuntimeFolder()),
            configureConsole().ignoreLocalConsole(),
            logLevel(LogLevel.WARN),
            mvnLocalRepoOption(),
            standardKarafFeatures(),
            editConfigurationFilePut(ORG_OPS4J_PAX_LOGGING_CFG, LOG4J_LOGGER_ORG_OPENDAYLIGHT_YANGTOOLS_FEATURETEST,
                    LogLevel.INFO.name()),
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_LOGGING_CFG, "log4j.rootLogger", "INFO, stdout, osgi:*"),
             /*
              *
              * Disables external snapshot repositories.
              *
              * Pax URL and Karaf by default always search for new version of snapshots
              * in all snapshots repository, even if that snapshots does not belong to that
              * repository and maven is invoked even with -nsu (no snapshot update)
              * or offline mode.
              *
              * This is also true for OpenDaylight snapshot artefacts - pax url tries
              * to resolve them from third-party repositories, even if they are not present
              * there - this increases time which takes for features to install.
              *
              * For more complex projects this actually means several HTTP GETs for each
              * snapshot bundle referenced, even if the bundle is already present
              * in local maven repository.
              *
              * In order to speed-up installation and remove unnecessary network traffic,
              * which fails for obvious reasons, external snapshot repositories are
              * removed.
              *
              *
              */
            disableExternalSnapshotRepositories(),
            CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP).value(
                    System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP)),
            CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP).value(
                    System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP)),
            CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP).value(
                    System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP)),
        };
    }

    private Option standardKarafFeatures() {
        String url = maven().groupId("org.apache.karaf.features").artifactId("standard").classifier("features").type(
                "xml").version(getKarafVersion()).getURL();
        try {
            Features features = JaxbUtil.unmarshal(new URL(url).openStream(), false);
            List<String> featureNames = new ArrayList<>();
            for (Feature f : features.getFeature()) {
                featureNames.add(f.getName());
            }

            return features(url, featureNames.toArray(new String[featureNames.size()]));
        } catch (Exception e) {
            throw new RuntimeException("Could not obtain features from URL " + url, e);
        }
    }

    private String getKarafVersion() {
        if (karafVersion == null) {
            // We use a properties file to retrieve ${karaf.version}, instead of .versionAsInProject()
            // This avoids forcing all users to depend on Karaf in their POMs
            Properties singleFeatureTestProps = new Properties();
            try (InputStream singleFeatureTestInputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(PROPERTIES_FILENAME)) {
                singleFeatureTestProps.load(singleFeatureTestInputStream);
            } catch (IOException e) {
                LOG.error("Unable to load {} to determine the Karaf version", PROPERTIES_FILENAME, e);
            }
            karafVersion = singleFeatureTestProps.getProperty(KARAF_DISTRO_VERSION_PROP);

            LOG.info("Retrieved karafVersion {} from properties file {}", karafVersion, PROPERTIES_FILENAME);
        } else {
            LOG.info("Retrieved karafVersion {} from system property {}", karafVersion, KARAF_DISTRO_VERSION_PROP);
        }

        return karafVersion;
    }

    private String getKarafDistroVersion() {
        if (karafDistroVersion == null) {
            karafDistroVersion = System.getProperty(KARAF_DISTRO_VERSION_PROP);
            if (karafDistroVersion == null) {
                karafDistroVersion = getKarafVersion();
            } else {
                LOG.info("Retrieved karafDistroVersion {} from system property {}", karafVersion,
                        KARAF_DISTRO_VERSION_PROP);
            }
        }

        return karafDistroVersion;
    }

    /**
     * Disables snapshot repositories, which are enabled by default in karaf distribution.
     *
     * @return Edit Configuration option which removes external snapshot repositories.
     */
    private static Option disableExternalSnapshotRepositories() {
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, ORG_OPS4J_PAX_URL_MVN_REPOSITORIES,
                EXTERNAL_DEFAULT_REPOSITORIES);
    }

    protected Option mvnLocalRepoOption() {
        String mvnRepoLocal = System.getProperty(MAVEN_REPO_LOCAL, "");
        LOG.info("mvnLocalRepo \"{}\"", mvnRepoLocal);
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY,
                mvnRepoLocal);
    }

    protected Option getKarafDistroOption() {
        String groupId = System.getProperty(KARAF_DISTRO_GROUPID_PROP, KARAF_DISTRO_GROUPID);
        String artifactId = System.getProperty(KARAF_DISTRO_ARTIFACTID_PROP, KARAF_DISTRO_ARTIFACTID);
        String type = System.getProperty(KARAF_DISTRO_TYPE_PROP, KARAF_DISTRO_TYPE);
        LOG.info("Using karaf distro {} {} {} {}", groupId, artifactId, getKarafDistroVersion(), type);
        return karafDistributionConfiguration()
                .frameworkUrl(
                        maven()
                                .groupId(groupId)
                                .artifactId(artifactId)
                                .type(type)
                                .version(getKarafDistroVersion()))
                .name("OpenDaylight")
                .unpackDirectory(new File("target/pax"))
                .useDeployFolder(false);
    }

    private static URI getRepoUri() throws URISyntaxException {
        return new URI(getProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP));
    }

    private static String getFeatureName() {
        return getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP);
    }

    public String getFeatureVersion() {
        return getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP);
    }

    private static String getProperty(final String propName) {
        String prop = System.getProperty(propName);
        Assert.assertTrue("Missing property :" + propName, prop != null);
        return prop;
    }

    private void checkRepository(final URI repoUri) {
        Repository repo = null;
        for (Repository r : featuresService.listRepositories()) {
            if (r.getURI().equals(repoUri)) {
                repo = r;
                break;
            }
        }
        Assert.assertNotNull("Repository not found: " + repoUri, repo);
    }

    /**
     * Sets the repository up.
     *
     * @throws Exception if an error occurs.
     */
    @Before
    public void installRepo() throws Exception {
        final URI repoUri = getRepoUri();
        LOG.info("Attempting to add repository {}", repoUri);
        featuresService.addRepository(repoUri);
        checkRepository(repoUri);
        LOG.info("Successfully loaded repository {}", repoUri);
    }

    // Give it 10 minutes max as we've seen feature install hang on jenkins.
    @Test(timeout = 600000)
    public void installFeature() throws Exception {
        LOG.info("Attempting to install feature {} {}", getFeatureName(), getFeatureVersion());
        featuresService.installFeature(getFeatureName(), getFeatureVersion());
        Feature feature = featuresService.getFeature(getFeatureName(), getFeatureVersion());
        Assert.assertNotNull(
                "Attempt to get feature " + getFeatureName() + " " + getFeatureVersion() + "resulted in null", feature);
        Assert.assertTrue("Failed to install Feature: " + getFeatureName() + " " + getFeatureVersion(),
                featuresService.isInstalled(feature));
        LOG.info("Successfull installed feature {} {}", getFeatureName(), getFeatureVersion());
    }
}
