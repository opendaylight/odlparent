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
}
