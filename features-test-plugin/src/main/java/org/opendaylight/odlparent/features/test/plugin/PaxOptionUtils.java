/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.KARAF_VERSION;
import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.RELEASE_VERSION;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.aether.artifact.Artifact;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.extra.VMOption;

final class PaxOptionUtils {
    // pax-url-mvn configuration, see detailed explanation at
    // https://ops4j1.jira.com/wiki/spaces/paxurl/pages/115802124/Aether+Configuration
    private static final String ETC_ORG_OPS4J_PAX_URL_MVN_CFG = "etc/org.ops4j.pax.url.mvn.cfg";
    private static final String ETC_ORG_OPS4J_PAX_LOGGING_CFG = "etc/org.ops4j.pax.logging.cfg";

    private PaxOptionUtils() {
        // utility class
    }

    public static Option[] vmOptions(final String maxHeap, final String heapDumpPath) {
        return new Option[]{
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
            new VMOption("--add-exports=java.base/sun.net.www.protocol.file=ALL-UNNAMED"),
            new VMOption("--add-exports=java.base/sun.net.www.protocol.ftp=ALL-UNNAMED"),
            new VMOption("--add-exports=java.base/sun.net.www.protocol.http=ALL-UNNAMED"),
            new VMOption("--add-exports=java.base/sun.net.www.protocol.https=ALL-UNNAMED"),
            new VMOption("--add-exports=java.base/sun.net.www.protocol.jar=ALL-UNNAMED"),
            new VMOption("--add-exports=java.base/sun.net.www.content.text=ALL-UNNAMED"),
            new VMOption("--add-exports=jdk.naming.rmi/com.sun.jndi.url.rmi=ALL-UNNAMED"),
            new VMOption("--add-exports=java.rmi/sun.rmi.registry=ALL-UNNAMED"),
            new VMOption("-classpath"),
            new VMOption("lib/jdk9plus/*" + File.pathSeparator + "lib/boot/*"
                + File.pathSeparator + "lib/endorsed/*")
        };
    }

    static Option[] profileOptions(final boolean profile) throws MojoExecutionException {
        if (profile) {
            try {
                final var jfrFile = Files.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr");
                return new Option[]{
                    new VMOption("-XX:StartFlightRecording=disk=true,settings=profile,dumponexit=true,filename="
                        + jfrFile.toAbsolutePath()),
                    bootDelegationPackages("jdk.jfr", "jdk.jfr.consumer", "jdk.jfr.event", "jdk.jfr.event.handlers",
                        "jdk.jfr.internal.*"),
                };
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to create JFR file", e);
            }
        }
        return new Option[0];
    }

    static Option[] miscOptions() {
        return new Option[]{
            // probe dependencies
            mavenBundle("org.opendaylight.odlparent", "bundles-diag", RELEASE_VERSION),

            // Needed for Agrona/aeron.io
            systemPackages("com.sun.media.sound", "sun.net", "sun.nio.ch")
        };
    }

    static Option[] karafDistroOptions(final String url, final boolean keepUnpack, final String buildDir) {
        return new Option[]{
            karafDistributionConfiguration().frameworkUrl(url)
                .name("OpenDaylight")
                .unpackDirectory(Path.of(buildDir).resolve("pax").toFile())
                .useDeployFolder(false),
            when(keepUnpack).useOptions(keepRuntimeFolder()),
//            configureSecurity().disableKarafMBeanServerBuilder(),
            configureConsole().ignoreLocalConsole().ignoreRemoteShell(),
        };
    }

    static Option[] karafConfigOptions(final String buildDir, final String localRepository)
            throws MojoExecutionException {
        final var karafLogPath = Path.of(buildDir, "SFT", "karaf.log");
        try {
            Files.createDirectories(karafLogPath.getParent());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create log directory", e);
        }
        return new Option[]{
            logLevel(LogLevelOption.LogLevel.INFO),

            // local repository
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, "org.ops4j.pax.url.mvn.localRepository",
                localRepository),
            // Make sure karaf's default repository is consulted before anything else, followed by the local repository
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, "org.ops4j.pax.url.mvn.defaultRepositories", """
                file:${karaf.home}/${karaf.default.repository}@id=system.repository,\
                file:%s@id=maven.local.repository""".formatted(localRepository)),
            // remote repository, exclude snapshots
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_URL_MVN_CFG, "org.ops4j.pax.url.mvn.repositories",
                "https://repo1.maven.org/maven2@id=central"),

            // redirect karaf log output
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_LOGGING_CFG, "log4j2.appender.rolling.fileName",
                karafLogPath.toString()),
            editConfigurationFilePut(ETC_ORG_OPS4J_PAX_LOGGING_CFG, "log4j2.appender.rolling.filePattern",
                karafLogPath + ".%i")
        };
    }

    static Option[] dependencyFeaturesOptions(final Collection<FeatureDependency> featureDependencies) {
        return featureDependencies == null ? new Option[0] :
            featureDependencies.stream()
                .map(fd -> features(urlReferenceOf(fd.artifact()), fd.featureNames().toArray(String[]::new)))
                .toArray(Option[]::new);
    }

    private static MavenArtifactUrlReference urlReferenceOf(final Artifact artifact) {
        return maven().groupId(artifact.getGroupId()).artifactId(artifact.getArtifactId())
            .type(artifact.getExtension()).classifier(artifact.getClassifier())
            .version(artifact.getVersion());
    }

    static Option[] concat(final Option[]... options) {
        return Arrays.stream(options).flatMap(Arrays::stream).toArray(Option[]::new);
    }
}