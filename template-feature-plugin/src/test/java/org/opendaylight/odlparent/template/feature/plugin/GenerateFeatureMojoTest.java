/*
 * Copyright (c) 2022 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.template.feature.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GenerateFeatureMojoTest {
    @InjectMocks
    private GenerateFeatureMojo mojo;
    @Mock
    private MavenProject mavenProject;
    @Mock
    private Dependency dependency;

    @Test
    public void testSemVerRange() {
        assertEquals("[0,1)", GenerateFeatureMojo.semVerRange("0"));
        assertEquals("[0.1,1)", GenerateFeatureMojo.semVerRange("0.1"));
        assertEquals("[0.0.1,1)", GenerateFeatureMojo.semVerRange("0.0.1"));
        assertEquals("[0.1,1)", GenerateFeatureMojo.semVerRange("0.1.0"));
        assertEquals("[0.1.1,1)", GenerateFeatureMojo.semVerRange("0.1.1"));
        assertEquals("[1.2.3,2)", GenerateFeatureMojo.semVerRange("1.2.3"));
    }

    @Test
    public void testProcessBundle() throws MojoFailureException {
        doReturn("org.opendaylight.genius").when(dependency).getGroupId();
        doReturn("lockmanager-api").when(dependency).getArtifactId();
        doReturn("1.2.3").when(dependency).getVersion();

        doReturn("odl-yangtools-util").when(mavenProject).getArtifactId();
        doReturn("8.0.0-SNAPSHOT").when(mavenProject).getVersion();
        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util" version="8.0.0.SNAPSHOT">
                    <bundle>mvn:org.opendaylight.genius/lockmanager-api/1.2.3</bundle>
                </feature>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util">
                    <bundle>mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}</bundle>
                </feature>
            </features>
            """);
    }

    @Test
    public void testProcessFeature() throws MojoFailureException {
        doReturn("odl-apache-commons-net").when(dependency).getArtifactId();
        doReturn("1.2.3").when(dependency).getVersion();
        doReturn("xml").when(dependency).getType();
        doReturn("features").when(dependency).getClassifier();

        doReturn("odl-yangtools-util").when(mavenProject).getArtifactId();
        doReturn("8.0.0-SNAPSHOT").when(mavenProject).getVersion();
        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util" version="8.0.0.SNAPSHOT">
                    <feature version="[1.2.3,2)">odl-apache-commons-net</feature>
                </feature>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util">
                    <feature version="{{semVerRange}}">odl-apache-commons-net</feature>
                </feature>
            </features>
            """);
        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util" version="8.0.0.SNAPSHOT">
                    <feature version="1.2.3">odl-apache-commons-net</feature>
                </feature>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="odl-yangtools-util">
                    <feature version="{{versionAsInProject}}">odl-apache-commons-net</feature>
                </feature>
            </features>
            """);
    }

    @Test
    public void testProcessFeatureProjectVersion() throws MojoFailureException {
        doReturn("1.2.3-SNAPSHOT").when(mavenProject).getVersion();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="self" version="1.2.3.SNAPSHOT"/>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="self" version="{{projectVersion}}"/>
            </features>
            """);
    }

    @Test
    public void testProcessFeatureProjectCustomVersion() {
        doReturn("odl-yangtools-util").when(mavenProject).getArtifactId();
        doReturn("1.2.3-TEST-SNAPSHOT").when(mavenProject).getVersion();

        final var trieMap = mock(Dependency.class);
        doReturn("pt-triemap").when(trieMap).getArtifactId();
        doReturn("2.3.4-TEST-SNAPSHOT").when(trieMap).getVersion();
        doReturn("xml").when(trieMap).getType();
        doReturn("features").when(trieMap).getClassifier();

        final var util = mock(Dependency.class);
        doReturn("org.opendaylight.yangtools").when(util).getGroupId();
        doReturn("util").when(util).getArtifactId();
        doReturn("3.4.5-TEST-SNAPSHOT").when(util).getVersion();

        doReturn(List.of(trieMap, util)).when(mavenProject).getDependencies();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="self" version="1.2.3.TEST-SNAPSHOT">
                    <feature version="2.3.4.TEST-SNAPSHOT">pt-triemap</feature>
                    <feature version="1.2.3.TEST-SNAPSHOT">concepts</feature>
                    <bundle>mvn:org.opendaylight.yangtools/util/3.4.5-TEST-SNAPSHOT</bundle>
                </feature>
                <feature name="odl-yangtools-util" version="1.2.3.TEST-SNAPSHOT"/>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <feature name="self" version="{{projectVersion}}">
                    <feature version="{{versionAsInProject}}">pt-triemap</feature>
                    <feature version="{{projectVersion}}">concepts</feature>
                    <bundle>mvn:org.opendaylight.yangtools/util/{{versionAsInProject}}</bundle>
                </feature>
                <feature name="odl-yangtools-util" version="{{versionAsInProject}}"/>
            </features>
            """);
    }

    @Test
    public void testProcessRepository() throws MojoFailureException {
        doReturn("example").when(dependency).getGroupId();
        doReturn("example").when(dependency).getArtifactId();
        doReturn("10.0.0-SNAPSHOT").when(dependency).getVersion();
        doReturn("xml").when(dependency).getType();
        doReturn("features").when(dependency).getClassifier();

        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:example/example/10.0.0-SNAPSHOT/xml/features</repository>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:example/example/{{versionAsInProject}}/xml/features</repository>
            </features>
            """);
    }

    @Test
    public void testFullTranslation() throws MojoFailureException {
        doReturn("odl-yangtools-util").when(mavenProject).getArtifactId();
        doReturn("8.0.0-SNAPSHOT").when(mavenProject).getVersion();

        final var trieMap = mock(Dependency.class);
        doReturn("tech.pantheon.triemap").when(trieMap).getGroupId();
        doReturn("pt-triemap").when(trieMap).getArtifactId();
        doReturn("1.2.0").when(trieMap).getVersion();
        doReturn("xml").when(trieMap).getType();
        doReturn("features").when(trieMap).getClassifier();

        final var concepts = mock(Dependency.class);
        doReturn("org.opendaylight.yangtools").when(concepts).getGroupId();
        doReturn("concepts").when(concepts).getArtifactId();
        doReturn("8.0.0-SNAPSHOT").when(concepts).getVersion();

        final var util = mock(Dependency.class);
        doReturn("org.opendaylight.yangtools").when(util).getGroupId();
        doReturn("util").when(util).getArtifactId();
        doReturn("8.0.0-SNAPSHOT").when(util).getVersion();

        doReturn(List.of(trieMap, concepts, util)).when(mavenProject).getDependencies();

        assertProcessFeature(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:tech.pantheon.triemap/pt-triemap/1.2.0/xml/features</repository>
                <feature name="odl-yangtools-util" description="Utilities" version="8.0.0.SNAPSHOT">
                    <details>YANG Tools common concepts and utilities</details>
                    <feature version="[1.2,2)">pt-triemap</feature>
                    <bundle>mvn:org.opendaylight.yangtools/concepts/8.0.0-SNAPSHOT</bundle>
                    <bundle>mvn:org.opendaylight.yangtools/util/8.0.0-SNAPSHOT</bundle>
                </feature>
            </features>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:tech.pantheon.triemap/pt-triemap/{{versionAsInProject}}/xml/features</repository>
                <feature name="odl-yangtools-util" description="Utilities" version="{{projectVersion}}">
                    <details>YANG Tools common concepts and utilities</details>
                    <feature version="{{semVerRange}}">pt-triemap</feature>
                    <bundle>mvn:org.opendaylight.yangtools/concepts/{{versionAsInProject}}</bundle>
                    <bundle>mvn:org.opendaylight.yangtools/util/{{versionAsInProject}}</bundle>
                </feature>
            </features>
            """);
    }

    private void assertProcessFeature(final String expected, final String input) {
        try {
            final var features = GenerateFeatureMojo.readFeature(null,
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            mojo.processFeatures(features);

            final var output = new ByteArrayOutputStream();
            GenerateFeatureMojo.writeFeature(features, output);
            assertEquals(expected, output.toString(StandardCharsets.UTF_8));
        } catch (IOException | JAXBException | MojoFailureException e) {
            throw new AssertionError("Failed to process " + input, e);
        }
    }
}
