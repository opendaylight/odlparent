/*
 * SPDX-License-Identifier: EPL-1.0
 *
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

public class KarafFeaturesDependencyFilter implements DependencyFilter {

    /**
     * Accepts only Karaf features.
     *
     * @param node The dependency node.
     * @param parents The parents (ignored).
     * @return {@code true} if the dependency is a Karaf feature, {@code false} otherwise.
     */
    @Override
    public boolean accept(DependencyNode node, List<DependencyNode> parents) {
        return node != null
                && node.getArtifact() != null
                && node.getDependency() != null
                && node.getDependency().getScope() != null
                && node.getArtifact().getClassifier().equals("features")
                && node.getArtifact().getExtension().equals("xml");
    }

}
