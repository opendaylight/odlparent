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

public class AetherUtil {

    private RepositorySystem repoSystem;

    private RepositorySystemSession repoSession;

    private List<RemoteRepository> remoteRepos;

    protected File localRepository;

    public AetherUtil(RepositorySystem repoSystem,RepositorySystemSession repoSession,List<RemoteRepository> remoteRepos, File localRepository) {
        this.repoSystem = repoSystem;
        this.repoSession = repoSession;
        this.remoteRepos = remoteRepos;
        this.localRepository = localRepository;
    }

    public Set<Artifact> resolveDependencies(List<Dependency>dependencies,DependencyFilter filter) throws DependencyResolutionException {
        Set<Artifact> artifacts = new LinkedHashSet<Artifact>();
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setDependencies(dependencies);
        collectRequest.setRepositories(remoteRepos);
        DependencyRequest request = new DependencyRequest(collectRequest,filter);
        DependencyResult results = repoSystem.resolveDependencies(repoSession, request);
        for(ArtifactResult artifactResult: results.getArtifactResults()) {
            artifacts.add(artifactResult.getArtifact());
        }
        return artifacts;
    }

    public Artifact resolveArtifact(Artifact artifact) throws ArtifactResolutionException {
        ArtifactRequest request = new ArtifactRequest(artifact, remoteRepos,null);
        ArtifactResult result = repoSystem.resolveArtifact(repoSession, request);
        return result.getArtifact();
    }

    public Artifact resolveArtifact(String coord) throws ArtifactResolutionException {
        DefaultArtifact artifact = new DefaultArtifact(coord);
        return resolveArtifact(artifact);
    }

    public Set<Artifact> resolveArtifacts(Set<String> coords) throws ArtifactResolutionException {
        Set<Artifact> result = new LinkedHashSet<Artifact>();
        for(String coord: coords) {
            result.add(resolveArtifact(coord));
        }
        return result;
    }

    public Dependency toDependency(String coord) {
        Artifact artifact = new DefaultArtifact(coord);
        Dependency dependency = new Dependency(artifact, null);
        return dependency;
    }

    public List<Dependency> toDependencies(List<String> coords) {
        List<Dependency> result = new ArrayList<Dependency>();
        for(String coord: coords) {
            result.add(toDependency(coord));
        }
        return result;
    }

    public void installArtifacts(Set<Artifact> artifacts) throws InstallationException {
        LocalRepository localRepo = new LocalRepository(localRepository);
        LocalRepositoryManager localManager = repoSystem.newLocalRepositoryManager(repoSession, localRepo);
        DefaultRepositorySystemSession localSession = new DefaultRepositorySystemSession();
        localSession.setLocalRepositoryManager(localManager);
        InstallRequest installRequest = new InstallRequest();
        for(Artifact featureArtifact : artifacts) {
            installRequest.addArtifact(featureArtifact);
        }
        repoSystem.install(localSession, installRequest);
    }
}
