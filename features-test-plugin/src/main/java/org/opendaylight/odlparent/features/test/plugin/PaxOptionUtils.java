/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.apache.maven.plugin.MojoExecutionException;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.extra.VMOption;

final class PaxOptionUtils {

    private static final String KARAF_DIST_VERSION = fromResource("/distro.version");
    private static final String RELEASE_VERSION = fromResource("/release.version");

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
            new VMOption("java.base=lib/endorsed/org.apache.karaf.specs.locator-" + KARAF_DIST_VERSION + ".jar"),
            new VMOption("--patch-module"),
            new VMOption("java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-" + KARAF_DIST_VERSION + ".jar"),
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

    static Option[] profileVmOptions(final boolean profile) throws MojoExecutionException {
        if (profile) {
            try {
                final var jfrFile = Files.createTempFile("SingleFeatureTest-Karaf-JavaFlightRecorder", ".jfr");
                return new Option[]{
                    new VMOption("-XX:StartFlightRecording=disk=true,settings=profile,dumponexit=true,filename="
                        + jfrFile.toAbsolutePath())};
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to create JFR file", e);
            }
        }
        return new Option[0];
    }

    static Option[] karafDistroOptions(final String distGroupId, final String distArtifactId, final String distType,
        final boolean keepUnpack, final String buildDir) {
        return new Option[]{
            karafDistributionConfiguration().frameworkUrl(
                    maven().groupId(distGroupId).artifactId(distArtifactId).type(distType).version(KARAF_DIST_VERSION))
                .name("OpenDaylight")
                .unpackDirectory(new File(buildDir, "pax"))
                .useDeployFolder(false),
            when(keepUnpack).useOptions(keepRuntimeFolder()),
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
            // Make sure karaf's default repository is consulted before anything else
            editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.defaultRepositories",
                "file:${karaf.home}/${karaf.default.repository}@id=system.repository"),
            // local repository
            editConfigurationFilePut("etc/org.ops4j.pax.url.mvn.cfg", "org.ops4j.pax.url.mvn.localRepository",
                localRepository),
            // redirect karaf log output
            editConfigurationFilePut("etc/org.ops4j.pax.logging.cfg", "log4j2.appender.rolling.fileName",
                karafLogPath.toString()),
            editConfigurationFilePut("etc/org.ops4j.pax.logging.cfg", "log4j2.appender.rolling.filePattern",
                karafLogPath + ".%i"),
        };
    }

    static Option[] concat(final Option[]... options) {
        return Arrays.stream(options).flatMap(Arrays::stream).toArray(Option[]::new);
    }

    private static String fromResource(final String path) {
        try (var is = PaxOptionUtils.class.getResourceAsStream(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
