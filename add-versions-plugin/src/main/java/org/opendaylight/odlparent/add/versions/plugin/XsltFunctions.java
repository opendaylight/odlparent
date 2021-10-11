/*
 * Copyright (c) 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.add.versions.plugin;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

public final class XsltFunctions {

    private static final String BUNDLE_VERSION_PLACEHOLDER = "{{versionAsInProject}}";

    private XsltFunctions() {
    }

    public static String addBundleVersion(MavenProject mavenProject, String mvnUrl) {
        final String[] coordinates = mvnUrl.replaceFirst("\\b.*?mvn:", "").split("/");
        // 0 - groupID, 1 - artifactID, 2 - version, 3 - type, 4 - Classifier
        final String groupId = coordinates[0];
        final String artifactId = coordinates[1];

        for (final Object object : mavenProject.getDependencies()) {
            final Dependency dependency = (Dependency) object;
            if (dependency.getArtifactId().equals(artifactId) && dependency.getGroupId().equals(groupId)) {
                return mvnUrl.replace(BUNDLE_VERSION_PLACEHOLDER, dependency.getVersion());
            }
        }

        throw new IllegalArgumentException("Bundle dependency " + groupId + ":" + artifactId
                + " is not a dependency in the project pom.xml.");
    }

    public static String addFeatureVersionRange(MavenProject mavenProject, String featureName) {
        for (final Object object : mavenProject.getDependencies()) {
            final Dependency dependency = (Dependency) object;
            if (dependency.getArtifactId().equals(featureName)) {
                return createVersionRange(dependency.getVersion());
            }
        }

        throw new IllegalArgumentException("Feature dependency " + featureName
                + " is not a dependency in the project pom.xml.");
    }

    private static String createVersionRange(String version) {
        final ArtifactVersion semVer = new DefaultArtifactVersion(version);
        if (semVer.getMajorVersion() == 0) {
            return String.format("[%s.%s,%s)",
                    semVer.getMajorVersion(), semVer.getMinorVersion(), semVer.getMajorVersion() + 1);
        }
        return String.format("[%s,%s)", semVer.getMajorVersion(), semVer.getMajorVersion() + 1);
    }
}
