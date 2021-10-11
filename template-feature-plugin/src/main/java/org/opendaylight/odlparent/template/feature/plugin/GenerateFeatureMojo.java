/*
 * Copyright (c) 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.template.feature.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate-feature", defaultPhase = LifecyclePhase.COMPILE,
      requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public class GenerateFeatureMojo extends AbstractMojo {
    // Common groups
    private static final String LEAD = "lead";
    private static final String TRAIL = "trail";
    private static final String VERSION = "version";

    // Groups in <bundle> line
    private static final String GROUP_ID = "groupId";
    private static final String ARTIFACT_ID = "artifactId";

    // Groups in <feature> line
    private static final String MID = "mid";
    private static final String NAME = "name";

    // Version specification
    private static final String VERSION_AS_IN_PROJECT = "{{versionAsInProject}}";
    private static final String SEM_VER_RANGE = "{{semVerRange}}";

    // <bundle>mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}</bundle>
    private static final Pattern BUNDLE_PATTERN = Pattern.compile("^(?<" + LEAD + ">.*<bundle>(wrap:)?mvn:)"
        + "(?<" + GROUP_ID + ">[^/]+)/(?<" + ARTIFACT_ID + ">[^/]+)/\\{\\{versionAsInProject\\}\\}"
        + "(?<" + TRAIL + "></bundle>.*)$");
    // <feature version="{{semVerRange}}">odl-apache-commons-net</feature>
    private static final Pattern FEATURE_PATTERN = Pattern.compile("^(?<" + LEAD + ">.*<feature (.* )?version=\")"
        + "(?<" + VERSION + ">(\\{\\{versionAsInProject\\}\\}" + ")|(\\{\\{semVerRange\\}\\}))"
        + "(?<" + MID + ">[^>]*>)(?<" + NAME + ">[^<]+)(?<" + TRAIL + "></feature>.*)$");

    private static final String FEATURES_TYPE = "xml";
    private static final String FEATURES_CLASSIFIER = "features";

    @Parameter(defaultValue = "${project.basedir}/src/main/feature/feature.xml")
    private File inputFile;

    @Parameter(defaultValue = "${project.build.directory}/feature/feature.xml")
    private File outputFile;

    @Parameter(required = true, defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Component
    private ArtifactFactory artifactFactory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if ("feature".equals(mavenProject.getPackaging())) {
            getLog().info("Project packaging is not 'feature', skipping execution");
            return;
        }

        final List<String> inputLines;
        try {
            inputLines = Files.readAllLines(inputFile.toPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read input " + inputFile, e);
        }

        //        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        //        <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
        // FIXME: this part does not work
        //            <repository>mvn:org.opendaylight.odlparent/odl-guava/10.0.0-SNAPSHOT/xml/features</repository>
        //            <repository>mvn:tech.pantheon.triemap/pt-triemap/1.2.0/xml/features</repository>
        // FIXME: 8.0.0.SNAPSHOT derived from {{projectVersion}}
        //            <feature name="odl-yangtools-util" description="Yangtools :: Utilities" version="8.0.0.SNAPSHOT">
        //                <details>YANG Tools common concepts and utilities</details>
        //                <feature version="[10,11)">odl-guava</feature>
        //                <feature version="[1,2)">pt-triemap</feature>
        //                <bundle>mvn:org.opendaylight.yangtools/concepts/8.0.0-SNAPSHOT</bundle>
        //                <bundle>mvn:org.opendaylight.yangtools/util/8.0.0-SNAPSHOT</bundle>
        //            </feature>
        //        </features>

        final var outputLines = new ArrayList<String>(inputLines.size());
        for (var line : inputLines) {
            outputLines.add(process(line));
        }

        try {
            Files.createDirectories(Path.of(outputFile.getParent()));
            Files.write(outputFile.toPath(), outputLines);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write output " + outputFile, e);
        }

        final var artifact = artifactFactory.createArtifactWithClassifier(mavenProject.getGroupId(),
            mavenProject.getArtifactId(), mavenProject.getVersion(), FEATURES_TYPE, FEATURES_CLASSIFIER);
        artifact.setFile(outputFile);
        mavenProject.setArtifact(artifact);
    }

    // Visible for testing
    String process(final String input) throws MojoFailureException {
        final var bundle = BUNDLE_PATTERN.matcher(input);
        if (bundle.matches()) {
            final var groupId = bundle.group(GROUP_ID);
            final var artifactId = bundle.group(ARTIFACT_ID);
            final var version = dependencyVersion(groupId, artifactId);

            return bundle.group(LEAD) + groupId + "/" + artifactId + "/" + version + bundle.group(TRAIL);
        }

        final var feature = FEATURE_PATTERN.matcher(input);
        if (feature.matches()) {
            final var name = feature.group(NAME);
            final var version = outputVersion(feature, featureVersion(name));

            return feature.group(LEAD) + version + feature.group(MID) + feature.group(NAME) + feature.group(TRAIL);
        }

        return input;
    }

    private String dependencyVersion(final String groupId, final String artifactId) throws MojoFailureException {
        for (var dependency : mavenProject.getDependencies()) {
            if (artifactId.equals(dependency.getArtifactId()) && groupId.equals(dependency.getGroupId())) {
                return dependency.getVersion();
            }
        }

        throw new MojoFailureException("Dependency \"" + groupId + ":" + artifactId + "\" not found");
    }

    private String featureVersion(final String featureName) throws MojoFailureException {
        for (var dependency : mavenProject.getDependencies()) {
            if (featureName.equals(dependency.getArtifactId()) && FEATURES_CLASSIFIER.equals(dependency.getClassifier())
                && FEATURES_TYPE.equals(dependency.getType())) {
                return dependency.getVersion();
            }
        }

        throw new MojoFailureException("Dependency matching feature \"" + featureName + "\" not found");
    }

    private static String outputVersion(final Matcher matcher, final String depVersion) {
        final var versionGroup = matcher.group(VERSION);
        switch (versionGroup) {
            case VERSION_AS_IN_PROJECT:
                return depVersion;
            case SEM_VER_RANGE:
                return semVerRange(depVersion);
            default:
                throw new IllegalStateException("Unexpected version " + versionGroup);
        }
    }

    // Visible for testing
    static String semVerRange(final String version) {
        final var semVer = new DefaultArtifactVersion(version);
        final var major = semVer.getMajorVersion();
        final var minor = semVer.getMinorVersion();
        final var patch = semVer.getIncrementalVersion();

        final var sb = new StringBuilder() .append('[').append(major);
        if (minor != 0 || patch != 0) {
            sb.append('.').append(minor);
            if (patch != 0) {
                sb.append('.').append(patch);
            }
        }
        return sb.append(',').append(major + 1).append(')').toString();
    }
}
