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
            .map(this::createMockFeature)
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
            .map(this::createMockFeature)
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
        final var includeJar = localRepoMojo.getBlackListedFeatures();
        assertEquals(3, includeJar.size());

        final var features = new HashSet<Features>();
        final var mockSpring60Features = createMockFeatures("spring-jdbc/6.0");
        features.add(mockSpring60Features);
        final var mockSpring52Features = createMockFeatures("spring-jdbc/5.2");
        features.add(mockSpring52Features);
        final var mockSpring62Features = createMockFeatures("spring-jdbc/6.2");
        features.add(mockSpring62Features);
        final var mockTomcatFeatures = createMockFeatures("a-tomcat-s");
        features.add(mockTomcatFeatures);
        final var mockPax10Features = createMockFeatures("pax-web/10.0");
        features.add(mockPax10Features);
        final var mockPax1Features = createMockFeatures("pax-web/1.0");
        features.add(mockPax1Features);
        final var mockTestFeatures = createMockFeatures("test");
        features.add(mockTestFeatures);

        // Remove excluded features.
        final var resultFeatures = localRepoMojo.removeBlackListedFeatures(features, includeJar);

        // Verify excluded features.
        assertEquals(4, resultFeatures.size());
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("spring-jdbc/5.2")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("spring-jdbc/6.2")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("pax-web/1.0")));
        assertTrue(resultFeatures.stream().anyMatch(t -> t.getName().equals("test")));
    }

    @Test
    void testEmptyKarafPluginConfiguration() {
        // Verify result without karaf-maven-plugin.
        assertTrue(localRepoMojo.getBlackListedFeatures().isEmpty());

        // Verify result without karaf-maven-plugin configuration.
        final var mockPlugin = mock(Plugin.class);
        doReturn(mockPlugin).when(mavenProject).getPlugin(eq("org.apache.karaf.tooling:karaf-maven-plugin"));
        assertEquals(List.of(), localRepoMojo.getBlackListedFeatures());

        // Verify result without karaf-maven-plugin blackListedFeatures configuration.
        final var mockConfiguration = mock(Xpp3Dom.class);
        doReturn(mockConfiguration).when(mockPlugin).getConfiguration();
        assertEquals(List.of(), localRepoMojo.getBlackListedFeatures());

        // Verify result with empty configuration of blackListedFeatures in karaf-maven-plugin.
        final var mockBlacklistFeatures = mock(Xpp3Dom.class);
        doReturn(new Xpp3Dom[0]).when(mockBlacklistFeatures).getChildren();
        doReturn(mockBlacklistFeatures).when(mockConfiguration).getChild(eq("blacklistedFeatures"));
        assertEquals(List.of(), localRepoMojo.getBlackListedFeatures());

        // Verify result when feature element is empty in blackListedFeatures configuration.
        doReturn(new Xpp3Dom[0]).when(mockBlacklistFeatures).getChildren();
        assertEquals(List.of(), localRepoMojo.getBlackListedFeatures());
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

    private Feature createMockFeature(final String name) {
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
