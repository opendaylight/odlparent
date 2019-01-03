/*
 * Copyright © 2019 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import java.util.Objects;
import org.eclipse.aether.artifact.Artifact;

/**
 * Group, artifact, classifier, extension.
 */
public final class Gace {
    private final String groupId;
    private final String artifactId;
    private final String classifier;
    private final String extension;

    public Gace(Artifact artifact) {
        groupId = artifact.getGroupId();
        artifactId = artifact.getArtifactId();
        classifier = artifact.getClassifier();
        extension = artifact.getExtension();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Gace gace = (Gace) obj;
        return Objects.equals(groupId, gace.groupId)
            && Objects.equals(artifactId, gace.artifactId)
            && Objects.equals(classifier, gace.classifier)
            && Objects.equals(extension, gace.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, classifier, extension);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + classifier + ":" + extension;
    }
}
