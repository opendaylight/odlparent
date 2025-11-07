.. _odl-parent-developer-guide:

Developing with ODL Parent
==========================

Parent POMs
-----------

Overview
~~~~~~~~

The ODL Parent component for OpenDaylight provides a number of Maven
parent POMs which allow Maven projects to be easily integrated in the
OpenDaylight ecosystem. Technically, the aim of projects in OpenDaylight
is to produce Karaf features, and these parent projects provide common
support for the different types of projects involved.

These parent projects are:

-  ``odlparent-lite`` — the basic parent POM for Maven modules which
   don’t produce artifacts (*e.g.* aggregator POMs)

-  ``odlparent`` — the common parent POM for Maven modules containing
   Java code

-  ``bnd-parent`` — the parent POM for Maven modules producing OSGi bundles
   the modern way via ``bnd-maven-plugin``

-  ``bundle-parent`` — the parent POM for Maven modules producing OSGi
   bundles the old-school way via ``maven-bundle-plugin``

-  ``single-feature-parent`` — the parent POM for Maven modules producing
   a single Karaf 4 feature by employing ``karaf-maven-plugin``'s feature
   generation facility

-  ``template-feature-parent`` — the parent POM for Maven modules producing
   a single Karaf 4 feature by filling out a template with build-time
   expansions

-  ``feature-repo-parent`` — the parent POM for Maven modules producing
   Karaf 4 feature repositories

-  ``karaf4-parent`` — the parent POM for Maven modules producing Karaf 4
   distributions

-  ``karaf-dist-static`` - the parent POM for Maven modules producing Karaf 4
   static distributions

odlparent-lite
~~~~~~~~~~~~~~

This is the base parent for all OpenDaylight Maven projects and
modules. It provides the following, notably to allow publishing
artifacts to Maven Central:

-  license information;

-  organization information;

-  issue management information (a link to our Bugzilla);

-  continuous integration information (a link to our Jenkins setup);

-  default Maven plugins (``maven-clean-plugin``,
   ``maven-deploy-plugin``, ``maven-install-plugin``,
   ``maven-javadoc-plugin`` with HelpMojo support,
   ``maven-project-info-reports-plugin``, ``maven-site-plugin`` with
   Asciidoc support, ``jdepend-maven-plugin``);

-  distribution management information.

It also defines a few profiles which help during development:

-  ``q`` (``-Pq``), the quick profile, which disables tests, code
   coverage, Javadoc generation, code analysis, etc. — anything which
   is not necessary to build the bundles and features (see `this blog
   post <http://blog2.vorburger.ch/2016/06/improve-maven-build-speed-with-q.html>`__
   for details). This profile is safe to execute in parallel via ``mvnd``.

-  ``f`` (``-Pf``), the fast profile, which is a subset of the quick profile,
   leaving more things enabled, among which are checkstyle, source jars and
   unit tests. This profile is mostly safe to execute in parallel via ``mvnd``,
   but some unit tests may fail due to resource conflicts. Such failures can
   be overcome via restarting the build via ``mvnd -r``, perhaps forcing it
   to execute single-threaded mode via ``mvnd -1``.

-  ``addInstallRepositoryPath``
   (``-DaddInstallRepositoryPath=…/karaf/system``) which can be used to
   drop a bundle in the appropriate Karaf location, to enable
   hot-reloading of bundles during development (see `this blog
   post <http://blog2.vorburger.ch/2016/06/maven-install-into-additional.html>`__
   for details).

For modules which don’t produce any useful artifacts (*e.g.* aggregator
POMs), you should add the following to avoid processing artifacts:

::

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-install-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
        </plugins>
    </build>

odlparent
~~~~~~~~~

This inherits from ``odlparent-lite`` and mainly provides dependency and
plugin management for OpenDaylight projects.

If you use any of the following libraries, you should rely on
``odlparent`` to provide the appropriate versions:

-  Apache Commons:

   -  ``commons-codec``

   -  ``commons-io``

   -  ``commons-lang3``

-  Guava

-  JAX-RS with Jersey

-  JSON processing:

   -  GSON

   -  Jackson

-  Logging:

   -  Logback

   -  SLF4J

-  Netty

-  OSGi:

   -  Apache Felix

   -  core OSGi dependencies (``core``, ``compendium``\ …)

-  Testing:

   -  Hamcrest

   -  JSON assert

   -  JUnit

   -  Mockito

   -  Pax Exam

-  XML/XSL:

   -  Xerces

   -  XML APIs

.. note::

    This list isn’t exhaustive. It’s also not cast in stone; if you’d
    like to add a new dependency (or migrate a dependency), please
    contact `the mailing
    list <https://lists.opendaylight.org/mailman/listinfo/odlparent-dev>`__.

``odlparent`` also enforces some Checkstyle verification rules. In
particular, it enforces the common license header used in all
OpenDaylight code:

::

    /*
     * Copyright © ${year} ${holder} and others.  All rights reserved.
     *
     * This program and the accompanying materials are made available under the
     * terms of the Eclipse Public License v1.0 which accompanies this distribution,
     * and is available at http://www.eclipse.org/legal/epl-v10.html
     */

where “\ ``${year}``\ ” is initially the first year of publication, then
(after a year has passed) the first and latest years of publication,
separated by commas (*e.g.* “2014, 2016”), and “\ ``${holder}``\ ” is
the initial copyright holder (typically, the first author’s employer).
“All rights reserved” is optional.

If you need to disable this license check, *e.g.* for files imported
under another license (EPL-compatible of course), you can override the
``maven-checkstyle-plugin`` configuration. ``features-test`` does this
for its ``CustomBundleUrlStreamHandlerFactory`` class, which is
ASL-licensed:

::

    <plugin>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <executions>
            <execution>
                <id>check-license</id>
                <goals>
                    <goal>check</goal>
                </goals>
                <phase>process-sources</phase>
                <configuration>
                    <configLocation>check-license.xml</configLocation>
                    <headerLocation>EPL-LICENSE.regexp.txt</headerLocation>
                    <includeResources>false</includeResources>
                    <includeTestResources>false</includeTestResources>
                    <sourceDirectory>${project.build.sourceDirectory}</sourceDirectory>
                    <excludes>
                        <!-- Skip Apache Licensed files -->
                        org/opendaylight/odlparent/featuretest/CustomBundleUrlStreamHandlerFactory.java
                    </excludes>
                    <failsOnError>false</failsOnError>
                    <consoleOutput>true</consoleOutput>
                </configuration>
            </execution>
        </executions>
    </plugin>

It also defines a few profiles which control static analysis:

- ``ep`` (``-Pep``) which can be enables `Error Prone <https://errorprone.info/>`__
   with a OpenDaylight-specific policy which is different from Error Prone
   default in a few respects. This policy can be tweaked in a downstream
   ``pom.xml`` (and via Maven ``-D`` facility) via the following properties:

   - ``odl.ef.fork`` controlling ``maven-compiler-plugin`` fork mode. It
     defaults to ``true``, which is the safe default. It can be set to
     ``false`` if the Maven JVM is known to be launched with the ``--add-exports``
     and ``--add-opens`` required by Error Prone, significanly reducing
     the impact on CPU usage/build time. Users are advised to add the corresponding
     configuration to their ``.mvn/jvm.config`` and then add ``-Dodl.ep.fork=false``
     to their ``.mvn/maven.config``.

   - ``odl.ep.var`` controlling the `Var <https://errorprone.info/bugpattern/Var>`__
     checker. It defaults to ``OFF``, which matches Java semantics. Other
     accepted values are ``WARN`` and ``ERROR``. opting into ``@Var``-annotated
     world.

   - ``odl.ep.extra`` which is can be used to pass additional arguments
     to Error Prone, in the form ``-Xep:Foo:OFF`` and similar. Note that
     this facility can also be used to override the default policy,
     by specifying the new severity for an already-defined check. For
     example, ``-Dodl.ep.var=ERROR -Dodl.ep.extra=-Xep:Var:OFF`` will
     result in ``Var`` checker to be disabled.

Furthermore, it defines the following profiles:

- ``dagger`` (``-Pdagger``) which enableis `Dagger <https://dagger.dev>`__

bnd-parent
~~~~~~~~~~

This inherits from ``odlparent`` and enables functionality useful for
OSGi bundles:

-  ``maven-javadoc-plugin`` is activated, to build the Javadoc JAR;

-  ``maven-source-plugin`` is activated, to build the source JAR;

-  various OSGi annotation-only dependencies are declared with ``provided``
   scope

-  `bnd-maven-plugin <https://github.com/bndtools/bnd/blob/7.1.0/maven-plugins/bnd-maven-plugin/README.md>`__
   is activated to derive corresponding OSGi metadata as well the bundle
   MANIFEST.

The default configuration includes provision for reproducible non-SNAPSHOT
builds. The recommended way of modifying the plugin configuration is through
``bnd.bnd``.

WARNING: when using this parent, please make sure to properly fill out ``<name/>``
and ``<description/>`` elements in your ``pom.xml``, as these are used
propagated to resulting bundle's metadata. Users would be quite confused
by seeing these to be set to values inherited from ``bnd-parent``!


bundle-parent
~~~~~~~~~~~~~

This inherits from ``odlparent`` and enables functionality useful for
OSGi bundles:

-  ``maven-javadoc-plugin`` is activated, to build the Javadoc JAR;

-  ``maven-source-plugin`` is activated, to build the source JAR;

-  `maven-bundle-plugin <https://felix.apache.org/documentation/_attachments/components/bundle-plugin/index.html>`__
   is activated (including extensions), to build OSGi bundles
   (using the “bundle” packaging).

In addition to this, JUnit is included as a default dependency in “test”
scope.

features-parent
~~~~~~~~~~~~~~~

This inherits from ``odlparent`` and enables functionality useful for
Karaf features:

-  ``karaf-maven-plugin`` is activated, to build Karaf features — but
   for OpenDaylight, projects need to use “jar” packaging (**not**
   “feature” or “kar”);

-  ``features.xml`` files are processed from templates stored in
   ``src/main/features/features.xml``;

-  Karaf features are tested after build to ensure they can be activated
   in a Karaf container.

The ``features.xml`` processing allows versions to be ommitted from
certain feature dependencies, and replaced with “\ ``{{version}}``\ ”.
For example:

::

    <features name="odl-mdsal-${project.version}" xmlns="http://karaf.apache.org/xmlns/features/v1.2.0"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://karaf.apache.org/xmlns/features/v1.2.0 http://karaf.apache.org/xmlns/features/v1.2.0">

        <repository>mvn:org.opendaylight.odlparent/features-odlparent/{{VERSION}}/xml/features</repository>

        [...]
        <feature name='odl-mdsal-broker-local' version='${project.version}' description="OpenDaylight :: MDSAL :: Broker">
            <feature version='${yangtools.version}'>odl-yangtools-common</feature>
            <feature version='${mdsal.version}'>odl-mdsal-binding-dom-adapter</feature>
            <feature version='${mdsal.model.version}'>odl-mdsal-models</feature>
            <feature version='${project.version}'>odl-mdsal-common</feature>
            <feature version='${config.version}'>odl-config-startup</feature>
            <feature version='${config.version}'>odl-config-netty</feature>
            <feature version='[3.3.0,4.0.0)'>odl-lmax</feature>
            [...]
            <bundle>mvn:org.opendaylight.controller/sal-dom-broker-config/{{VERSION}}</bundle>
            <bundle start-level="40">mvn:org.opendaylight.controller/blueprint/{{VERSION}}</bundle>
            <configfile finalname="${config.configfile.directory}/${config.mdsal.configfile}">mvn:org.opendaylight.controller/md-sal-config/{{VERSION}}/xml/config</configfile>
        </feature>

As illustrated, versions can be ommitted in this way for repository
dependencies, bundle dependencies and configuration files. They must be
specified traditionally (either hard-coded, or using Maven properties)
for feature dependencies.

karaf-parent
~~~~~~~~~~~~

This allows building a Karaf 3 distribution, typically for local testing
purposes. Any runtime-scoped feature dependencies will be included in the
distribution, and the ``karaf.localFeature`` property can be used to
specify the boot feature (in addition to ``standard``).

single-feature-parent
~~~~~~~~~~~~~~~~~~~~~

This inherits from ``odlparent`` and enables functionality useful for
Karaf 4 features:

-  ``karaf-maven-plugin`` is activated, to build Karaf features, typically
   with “feature” packaging (“kar” is also supported);

-  ``feature.xml`` files are generated based on the compile-scope dependencies
   defined in the POM, optionally initialised from a stub in
   ``src/main/feature/feature.xml``.

-  Karaf features are tested after build to ensure they can be activated
   in a Karaf container.

The ``feature.xml`` processing adds transitive dependencies by default, which
allows features to be defined using only the most significant dependencies
(those that define the feature); other requirements are determined
automatically as long as they exist as Maven dependencies.

“configfiles” need to be defined both as Maven dependencies (with the
appropriate type and classifier) and as ``<configfile>`` elements in the
``feature.xml`` stub.

Other features which a feature depends on need to be defined as Maven
dependencies with type “xml” and classifier “features” (note the plural here).

template-feature-parent
~~~~~~~~~~~~~~~~~~~~~~~

This profiles in most regards the same way as ``single-feature-parent``, but
rather than generating ``feature.xml`` via ``karaf-maven-plugin``, they process
a pre-existing ``src/main/feature/template.xml`` file using
``template-feature-plugin``.

This approach allows tight control over the resulting ``feature.xml``,
as only non-critical components like ``<details>`` are generated and all
capabilities of a ``<feature>`` definition can be utilized.

In addition to the usual ``${foo}`` Maven substitutions, more powerful
mustache-substitutions are expanded from Maven project:

-  ``{{versionAsInProject}}`` resolves to the corresponding dependency version

-  ``{{semVerRange}}`` resolves to the semantic version interval with lower
   bound being the corresponding dependency version (including) and the next
   major version (excluding)

-  ``{{projectVersion}}`` is a legacy alias for ``${project.version}``

This allows for powerful handling of dependencies between features, for example
as done between ``odl-stax2-api`` and ``odl-woodstox``.

The power of this templating approach can be witnessed in ``odl-netty-4``
feature, which deploys platform-specific bundles based on the actual run-time
being used.

feature-repo-parent
~~~~~~~~~~~~~~~~~~~

This inherits from ``odlparent`` and enables functionality useful for
Karaf 4 feature repositories. It follows the same principles as
``single-feature-parent``, but is designed specifically for repositories
and should be used only for this type of artifacts.

It builds a feature repository referencing all the (feature) dependencies
listed in the POM.

karaf4-parent
~~~~~~~~~~~~~

This allows building a Karaf 4 distribution, typically for local testing
purposes. Any runtime-scoped feature dependencies will be included in the
distribution, and the ``karaf.localFeature`` property can be used to
specify the boot feature (in addition to ``standard``).

karaf-dist-static
~~~~~~~~~~~~~~~~~

This allows building a kind of immutable static distribution by adding
this as a parent to your project's pom.xml. This pom file defines the static
karaf framework alongside common OpenDaylight's components(branding,
bouncycastle items, etc). The major difference to the dynamic distribution is
that validation of features dependencies happens during the build phase and
all of the dependencies are installed as *"reference:file:url"* into the
*"etc/startup.properties"*. Static distribution might be the right choice when
you need to to produce a lightweight and immutable package for your deployment.
You can find a ``test-static`` project that inherits from ``karaf-dist-static``
and demonstrates how this parent can be used.

Generally speaking, to build a static distribution with selected for your
purposes features, you have to follow the next two steps:

1. Add features you want to be included in distribution under the
   dependencies block.

.. code:: xml

    <dependencies>
        <dependency>
            <groupId>org.opendaylight.odlparent</groupId>
            <artifactId>odl-dropwizard-metrics</artifactId>
            <version>${project.version}</version>
            <type>xml</type>
            <classifier>features</classifier>
        </dependency>
        <dependency>
            <groupId>org.opendaylight.odlparent</groupId>
            <artifactId>odl-gson</artifactId>
            <version>${project.version}</version>
            <type>xml</type>
            <classifier>features</classifier>
        </dependency>
    </dependencies>

2. Put additional configuration for the karaf-maven-plugin about these features:

.. code:: xml

            <plugin>
                <groupId>org.apache.karaf.tooling</groupId>
                <artifactId>karaf-maven-plugin</artifactId>
                <extensions>true</extensions>
                <configuration>
                    <startupFeatures combine.children="append">
                        <feature>shell</feature>
                    </startupFeatures>
                    <bootFeatures combine.children="append">
                        <feature>odl-dropwizard-metrics</feature>
                        <feature>odl-gson</feature>
                    </bootFeatures>
                </configuration>
            </plugin>

.. note::  If you need to add something from the default karaf features
           (like ``shell`` feature in our example), you should use
           **<startupFeatures>** block, and not forget about
           **combine.children="append"** attribute. Everything else can
           be added to the bootFeatures block.


**Known issues**

* An issue with FeatureDeploymentListener.bundleChanged and NPE records in
  log files. More details available here:
  https://issues.apache.org/jira/browse/KARAF-6612

* Some of the features might try to update configuration files, but that's
  not supported by static distribution, so StaticConfigurationImpl.update
  will throw UnsupportedOperationException.

Features (for Karaf 3)
----------------------

The ODL Parent component for OpenDaylight provides a number of Karaf 3
features which can be used by other Karaf 3 features to use certain
third-party upstream dependencies.

These features are:

-  Akka features (in the ``features-akka`` repository):

   -  ``odl-akka-all`` — all Akka bundles;

   -  ``odl-akka-scala-2.11`` — Scala runtime for OpenDaylight;

   -  ``odl-akka-system-2.4`` — Akka actor framework bundles;

   -  ``odl-akka-clustering-2.4`` — Akka clustering bundles and
      dependencies;

   -  ``odl-akka-leveldb-0.7`` — LevelDB;

   -  ``odl-akka-persistence-2.4`` — Akka persistence;

-  general third-party features (in the ``features-odlparent``
   repository):

   -  ``odl-netty-4`` — all Netty bundles;

   -  ``odl-guava-18`` — Guava 18;

   -  ``odl-guava-21`` — Guava 21 (not indended for use in Carbon);

   -  ``odl-lmax-3`` — LMAX Disruptor;

   -  ``odl-triemap-0.2`` — Concurrent Trie HashMap.

To use these, you need to declare a dependency on the appropriate
repository in your ``features.xml`` file:

::

    <repository>mvn:org.opendaylight.odlparent/features-odlparent/{{VERSION}}/xml/features</repository>

and then include the feature, *e.g.*:

::

    <feature name='odl-mdsal-broker-local' version='${project.version}' description="OpenDaylight :: MDSAL :: Broker">
        [...]
        <feature version='[3.3.0,4.0.0)'>odl-lmax</feature>
        [...]
    </feature>

You also need to depend on the features repository in your POM:

::

    <dependency>
        <groupId>org.opendaylight.odlparent</groupId>
        <artifactId>features-odlparent</artifactId>
        <classifier>features</classifier>
        <type>xml</type>
    </dependency>

assuming the appropriate dependency management:

::

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.opendaylight.odlparent</groupId>
                <artifactId>odlparent-artifacts</artifactId>
                <version>1.8.0-SNAPSHOT</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

(the version number there is appropriate for Carbon). For the time being
you also need to depend separately on the individual JARs as
compile-time dependencies to build your dependent code; the relevant
dependencies are managed in ``odlparent``'s dependency management.

| The suggested version ranges are as follows:

-  ``odl-netty``: ``[4.0.37,4.1.0)`` or ``[4.0.37,5.0.0)``;

-  ``odl-guava``: ``[18,19)`` (if your code is ready for it, ``[19,20)``
   is also available, but the current default version of Guava in
   OpenDaylight is 18);

-  ``odl-lmax``: ``[3.3.4,4.0.0)``

Features (for Karaf 4)
----------------------

There are equivalent features to all the Karaf 3 features, for Karaf 4.
The repositories use “features4” instead of “features”, and the features
use “odl4” instead of “odl”.

The following new features are specific to Karaf 4:

-  Karaf wrapper features (also in the ``features4-odlparent``
   repository) — these can be used to pull in a Karaf feature
   using a Maven dependency in a POM:

   -  ``odl-karaf-feat-feature`` — the Karaf ``feature`` feature;

   -  ``odl-karaf-feat-jdbc`` — the Karaf ``jdbc`` feature;

   -  ``odl-karaf-feat-jetty`` — the Karaf ``jetty`` feature;

   -  ``odl-karaf-feat-war`` — the Karaf ``war`` feature.

To use these, all you need to do now is add the appropriate dependency
in your feature POM; for example:

::

    <dependency>
        <groupId>org.opendaylight.odlparent</groupId>
        <artifactId>odl4-guava-18</artifactId>
        <classifier>features</classifier>
        <type>xml</type>
    </dependency>

assuming the appropriate dependency management:

::

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.opendaylight.odlparent</groupId>
                <artifactId>odlparent-artifacts</artifactId>
                <version>1.8.0-SNAPSHOT</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

(the version number there is appropriate for Carbon). We no longer use version
ranges, the feature dependencies all use the ``odlparent`` version (but you
should rely on the artifacts POM).

Automating feature template migration
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This repository includes a `small sh  helper script <migrate-feature.sh>`_ which prepares
``src/main/feature/template.xml`` files and updates feature modules for use with
``template-feature-parent``.

What it does
^^^^^^^^^^^^
* Optionally builds the repo once with ``mvn clean install -Pq`` (triggered by ``--build``).
* Copies each generated ``target/feature/feature.xml`` to
  ``src/main/feature/template.xml`` (keeps the original in ``target/``).
* Normalizes the XML prolog and injects the copyright header
  (header immediately follows the ``<?xml …?>`` declaration).
* Rewrites versions inside the **template.xml**:

  * All non-``<configfile>`` Maven coords → ``{{versionAsInProject}}``.
  * ``<configfile>… mvn:…/<version>/ …</configfile>`` → ``${project.version}``.
  * Dependency ``<feature version="…">``:

    * ranges like ``[14,15)`` → ``version="{{semVerRange}}"``
    * exact versions → ``version="{{versionAsInProject}}"``

* Cleans up the **template.xml**:

  * Removes ``description`` and ``version`` attributes from the top-level ``<feature name="…">``.
  * Removes any ``<details>…</details>`` lines.
  * Removes ``prerequisite="…"`` and ``dependency="…"`` attributes from dependency ``<feature>`` lines.

- Updates each module’s ``pom.xml`` parent:

  * ``<artifactId>feature-parent</artifactId>`` → ``template-feature-parent``
  * ``<relativePath>../feature-parent[…]</relativePath>`` → ``../parent[…]``
  * add ``<description/>`` if no exists

How to run
^^^^^^^^^^
From the ``features/`` directory:

.. code-block:: bash

   # from features/
   ./migrate-feature.sh
   ./migrate-feature.sh --build   # run mvn once before migration


Notes
^^^^^
- The script accepts exactly one optional parameter: ``--build``.
  If provided, it runs ``mvn clean install -Pq`` **once before** migration.

- Rebuild to verify:

  .. code-block:: bash

     mvn clean install
