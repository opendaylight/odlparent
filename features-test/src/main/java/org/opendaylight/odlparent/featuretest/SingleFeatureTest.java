/*
 * Copyright © 2014, 2017 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP;
import static org.opendaylight.odlparent.featuretest.Constants.ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.propagateSystemProperty;
import static org.ops4j.pax.exam.CoreOptions.systemPackages;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import javax.inject.Inject;
import org.apache.karaf.bundle.core.BundleService;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.features.Repository;
import org.awaitility.Awaitility;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.odlparent.bundlestest.lib.TestBundleDiag;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
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

    private static final String ETC_ORG_OPS4J_PAX_URL_MVN_CFG = "etc/org.ops4j.pax.url.mvn.cfg";
    private static final String ETC_ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";

    private static final String KEEP_UNPACK_DIRECTORY_PROP = "karaf.keep.unpack";
    private static final String PROFILE_PROP = "karaf.featureTest.profile";
    private static final String BUNDLES_DIAG_SKIP_PROP = "sft.diag.skip";
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
    private static final String KARAF_DISTRO_TYPE_PROP = "karaf.distro.type";
    private static final String KARAF_DISTRO_ARTIFACTID_PROP = "karaf.distro.artifactId";
    private static final String KARAF_DISTRO_GROUPID_PROP = "karaf.distro.groupId";

    /**
     * <p>List of Karaf 4.2.6 default maven repositories with snapshot repositories excluded.</p>
     * <p>Unfortunately this must be hard-coded since declarative model which uses Options,
     * does not allow us to read value, parse it (properties has always
     * problems with lists) and construct replacement string which does
     * not contains snapshots.</p>
     * <p>When updating Karaf, check this against org.ops4j.pax.url.mvn.cfg in the Karaf distribution.</p>
     */
    private static final String EXTERNAL_DEFAULT_REPOSITORIES = "https://repo1.maven.org/maven2@id=central ";

    @Inject @NonNull
    private BundleContext bundleContext;

    @Inject @NonNull
    private BundleService bundleService; // NOT BundleStateService, see checkBundleStatesDiag()

    @Inject @NonNull
    private FeaturesService featuresService;

    private String karafReleaseVersion;
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
            when("Linux".equals(System.getProperty("os.name"))).useOptions(
                // This prevents low entropy issues on Linux to affect Java random numbers
                // which can block crypto such as the SSH server in netconf
                // see https://jira.opendaylight.org/browse/ODLPARENT-49
                new VMOption("-Djava.security.egd=file:/dev/./urandom")
            ),
            when(Boolean.getBoolean(PROFILE_PROP)).useOptions(
                new VMOption("-XX:StartFlightRecording=disk=true,settings=profile,dumponexit=true,filename="
                               + getNewJFRFile())),
            getKarafDistroOption(),
            when(Boolean.getBoolean(KEEP_UNPACK_DIRECTORY_PROP)).useOptions(keepRuntimeFolder()),
            configureConsole().ignoreLocalConsole().ignoreRemoteShell(),
            logLevel(LogLevel.INFO),
            mvnLocalRepoOption(),

            // Make sure karaf's default repository is consulted before anything else
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, "org.ops4j.pax.url.mvn.defaultRepositories",
                "file:${karaf.home}/${karaf.default.repository}@id=system.repository"),

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
            propagateSystemProperty(BUNDLES_DIAG_TIMEOUT_PROP),
            // Needed for Agrona/aeron.io
            systemPackages("com.sun.media.sound", "sun.net", "sun.nio.ch"),
            // Needed to run akka with a java flight recorder enabled
            bootDelegationPackages("jdk.jfr", "jdk.jfr.consumer", "jdk.jfr.event", "jdk.jfr.event.handlers",
                "jdk.jfr.internal.*"),

            // Install SCR
            features(maven().groupId("org.apache.karaf.features").artifactId("standard").type("xml")
                .classifier("features").versionAsInProject(), "scr"),

            // Enable JaCoCo, if present
            jacocoOption(),
        };

        if (JavaVersionUtil.getMajorVersion() <= 8) {
            return baseConfig;
        }

        final String version = getKarafReleaseVersion();
        return OptionUtils.combine(baseConfig,
            new VMOption("--add-reads=java.xml=java.logging"),
            new VMOption("--add-exports=java.base/org.apache.karaf.specs.locator=java.xml,ALL-UNNAMED"),
            new VMOption("--patch-module"),
            new VMOption("java.base=lib/endorsed/org.apache.karaf.specs.locator-" + version + ".jar"),
            new VMOption("--patch-module"),
            new VMOption("java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-" + version + ".jar"),
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
            new VMOption("lib/jdk9plus/*" + File.pathSeparator + "lib/boot/*"));
    }

    private static Option jacocoOption() {
        final String sftArgLine = System.getProperty("sftArgLine");
        return sftArgLine == null || sftArgLine.isBlank() ? null : new VMOption(sftArgLine);
    }

    private static String getNewJFRFile() throws IOException {
        return File.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr").getAbsolutePath();
    }

    private String getKarafReleaseVersion() throws IOException {
        if (karafReleaseVersion == null) {
            karafReleaseVersion = KarafConstants.karafReleaseVersion();
        }
        return karafReleaseVersion;
    }

    private String getKarafDistroVersion() {
        if (karafDistroVersion == null) {
            karafDistroVersion = KarafConstants.karafDistroVersion();
        }
        return karafDistroVersion;
    }

    /**
     * Disables snapshot repositories, which are enabled by default in karaf distribution.
     *
     * @return Edit Configuration option which removes external snapshot repositories.
     */
    private static Option disableExternalSnapshotRepositories() {
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG,
            "org.ops4j.pax.url.mvn.repositories", EXTERNAL_DEFAULT_REPOSITORIES);
    }

    protected Option mvnLocalRepoOption() {
        String mvnRepoLocal = System.getProperty(MAVEN_REPO_LOCAL, "");
        LOG.info("mvnLocalRepo \"{}\"", mvnRepoLocal);
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG,
            "org.ops4j.pax.url.mvn.localRepository", mvnRepoLocal);
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

    private static String getProperty(final String propName) {
        String prop = System.getProperty(propName);
        assertNotNull("Missing property :" + propName, prop);
        return prop;
    }

    private void checkRepository(final URI repoUri) throws Exception {
        for (Repository r : featuresService.listRepositories()) {
            if (r.getURI().equals(repoUri)) {
                return;
            }
        }
        fail("Repository not found: " + repoUri);
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
        // https://jira.opendaylight.org/browse/ODLPARENT-78:
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
        //  * https://jira.opendaylight.org/browse/CONTROLLER-1614
        //  * https://jira.opendaylight.org/browse/ODLPARENT-76 (?)
        //  * https://jira.opendaylight.org/browse/ODLPARENT-77
        bundleContext = bundleContext.getBundle(0).getBundleContext();

        // Acquire feature details from properties
        final String featureName = getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP);
        final String featureVersion = getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP);

        LOG.info("Attempting to install feature {} {}", featureName, featureVersion);
        featuresService.installFeature(featureName, featureVersion, EnumSet.of(FeaturesService.Option.Verbose));
        LOG.info("installFeature() completed");

        Feature feature = featuresService.getFeature(featureName, featureVersion);
        LOG.info("getFeature() completed");
        assertNotNull("Attempt to get feature " + featureName + " " + featureVersion + "resulted in null", feature);
        boolean isInstalled = featuresService.isInstalled(feature);
        LOG.info("isInstalled() completed");
        assertTrue("Failed to install Feature: " + featureName + " " + featureVersion, isInstalled);
        LOG.info("Successfully installed feature {} {}", featureName, featureVersion);

        if (!Boolean.getBoolean(BUNDLES_DIAG_SKIP_PROP)) {
            LOG.info("new TestBundleDiag().checkBundleDiagInfos() STARTING");
            Integer timeOutInSeconds = Integer.getInteger(BUNDLES_DIAG_TIMEOUT_PROP, 5 * 60);
            new TestBundleDiag(bundleContext, bundleService).checkBundleDiagInfos(timeOutInSeconds, SECONDS);
            LOG.info("new TestBundleDiag().checkBundleDiagInfos() ENDED");
        } else {
            LOG.warn("SKIPPING TestBundleDiag because system property {} is true: {}", BUNDLES_DIAG_SKIP_PROP,
                featureName);
        }
    }
}
