<?xml version="1.0" encoding="UTF-8"?>
<!--
 Copyright © 2021 PANTHEON.tech, s.r.o. and others.  All rights reserved.

 This program and the accompanying materials are made available under the
 terms of the Eclipse Public License v1.0 which accompanies this distribution,
 and is available at http://www.eclipse.org/legal/epl-v10.html
 -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:functions="java:org.opendaylight.odlparent.add.versions.plugin.XsltFunctions" >
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:param name="mavenProject" />

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="comment()" />

    <xsl:template match="*[name()='feature' and @version='{{semVerRange}}']/@version" >
        <xsl:attribute name="version">
            <xsl:value-of select="functions:addFeatureVersionRange($mavenProject, ../text())"/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="*[name()='bundle']/text()">
        <xsl:choose>
            <xsl:when test="(starts-with(.,'mvn:')) or (starts-with(.,'wrap:mvn:'))">
                <xsl:value-of select="functions:addBundleVersion($mavenProject, .)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>