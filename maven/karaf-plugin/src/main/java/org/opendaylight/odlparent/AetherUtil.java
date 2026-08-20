/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for resolving maven artifacts and their dependencies.
 */
final class AetherUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AetherUtil.class);

    private final RepositorySystem repoSystem;
    private final RepositorySystemSession repoSession;
    private final List<RemoteRepository> remoteRepos;

    /**
     * Local repository path.
     */
    private final File localRepository;

    /**
     * Create an instance for the given repositories.
     *
     * @param repoSystem The repository system.
     * @param repoSession The repository session.
     * @param remoteRepos The remote repositories.
     * @param localRepository The local repository.
     */
    AetherUtil(final RepositorySystem repoSystem, final RepositorySystemSession repoSession,
            final List<RemoteRepository> remoteRepos, final File localRepository) {
        this.repoSystem = repoSystem;
        this.repoSession = repoSession;
        this.remoteRepos = remoteRepos;
        this.localRepository = localRepository;
    }

    /**
     * Resolves the given dependencies.
     *
     * @param dependencies The dependencies.
     * @return The corresponding artifacts.
     * @throws DependencyResolutionException if an error occurs.
     */
    Set<Artifact> resolveDependencies(final List<Dependency> dependencies) throws DependencyResolutionException {
        final var artifacts = new LinkedHashSet<Artifact>();
        final var collectRequest = new CollectRequest();
        collectRequest.setDependencies(dependencies);
        collectRequest.setRepositories(remoteRepos);
        final var request = new DependencyRequest(collectRequest, KarafFeaturesDependencyFilter.INSTANCE);
        final var results = repoSystem.resolveDependencies(repoSession, request);
        for (var artifactResult : results.getArtifactResults()) {
            artifacts.add(artifactResult.getArtifact());
        }
        LOG.trace("resolveDependencies({}) returns {}", dependencies, artifacts);
        return artifacts;
    }

    /**
     * Resolves the given artifact.
     *
     * @param artifact The artifact.
     * @return The resolved artifact, or {@code null} if it can't be resolved.
     */
    Artifact resolveArtifact(final Artifact artifact) {
        final var request = new ArtifactRequest(artifact, remoteRepos, null);
        final ArtifactResult result;
        try {
            result = repoSystem.resolveArtifact(repoSession, request);
        } catch (ArtifactResolutionException e) {
            LOG.warn("Unable to resolve artifact", e);
            return null;
        }
        LOG.trace("resolveArtifacts({}) returns {}", artifact, result.getArtifact());
        return result.getArtifact();
    }

    /**
     * Resolves the given coordinates.
     *
     * @param coord The coordinates to resolve.
     * @return The resolved artifact, or {@code null} if the coordinates can't be resolved.
     */
    Artifact resolveArtifact(final String coord) {
        return resolveArtifact(new DefaultArtifact(coord));
    }

    /**
     * Resolves the given coordinates.
     *
     * @param coords The set of coordinates to resolve.
     * @return The resolved artifacts. Unresolvable coordinates are skipped without error.
     */
    Set<Artifact> resolveArtifacts(final Set<String> coords) {
        final var result = new LinkedHashSet<Artifact>();
        for (var coord : coords) {
            final var artifact = resolveArtifact(coord);
            if (artifact != null) {
                result.add(artifact);
            }
        }
        LOG.trace("resolveArtifacts({}) returns {}", coords, result);
        return result;
    }

    /**
     * Installs the given artifacts.
     *
     * @param artifacts The artifacts to install.
     * @throws InstallationException if an error occurs.
     */
    void installArtifacts(final Set<Artifact> artifacts) throws InstallationException {
        final var localRepo = new LocalRepository(localRepository);
        final var localManager = repoSystem.newLocalRepositoryManager(repoSession, localRepo);
        final var localSession = new DefaultRepositorySystemSession();
        localSession.setLocalRepositoryManager(localManager);
        final var installRequest = new InstallRequest();
        for (var featureArtifact : artifacts) {
            installRequest.addArtifact(featureArtifact);
        }
        repoSystem.install(localSession, installRequest);
    }
}
