/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent.featuretest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.features.Repository;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.odlparent.bundlestest.TestBundleDiag;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.PropagateSystemPropertyOption;
import org.ops4j.pax.exam.options.extra.VMOption;
import org.osgi.framework.BundleContext;
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
    private static final String PROFILE_PROP = "karaf.featureTest.profile";
    private static final String BUNDLES_DIAG_SKIP_PROP = "sft.diag.skip";
    private static final String BUNDLES_DIAG_FORCE_PROP = "sft.diag.force";
    private static final String BUNDLES_DIAG_TIMEOUT_PROP = "sft.diag.timeout";

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

    @Inject @NonNull
    private BundleContext bundleContext;

    @Inject @NonNull
    private FeaturesService featuresService;

    @Inject @NonNull
    private BundleService bundleService; // NOT BundleStateService, see checkBundleStatesDiag()

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
            // inspired by org.apache.commons.lang.SystemUtils
            when(System.getProperty("os.name").toLowerCase().startsWith("linux")).useOptions(
                // This prevents low entropy issues on Linux to affect Java random numbers
                // which can block crypto such as the SSH server in netconf
                // see https://bugs.opendaylight.org/show_bug.cgi?id=6790
                new VMOption("-Djava.security.egd=file:/dev/./urandom")
            ),
            when(Boolean.getBoolean(PROFILE_PROP)).useOptions(
                new VMOption("-XX:+UnlockCommercialFeatures"),
                new VMOption("-XX:+FlightRecorder"),
                new VMOption("-XX:FlightRecorderOptions=defaultrecording=true,dumponexit=true,dumponexitpath="
                               + getNewJFRFile())
            ),
            getKarafDistroOption(),
            when(Boolean.getBoolean(KEEP_UNPACK_DIRECTORY_PROP)).useOptions(keepRuntimeFolder()),
            configureConsole().ignoreLocalConsole(),
            logLevel(LogLevel.WARN),
            mvnLocalRepoOption(),
            standardKarafFeatures(),
            wrappedBundle(maven("org.awaitility", "awaitility").versionAsInProject()), // req. by bundles-test
            mavenBundle(maven("com.google.guava", "guava").versionAsInProject()),      // req. by bundles-test
            mavenBundle(maven("org.opendaylight.odlparent", "bundles-test").versionAsInProject()),
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
            new PropagateSystemPropertyOption(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP),
            new PropagateSystemPropertyOption(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP),
            new PropagateSystemPropertyOption(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_SKIP_PROP),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_FORCE_PROP),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_TIMEOUT_PROP),
            // Needed for Agrona/aeron.io
            CoreOptions.systemPackages("com.sun.media.sound", "sun.nio.ch"),
        };
    }

    private String getNewJFRFile() throws IOException {
        return File.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr").getAbsolutePath();
    }

    private Option standardKarafFeatures() throws IOException {
        String url = maven().groupId("org.apache.karaf.features").artifactId("standard").classifier("features").type(
                "xml").version(getKarafVersion()).getURL();
        try {
            Features features = JaxbUtil.unmarshal(new URL(url).openStream(), false);
            List<String> featureNames = new ArrayList<>();
            for (Feature f : features.getFeature()) {
                featureNames.add(f.getName());
            }

            return features(url, featureNames.toArray(new String[featureNames.size()]));
        } catch (IOException e) {
            throw new IOException("Could not obtain features from URL: " + url, e);
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
        featuresService.installFeature(getFeatureName(), getFeatureVersion(),
                EnumSet.of(FeaturesService.Option.NoCleanIfFailure, FeaturesService.Option.PrintExecptionPerFeature));
        Feature feature = featuresService.getFeature(getFeatureName(), getFeatureVersion());
        Assert.assertNotNull(
                "Attempt to get feature " + getFeatureName() + " " + getFeatureVersion() + "resulted in null", feature);
        Assert.assertTrue("Failed to install Feature: " + getFeatureName() + " " + getFeatureVersion(),
                featuresService.isInstalled(feature));
        LOG.info("Successfull installed feature {} {}", getFeatureName(), getFeatureVersion());

        if (!Boolean.getBoolean(BUNDLES_DIAG_SKIP_PROP)
                && (Boolean.getBoolean(BUNDLES_DIAG_FORCE_PROP)
                    || !BLACKLISTED_BROKEN_FEATURES.contains(getFeatureName()))) {
            Integer timeOutInSeconds = Integer.getInteger(BUNDLES_DIAG_TIMEOUT_PROP, 5 * 60);
            new TestBundleDiag(bundleContext, bundleService).checkBundleDiagInfos(timeOutInSeconds, SECONDS);
        } else {
            LOG.warn("SKIPPING TestBundleDiag because system property {} is true or feature is blacklisted: {}",
                    BUNDLES_DIAG_SKIP_PROP, getFeatureName());
        }
    }

    // TODO remove this when all issues linked to parent https://bugs.opendaylight.org/show_bug.cgi?id=7582 are resolved
    private static final List<String> BLACKLISTED_BROKEN_FEATURES = ImmutableList.of(
            // integration/distribution/features-test due to DOMRpcService
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7595
            "odl-integration-all",
            // controller/features/mdsal/ due to IllegalStateException: ./configuration/initial/akka.conf is missing
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7583
            "odl-mdsal-broker-local",
            "odl-mdsal-clustering-commons",
            "odl-mdsal-distributed-datastore",
            "odl-mdsal-remoterpc-connector",
            // aaa/features/authn due to Cassandra expected to be up on
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7584
            "odl-aaa-authn-cassandra-cluster",
            // 3/18 in bgpcep/features/bgp/ due to NoSuchFileException: etc/....
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7585
            "odl-bgpcep-bgp-rib-impl",
            "odl-bgpcep-bgp-topology",
            "odl-bgpcep-bgp-cli",
            // 1/1 in bgpcep/features/bmp due to NoSuchFileException: etc/opendaylight/bgp
            // also https://bugs.opendaylight.org/show_bug.cgi?id=7585 (same as above)
            "odl-bgpcep-bmp",
            // 4/8 in lispflowmapping/features due to.. unclear, similar issue to odl-integration-all?
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7586
            // TODO retry after https://bugs.opendaylight.org/show_bug.cgi?id=7595 is fixed
            "odl-lispflowmapping-mappingservice",
            "odl-lispflowmapping-mappingservice-shell",
            "odl-lispflowmapping-neutron",
            "odl-lispflowmapping-ui",
            // 1/17 in groupbasedpolicy/features due to NOK org.opendaylight.groupbasedpolicy
            // Caused by: org.opendaylight.mdsal.eos.common.api.CandidateAlreadyRegisteredException
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7587
            "odl-groupbasedpolicy-ne-location-provider",
            // 1/11 in tsdr/features due to (strange) ClassNotFoundException: odlparent.bundlestest
            //   .TestBundleDiag (works for all other features; class loading issue in that feature?)
            // see https://bugs.opendaylight.org/show_bug.cgi?id=7588
            "odl-hbaseclient",
            // 1/9 in unimgr/features due missing mdsal, similar to issue to odl-integration-all?
            // TODO retry after https://bugs.opendaylight.org/show_bug.cgi?id=7595 is fixed
            "odl-unimgr-netvirt"
    );
}
