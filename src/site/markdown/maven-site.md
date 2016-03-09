Maven Site is a handy tool for auto-generating a website containing site
details for your project based on information scanned from project pom files.
It can also be used to auto-generate coverage reports and Javadoc pages. This
page will describe how to generate a Maven site for your ODL project.

* [Enable Maven Site generation](#enable_site)
* [Fix site urls](#fix_site_urls)
* [Customize project site](#customize_site)

## <a name="enable_site">Enabling Maven Site generation</a>

The odlparent project provides most of the necessary configuration already to
generate a Maven site for all ODL projects but the site generation is disabled
by default. In order to enable site generation you will need to create a file
called "deploy-site.xml" in the root of your project repo. This file's
existance will cause the Maven build as well as ODL Jenkins system to start
generating and deploying the site to Nexus.

Below is an example configuration you can copy to create the deploy-site.xml
this file is not too complicated and the only thing you need to change is to
ensure that the \<groupId\> is configured to use your project's namespace.

    <?xml version="1.0" encoding="UTF-8"?>
    <!-- vi: set et smarttab sw=2 tabstop=2: -->
    <!--
        Copyright (c) 2015 The Linux Foundation and others.  All rights reserved.

        This program and the accompanying materials are made available under the
        terms of the Eclipse Public License v1.0 which accompanies this distribution,
        and is available at http://www.eclipse.org/legal/epl-v10.html
    -->
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>org.opendaylight.PROJECT</groupId>
      <artifactId>deploy-site</artifactId>
      <version>1.0.0-SNAPSHOT</version>
      <packaging>pom</packaging>

      <properties>
        <stream>latest</stream>
        <nexus.site.url>dav:https://nexus.opendaylight.org/content/sites/site/${project.groupId}/${stream}/</nexus.site.url>
      </properties>

      <build>
        <extensions>
          <extension>
            <groupId>org.apache.maven.wagon</groupId>
             <artifactId>wagon-webdav-jackrabbit</artifactId>
             <version>2.9</version>
          </extension>
        </extensions>

        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-site-plugin</artifactId>
            <version>3.4</version>
            <configuration>
              <inputDirectory>${project.build.directory}/staged-site</inputDirectory>
            </configuration>
          </plugin>
        </plugins>
      </build>

      <distributionManagement>
        <site>
          <id>opendaylight-site</id>
          <url>${nexus.site.url}</url>
        </site>
      </distributionManagement>
    </project>

Note that the above file is a dummy file who's only purpose is to push a
staged-site to Nexus as well as being the trigger for odlparent to activate
site generation for your project.

## <a name="fix_site_urls">Fix Maven Site URLs</a>

The maven-site-plugin assumes projects are configured in a certain Maven way
which unfortunately ODL is not configured as such. This causes the site plugin
to generate invalid URLs to all project modules. The workaround for this is to
pass both a url and a distribution.site.url for every single module in your
Maven project. The following template should be placed into all modules in an
ODL project with the exception of the root pom (this will be explained below the
example).

    <!--
        Maven Site Configuration

        The following configuration is necessary for maven-site-plugin to
        correctly identify the correct deployment path for OpenDaylight Maven
        sites.
    -->
    <url>${odl.site.url}/${project.groupId}/${stream}/${project.artifactId}/</url>

    <distributionManagement>
      <site>
        <id>opendaylight-site</id>
        <url>${nexus.site.url}/${project.artifactId}/</url>
      </site>
    </distributionManagement>

**Note:** For the project root pom.xml remove the final path
"${project.artifactId}" from both URLs. This is so that the root project
can represent the root index page for your project when the Maven Site is
deployed.

## <a name="customize_site">Customize project site</a>

In the root pom of your project you can create customized site including
creating your own project Documentation via markdown (or other formats)
using the maven-site-plugin. Please refer to Maven Site documentation for
how to customize your site.

The general documentation can be found here
https://maven.apache.org/plugins/maven-site-plugin/

The following pages are particularly useful regarding creating your site
content:

  * https://maven.apache.org/plugins/maven-site-plugin/examples/creating-content.html
  * https://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html

## <a name="aggregate_apidocs">Aggregating Java apidocs</a>

Javadoc is generated automatically for each bundle however to aggregate them
all into a single convenient url we need to ensure that the root pom has a
profile to activate it. The following should be copied into the profiles
section of the project root pom.

    <profiles>
      <profile>
        <!--
            This profile is to ensure we only build javadocs reports
            when we plan to deploy Maven site for our project.
        -->
        <id>maven-site</id>
        <activation>
          <file>
            <exists>${user.dir}/deploy-site.xml</exists>
          </file>
        </activation>

        <build>
          <plugins>
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-javadoc-plugin</artifactId>
              <inherited>false</inherited>
              <executions>
                <execution>
                  <id>aggregate</id>
                  <goals>
                    <goal>aggregate</goal>
                  </goals>
                  <phase>package</phase>
              </execution>
              </executions>
            </plugin>
          </plugins>
        </build>
      </profile>
    </profiles>
