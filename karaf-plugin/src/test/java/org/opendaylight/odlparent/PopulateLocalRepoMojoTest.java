/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PopulateLocalRepoMojoTest {

    @InjectMocks
    private PopulateLocalRepoMojo localRepoMojo;

    @Mock
    private MavenProject mavenProject;

    @Test
    void testBlackListedInnerFeature() {
        // Prepare environment.
        mockBlackListedFeatures(List.of("pax-jdbc-teradata", "pax-jdbc-mssql"));
        final var includeJar = localRepoMojo.getBlackListedFeatures();
        assertEquals(2, includeJar.size());

        final var features = new HashSet<Features>();
        final var mockPaxFeatures = createMockFeatures("org.ops4j.pax.jdbc-1.5.7");
        final var mockPaxFeature = Set.of("pax-jdbc-db2", "pax-jdbc-teradata", "pax-jdbc-mssql", "pax-jdbc-derbyclient")
            .stream()
            .map(PopulateLocalRepoMojoTest::createMockFeature)
            .collect(Collectors.toCollection(ArrayList::new));
        doReturn(mockPaxFeature).when(mockPaxFeatures).getFeature();
        features.add(mockPaxFeatures);
        final var mockDataFeatures = createMockFeatures("odl-jakarta-activation-api");
        features.add(mockDataFeatures);

        // Remove excluded features.
        final var resultFeatures = localRepoMojo.removeBlackListedFeatures(features, includeJar);

       // Verify excluded features.
        assertEquals(2, resultFeatures.size());
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("odl-jakarta-activation-api")));
        final var optionalPaxFeatures = resultFeatures.stream()
            .filter(t -> t.getName().equals("org.ops4j.pax.jdbc-1.5.7"))
            .findFirst();
        assertTrue(optionalPaxFeatures.isPresent());
        final var paxFeatures = optionalPaxFeatures.orElseThrow().getFeature();
        assertEquals(2, paxFeatures.size());
        assertTrue(paxFeatures.stream().anyMatch(t -> t.getName().equals("pax-jdbc-db2")));
        assertTrue(paxFeatures.stream().anyMatch(t -> t.getName().equals("pax-jdbc-derbyclient")));
    }

    @Test
    void testBlackListFeatures() {
        // Prepare environment.
        mockBlackListedFeatures(List.of("test-data", "framework-4.4.6"));
        final var includeJar = localRepoMojo.getBlackListedFeatures();
        assertEquals(2, includeJar.size());

        final var features = new HashSet<Features>();
        final var mockPaxFeatures = createMockFeatures("org.ops4j.pax.jdbc-1.5.7");
        final var mockPaxFeature = Set.of("pax-jdbc-db2", "pax-jdbc-teradata", "pax-jdbc-derbyclient").stream()
            .map(PopulateLocalRepoMojoTest::createMockFeature)
            .collect(Collectors.toCollection(ArrayList::new));
        doReturn(mockPaxFeature).when(mockPaxFeatures).getFeature();
        features.add(mockPaxFeatures);
        final var mockFrameworkFeatures = createMockFeatures("framework-4.4.6");
        features.add(mockFrameworkFeatures);
        final var mockDataFeatures = createMockFeatures("test-data");
        features.add(mockDataFeatures);

        // Remove excluded features.
        final var resultFeatures = localRepoMojo.removeBlackListedFeatures(features, includeJar);

        // Verify excluded features.
        assertEquals(1, resultFeatures.size());
        final var optionalPaxFeatures = resultFeatures.stream()
            .filter(t -> t.getName().equals("org.ops4j.pax.jdbc-1.5.7"))
            .findFirst();
        assertTrue(optionalPaxFeatures.isPresent());
        final var paxFeatures = optionalPaxFeatures.orElseThrow().getFeature();
        assertEquals(3, paxFeatures.size());
        assertTrue(paxFeatures.stream().anyMatch(t -> t.getName().equals("pax-jdbc-db2")));
        assertTrue(paxFeatures.stream().anyMatch(t -> t.getName().equals("pax-jdbc-teradata")));
        assertTrue(paxFeatures.stream().anyMatch(t -> t.getName().equals("pax-jdbc-derbyclient")));
    }

    @Test
    void testSpecialCharactersInBlackListedFeatures() {
        // Prepare environment.
        mockBlackListedFeatures(List.of("spring-jdbc/(5.2,6.0]", "*tomcat*", "pax-web/[1.2,)"));
        final var blackListedFeatures = localRepoMojo.getBlackListedFeatures();
        assertEquals(3, blackListedFeatures.size());

        final var features = new HashSet<Features>();
        features.add(createMockFeatures("spring-jdbc/6.0"));
        features.add(createMockFeatures("spring-jdbc/5.2"));
        features.add(createMockFeatures("spring-jdbc/6.2"));
        features.add(createMockFeatures("a-tomcat-s"));
        features.add(createMockFeatures("pax-web/10.0"));
        features.add(createMockFeatures("pax-web/1.0"));
        features.add(createMockFeatures("test"));

        // Remove blacklisted features.
        final var resultFeatures = localRepoMojo.removeBlackListedFeatures(features, blackListedFeatures);

        // Verify blacklisted features.
        assertEquals(4, resultFeatures.size());
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("spring-jdbc/5.2")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("spring-jdbc/6.2")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("pax-web/1.0")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("test")));
    }

    private void mockBlackListedFeatures(final List<String> featureNames) {
        final var mockBlacklistFeatures = mock(Xpp3Dom.class);
        final var xpp3Doms = featureNames.stream()
            .map(featureName -> {
                final var mockBlackListedFeature = mock(Xpp3Dom.class);
                doReturn(featureName).when(mockBlackListedFeature).getValue();
                return mockBlackListedFeature;
            })
            .toArray(Xpp3Dom[]::new);

        doReturn(xpp3Doms).when(mockBlacklistFeatures).getChildren();
        final var mockConfiguration = mock(Xpp3Dom.class);
        doReturn(mockBlacklistFeatures).when(mockConfiguration).getChild(eq("blacklistedFeatures"));
        final var mockPlugin = mock(Plugin.class);
        doReturn(mockConfiguration).when(mockPlugin).getConfiguration();
        doReturn(mockPlugin).when(mavenProject).getPlugin(eq("org.apache.karaf.tooling:karaf-maven-plugin"));
    }

    private static Feature createMockFeature(final String name) {
        final var mockFeature = mock(Feature.class);
        doReturn(name).when(mockFeature).getName();
        return mockFeature;
    }

    private static Features createMockFeatures(final String name) {
        final var mockFeature = mock(Features.class);
        doReturn(name).when(mockFeature).getName();
        return mockFeature;
    }
}
