/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.artifact.DefaultArtifactType;
import org.eclipse.aether.graph.Dependency;

public class MvnToAetherMapper {

    public static Dependency toAether(org.apache.maven.model.Dependency dependency) {
        DefaultArtifact artifact = new DefaultArtifact(dependency.getGroupId(),
                dependency.getArtifactId(),
                dependency.getClassifier(),
                null,
                dependency.getVersion(),
                new DefaultArtifactType(dependency.getType()));
        Dependency result = new Dependency(artifact, null);
        return result;
    }

    public static List<Dependency> toAether(List<org.apache.maven.model.Dependency> dependencies) {
        List<Dependency> result = new ArrayList<Dependency>();
        for(org.apache.maven.model.Dependency dependency : dependencies) {
            result.add(toAether(dependency));
        }
        return result;
    }
}
