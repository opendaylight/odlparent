/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import java.util.List;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;

final class KarafFeaturesDependencyFilter implements DependencyFilter {
    static final KarafFeaturesDependencyFilter INSTANCE = new KarafFeaturesDependencyFilter();

    private KarafFeaturesDependencyFilter() {
        // hidden on purpose
    }

    /**
     * Accepts only Karaf features.
     *
     * @param node The dependency node.
     * @param parents The parents (ignored).
     * @return {@code true} if the dependency is a Karaf feature, {@code false} otherwise.
     */
    @Override
    public boolean accept(final DependencyNode node, final List<DependencyNode> parents) {
        if (node == null) {
            return false;
        }
        final var artifact = node.getArtifact();
        final var dependency = node.getDependency();

        return artifact != null
            && artifact.getClassifier().equals("features")
            && artifact.getExtension().equals("xml")
            && dependency != null && dependency.getScope() != null;
    }
}
