/*
 * Copyright © 2014, 2017 Cisco Systems, Inc. and others.  All rights reserved.
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
import static org.ops4j.pax.exam.CoreOptions.propagateSystemProperty;
import static org.ops4j.pax.exam.CoreOptions.systemPackages;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.features.Repository;
import org.awaitility.Awaitility;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.odlparent.bundlestest.lib.TestBundleDiag;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.karaf.container.internal.JavaVersionUtil;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
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

    // Maximum heap size
    private static final String HEAP_MAX_PROP = "sft.heap.max";
    private static final String DEFAULT_HEAP_MAX = "2g";

    // Path for placing heap dump file.
    private static final String HEAP_DUMP_PATH_PROP = "sft.heap.dump.path";
    private static final String DEFAULT_HEAP_DUMP_PATH = "/dev/null";

    private static final Logger LOG = LoggerFactory.getLogger(SingleFeatureTest.class);

    /*
     * File name to add our logging config property too.
     */
    private static final String ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";

    /*
     * Default values for karaf distro type, groupId, and artifactId
     */
    private static final String KARAF_DISTRO_TYPE = "zip";
    private static final String KARAF_DISTRO_ARTIFACTID = "opendaylight-karaf-empty";
    private static final String KARAF_DISTRO_GROUPID = "org.opendaylight.odlparent";

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

    private static final String KARAF_VERSION = System.getProperty("karaf.version", "4.2.2");

    private static final VMOption[] JDK9PLUS_VMOPIONS = new VMOption[] {
        new VMOption("--add-reads=java.xml=java.logging"),
        new VMOption("--add-exports=java.base/org.apache.karaf.specs.locator=java.xml,ALL-UNNAMED"),
        new VMOption("--patch-module"),
        new VMOption("java.base=lib/endorsed/org.apache.karaf.specs.locator-" + KARAF_VERSION + ".jar"),
        new VMOption("--patch-module"),
        new VMOption("java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-" + KARAF_VERSION + ".jar"),
        new VMOption("--add-opens"),
        new VMOption("java.base/java.security=ALL-UNNAMED"),
        new VMOption("--add-opens"),
        new VMOption("java.base/java.net=ALL-UNNAMED"),
        new VMOption("--add-opens"),
        new VMOption("java.base/java.lang=ALL-UNNAMED"),
        new VMOption("--add-opens"),
        new VMOption("java.base/java.util=ALL-UNNAMED"),
        new VMOption("--add-opens"),
        new VMOption("java.naming/javax.naming.spi=ALL-UNNAMED"),
        new VMOption("--add-opens"),
        new VMOption("java.rmi/sun.rmi.transport.tcp=ALL-UNNAMED"),
        new VMOption("--add-exports=java.base/sun.net.www.protocol.http=ALL-UNNAMED"),
        new VMOption("--add-exports=java.base/sun.net.www.protocol.https=ALL-UNNAMED"),
        new VMOption("--add-exports=java.base/sun.net.www.protocol.jar=ALL-UNNAMED"),
        new VMOption("--add-exports=jdk.naming.rmi/com.sun.jndi.url.rmi=ALL-UNNAMED"),
        new VMOption("-classpath"),
        new VMOption("lib/jdk9plus/*" + File.pathSeparator + "lib/boot/*")
    };

    @Inject @NonNull
    private BundleContext bundleContext;

    @Inject @NonNull
    private BundleService bundleService; // NOT BundleStateService, see checkBundleStatesDiag()

    @Inject @NonNull
    private FeaturesService featuresService;

    private String karafVersion;
    private String karafDistroVersion;

    @ProbeBuilder
    public TestProbeBuilder probeConfiguration(final TestProbeBuilder probe) {
        // add this to test Karaf Commands, according to green Karaf book
        // also see http://iocanel.blogspot.ch/2012/01/advanced-integration-testing-with-pax.html
        // probe.setHeader(org.osgi.framework.Constants.DYNAMICIMPORT_PACKAGE, "*;status=provisional");

        // adding these so that loading of TestBundleDiag and its dependencies works
        // NB that here in the features4-test this works completely differently than
        // in the original features-test for Karaf 3; there we installed the bundle in
        // config() whereas here we embed dependencies into a single JAR to simplify
        // problems we've had in distribution jobs with custom local Maven repos;
        // but because of this we have to "help" Pax Exam with what classes need
        // to be bundled with its probe:
        ReflectionUtil.addAllClassesInSameAndSubPackageOfClass(probe, TestBundleDiag.class);
        ReflectionUtil.addAllClassesInSameAndSubPackageOfClass(probe, Awaitility.class);
        ReflectionUtil.addAllClassesInSameAndSubPackageOfPackage(probe, "com.google.common");

        return probe;
    }

    /**
     * Returns the required configuration.
     *
     * @return The Pax Exam configuration.
     * @throws IOException if an error occurs.
     */
    @Configuration
    public Option[] config() throws IOException {
        final String envMaxHeap = System.getProperty(HEAP_MAX_PROP);
        final String maxHeap = envMaxHeap != null ? envMaxHeap : DEFAULT_HEAP_MAX;
        final String envHeapDumpPath = System.getProperty(HEAP_DUMP_PATH_PROP);
        final String heapDumpPath = envHeapDumpPath != null ? envHeapDumpPath : DEFAULT_HEAP_DUMP_PATH;

        // ODLPARENT-148 must use getAbsoluteFile(), because the current working directory changes;
        // when this runs from maven-surefire-plugin, it's the Maven project directory (where src/ and target/ are),
        // but when Pax Exam runs Karaf with the options we configure below, then the it's ./target/pax/6f...6c/;
        // so because we don't want this to be ./target/pax/6f...6c/target/SFT/karaf.log but target/SFT/karaf.log:
        final File karafLogFile = new File("target/SFT/karaf.log").getAbsoluteFile();
        karafLogFile.getParentFile().mkdir();

        final Option[] baseConfig = new Option[] {
            new VMOption("-Xmx" + maxHeap),
            new VMOption("-XX:+HeapDumpOnOutOfMemoryError"),
            new VMOption("-XX:HeapDumpPath=" + heapDumpPath),
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
            logLevel(LogLevel.INFO),
            mvnLocalRepoOption(),

            // TODO ODLPARENT-148: We change the karaf.log location because it's very useful for this to be preserved
            // even if one does not use "-Dkaraf.keep.unpack=true", which on build server is typically not feasible,
            // because that leads to excessive disk space consumption (full karaf dist; what we really want is the log)
            // replace default "${karaf.data}/log/karaf.log" by "target/SFT/karaf.log"
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_LOGGING_CFG, "log4j2.appender.rolling.fileName",
                    karafLogFile.getPath()),
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_LOGGING_CFG, "log4j2.appender.rolling.filePattern",
                    karafLogFile.getPath() + ".%i"),

             /*
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
              */
            disableExternalSnapshotRepositories(),
            propagateSystemProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP),
            propagateSystemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP),
            propagateSystemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP),
            propagateSystemProperty(BUNDLES_DIAG_SKIP_PROP),
            propagateSystemProperty(BUNDLES_DIAG_FORCE_PROP),
            propagateSystemProperty(BUNDLES_DIAG_TIMEOUT_PROP),
            // Needed for Agrona/aeron.io
            systemPackages("com.sun.media.sound", "sun.net", "sun.nio.ch"),
        };

        if (JavaVersionUtil.getMajorVersion() <= 8) {
            return baseConfig;
        }

        final Option[] jdk9plus = Arrays.copyOf(baseConfig, baseConfig.length + JDK9PLUS_VMOPIONS.length);
        System.arraycopy(JDK9PLUS_VMOPIONS, 0, jdk9plus, baseConfig.length, JDK9PLUS_VMOPIONS.length);
        return jdk9plus;
    }

    private static String getNewJFRFile() throws IOException {
        return File.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr").getAbsolutePath();
    }

    private String getKarafVersion() throws IOException {
        if (karafVersion == null) {
            // We use a properties file to retrieve Karaf's version, instead of .versionAsInProject()
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

    private String getKarafDistroVersion() throws IOException {
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

    protected Option getKarafDistroOption() throws IOException {
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

    private void checkRepository(final URI repoUri) throws Exception {
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
    @SuppressWarnings("checkstyle:IllegalCatch")
    public void installFeatureCatchAndLog() throws Exception {
        // TODO remove this when the underlying problem is solved
        // https://bugs.opendaylight.org/show_bug.cgi?id=7981:
        // "SFT never fails, Pax Exam (or our wrappers) swallow all exceptions"
        try {
            installFeature();
        } catch (Throwable t) {
            LOG.error("installFeature() failed", t);
            // as of 2017.03.20, this re-throw seems to have no effect,
            // the exception gets lost in space, swallowed somewhere! :(
            throw t;
        }
    }

    public void installFeature() throws Exception {
        // The BundleContext originally @Inject'd into the field
        // is, as expected, the PAXEXAM-PROBE.  For some strange reason,
        // under Karaf 4 (this works under Karaf 3 without this trick),
        // after the installFeature() & getFeature() & isInstalled()
        // below are through, that BundleContext has become invalid
        // already (too soon!), and using it leads to "IllegalStateException:
        // BundleContext is no longer valid". -- Because we don't actually
        // need the PAXEXAM-PROBE, just ANY BundleContext, we employ a
        // little trick, and obtain the OSGi Framework's (Felix or Equinox's)
        // own BundleContext, which will never become invalid, and use that instead.
        // This works, but is a work around, and the fact that we have to do this
        // may be an indication of a larger problem... see also related strange open bugs
        // which make it seem like at least some other bundles also get uninstalled
        // way too soon, for some reason:
        //  * https://bugs.opendaylight.org/show_bug.cgi?id=7924
        //  * https://bugs.opendaylight.org/show_bug.cgi?id=7923 (?)
        //  * https://bugs.opendaylight.org/show_bug.cgi?id=7926
        bundleContext = bundleContext.getBundle(0).getBundleContext();

        LOG.info("Attempting to install feature {} {}", getFeatureName(), getFeatureVersion());
        featuresService.installFeature(getFeatureName(), getFeatureVersion(),
                EnumSet.of(FeaturesService.Option.Verbose));
        LOG.info("installFeature() completed");
        Feature feature = featuresService.getFeature(getFeatureName(), getFeatureVersion());
        LOG.info("getFeature() completed");
        Assert.assertNotNull(
                "Attempt to get feature " + getFeatureName() + " " + getFeatureVersion() + "resulted in null",
                feature);
        boolean isInstalled = featuresService.isInstalled(feature);
        LOG.info("isInstalled() completed");
        Assert.assertTrue(
                "Failed to install Feature: " + getFeatureName() + " " + getFeatureVersion(), isInstalled);
        LOG.info("Successfully installed feature {} {}", getFeatureName(), getFeatureVersion());

        if (!Boolean.getBoolean(BUNDLES_DIAG_SKIP_PROP)
                && (Boolean.getBoolean(BUNDLES_DIAG_FORCE_PROP)
                    || !BLACKLISTED_BROKEN_FEATURES.contains(getFeatureName()))) {
            LOG.info("new TestBundleDiag().checkBundleDiagInfos() STARTING");
            Integer timeOutInSeconds = Integer.getInteger(BUNDLES_DIAG_TIMEOUT_PROP, 5 * 60);
            new TestBundleDiag(bundleContext, bundleService).checkBundleDiagInfos(timeOutInSeconds, SECONDS);
            LOG.info("new TestBundleDiag().checkBundleDiagInfos() ENDED");
        } else {
            LOG.warn("SKIPPING TestBundleDiag because system property {} is true or feature is blacklisted: {}",
                    BUNDLES_DIAG_SKIP_PROP, getFeatureName());
        }
    }

    // TODO remove this when all issues linked to parent https://bugs.opendaylight.org/show_bug.cgi?id=7582 are resolved
    private static final List<String> BLACKLISTED_BROKEN_FEATURES = new ArrayList<>(Arrays.asList(
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
    ));
}
