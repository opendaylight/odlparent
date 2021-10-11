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
import org.eclipse.jdt.annotation.Nullable;

@Mojo(name = "generate-feature", defaultPhase = LifecyclePhase.COMPILE,
      requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public class GenerateFeatureMojo extends AbstractMojo {
    // Common groups
    private static final String LEAD = "lead";
    private static final String TRAIL = "trail";
    private static final String VERSION = "version";

    // Groups in <bundle> and <repository> lines
    private static final String GROUP_ID = "groupId";
    private static final String ARTIFACT_ID = "artifactId";
    private static final String TYPE = "type";
    private static final String CLASSIFIER = "classifier";

    // Groups in <feature> line
    private static final String MID = "mid";
    private static final String NAME = "name";

    // Version specification
    private static final String VERSION_AS_IN_PROJECT = "{{versionAsInProject}}";
    private static final String SEM_VER_RANGE = "{{semVerRange}}";
    private static final String PROJECT_VERSION = "{{projectVersion}}";

    private static final String MVNURL_COMPONENTS =
        "(?<" + GROUP_ID + ">[^/]+)/(?<" + ARTIFACT_ID + ">[^/]+)/\\{\\{versionAsInProject\\}\\}"
        + "(/(?<" + TYPE + ">[^/]+)(/(?<" + CLASSIFIER + ">[^/]+))?)?";

    // <bundle>mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}</bundle>
    private static final Pattern BUNDLE_PATTERN = Pattern.compile("^(?<" + LEAD + ">.*<bundle>(wrap:)?mvn:)"
        + MVNURL_COMPONENTS + "(?<" + TRAIL + "></bundle>.*)$");
    // <feature version="{{semVerRange}}">odl-apache-commons-net</feature>
    private static final Pattern FEATURE_PATTERN = Pattern.compile("^(?<" + LEAD + ">.*<feature (.* )?version=\")"
        + "(?<" + VERSION + ">(\\{\\{versionAsInProject\\}\\}" + ")"
            + "|(\\{\\{semVerRange\\}\\})"
            + "|(\\{\\{projectVersion\\}\\}))"
        + "(?<" + MID + ">[^>]*>)(?<" + NAME + ">[^<]+)(?<" + TRAIL + "></feature>.*)$");
    // <repository>mvn:org.opendaylight.odlparent/odl-guava/10.0.0-SNAPSHOT/xml/features</repository>
    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("^(?<" + LEAD + ">.*<repository>(wrap:)?mvn:)"
        + MVNURL_COMPONENTS + "(?<" + TRAIL + "></repository>.*)$");

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

        final var outputLines = processLines(inputLines);

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
    List<String> processLines(final List<String> inputLines) throws MojoFailureException {
        final var ret = new ArrayList<String>(inputLines.size());
        for (var line : inputLines) {
            ret.add(process(line));
        }
        return ret;
    }

    // Visible for testing
    String process(final String input) throws MojoFailureException {
        final var bundle = BUNDLE_PATTERN.matcher(input);
        if (bundle.matches()) {
            return processMavenReference(bundle);
        }

        final var feature = FEATURE_PATTERN.matcher(input);
        if (feature.matches()) {
            final var name = feature.group(NAME);
            final var version = outputVersion(feature, featureVersion(name));

            return feature.group(LEAD) + version + feature.group(MID) + feature.group(NAME) + feature.group(TRAIL);
        }

        final var repository = REPOSITORY_PATTERN.matcher(input);
        if (repository.matches()) {
            return processMavenReference(repository);
        }

        return input;
    }

    private String processMavenReference(final Matcher matcher) throws MojoFailureException {
        final var groupId = matcher.group(GROUP_ID);
        final var artifactId = matcher.group(ARTIFACT_ID);
        final var type = matcher.group(TYPE);
        final var classifier = matcher.group(CLASSIFIER);
        final var version = dependencyVersion(groupId, artifactId, type, classifier);

        final var sb = new StringBuilder()
            .append(matcher.group(LEAD)).append(groupId).append('/').append(artifactId).append('/').append(version);
        if (type != null) {
            sb.append('/').append(type);
            if (classifier != null) {
                sb.append('/').append(classifier);
            }
        }

        return sb.append(matcher.group(TRAIL)).toString();
    }

    private String dependencyVersion(final String groupId, final String artifactId, final @Nullable String type,
            final @Nullable String classifier) throws MojoFailureException {
        for (var dep : mavenProject.getDependencies()) {
            if (artifactId.equals(dep.getArtifactId()) && groupId.equals(dep.getGroupId())
                && (type == null || type.equals(dep.getType()))
                && (classifier == null || classifier.equals(dep.getClassifier()))) {
                return dep.getVersion();
            }
        }

        throw new MojoFailureException("Dependency \"" + groupId + ":" + artifactId + "\" not found");
    }

    private String featureVersion(final String featureName) throws MojoFailureException {
        // This feature's version
        if (featureName.equals(mavenProject.getArtifactId())) {
            return mavenProject.getVersion();
        }

        for (var dependency : mavenProject.getDependencies()) {
            if (featureName.equals(dependency.getArtifactId()) && FEATURES_CLASSIFIER.equals(dependency.getClassifier())
                && FEATURES_TYPE.equals(dependency.getType())) {
                return dependency.getVersion();
            }
        }

        throw new MojoFailureException("Dependency matching feature \"" + featureName + "\" not found");
    }

    private String outputVersion(final Matcher matcher, final String depVersion) {
        final var versionGroup = matcher.group(VERSION);
        switch (versionGroup) {
            case PROJECT_VERSION:
                return osgiVersion(mavenProject.getVersion());
            case SEM_VER_RANGE:
                return semVerRange(depVersion);
            case VERSION_AS_IN_PROJECT:
                return osgiVersion(depVersion);
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

    private static String osgiVersion(final String version) {
        // Sufficient for now
        return version.replace('-', '.');
    }
}
