/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import java.util.Set;
import org.eclipse.aether.artifact.Artifact;

/**
 * Feature descriptor in maven dependency context.
 *
 * @param artifact aether object representing maven artifact
 * @param featureNames all feature names extracted from xml file
 */
public record FeatureDependency(Artifact artifact, Set<String> featureNames) {
}