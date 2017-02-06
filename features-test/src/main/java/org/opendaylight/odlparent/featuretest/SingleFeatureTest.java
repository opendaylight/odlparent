/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent.featuretest;

import static java.util.concurrent.TimeUnit.SECONDS;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.common.collect.ImmutableList;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
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
import org.junit.runners.Parameterized.Parameters;
import org.opendaylight.odlparent.bundlestest.TestBundleDiag;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamParameterized;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.PropagateSystemPropertyOption;
import org.ops4j.pax.exam.options.extra.VMOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExamParameterized.class)
@ExamReactorStrategy(PerClass.class)
public class SingleFeatureTest {

    private static final String ORG_OPENDAYLIGHT_FEATURETEST_FEATURE_VERSION_REPO_LIST = "features_versions_repos";
    private static final String ORG_OPENDAYLIGHT_FEATURETEST_ARE_FEATURES_INITIALIZED_FLAG = "get_features_initialized";
    private static final String MAVEN_REPO_LOCAL = "maven.repo.local";
    private static final String ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY = "org.ops4j.pax.url.mvn.localRepository";
    private static final String ORG_OPS4J_PAX_URL_MVN_REPOSITORIES = "org.ops4j.pax.url.mvn.repositories";
    private static final String ETC_ORG_OPS4J_PAX_URL_MVN_CFG = "etc/org.ops4j.pax.url.mvn.cfg";

//    //@Parameter(value = 0)
    public static String theFeature;
//
//    //@Parameter(value = 1)
    public String theVersion;
//
//    //@Parameter(value = 2)
    public String theRepoUri;

    public static Option[] theConfig;

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

    private static String karafVersion;
    private static String karafDistroVersion;
    private static boolean isURLStreamHandlerFactorySet = false;

    // We have to exceptionally suppress IllegalCatch just because URL.setURLStreamHandlerFactory stupidly throws Error
    @SuppressWarnings("checkstyle:IllegalCatch")
    // see doc on isURLStreamHandlerFactorySet for why we do NOT want to do this in a static block
    static synchronized void setURLStreamHandlerFactory() {
        if (!isURLStreamHandlerFactorySet) {
            try {
                URL.setURLStreamHandlerFactory(new CustomBundleUrlStreamHandlerFactory());
                isURLStreamHandlerFactorySet = true;
            } catch (Error e) {
                LOG.warn("Failed to setURLStreamHandlerFactory to CustomBundleUrlStreamHandlerFactory "
                       + "(depending on which is already set, this may or may not actually be a problem"
                       + "; e.g. Karaf 4 already registers the neccessary handlers, so OK to ignore)", e);
            }
        }
    }


    /**
     * Returns the required configuration.
     *
     * @return The Pax Exam configuration.
     * @throws IOException if an error occurs.
     */
    public static Option[] commonConfig() throws IOException {

        LOG.info("commonConfig(): the suite is starting!");
        setURLStreamHandlerFactory();

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
            //todo ediegra remove org.ops4j.pax.exam.CoreOptions.junitBundles(),
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
            new PropagateSystemPropertyOption(ORG_OPENDAYLIGHT_FEATURETEST_ARE_FEATURES_INITIALIZED_FLAG),
            new PropagateSystemPropertyOption(ORG_OPENDAYLIGHT_FEATURETEST_FEATURE_VERSION_REPO_LIST),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_SKIP_PROP),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_FORCE_PROP),
            new PropagateSystemPropertyOption(BUNDLES_DIAG_TIMEOUT_PROP),
            // Needed for Agrona/aeron.io
            CoreOptions.systemPackages("com.sun.media.sound", "sun.nio.ch"),
        };
    }

    private static String getNewJFRFile() throws IOException {
        return File.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr").getAbsolutePath();
    }

    private static Option standardKarafFeatures() throws IOException {
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

    private static String getKarafVersion() {
        if (karafVersion == null) {
            // We use a properties file to retrieve ${karaf.version}, instead of .versionAsInProject()
            // This avoids forcing all users to depend on Karaf in their POMs
            Properties singleFeatureTestProps = new Properties();
            try (InputStream singleFeatureTestInputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(PROPERTIES_FILENAME)) {
                if (singleFeatureTestInputStream == null) {
                    throw new IOException("Resource not found; expected to be present on current thread classloader: "
                            + PROPERTIES_FILENAME);
                }
                singleFeatureTestProps.load(singleFeatureTestInputStream);
            }
            karafVersion = singleFeatureTestProps.getProperty(KARAF_DISTRO_VERSION_PROP);

            LOG.info("Retrieved karafVersion {} from properties file {}", karafVersion, PROPERTIES_FILENAME);
        } else {
            LOG.info("Retrieved karafVersion {} from system property {}", karafVersion, KARAF_DISTRO_VERSION_PROP);
        }

        return karafVersion;
    }

    private static String getKarafDistroVersion() {
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

    protected static Option mvnLocalRepoOption() {
        String mvnRepoLocal = System.getProperty(MAVEN_REPO_LOCAL, "");
        LOG.info("mvnLocalRepo \"{}\"", mvnRepoLocal);
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY,
                mvnRepoLocal);
    }

    protected static Option getKarafDistroOption() {
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

    @Parameters
    public static Collection<String[]> getFeatures() throws ClassNotFoundException, IOException {

        LOG.info("getFeatures() (parameterization): starting!");

        // List of features is calculated in the original VM (the one started by the surefire plugin).
        // The internal (pax) VM cannot build that list itself; instead, it receives that  list
        // composed of features + versions + repos to test via property propagation
        boolean isInitialization = (System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_ARE_FEATURES_INITIALIZED_FLAG) == null);
        if (!isInitialization) {
            LOG.info("getFeatures() already initialized. Returning stored value value");
            return Arrays.asList(base64Deserialize(getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURE_VERSION_REPO_LIST)));
        }

        LOG.info("getFeatures(). Uninitialized. Calculating list of features / versions to test");
        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_ARE_FEATURES_INITIALIZED_FLAG, "true");
        Features features;
        String[][] myList = null;
        try {
            String repoUri = getUriString();
            URL theUrl = new URL(repoUri);
            LOG.info("getFeatures() theurl: {}", theUrl);
            features = JaxbUtil.unmarshal(theUrl.openStream(), false);

            final List<org.apache.karaf.features.internal.model.Feature> featureList = features.getFeature();
            LOG.info("getFeatures() Number of features: {}", featureList.size());
            myList = new String[featureList.size()][3];
            int index = 0;
            for (final Feature f : featureList) {
                myList[index][0] = f.getName();
                myList[index][1] = f.getVersion();
                myList[index][2] = repoUri;
                LOG.info("getFeatures() Added feature [{}] version [{}] repo [{}]",
                        myList[index][0],  myList[index][1],  myList[index][2]);
                index++;
            }
        } catch (IOException e) {
            LOG.error("getFeatures(): failed!!", e);
            throw e;
        }

        System.setProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURE_VERSION_REPO_LIST, base64Serialize(myList));
        return Arrays.asList(myList);
    }

    /**
     * Returns the required configuration.
     *
     * @return The Pax Exam configuration.
     * @throws IOException if an error occurs.
     */
    @Configuration
    public static Option[] config() {
        LOG.info("config() starting");
        try {
            return commonConfig();
        } catch (Exception e) {
            LOG.error("config() failed!", e);
            return null;
        }
    }

    public SingleFeatureTest(String feature, String version, String uri) {
        theFeature = feature;
        theVersion = version;
        theRepoUri = uri;
        LOG.info("singleFeatureTest():called with values {},{},{}", theFeature, theVersion, theRepoUri);
    }

    private static String getUriString() {
            String[] FEATURES_FILENAMES = new String[] { "features.xml", "feature.xml" };
            for (String filename : FEATURES_FILENAMES) {
                LOG.info("getUriString(): checking file: {}", filename);
                final URL repoUrl = SingleFeatureTest.class.getClassLoader().getResource(filename);
                if (repoUrl != null) {
                    LOG.info("getUriString: returning url: {}", repoUrl);
                    return repoUrl.toString();
                }
            }
            LOG.info("getUriString: returning null");
            return null;
    }

    private String getFeatureName() {
        return theFeature;
    }

    public String getFeatureVersion() {
        return theVersion;
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
        final URI repoUri = new URI(theRepoUri);
        LOG.info("Attempting to add repository from param uri [{}]", repoUri);
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

    /*
     * Storage of an String[][] as a base-64 encoded string
     */
    public static String base64Serialize(String[][] input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(input);

        String result = new String(Base64.encodeBase64(out.toByteArray()));
        return result;
    }

    /*
     * Recovery of the original String[][] from the a base-64 encoded string
     *@param input the string to decode
     */
    public static String[][] base64Deserialize(String input) throws ClassNotFoundException, IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(input.getBytes()));
        String[][] decodedArray = (String[][])new ObjectInputStream(in).readObject();
        return decodedArray;
    }
}
