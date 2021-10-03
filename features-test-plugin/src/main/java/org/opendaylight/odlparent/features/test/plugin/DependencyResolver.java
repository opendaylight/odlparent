/*
 * Copyright (c) 2024 PANTHEON.tech s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.features.test.plugin;

import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.extractDependencies;
import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.identifierOf;
import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.isFeature;
import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.pluginFeatureDependencies;
import static org.opendaylight.odlparent.features.test.plugin.DependencyUtils.toAetherArtifact;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.karaf.features.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.karaf.features.internal.model.JaxbUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DependencyResolver {
    private static final Logger LOG = LoggerFactory.getLogger(DependencyResolver.class);

    private final Map<String, Artifact> resolvedArtifacts = new HashMap<>();
    private final Map<String, Set<String>> resolvedFeatures = new HashMap<>();
    private final RepositorySystemSession repoSession;
    private final List<RemoteRepository> repositories;
    private final RepositorySystem repoSystem;

    DependencyResolver(final RepositorySystem repoSystem, final RepositorySystemSession repoSession,
        final List<RemoteRepository> repositories) {
        this.repoSession = repoSession;
        this.repositories = repositories;
        this.repoSystem = repoSystem;
    }

    Set<FeatureDependency> resolveFeatures(final Collection<org.apache.maven.artifact.Artifact> artifacts)
            throws MojoExecutionException {
        final var featureDependencies = new LinkedList<FeatureDependency>();
        for (var mvnArtifact : artifacts) {
            final var artifact = toAetherArtifact(mvnArtifact);
            if (isFeature(artifact)) {
                final var resolved = resolve(artifact);
                final var featureNames = resolveFeatureFile(resolved.getFile());
                featureDependencies.add(new FeatureDependency(resolved, featureNames));
            }
        }
        return Set.copyOf(featureDependencies);
    }

    Set<FeatureDependency> resolvePluginFeatures() throws MojoExecutionException {
        final var featureDependencies = new LinkedList<FeatureDependency>();
        for (var fd : pluginFeatureDependencies()) {
            final var resolved = resolve(fd.artifact());
            final var featureNames = resolveFeatureFile(resolved.getFile(), fd.featureNames());
            featureDependencies.add(new FeatureDependency(resolved, featureNames));
        }
        return Set.copyOf(featureDependencies);
    }

    Set<String> resolveFeatureFile(final File featureFile) throws MojoExecutionException {
        return resolveFeatureFile(featureFile, Set.of());
    }

    private Set<String> resolveFeatureFile(final File featureFile, final Set<String> filterFeatures)
            throws MojoExecutionException {
        final var filePath = featureFile.getAbsolutePath();
        final var identifier = filePath + " " + String.join(",", filterFeatures);
        LOG.debug("Resolving dependencies for feature file: {}", filePath);
        final var cached = resolvedFeatures.get(identifier);
        if (cached != null) {
            return cached;
        }
        if (!featureFile.exists()) {
            LOG.debug("Feature file {} does not exist. Dependency resolution omitted.", filePath);
            return Set.of();
        }
        final Features features;
        try (var inputStream = new FileInputStream(featureFile)) {
            features = JaxbUtil.unmarshal(featureFile.toURI().toString(), inputStream, false);
        } catch (IOException e) {
            throw new MojoExecutionException("Could not read feature file " + featureFile, e);
        }
        for (var unresolved : extractDependencies(features, filterFeatures)) {
            LOG.debug("Resolving feature dependency: {}", unresolved);
            final var resolved = resolve(unresolved);
            if (isFeature(resolved)) {
                resolveFeatureFile(resolved.getFile());
            }
        }
        final var featureNames = features.getFeature().stream().map(Feature::getName).collect(Collectors.toSet());
        resolvedFeatures.put(identifier, featureNames);
        return featureNames;
    }

    private Artifact resolve(final Artifact unresolved) throws MojoExecutionException {
        final var identifier = identifierOf(unresolved);
        final var cached = resolvedArtifacts.get(identifier);
        if (cached != null) {
            return cached;
        }
        final var request = new ArtifactRequest().setRepositories(repositories).setArtifact(unresolved);
        final ArtifactResult resolutionResult;
        try {
            resolutionResult = repoSystem.resolveArtifact(repoSession, request);
        } catch (ArtifactResolutionException e) {
            throw new MojoExecutionException("Could not resolve artifact " + identifier, e);
        }
        final var resolved = resolutionResult.getArtifact();
        LOG.debug("Dependency resolved for {}", identifier);
        final var file = resolved.getFile();
        if (file == null || !file.exists()) {
            LOG.warn("Dependency artifact {} is resolved but has no attached file.", identifier);
        }
        resolvedArtifacts.put(identifier, resolved);
        return resolved;
    }
}
