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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.karaf.features.internal.model.Feature;
import org.apache.karaf.features.internal.model.Features;
import org.apache.maven.project.MavenProject;
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
    void testExcludeInnerFeatureJar() {
        // Prepare environment.
        final var properties = new Properties();
        properties.setProperty("pax.jdbc.db2.include.jar", "true");
        properties.setProperty("pax.jdbc.teradata.include.jar", "false");
        doReturn(properties).when(mavenProject).getProperties();

        final var includeJar = localRepoMojo.getIncludeJar();
        assertEquals(2, includeJar.size());

        final var features = new HashSet<Features>();
        final var mockPaxFeatures = createMockFeatures("org.ops4j.pax.jdbc-1.5.7");
        final var mockPaxFeature = Set.of("pax-jdbc-db2", "pax-jdbc-teradata", "pax-jdbc-derbyclient").stream()
            .map(this::createMockFeature)
            .collect(Collectors.toCollection(ArrayList::new));
        doReturn(mockPaxFeature).when(mockPaxFeatures).getFeature();
        features.add(mockPaxFeatures);
        final var mockDataFeatures = createMockFeatures("odl-jakarta-activation-api");
        features.add(mockDataFeatures);

       // Remove excluded features.
        final var resultFeatures = localRepoMojo.removeExcludedFeatures(features, includeJar);

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
    void testExcludeFeatureJar() {
        // Prepare environment.
        final var properties = new Properties();
        properties.setProperty("org.ops4j.pax.jdbc.1.5.7.include.jar", "true");
        properties.setProperty("test.data.include.jar", "false");
        doReturn(properties).when(mavenProject).getProperties();

        final var includeJar = localRepoMojo.getIncludeJar();
        assertEquals(2, includeJar.size());

        final var features = new HashSet<Features>();
        final var mockPaxFeatures = createMockFeatures("org.ops4j.pax.jdbc-1.5.7");
        final var mockPaxFeature = Set.of("pax-jdbc-db2", "pax-jdbc-teradata", "pax-jdbc-derbyclient").stream()
            .map(this::createMockFeature)
            .collect(Collectors.toCollection(ArrayList::new));
        doReturn(mockPaxFeature).when(mockPaxFeatures).getFeature();
        features.add(mockPaxFeatures);
        final var mockDataFeatures = createMockFeatures("test-data");
        features.add(mockDataFeatures);

        // Remove excluded features.
        final var resultFeatures = localRepoMojo.removeExcludedFeatures(features, includeJar);

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
    void testIncludeJarWithWrongValueProperties() {
        final var properties = new Properties();
        properties.setProperty("pax.jdbc.db2.include.jar", "foo");
        properties.setProperty("pax.jdbc.teradata.include.jar", "10");
        properties.setProperty("data2.include.jar", "");
        doReturn(properties).when(mavenProject).getProperties();

        assertEquals(Map.of(), localRepoMojo.getIncludeJar());
    }

    @Test
    void testIncludeJarWithWrongProperties() {
        final var properties = new Properties();
        properties.setProperty("pax.jdbc.db2.include", "true");
        properties.setProperty("pax.jdbc.teradata.jar", "false");
        properties.setProperty("pax.jdbc.teradata", "false");
        properties.setProperty("karaf.localFeature", "standard");
        doReturn(properties).when(mavenProject).getProperties();

        assertEquals(Map.of(), localRepoMojo.getIncludeJar());
    }

    @Test
    void testIncludeJarWithNoIncludeJarProperty() {
        final var properties = new Properties();
        doReturn(properties).when(mavenProject).getProperties();

        assertEquals(Map.of(), localRepoMojo.getIncludeJar());
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
