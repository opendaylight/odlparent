/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.odlparent;

import java.io.File;
import java.util.ArrayList;
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
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AetherUtil {
    private static final Logger LOG = LoggerFactory.getLogger(AetherUtil.class);
    private RepositorySystem repoSystem;

    private RepositorySystemSession repoSession;

    private List<RemoteRepository> remoteRepos;

    protected File localRepository;

    /**
     * Create an instance for the given repositories.
     *
     * @param repoSystem The repository system.
     * @param repoSession The repository session.
     * @param remoteRepos The remote repositories.
     * @param localRepository The local repository.
     */
    public AetherUtil(
            RepositorySystem repoSystem, RepositorySystemSession repoSession, List<RemoteRepository> remoteRepos,
            File localRepository) {
        this.repoSystem = repoSystem;
        this.repoSession = repoSession;
        this.remoteRepos = remoteRepos;
        this.localRepository = localRepository;
    }

    /**
     * Resolves the given dependencies.
     *
     * @param dependencies The dependencies.
     * @param filter The dependency filter.
     * @return The corresponding artifacts.
     * @throws DependencyResolutionException if an error occurs.
     */
    public Set<Artifact> resolveDependencies(List<Dependency> dependencies, DependencyFilter filter)
            throws DependencyResolutionException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setDependencies(dependencies);
        collectRequest.setRepositories(remoteRepos);
        DependencyRequest request = new DependencyRequest(collectRequest, filter);
        DependencyResult results = repoSystem.resolveDependencies(repoSession, request);
        for (ArtifactResult artifactResult : results.getArtifactResults()) {
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
    public Artifact resolveArtifact(Artifact artifact) {
        ArtifactRequest request = new ArtifactRequest(artifact, remoteRepos, null);
        ArtifactResult result;
        try {
            result = repoSystem.resolveArtifact(repoSession, request);
        } catch (ArtifactResolutionException e) {
            LOG.warn("Unable to resolve artifact: {}", e.getMessage(), e);
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
    public Artifact resolveArtifact(String coord) {
        DefaultArtifact artifact = new DefaultArtifact(coord);
        return resolveArtifact(artifact);
    }

    /**
     * Resolves the given coordinates.
     *
     * @param coords The set of coordinates to resolve.
     * @return The resolved artifacts. Unresolvable coordinates are skipped without error.
     */
    public Set<Artifact> resolveArtifacts(Set<String> coords) {
        Set<Artifact> result = new LinkedHashSet<Artifact>();
        for (String coord : coords) {
            Artifact artifact = resolveArtifact(coord);
            if (artifact != null) {
                result.add(artifact);
            }
        }
        LOG.trace("resolveArtifacts({}) returns {}", coords, result);
        return result;
    }

    /**
     * Converts the given artifact coordinates to a {@link Dependency} instance.
     *
     * @param coord The coordinates.
     * @return The dependency.
     */
    public Dependency toDependency(String coord) {
        Artifact artifact = new DefaultArtifact(coord);
        return new Dependency(artifact, null);
    }

    /**
     * Converts the given list of artifact coordinates to dependencies.
     *
     * @param coords The list of coordinates.
     * @return The corresponding dependencies.
     */
    public List<Dependency> toDependencies(List<String> coords) {
        List<Dependency> result = new ArrayList<Dependency>();
        for (String coord : coords) {
            result.add(toDependency(coord));
        }
        LOG.trace("toDependencies({}) returns {}", coords, result);
        return result;
    }

    /**
     * Installs the given artifacts.
     *
     * @param artifacts The artifacts to install.
     * @throws InstallationException if an error occurs.
     */
    public void installArtifacts(Set<Artifact> artifacts) throws InstallationException {
        LocalRepository localRepo = new LocalRepository(localRepository);
        LocalRepositoryManager localManager = repoSystem.newLocalRepositoryManager(repoSession, localRepo);
        DefaultRepositorySystemSession localSession = new DefaultRepositorySystemSession();
        localSession.setLocalRepositoryManager(localManager);
        InstallRequest installRequest = new InstallRequest();
        for (Artifact featureArtifact : artifacts) {
            installRequest.addArtifact(featureArtifact);
        }
        repoSystem.install(localSession, installRequest);
    }
}
