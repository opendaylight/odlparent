/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static java.util.Objects.requireNonNull;

import java.util.Properties;
import org.eclipse.jdt.annotation.NonNull;

final class MavenDependency {
    private final @NonNull String groupId;
    private final @NonNull String artifactId;
    private final @NonNull String version;

    private final String type;
    private final String scope;
    private final String classifier;

    private MavenDependency(final String groupId, final String artifactId, final String version, final String scope,
            final String type, final String classifier) {
        this.groupId = requireNonNull(groupId);
        this.artifactId = requireNonNull(artifactId);
        this.version = requireNonNull(version);
        this.scope = scope;
        this.type = type;
        this.classifier = classifier;
    }

    MavenDependency(final String groupId, final String artifactId, final String version) {
        this(groupId, artifactId, version, null, null, null);
    }

    @NonNull String groupId() {
        return groupId;
    }

    @NonNull String artifactId() {
        return artifactId;
    }

    @NonNull String version() {
        return version;
    }

    String type() {
        return type;
    }

    String scope() {
        return scope;
    }

    String classifier() {
        return classifier;
    }

    static MavenDependency create(final Properties dependencies, final String key) {
        // We need at least two slashes
        final int firstSlash = key.indexOf('/');
        if (firstSlash == -1) {
            throw new IllegalArgumentException("Invalid key " + key);
        }
        final int secondSlash = key.indexOf('/', firstSlash + 1);
        if (firstSlash == -1) {
            throw new IllegalArgumentException("Invalid key " + key);
        }

        final String prefix = key.substring(0, secondSlash + 1);
        return new MavenDependency(key.substring(0, firstSlash), key.substring(firstSlash + 1, secondSlash),
            dependencies.getProperty(prefix + "version"), dependencies.getProperty(prefix + "scope"),
            dependencies.getProperty(prefix + "type"), dependencies.getProperty(prefix + "classifier"));
    }
}
