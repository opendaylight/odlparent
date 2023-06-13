/*
 * Copyright (c) 2023 PANTHEON.tech s.r.o. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import java.util.List;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;

public class KarafTestFeaturesDependencyFilter implements DependencyFilter {

    /**
     * Accepts only Karaf features with test scope.
     *
     * @param node The dependency node.
     * @param list The parents (ignored).
     * @return {@code true} if the dependency is a Karaf feature with test scope, {@code false} otherwise.
     */
    @Override
    public boolean accept(final DependencyNode node, final List<DependencyNode> list) {
        return node != null
            && node.getArtifact() != null
            && node.getDependency() != null
            && node.getDependency().getScope() != null
            && node.getDependency().getScope().equals("test")
            && node.getArtifact().getClassifier().equals("features")
            && node.getArtifact().getExtension().equals("xml");
    }
}
