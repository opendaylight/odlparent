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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import org.apache.felix.utils.version.VersionCleaner;
import org.apache.karaf.features.internal.model.Dependency;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.jdt.annotation.Nullable;

@Mojo(name = "generate-feature", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
      requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public class GenerateFeatureMojo extends AbstractMojo {
    // Common groups
    private static final String LEAD = "lead";

    // Groups in <bundle> and <repository> lines
    private static final String GROUP_ID = "groupId";
    private static final String ARTIFACT_ID = "artifactId";
    private static final String TYPE = "type";
    private static final String CLASSIFIER = "classifier";

    // Version specification as they appear in raw features and bundles
    // FIXME: alias for new 'buildVersion'
    private static final String VERSION_AS_IN_PROJECT = "{{versionAsInProject}}";
    // FIXME: alias for new 'buildRange'
    private static final String SEM_VER_RANGE = "{{semVerRange}}";
    // FIXME: add 'majorRange' to derive '[2,3)' from '2.3.4'
    // FIXME: add 'minorRange' to derive '[2.3,3)' from '2.3.4'
    // FIXME: add 'buildMinorRange' to derive [2.3,2.4)' from '2.3.4'
    // FIXME: deprecate: we are building with maven substitution enabled, so ${project.version} will do the same
    private static final String PROJECT_VERSION = "{{projectVersion}}";
    // TODO: would something to derive '[2,)' from '2.3.4' be useful? it goes against semVer and what would we call it?

    // Version specifications as they appear in features after being scrubbed by Karaf marshaller
    private static final String VERSION_AS_IN_PROJECT_CLEAN = VersionCleaner.clean(VERSION_AS_IN_PROJECT);
    private static final String SEM_VER_RANGE_CLEAN = VersionCleaner.clean(SEM_VER_RANGE);
    private static final String PROJECT_VERSION_CLEAN = VersionCleaner.clean(PROJECT_VERSION);

    // mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}
    // mvn:org.opendaylight.odlparent/odl-guava/10.0.0-SNAPSHOT/xml/features
    private static final Pattern MVNURL_PATTERN = Pattern.compile("^(?<" + LEAD + ">(wrap:)?mvn:)"
        + "(?<" + GROUP_ID + ">[^/]+)/(?<" + ARTIFACT_ID + ">[^/]+)/\\{\\{versionAsInProject\\}\\}"
        + "(/(?<" + TYPE + ">[^/]+)(/(?<" + CLASSIFIER + ">[^/]+))?)?$");

    private static final String FEATURES_TYPE = "xml";
    private static final String FEATURES_CLASSIFIER = "features";

    @Parameter(defaultValue = "${project.basedir}/src/main/feature/template.xml")
    private File inputFile;

    @Parameter(defaultValue = "${project.build.directory}/feature/templated-feature.xml")
    private File outputFile;

    @Parameter(required = true, defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!"feature".equals(mavenProject.getPackaging())) {
            getLog().info("Project packaging is not 'feature', skipping execution");
            return;
        }

        // read, process and write the feature
        final Features features = readFeature(inputFile.toPath());
        processFeatures(features);
        writeFeature(features, outputFile.toPath());
    }

    // Visible for testing
    void processFeatures(final Features features) throws MojoFailureException {
        // Process feature repository references, dancing around encapsulation
        final var featRepos = features.getRepository();
        final var newRepos = new ArrayList<String>(featRepos.size());
        for (var repo : featRepos) {
            newRepos.add(processReference(repo));
        }
        featRepos.clear();
        featRepos.addAll(newRepos);

        // Process all features in-place
        for (var feature : features.getFeature()) {
            processFeature(feature);
        }
    }

    private void processFeature(final Feature feature) throws MojoFailureException {
        final var artifactFeature = feature.getName().equals(mavenProject.getArtifactId());

        // Update feature version if needed
        if (feature.hasVersion()) {
            feature.setVersion(processVersion(feature));
        } else if (artifactFeature) {
            feature.setVersion(mavenProject.getVersion());
        } else {
            throw new MojoFailureException("Feature \"" + feature.getName() + "\" does not define a version");
        }

        // Fill in other details if not provided
        if (artifactFeature) {
            if (feature.getDescription() == null) {
                feature.setDescription(mavenProject.getName());
            }
            if (feature.getDetails() == null) {
                feature.setDetails(mavenProject.getDescription());
            }
        }

        // Process feature dependencies, updating versions as needed
        for (var dependency : feature.getFeature()) {
            if (dependency.hasVersion()) {
                dependency.setVersion(processVersion(dependency));
            }
        }

        // Process feature bundles, updating versions as needed
        for (var bundle : feature.getBundle()) {
            bundle.setLocation(processReference(bundle.getLocation()));
        }
    }

    private String processReference(final String repository) throws MojoFailureException {
        final var matcher = MVNURL_PATTERN.matcher(repository);
        if (!matcher.matches()) {
            return repository;
        }

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

        return sb.toString();
    }

    private String processVersion(final Dependency dependency) throws MojoFailureException {
        final var version = dependency.getVersion();
        return switch (version) {
            case PROJECT_VERSION -> VersionCleaner.clean(mavenProject.getVersion());
            case SEM_VER_RANGE -> featureSemVerRange(dependency.getName());
            case VERSION_AS_IN_PROJECT -> VersionCleaner.clean(featureVersion(dependency.getName()));
            default -> version;
        };
    }

    private String processVersion(final Feature feature) throws MojoFailureException {
        // We really would want a switch expression, but alas that is not to be: the input is processed by unmarshaller
        // and scrubbed in ways that are not compile-time constants
        final String version = feature.getVersion();
        if (PROJECT_VERSION_CLEAN.equals(version)) {
            return mavenProject.getVersion();
        } else if (SEM_VER_RANGE_CLEAN.equals(version)) {
            return featureSemVerRange(feature.getName());
        } else if (VERSION_AS_IN_PROJECT_CLEAN.equals(version)) {
            return featureVersion(feature.getName());
        } else {
            return version;
        }
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

    private String featureSemVerRange(final String featureName) throws MojoFailureException {
        return semVerRange(featureVersion(featureName));
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

    private static Features readFeature(final Path path) throws MojoExecutionException {
        try (var is = Files.newInputStream(path)) {
            return readFeature(path.toUri().toString(), is);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to read input " + path, e);
        }
    }

    // Visible for testing
    static Features readFeature(final String uri, final InputStream input) throws IOException {
        return JaxbUtil.unmarshal(uri, input, true);
    }

    private static void writeFeature(final Features feature, final Path path) throws MojoExecutionException {
        final var parent = path.getParent();
        try {
            Files.createDirectories(parent);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create parent directory " + parent, e);
        }

        try (var os = Files.newOutputStream(path)) {
            writeFeature(feature, os);
        } catch (IOException | JAXBException e) {
            throw new MojoExecutionException("Failed to write output " + path, e);
        }
    }

    // Visible for testing
    static void writeFeature(final Features feature, final OutputStream output) throws JAXBException {
        JaxbUtil.marshal(feature, output);
    }
}
