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

import java.util.List;
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

        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertEquals("<bundle>mvn:org.opendaylight.genius/lockmanager-api/1.2.3</bundle>",
            mojo.process("<bundle>mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}</bundle>"));
    }

    @Test
    public void testProcessFeature() throws MojoFailureException {
        doReturn("odl-apache-commons-net").when(dependency).getArtifactId();
        doReturn("1.2.3").when(dependency).getVersion();
        doReturn("xml").when(dependency).getType();
        doReturn("features").when(dependency).getClassifier();

        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertEquals("<feature version=\"[1.2.3,2)\">odl-apache-commons-net</feature>",
            mojo.process("<feature version=\"{{semVerRange}}\">odl-apache-commons-net</feature>"));
        assertEquals("<feature version=\"1.2.3\">odl-apache-commons-net</feature>",
            mojo.process("<feature version=\"{{versionAsInProject}}\">odl-apache-commons-net</feature>"));
    }

    @Test
    public void testProcessFeatureProjectVersion() throws MojoFailureException {
        doReturn("self").when(mavenProject).getArtifactId();
        doReturn("1.2.3-SNAPSHOT").when(mavenProject).getVersion();

        assertEquals("<feature version=\"1.2.3.SNAPSHOT\">self</feature>",
            mojo.process("<feature version=\"{{projectVersion}}\">self</feature>"));
    }

    @Test
    public void testProcessRepository() throws MojoFailureException {
        doReturn("example").when(dependency).getGroupId();
        doReturn("example").when(dependency).getArtifactId();
        doReturn("10.0.0-SNAPSHOT").when(dependency).getVersion();
        doReturn("xml").when(dependency).getType();
        doReturn("features").when(dependency).getClassifier();

        doReturn(List.of(dependency)).when(mavenProject).getDependencies();

        assertEquals("<repository>mvn:example/example/10.0.0-SNAPSHOT/xml/features</repository>",
            mojo.process("<repository>mvn:example/example/{{versionAsInProject}}/xml/features</repository>"));
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
        doReturn("8.0.0-SNAPSHOT<").when(concepts).getVersion();

        final var util = mock(Dependency.class);
        doReturn("org.opendaylight.yangtools").when(util).getGroupId();
        doReturn("util").when(util).getArtifactId();
        doReturn("8.0.0-SNAPSHOT<").when(util).getVersion();

        doReturn(List.of(trieMap, concepts, util)).when(mavenProject).getDependencies();

        final var result = assertProcessFeature("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:tech.pantheon.triemap/pt-triemap/{{versionAsInProject}}/xml/features</repository>
                <feature name="odl-yangtools-util" description="Utilities" version="{{projectVersion}}">
                    <details>YANG Tools common concepts and utilities</details>
                    <feature version="{{semVerRange}}">pt-triemap</feature>
                    <bundle>mvn:org.opendaylight.yangtools/concepts/{{versionAsInProject}}</bundle>
                    <bundle>mvn:org.opendaylight.yangtools/util/{{versionAsInProject}}</bundle>
                </feature>
            </features>""");
        assertEquals("""
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <features xmlns="http://karaf.apache.org/xmlns/features/v1.6.0" name="odl-yangtools-util">
                <repository>mvn:tech.pantheon.triemap/pt-triemap/1.2.0/xml/features</repository>
                <feature name="odl-yangtools-util" description="Utilities" version="8.0.0.SNAPSHOT">
                    <details>YANG Tools common concepts and utilities</details>
                    <feature version="[1.2,2)">pt-triemap</feature>
                    <bundle>mvn:org.opendaylight.yangtools/concepts/8.0.0-SNAPSHOT</bundle>
                    <bundle>mvn:org.opendaylight.yangtools/util/8.0.0-SNAPSHOT</bundle>
                </feature>
            </features>""", result);
    }

    private String assertProcessFeature(final String input) {
        try {
            return mojo.process(input);
        } catch (MojoFailureException e) {
            throw new AssertionError("Failed to process " + input, e);
        }
    }
}
