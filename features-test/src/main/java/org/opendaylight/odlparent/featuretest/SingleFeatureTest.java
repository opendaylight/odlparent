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
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
//import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.apache.karaf.features.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(PerRepoTestRunner.class)
public class SingleFeatureTest {
    private static final String MAVEN_REPO_LOCAL = "maven.repo.local";
    private static final String ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY = "org.ops4j.pax.url.mvn.localRepository";
    private static final String ORG_OPS4J_PAX_URL_MVN_REPOSITORIES = "org.ops4j.pax.url.mvn.repositories";
    private static final String ETC_ORG_OPS4J_PAX_URL_MVN_CFG = "etc/org.ops4j.pax.url.mvn.cfg";
    private static final String LOG4J_LOGGER_ORG_OPENDAYLIGHT_YANGTOOLS_FEATURETEST = "log4j.logger.org.opendaylight.odlparent.featuretest";
    private static final Logger LOG = LoggerFactory.getLogger(SingleFeatureTest.class);

    /*
     * File name to add our logging config property too.
     */
    private static final String ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";

    /*
     * Default values for karaf distro version, type, groupId, and artifactId
     */
    private static final String KARAF_DISTRO_VERSION = "3.0.2";
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
     * List of karaf 3.0.2 default maven repositories with snapshot repositories excluded.
     *
     * Unfortunately this must be hard-coded since declarative model which uses Options,
     * does not allow us to read value, parse it (properties has allways
     * problems with lists) and construct replacement string which does
     * not contains snapshots.
     *
     */
    private static final String EXTERNAL_DEFAULT_REPOSITORIES = "http://repo1.maven.org/maven2@id=central, " +
    "http://repository.springsource.com/maven/bundles/release@id=spring.ebr.release, " +
    "http://repository.springsource.com/maven/bundles/external@id=spring.ebr.external, " +
    "http://zodiac.springsource.com/maven/bundles/release@id=gemini ";


   @Inject
   private FeaturesService featuresService;

    @Configuration
    public Option[] config() throws IOException {
       return new Option[] {
             getKarafDistroOption(),
             keepRuntimeFolder(),
             configureConsole().ignoreLocalConsole(),
             logLevel(LogLevel.WARN),
             mvnLocalRepoOption(),
             editConfigurationFilePut(ORG_OPS4J_PAX_LOGGING_CFG,LOG4J_LOGGER_ORG_OPENDAYLIGHT_YANGTOOLS_FEATURETEST,LogLevel.INFO.name()),
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
             CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP).value(System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP)),
             CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP).value(System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP)),
             CoreOptions.systemProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP).value(System.getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP)),
       };
    }

    /**
     *
     * Disables snapshot repositories, which are enabled by default in karaf distribution.
     *
     *
     *
     * @return Edit Configuration option which removes external snapshot repositories.
     */
    private static Option disableExternalSnapshotRepositories() {
        return editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, ORG_OPS4J_PAX_URL_MVN_REPOSITORIES,EXTERNAL_DEFAULT_REPOSITORIES);
    }

    protected Option mvnLocalRepoOption() {
        String mvnRepoLocal = System.getProperty(MAVEN_REPO_LOCAL, "");
        LOG.info("mvnLocalRepo \"{}\"",mvnRepoLocal);
        Option option =
                editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG,ORG_OPS4J_PAX_URL_MVN_LOCAL_REPOSITORY,mvnRepoLocal);
        return option;
    }

    protected Option getKarafDistroOption() {
        String groupId = System.getProperty(KARAF_DISTRO_GROUPID_PROP,KARAF_DISTRO_GROUPID);
        String artifactId = System.getProperty(KARAF_DISTRO_ARTIFACTID_PROP,KARAF_DISTRO_ARTIFACTID);
        String version = System.getProperty(KARAF_DISTRO_VERSION_PROP,KARAF_DISTRO_VERSION);
        String type = System.getProperty(KARAF_DISTRO_TYPE_PROP,KARAF_DISTRO_TYPE);
        LOG.info("Using karaf distro {} {} {} {}",groupId,artifactId,version,type);
        return karafDistributionConfiguration()
                .frameworkUrl(
                        maven()
                                .groupId(groupId)
                                .artifactId(artifactId)
                                .type(type)
                                .version(version))
               .name("OpenDaylight")
               .unpackDirectory(new File("target/pax"))
               .useDeployFolder(false);
    }

    private URI getRepoURI() throws URISyntaxException {
        return new URI(getProperty(ORG_OPENDAYLIGHT_FEATURETEST_URI_PROP));
    }

    private String getFeatureName() {
        return getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATURENAME_PROP);
    }

    public String getFeatureVersion() {
        return getProperty(ORG_OPENDAYLIGHT_FEATURETEST_FEATUREVERSION_PROP);
    }

    private String getProperty(final String propName) {
        String prop = System.getProperty(propName);
        Assert.assertTrue("Missing property :" +propName, prop!=null);
        return prop;
    }

    private void checkRepository(final URI repoURI) {
        Repository repo = null;
        for(Repository r: featuresService.listRepositories()) {
            if(r.getURI().equals(repoURI)){
                repo = r;
                break;
            }
        }
        Assert.assertNotNull("Repository not found: " + repoURI,repo);
    }

    @Before
    public void installRepo() throws Exception {
        LOG.info("Attempting to add repository {}", getRepoURI());
        featuresService.addRepository(getRepoURI());
        checkRepository(getRepoURI());
        LOG.info("Successfully loaded repository {}", getRepoURI());
    }

    @Test
    public void installFeature() throws Exception {
      //LOG.info("Attempting to install feature {} {}", getFeatureName(),getFeatureVersion());
      //featuresService.installFeature(getFeatureName(), getFeatureVersion());
      //Feature f = featuresService.getFeature(getFeatureName(), getFeatureVersion());
      //Assert.assertNotNull("Attempt to get feature "+ getFeatureName() + " " + getFeatureVersion() + "resulted in null" , f);
      //Assert.assertTrue("Failed to install Feature: " + getFeatureName() + " " + getFeatureVersion(),featuresService.isInstalled(f));
      //LOG.info("Successfull installed feature {} {}", getFeatureName(),getFeatureVersion());
    }
}
