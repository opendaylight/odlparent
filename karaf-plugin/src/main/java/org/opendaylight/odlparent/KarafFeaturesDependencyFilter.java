package org.opendaylight.odlparent;

import java.util.List;

import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;

public class KarafFeaturesDependencyFilter implements DependencyFilter {

    public boolean accept(DependencyNode node, List<DependencyNode> parents) {
        if(node != null
                && node.getArtifact() != null
                && node.getDependency() != null
                && node.getDependency().getScope() != null
                && node.getArtifact().getClassifier().equals("features")
                && node.getArtifact().getExtension().equals("xml")) {
            return true;
        }
        return false;
    }

}
