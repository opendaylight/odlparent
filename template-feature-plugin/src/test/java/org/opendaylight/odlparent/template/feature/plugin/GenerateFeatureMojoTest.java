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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class GenerateFeatureMojoTest {
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
        final var mojo = mockMojo();

        final var dep = mock(Dependency.class);
        doReturn("org.opendaylight.genius").when(dep).getGroupId();
        doReturn("lockmanager-api").when(dep).getArtifactId();
        doReturn("1.2.3").when(dep).getVersion();

        doReturn(List.of(dep)).when(mojo.mavenProject).getDependencies();

        assertEquals("<bundle>mvn:org.opendaylight.genius/lockmanager-api/1.2.3</bundle>",
            mojo.process("<bundle>mvn:org.opendaylight.genius/lockmanager-api/{{versionAsInProject}}</bundle>"));
    }

    @Test
    public void testProcessFeature() throws MojoFailureException {
        final var mojo = mockMojo();

        assertEquals("",
            mojo.process("<feature version=\"{{semVerRange}}\">odl-apache-commons-net</feature>"));
    }

    private static GenerateFeatureMojo mockMojo() {
        final var mojo = new GenerateFeatureMojo();
        mojo.mavenProject = mock(MavenProject.class);
        return mojo;
    }
}
