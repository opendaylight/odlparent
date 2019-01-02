========================
ODL Parent release notes
========================

Version 4.0.3
-------------

This is a bug-fix and minor upstream bump upgrade from version 4.0.2.

Bug fixes
~~~~~~~~~

* Our FindBugs configuration for JDK 9 and later caused the plugin to run
  everywhere; instead, this version defines the ``findbugs.skip`` property to
  disable the plugin in modules where it would be used otherwise.

* The PowerMock declarations in dependency management missed
  ``powermock-api-mockito2``, which is necessary for modules using PowerMock
  with Mockito 2.

* The “quick” profile (``-Pq``) now skips SpotBugs.

* JSR-305 annotations are now optional, which fixes a number of issues when
  building with newer JDKs.

* We provide JAXB with JDK 11 and later (where it is no longer provided by the
  base platform).

* ``odlparent-artifacts`` has been updated to accurately represent the
  artifacts provided.

* ``javax.activation`` is now excluded from generated features (it’s provided
  on Karaf’s boot classpath).

* When the build is configured to build Karaf distributions in ``tar.gz``
  archives, but not ``zip`` archives, ``features-test`` used to fail; it will
  now used whichever is available
  (`ODLPARENT-174 <https://jira.opendaylight.org/browse/ODLPARENT-174>`__).

* Explicit GCs are disabled by default, so that calls to ``System.gc()`` are
  ignored
  (`ODLPARENT-175 <https://jira.opendaylight.org/browse/ODLPARENT-175>`__).

* Null checks are disabled in SpotBugs because of bad interactions with newer
  annotations and the bytecode produces by JDK 11 and later for
  ``try``-with-resources.

* Akka Persistence expects LevelDB 0.10, so we now pull in that version
  instead of 0.7.

Dependency convergence
~~~~~~~~~~~~~~~~~~~~~~

A number of dependencies have been added or constrained so that projects using
this parent can enforce dependency convergence:

* Karaf’s ``framework`` feature is used as an import POM, so that we converge
  by default on the versions used in Karaf.

* The following dependencies have been added to dependency management:

  * ``commons-beanutils``
  * the Checker Framework
  * Error Prone annotations
  * ``javax.activation``
  * ``xml-apis``

New features
~~~~~~~~~~~~

The following Karaf features have been added:

* ``odl-antlr4`` (providing ``antlr4-runtime``);

* ``odl-gson`` (providing ``gson``);

* ``odl-jersey-2`` (providing Jersey client, server, and container servlet,
  along with the necessary feature dependencies);

* ``odl-servlet-api`` (providing ``javax.servlet-api``);

* ``odl-stax2-api`` (providing ``stax2-api``);

* ``odl-ws-rs-api`` (providing ``javax.ws.rs-api``);

A new ``sonar-jacoco-aggregate`` profile can be used to produce Sonar reports
with aggregated JaCoCo reports. Additionally, Sonar builds (run with
``-Dsonar``) are detected and run with a number of irrelevant plugins disabled.

Upstream version upgrades
~~~~~~~~~~~~~~~~~~~~~~~~~

* Akka 2.5.14 → 2.5.19 (and related ``ssl-config``, Aeron and Agrona upgrades):

  * `2.5.15 <https://akka.io/blog/news/2018/08/24/akka-2.5.15-released>`__.
  * `2.5.16 <https://akka.io/blog/news/2018/08/29/akka-2.5.16-security-fix-released>`__.
  * `2.5.17 <https://akka.io/blog/news/2018/09/27/akka-2.5.17-released>`__.
  * `2.5.18 <https://akka.io/blog/news/2018/10/07/akka-2.5.18-released>`__.
  * `2.5.19 <https://akka.io/blog/news/2018/12/07/akka-2.5.19-released>`__.

* Commons Text `1.4 → 1.6 <http://www.apache.org/dist/commons/text/RELEASE-NOTES.txt>`__.

* Eclipse JDT annotations 2.2.0 → 2.2.100.

* Javassist 3.23.1 → 3.24.0.

* Karaf 4.2.1 → 4.2.2, with related upgrades.

* LMAX Disruptor `3.4.1 → 3.4.2 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.2>`__.

* Mockito `2.20.1 → 2.23.4 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md>`__.

* Netty 4.1.29 → 4.1.31:

  * `4.1.30 <https://netty.io/news/2018/09/28/4-1-30-Final.html>`__.
  * `4.1.31 <https://netty.io/news/2018/10/30/4-1-31-Final.html>`__.

* Pax Exam 4.12.0 → 4.13.1.

* Scala 2.12.6 → 2.12.8:

  * `2.12.7 <https://github.com/scala/scala/releases/tag/v2.12.7>`__.
  * `2.12.8 <https://github.com/scala/scala/releases/tag/v2.12.8>`__.

* Wagon HTTP 3.1.0 → 3.2.0.

* Xtend `2.14.0 → 2.16.0 <https://www.eclipse.org/xtend/releasenotes.html>`__.

Plugin version upgrades
~~~~~~~~~~~~~~~~~~~~~~~

* Asciidoctor `1.5.6 → 1.5.7.1 <https://github.com/asciidoctor/asciidoctor-maven-plugin/releases>`__
  (with related AsciidoctorJ upgrades).

* Bundle 4.0.0 → 4.1.0.

* Checkstyle `8.12 → 8.15 <https://checkstyle.org/releasenotes.html#Release_8.13>`__.

* DependencyCheck `3.3.2 → 4.0.0 <https://github.com/jeremylong/DependencyCheck/blob/master/RELEASE_NOTES.md>`__.

* Failsafe / Surefire `2.22.0 → 2.22.1 <https://blogs.apache.org/maven/entry/apache-maven-surefire-plugin-version1>`__.

* Help 3.1.0 → 3.1.1.

* JAR 3.1.0 → 3.1.1.

* PMD `3.10.0 → 3.11.0 <https://blogs.apache.org/maven/entry/apache-maven-pmd-plugin-3>`__.

* Remote Resources `1.5 → 1.6.0 <https://blogs.apache.org/maven/entry/apache-maven-remote-resources-plugin>`__.

* Shade
  `3.2.0 → 3.2.1 <https://blog.soebes.de/blog/2018/11/12/apache-maven-shade-plugin-version-3-dot-2-1-released/>`__.

* SpotBugs `3.1.6 → 3.1.9 <https://github.com/spotbugs/spotbugs/blob/release-3.1/CHANGELOG.md>`__.

* XBean finder 4.9 → 4.12.

* XTend 2.14.0 → 2.16.0.

Version 4.0.2
-------------

This is a bug-fix and minor upstream bump upgrade from version 4.0.1.

Bug fixes
~~~~~~~~~

Previous releases overrode Karaf’s ``jre.properties``; this is no longer
necessary, and was causing failures with Java 9 and later (our version of
``jre.properties`` didn’t have the appropriate settings for anything after
Java 8). This release drops that override. See
`ODLPARENT-168 <https://jira.opendaylight.org/browse/ODLPARENT-168>`__ for
details.

Upstream version upgrades
~~~~~~~~~~~~~~~~~~~~~~~~~

* Commons Lang `3.8 → 3.8.1 <http://www.apache.org/dist/commons/lang/RELEASE-NOTES.txt>`__.

* Jackson `2.9.6 → 2.9.7 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.7>`__.

* Netty `4.1.28 → 4.1.29 <http://netty.io/news/2018/08/24/4-1-29-Final.html>`__.

Plugin version upgrades
~~~~~~~~~~~~~~~~~~~~~~~

* JAR `3.0.2 → 3.1.0 <https://blog.soebes.de/blog/2018/04/10/apache-maven-jar-plugin-version-3-dot-1-dot-0-released>`__.

* Javadoc `3.0.0 → 3.0.1 <https://blogs.apache.org/maven/entry/apache-maven-javadoc-plugin-version>`__.

* Jersey `2.22.2 → 2.25.1 <https://jersey.github.io/release-notes/2.25.html>`__,
  along with Glassfish JSON 1.0.4 → 1.1.2.

* Plugin 3.5 → 3.5.2:

  * `3.5.1 <https://blog.soebes.de/blog/2018/01/22/apache-maven-plugin-tools-version-3-dot-5-1-released/>`__.
  * `3.5.2 <https://blog.soebes.de/blog/2018/05/26/apache-mave-plugin-tools-version-3-dot-5-2-released/>`__.

* Resources `3.0.1 → 3.1.0 <https://blogs.apache.org/maven/entry/apache-maven-resources-plugin-version>`__.

Version 4.0.1
-------------

This is a bug-fix and minor upstream bump upgrade from version 4.0.0.

Bug fixes
~~~~~~~~~

The JaCoCo execution profile was incorrect, breaking Sonar; the report is now
written correctly, so that Sonar can find it.

The Blueprint Maven plugin fails when it encounters Java 9 classes; this is
fixed by forcefully upgrading its dependency on xbean-finder. See
`ODLPARENT-167 <https://jira.opendaylight.org/browse/ODLPARENT-167>`__ for
details.

Upstream version upgrades
~~~~~~~~~~~~~~~~~~~~~~~~~

* SpotBugs `3.1.6 → 3.1.7 <https://github.com/spotbugs/spotbugs/blob/release-3.1/CHANGELOG.md>`__.

Upstream version additions
~~~~~~~~~~~~~~~~~~~~~~~~~~

* Mockito Inline is added alongside Mockito Core, to ensure that the versions
  are kept in sync.

Plugin version upgrades
~~~~~~~~~~~~~~~~~~~~~~~

* Clean `3.0.0 → 3.1.0 <https://blog.soebes.de/blog/2018/04/14/apache-maven-clean-plugin-version-3-dot-1-0-released/>`__.

* Compiler `3.7.0 → 3.8.0 <https://blog.soebes.de/blog/2018/07/30/apache-maven-compiler-plugin-version-3-dot-8-0-released/>`__.

* Dependency 3.0.2 → 3.1.1:

  * `3.1.0 <https://blog.soebes.de/blog/2018/04/06/apache-maven-dependency-plugin-version-3-dot-1-0-released/>`__.
  * `3.1.1 <https://blog.soebes.de/blog/2018/05/24/apache-maven-dependency-plugin-version-3-dot-1-1-released/>`__.

* Dependency Check `3.3.1 → 3.3.2 <https://github.com/jeremylong/DependencyCheck/blob/master/RELEASE_NOTES.md>`__.

* Enforcer `3.0.0-M1 → 3.0.0-M2 <https://mail-archives.apache.org/mod_mbox/maven-announce/201806.mbox/%3Cop.zko9b2vhkdkhrr%40desktop-2khsk44.dynamic.ziggo.nl%3E>`__.

* Failsafe 2.20.1 → 2.22:

  * `2.21 <https://blog.soebes.de/blog/2018/03/06/apache-maven-surefire-plugin-version-2-dot-21-released/>`__.
  * `2.22 <https://blog.soebes.de/blog/2018/06/16/apache-maven-surefire-plugin-version-2-dot-22-released/>`__.

* Help 2.2 → 3.1.0:

  * `3.0.0 <https://blog.soebes.de/blog/2018/03/18/apache-maven-help-plugin-version-3-dot-0-0-released/>`__.
  * `3.0.1 <https://blog.soebes.de/blog/2018/03/28/apache-maven-help-plugin-version-3-dot-0-1-released/>`__.
  * `3.1.0 <https://blog.soebes.de/blog/2018/06/09/apache-maven-help-plugin-version-3-dot-1-0-released/>`__.

* Invoker 2.0.0 → 3.1.0:

  * `3.0.0 <https://blog.soebes.de/blog/2017/05/24/apache-maven-invoker-plugin-version-3-dot-0-0-released/>`__.
  * `3.1.0 <https://blog.soebes.de/blog/2018/05/31/apache-maven-invoker-plugin-version-3-dot-1-0-released/>`__.

* JAR `3.0.2 → 3.1.0 <https://blog.soebes.de/blog/2018/04/10/apache-maven-jar-plugin-version-3-dot-1-dot-0-released/>`__.

* Project Info Reports `2.9 → 3.0.0 <https://blog.soebes.de/blog/2018/06/27/apache-maven-project-info-reports-plugin-3-dot-0-0-released/>`__.

* Resources `3.0.1 → 3.1.0 <https://blog.soebes.de/blog/2018/05/01/apache-maven-resources-plugin-version-3-dot-1-0-released/>`__.

* Shade `3.1.0 → 3.2.0 <https://blog.soebes.de/blog/2018/09/13/apache-maven-shade-plugin-version-3-dot-2-0-released/>`__.

* Site `3.7 → 3.7.1 <https://blog.soebes.de/blog/2018/04/29/apache-maven-site-plugin-version-3-dot-7-1-released/>`__.

* Surefire 2.18.1 → 2.22.0:

  * `2.19 <https://blog.soebes.de/blog/2015/10/19/apache-maven-surefire-plugin-version-2-dot-19-released/>`__.
  * `2.19.1 <https://blog.soebes.de/blog/2016/01/03/apache-maven-surefire-plugin-version-2-dot-19-dot-1-released/>`__.
  * `2.20 <https://blog.soebes.de/blog/2017/04/12/apache-maven-surefire-plugin-version-2-dot-20-released/>`__.
  * `2.21 <https://blog.soebes.de/blog/2018/03/06/apache-maven-surefire-plugin-version-2-dot-21-released/>`__.
  * `2.22 <https://blog.soebes.de/blog/2018/06/16/apache-maven-surefire-plugin-version-2-dot-22-released/>`__.


Version 4.0.0
-------------

This is a major upgrade from version 3, with breaking changes; projects will
need to make changes to upgrade to this version.

`This Wiki page <https://wiki.opendaylight.org/view/Neon_platform_upgrade>`_
has detailed step-by-step migration instructions.

ODL Parent 4 requires Maven 3.5.3 or later; this is needed in particular to
enable SpotBugs support with current versions of the SpotBugs plugin.

Known issues
~~~~~~~~~~~~

This release’s SpotBugs support doesn’t handle Guava 25.1 correctly, resulting
in false-positives regarding null handling; see
`ODLPARENT-161 <https://jira.opendaylight.org/browse/ODLPARENT-161>`_ for
details. Until this is fixed, the corresponding warnings are disabled, which
matches our existing FindBugs configuration (which suffers from the a variant
of this, with the same consequences).

We are planning on upgrading Akka during the 4.x cycle, even if it results in
a technically breaking upgrade. This is currently blocked on an OSGi bug in
Akka; see `Akka issue 25579 <https://github.com/akka/akka/issues/25579>`_ for
details.

Blueprint and OSGi service handling
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Previous releases used an OpenDaylight-specific directory for Blueprint XML
files, ``org/opendaylight/blueprint``. It turned out this wasn’t useful, so
version 4 uses the default directory, ``OSGI-INF/blueprint``.

The Maven bundle plugin is now configured to omit the ``Import-Service`` and
``Export-Service`` headers, since they are deprecated, unnecessary in
OpenDaylight, and liable to cause issues.

With previous releases of OpenDaylight, projects were encouraged to use Pax
CDI API annotations to describe their Blueprint beans, services and injections;
with version 4, Blueprint annotations should be used instead:

* modules should depend on
  ``org.apache.aries.blueprint:blueprint-maven-plugin-annotation``, with the
  ``<optional>true</optional>`` flag, instead of
  ``org.ops4j.pax.cdi:pax-cdi-api``;

* ``@OsgiServiceProvider`` on bean definitions is replaced by ``@Service``;

* ``@OsgiService`` at injection points is replaced by ``@Reference``;

* ``@OsgiService`` on bean definitions, while technically wrong, can be seen in
  the OpenDaylight codebase; this is replaced by ``@Service``;

* service lists can be injected using ``@ReferenceList``.

See `this Gerrit patch <https://git.opendaylight.org/gerrit/75699>`_ for an
example.

Compiler settings
~~~~~~~~~~~~~~~~~

Builds now warn about unchecked type uses (such as raw types where generics
are available).

JUnit and Mockito are always available as test dependencies and no longer need
to be declared in POMs.

New build profiles
~~~~~~~~~~~~~~~~~~

An OWASP profile is now available to run OWASP’s dependency checker; this will
check all third-party dependencies against the NVD vulnerability database. To
enable this, run Maven with ``-Powasp``.

Build profile changes
~~~~~~~~~~~~~~~~~~~~~

``-Pq`` now skips Modernizer.

New features
~~~~~~~~~~~~

``odl-akka-leveldb-0.10`` wraps LevelDB 0.10 for Akka.

``odl-apache-commons-codec`` wraps Apache Commons Codec.

``odl-apache-commons-lang3`` wraps Apache Commons Lang 3.

``odl-apache-commons-net`` wraps Apache Commons Net.

``odl-apache-commons-text`` wraps Apache Commons Text.

``odl-apache-sshd`` wraps Apache SSHD.

``odl-guava`` provides the default ODL version of Guava; it should be used
instead of ``odl-guava-23`` or the new ``odl-guava-25``.

``odl-jackson-2.9`` wraps Jackson 2.9.

New FindBugs and SpotBugs settings
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

FindBugs and SpotBugs are configured with
`the SLF4J extension <http://kengotoda.github.io/findbugs-slf4j/>`_ (version
1.4.0 for FindBugs, 1.4.1 for SpotBugs). This will flag misused SLF4J calls, in
particular message templates which don’t match the arguments, and invalid
placeholders (*e.g.* ``%s`` instead of ``{}``).

Deleted artifacts
~~~~~~~~~~~~~~~~~

``aggregator-parent`` was unusable outside ``odlparent`` and has been removed.
Instead, the ``maven.deploy.skip`` and ``maven.install.skip`` properties are
available to disable deploying and installing artifacts.

Upstream version upgrades
~~~~~~~~~~~~~~~~~~~~~~~~~

This version upgrades the following third-party dependencies:

* Aeron 1.7.0 → 1.9.3:

  * `1.8.0 <https://github.com/real-logic/aeron/releases/tag/1.8.0>`_.
  * `1.8.1 <https://github.com/real-logic/aeron/releases/tag/1.8.1>`_.
  * `1.8.2 <https://github.com/real-logic/aeron/releases/tag/1.8.2>`_.
  * `1.9.0 <https://github.com/real-logic/aeron/releases/tag/1.9.0>`_.
  * `1.9.1 <https://github.com/real-logic/aeron/releases/tag/1.9.1>`_.
  * `1.9.2 <https://github.com/real-logic/aeron/releases/tag/1.9.2>`_.
  * `1.9.3 <https://github.com/real-logic/aeron/releases/tag/1.9.3>`_.

* Agrona 0.9.12 → 0.9.21:

  * `0.9.13 <https://github.com/real-logic/agrona/releases/tag/0.9.13>`_.
  * `0.9.14 <https://github.com/real-logic/agrona/releases/tag/0.9.14>`_.
  * `0.9.15 <https://github.com/real-logic/agrona/releases/tag/0.9.15>`_.
  * `0.9.16 <https://github.com/real-logic/agrona/releases/tag/0.9.16>`_.
  * `0.9.17 <https://github.com/real-logic/agrona/releases/tag/0.9.17>`_.
  * `0.9.18 <https://github.com/real-logic/agrona/releases/tag/0.9.18>`_.
  * `0.9.19 <https://github.com/real-logic/agrona/releases/tag/0.9.19>`_.
  * `0.9.20 <https://github.com/real-logic/agrona/releases/tag/0.9.20>`_.
  * `0.9.21 <https://github.com/real-logic/agrona/releases/tag/0.9.21>`_.

* Akka 2.5.11 → 2.5.14:

  * `2.5.12 <https://akka.io/blog/news/2018/04/13/akka-2.5.12-released>`_.
  * `2.5.13 <https://akka.io/blog/news/2018/06/08/akka-2.5.13-released>`_.
  * `2.5.14 <https://akka.io/blog/news/2018/07/13/akka-2.5.14-released>`_.

* ASM 5.1 → 6.2.1 (synchronised with Karaf).

* Bouncy Castle `1.59 → 1.60 <https://www.bouncycastle.org/releasenotes.html>`_.

* Checkstyle `8.4 → 8.12 <http://checkstyle.sourceforge.net/releasenotes.html#Release_8.12>`_.

* Commons Lang `3.7 → 3.8 <http://www.apache.org/dist/commons/lang/RELEASE-NOTES.txt>`_.

* Commons Text 1.1 → 1.4:

  * `1.2 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.2.txt>`_.
  * `1.3 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.3.txt>`_.
  * `1.4 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.4.txt>`_.

* Eclipse JDT annotations 2.1.150 → 2.2.0.

* EclipseLink Moxy JAXB `2.7.1 → 2.7.3 <https://www.eclipse.org/eclipselink/releases/2.7.php>`_.

* Enunciate core annotations
  `2.10.1 → 2.11.1 <https://github.com/stoicflame/enunciate/releases>`_.

* Felix Metatype 1.1.6 → 1.2.0 (synchronised with Karaf).

* Google Truth `0.40 → 0.42 <https://github.com/google/truth/releases>`_.

* Guava 23.6.1 → 25.1:

  * `23.4 <https://github.com/google/guava/releases/tag/v23.4>`_.
  * `23.5 <https://github.com/google/guava/releases/tag/v23.5>`_.
  * `23.6 <https://github.com/google/guava/releases/tag/v23.6>`_.
  * `24.0 <https://github.com/google/guava/releases/tag/v24.0>`_.
  * `24.1 <https://github.com/google/guava/releases/tag/v24.1>`_.
  * `25.0 <https://github.com/google/guava/releases/tag/v25.0>`_.
  * `25.1 <https://github.com/google/guava/releases/tag/v25.1>`_.

* Immutables `2.5.6 → 2.7.1 <https://github.com/immutables/immutables/blob/master/README.md#changelog>`_.

* Jackson 2.8.9 → 2.9.6:

  * `2.9 feature overview <https://medium.com/@cowtowncoder/jackson-2-9-features-b2a19029e9ff>`_.
  * `2.9 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9>`_.
  * `2.9.1 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.1>`_.
  * `2.9.2 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.2>`_.
  * `2.9.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.3>`_.
  * `2.9.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.4>`_.
  * `2.9.5 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.5>`_.
  * `2.9.6 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.6>`_.

* JaCoCo `0.8.1 → 0.8.2 <https://github.com/jacoco/jacoco/releases/tag/v0.8.2>`_.

* Javassist 3.22.0 → 3.23.1. This provides compatibility with Java 9 and later,
  and `fixes a file handle leak <https://github.com/jboss-javassist/javassist/issues/165>`_.

* Jettison 1.3.8 → 1.4.0.

* Jetty 9.3.21 → 9.4.11 (synchronised with Karaf):

  * `9.4.0 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00097.html>`_.
  * `9.4.1 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00100.html>`_.
  * `9.4.2 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00101.html>`_.
  * `9.4.3 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00102.html>`_.
  * `9.4.4 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00105.html>`_.
  * `9.4.5 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00107.html>`_.
  * `9.4.6 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00109.html>`_.
  * `9.4.7 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00111.html>`_.
  * `9.4.8 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00114.html>`_.
  * `9.4.9 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00117.html>`_.
  * `9.4.10 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00119.html>`_.
  * `9.4.11 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00122.html>`_.

* Jolokia OSGi `1.5.0 → 1.6.0 <https://jolokia.org/changes-report.html#a1.6.0>`_.

* Karaf 4.1.5 → 4.2.1:

  * `4.1.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342748>`_.
  * `4.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342945>`_.

* LMAX Disruptor 3.3.10 → 3.4.1:

  * `3.4.0 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.0>`_.
  * `3.4.1 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.1>`_.

* META-INF services 1.7 → 1.8.

* Mockito 1.10.19 → 2.20.1; see
  `What’s new in Mockito 2 <https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2>`_
  for upgrade instructions and
  `the list of issues you might run into <https://asolntsev.github.io/en/2016/10/11/mockito-2.1/>`_.

* Netty 4.1.22 → 4.1.28:

  * `4.1.17 <http://netty.io/news/2017/11/08/4-0-53-Final-4-1-17-Final.html>`_.
  * `4.1.18 <http://netty.io/news/2017/12/11/4-0-54-Final-4-1-18-Final.html>`_.
  * `4.1.19 <http://netty.io/news/2017/12/18/4-1-19-Final.html>`_.
  * `4.1.20 <http://netty.io/news/2018/01/22/4-0-55-Final-4-1-20-Final.html>`_.
  * `4.1.21 <http://netty.io/news/2018/02/05/4-0-56-Final-4-1-21-Final.html>`_.
  * `4.1.22 <http://netty.io/news/2018/02/21/4-1-22-Final.html>`_.
  * `4.1.23 <http://netty.io/news/2018/04/04/4-1-23-Final.html>`_.
  * `4.1.24 <http://netty.io/news/2018/04/19/4-1-24-Final.html>`_.
  * `4.1.25 <http://netty.io/news/2018/05/14/4-1-25-Final.html>`_.
  * `4.1.26 <http://netty.io/news/2018/07/10/4-1-26-Final.html>`_.
  * `4.1.27 <http://netty.io/news/2018/07/11/4-1-27-Final.html>`_.
  * `4.1.28 <http://netty.io/news/2018/07/27/4-1-28-Final.html>`_.

* Pax Exam 4.11.0 → 4.12.0.

* Pax URL 2.5.3 → 2.5.4, which only fixes
  `a potential NullPointerException <https://ops4j1.jira.com/browse/PAXURL-346>`_.

* PowerMock 1.6.4 → 1.7.4:

  * `1.6.5 <https://github.com/powermock/powermock/releases/tag/powermock-1.6.5>`_.
  * `1.6.6 <https://github.com/powermock/powermock/releases/tag/powermock-1.6.6>`_.
  * `1.7.0 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.0>`_.
  * `1.7.1 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.1>`_.
  * `1.7.2 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.2>`_.
  * `1.7.3 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.3>`_.
  * `1.7.4 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.4>`_.

* Scala parser combinators 1.0.7 → 1.1.1:

  * `1.1.0 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.1.0>`_.
  * `1.1.1 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.1.1>`_.

* SpotBugs `3.1.0 → 3.1.6 <https://github.com/spotbugs/spotbugs/blob/3.1.6/CHANGELOG.md>`_.

* Threeten Extra `1.3.2 → 1.4 <https://github.com/ThreeTen/threeten-extra/releases>`_.

* Typesafe SSL config 0.2.2 → 0.2.4:

  * `0.2.3 <https://github.com/lightbend/ssl-config/releases/tag/v0.2.3>`_.
  * `0.2.4 <https://github.com/lightbend/ssl-config/releases/tag/v0.2.4>`_.

* Wagon HTTP
  `2.10 → 3.1.0 <https://lists.apache.org/thread.html/96024c54db7680697cb066e22a37b0ed5b4498386714a8a9ae1ec9cd@%3Cannounce.maven.apache.org%3E>`_.

* XMLUnit `1.6 → 2.6.2 <https://github.com/xmlunit/xmlunit/blob/master/RELEASE_NOTES.md>`_.

Upstream version additions
~~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been added to dependency management:

* Apache SSHD 2.0.0, with EdDSA and Netty support (EdDSA is provided by ``net.i2p.crypto:eddsa``).

* Blueprint annotations (``org.apache.aries.blueprint:blueprint-maven-plugin-annotation``).

* Log4J2.

* Pax Web 7.2.3 (synchronised with Karaf).

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been removed from dependency management:

* Google Protobuf.

* Our repackaging of Jersey Servlet.

* JUnit’s ``junit-dep``, which has long been obsolete.

* LevelDB (which is still available as features).

* Pax CDI API — Blueprint annotations should be used instead.

Plugin version upgrades
~~~~~~~~~~~~~~~~~~~~~~~

The following plugins have been upgraded:

* Blueprint 1.4.0 → 1.10.0.

* Build helper 1.12 → 3.0.0.

* Bundle plugin 3.5.0 → 4.0.0.

* Checkstyle
  `2.17 → 3.0.0 <https://mail-archives.apache.org/mod_mbox/maven-announce/201801.mbox/%3Cop.zchs68akkdkhrr%40desktop-2khsk44.mshome.net%3E>`_.

* Duplicate finder
  `1.2.1 → 1.3.0 <https://github.com/basepom/duplicate-finder-maven-plugin/blob/master/CHANGES.md>`_.

* Git commit id `2.2.4 → 2.2.5 <https://github.com/ktoso/maven-git-commit-id-plugin/releases/tag/v2.2.5>`_.

* Jacoco Maven plugin `0.8.1 → 0.8.2 <https://github.com/jacoco/jacoco/releases/tag/v0.8.2>`_.

* Javadoc `3.0.0 → 3.0.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12342283>`_.

* PMD 3.8 → 3.10.0:

  * `3.10.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?version=12342689&styleName=Text&projectId=12317621>`_.

* Sevntu `1.29.0 → 1.32.0 <http://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.32.0>`_.

* SpotBugs 3.1.0-RC6 → 3.1.6 (see the SpotBugs changes above).

Version 3.1.3
-------------

This version fixes the following issues:

* `ODLPARENT-156 <https://jira.opendaylight.org/browse/ODLPARENT-156>`_:
  ``xtend-maven-plugin``’s dependencies end up pulling in conflicting
  dependencies. ODL Parent now constrains part of its dependency tree to avoid
  this.

This version adds ``odl-jackson-2.8`` to ``odlparent-artifacts``.

Version 3.1.2
-------------

This version fixes the following issues:

* `INFRAUTILS-41 <https://jira.opendaylight.org/browse/INFRAUTILS-41>`_:
  ``jre.properties`` includes ``com.sun.management`` so that it can be
  enabled if necessary. (This doesn’t add a dependency on
  ``com.sun.management``, it allows bundles to use it if it is present.)

* `ODLPARENT-136 <https://jira.opendaylight.org/browse/ODLPARENT-136>`_:
  ``SingleFeatureTest`` pulls in ``org.osgi.compendium``.

* `ODLPARENT-144 <https://jira.opendaylight.org/browse/ODLPARENT-144>`_:
  ``org.apache.karaf.scr.management`` is whitelisted so that it no longer
  affects ``SingleFeatureTest``.

* `ODLPARENT-146 <https://jira.opendaylight.org/browse/ODLPARENT-146>`_:
  null-related FindBugs checks which produce false-positives with Guava 23.6
  and later are disabled, so that this really is fully backwards-compatible
  with 3.0 and later.

* `ODLPARENT-148 <https://jira.opendaylight.org/browse/ODLPARENT-148>`_:
  ``SingleFeatureTest`` preserves ``target/SFT/karaf.log``.

This version includes the following improvements:

* ``custom.properties`` no longer includes OVSDB-specific configuration.

* The ``odl-jersey-1`` feature includes the Jersey client.

* Redundant bundle dependency declarations in ``SingleFeatureTest`` have been
  removed (these are declarations which are also present in our base Karaf
  distribution).

* Build errors involving invalid feature or bundle URLs now indicate which
  feature is at fault.

* Obsolete Log4J overrides have been removed from ``SingleFeatureTest``.

When building using JDK 9 or 10, the default settings have been changed as
follows to avoid errors or extraneous warnings:

* SFT is disabled (it needs Karaf 4.2 or later);

* Javadocs are generated as HTML 4;

* SpotBugs is disabled on JDK 10 or later;

* FindBugs is disabled on JDK 9 or later.

The following third-party dependencies have been upgraded:

* `EclipseLink Moxy JAXB 2.6.2 → 2.7.1 <https://www.eclipse.org/eclipselink/releases/2.7.php>`_.

* `Google Truth 0.36 → 0.40 <https://github.com/google/truth/releases>`_.

* `Gson 2.8.2 → 2.8.5 <https://github.com/google/gson/blob/master/CHANGELOG.md>`_.

* `Guava 23.6 → 23.6.1 <https://github.com/google/guava/compare/v23.6...v23.6.1>`_.
  This addresses CVE-2018-10237 (that’s the only change in this release).

* `Jacoco Maven plugin 0.8.0 → 0.8.1 <https://github.com/jacoco/jacoco/releases/tag/v0.8.1>`_.

* JDT annotations 2.1.0 → 2.1.150.

* `Scala 2.12.5 → 2.12.6 <https://github.com/scala/scala/releases/tag/v2.12.6>`_.

* `Scala Parser Combinators 1.0.6 → 1.0.7 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.0.7>`_.

* `Sevntu 1.24.2 → 1.29.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.29.0>`_.

* `Xtext and Xtend 2.13.0 → 2.14.0 <https://github.com/eclipse/xtext/blob/website-master/xtend-website/_posts/releasenotes/2018-05-23-version-2-14-0.md>`_.

The following Maven plugin has been upgraded:

* `Javadoc 3.0.0 → 3.0.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12342283>`_.

Version 3.1.1
-------------

This version fixes the following issues:

* `ODLPARENT-137 <https://jira.opendaylight.org/browse/ODLPARENT-137>`_:
  restore the OpenDaylight prompt.

* `ODLPARENT-146 <https://jira.opendaylight.org/browse/ODLPARENT-146>`_:
  Guava 23.6 switched from @Nullable to @NullableDecl, which causes false
  positives in FindBugs’ ``NP_NONNULL_PARAM_VIOLATION`` rule; we’re
  disabling the rule for now.

Version 3.1.0
-------------

This version fixes the following issues:

* Mycila dependencies are now “compile” scoped rather than “test”; this allows
  child projects to use Guice with Mycila more easily.

* The duplicate finder now ignores ``web.xml`` and BluePrint XML files.

This version includes the following improvements:

* The ``-Pq`` profile skips Maven Modernizer, in preparation for its future
  integration (and its use in child projects).

* An OWASP profile, ``-Powasp`` is available for vulnerability checking.

* A new ``odl-jackson-2.8`` feature provides Jackson 2.8 to child projects.

The following third-party dependencies have been added to dependency management:

* `ThreeTen-Extra <http://www.threeten.org/threeten-extra/>`_

The following third-party dependencies have been upgraded:

* Aeron 1.2.5 → 1.7.0; release notes:

  * `1.3.0 <https://github.com/real-logic/aeron/releases/tag/1.3.0>`_
  * `1.4.0 <https://github.com/real-logic/aeron/releases/tag/1.4.0>`_
  * `1.5.0 <https://github.com/real-logic/aeron/releases/tag/1.5.0>`_
  * `1.5.1 <https://github.com/real-logic/aeron/releases/tag/1.5.1>`_
  * `1.5.2 <https://github.com/real-logic/aeron/releases/tag/1.5.2>`_
  * `1.6.0 <https://github.com/real-logic/aeron/releases/tag/1.6.0>`_
  * `1.7.0 <https://github.com/real-logic/aeron/releases/tag/1.7.0>`_

* Agrona 0.9.5 → 0.9.12; release notes:

  * `0.9.6 <https://github.com/real-logic/Agrona/releases/tag/0.9.6>`_
  * `0.9.7 <https://github.com/real-logic/Agrona/releases/tag/0.9.7>`_
  * `0.9.8 <https://github.com/real-logic/Agrona/releases/tag/0.9.8>`_
  * `0.9.9 <https://github.com/real-logic/Agrona/releases/tag/0.9.9>`_
  * `0.9.10 <https://github.com/real-logic/Agrona/releases/tag/0.9.10>`_
  * `0.9.11 <https://github.com/real-logic/Agrona/releases/tag/0.9.11>`_
  * `0.9.12 <https://github.com/real-logic/Agrona/releases/tag/0.9.12>`_

* Akka 2.5.5 → 2.5.11; release notes:

  * `2.5.6 <https://akka.io/blog/news/2017/09/28/akka-2.5.6-released>`_
  * `2.5.7 <https://akka.io/blog/news/2017/11/17/akka-2.5.7-released>`_
  * `2.5.8 <https://akka.io/blog/news/2017/12/08/akka-2.5.8-released>`_
  * `2.5.9 <https://akka.io/blog/news/2018/01/11/akka-2.5.9-released-2.4.x-end-of-life>`_
  * `2.5.10 <https://akka.io/blog/news/2018/02/23/akka-2.5.10-released>`_
  * `2.5.11 <https://akka.io/blog/news/2018/02/28/akka-2.5.11-released>`_

* Commons Lang 3 `3.6 → 3.7 <http://www.apache.org/dist/commons/lang/RELEASE-NOTES.txt>`_

* Guava 23.3 → 23.6; release notes:

  * `23.4 <https://github.com/google/guava/releases/tag/v23.4>`_
  * `23.5 <https://github.com/google/guava/releases/tag/v23.5>`_
  * `23.6 <https://github.com/google/guava/releases/tag/v23.6>`_

* H2 database `1.4.193 → 1.4.196 <http://www.h2database.com/html/changelog.html>`_

* Jacoco `0.7.9 → 0.8.0 <https://github.com/jacoco/jacoco/releases/tag/v0.8.0>`_

* Javassist `3.21.0 → 3.22.0 <https://github.com/jboss-javassist/javassist/compare/rel_3_21_0_ga...rel_3_22_0_ga>`_

* lmax-disruptor 3.3.7 → 3.3.10; release notes:

  * `3.3.8 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.3.8>`_
  * `3.3.9 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.3.9>`_
  * `3.3.10 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.3.10>`_

* Netty 4.1.16 → 4.1.22; release notes:

  * `4.1.17 <http://netty.io/news/2017/11/08/4-0-53-Final-4-1-17-Final.html>`_
  * `4.1.18 <http://netty.io/news/2017/12/11/4-0-54-Final-4-1-18-Final.html>`_
  * `4.1.19 <http://netty.io/news/2017/12/18/4-1-19-Final.html>`_
  * `4.1.20 <http://netty.io/news/2018/01/22/4-0-55-Final-4-1-20-Final.html>`_
  * `4.1.21 <http://netty.io/news/2018/02/05/4-0-56-Final-4-1-21-Final.html>`_
  * `4.1.22 <http://netty.io/news/2018/02/21/4-1-22-Final.html>`_

* Scala `2.12.4 → 2.12.5 <http://www.scala-lang.org/news/2.12.5>`_

* Typesafe Config `0.2.1 → 0.2.2 <https://github.com/typesafehub/config/blob/master/NEWS.md>`_

The following Maven plugins have been upgraded:

* FindBugs 3.0.4 → 3.0.5

* Git commit id 2.2.2 → 2.2.4; release notes:

  * `2.2.3 <https://github.com/ktoso/maven-git-commit-id-plugin/releases/tag/v2.2.3>`_
  * `2.2.4 <https://github.com/ktoso/maven-git-commit-id-plugin/releases/tag/v2.2.4>`_

Version 3.0.3
-------------

This version fixes the following issues:

* `ODLPARENT-136`_: ``features-test`` needs ``org.osgi.compendium``.

* Jackson dependencies are declared using ``jackson-bom`` to ensure all they
  remain consistent.

* ``find-duplicate-classpath-entries`` is run in the “verify” phase rather than
  the “validate” phase, which is too early.

* The version of Jetty we pull in is now aligned with that declared in Karaf,
  resolving a number of restart and dependency issues.

* Pulling in the ``wrap`` feature unconditionally is no longer necessary, so
  ``karaf4-parent`` no longer does so.

* ``metainf-services`` are declared with scope “provided” to avoid their being
  included in downstream features (it’s a build-time dependency only).

* ``leveldb-api`` is excluded from ``odl-akka-leveldb-0.7``, and ``jsr250-api``
  from ``enunciate-core-annotations``, to avoid duplicate having classes on the
  classpath.

* Since the ``ssh`` feature is excluded from generated features, our Karaf
  need to enable it at boot in all cases.

* ``bundle-test-lib`` is now a bundle.

* Since we use static SLF4J loggers, the ``SLF4J_LOGGER_SHOULD_BE_NON_STATIC``
  rule needs to be disabled in our FindBugs configuration (this allows
  downstream projects to enable ``findbugs-slf4j`` without having to deal with
  all the resulting false-positives).

* ``org.apache.karaf.scr.management`` is white-listed in SFT to avoid failures
  apparently related to that component (which we don’t care about).

.. _ODLPARENT-136: https://jira.opendaylight.org/browse/ODLPARENT-136

This version upgrades the following third-party dependencies:

* `Antlr 4.7 → 4.7.1`_

* `BouncyCastle 1.58 → 1.59`_

* Jersey 1.17 → 1.19.4 (additionally available as the ``odl-jersey-1`` feature)

* Jolokia 1.3.7 → 1.5.0

* Karaf 4.1.3 → 4.1.5; release notes:

  * `Karaf 4.1.4`_
  * `Karaf 4.1.5`_

.. _Antlr 4.7 → 4.7.1: https://github.com/antlr/antlr4/releases/tag/4.7.1
.. _BouncyCastle 1.58 → 1.59: https://www.bouncycastle.org/releasenotes.html
.. _Karaf 4.1.4: https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12341702
.. _Karaf 4.1.5: https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342294

Version 3.0.2
-------------

This version fixes the following issues:

* SingleFeatureTest uses the configured local Maven repository for Pax Exam.

* JavaDoc links are disabled for now to `speed up builds`_. A new
  ``javadoc-links`` profile enables the links.

* Conditional feature dependencies are processed, ensuring our
  `distribution is complete`_.

* Startup features are `adjusted for Karaf 4.1`_, avoiding unnecessary
  refreshes.

* The ``hiddenField`` Checkstyle check is disabled for abstract methods.

* The default logging configuration uses Log4J2, which is the new default in
  Karaf 4.1.

.. _speed up builds: https://jira.opendaylight.org/browse/ODLPARENT-121
.. _distribution is complete: https://jira.opendaylight.org/browse/ODLPARENT-133
.. _adjusted for Karaf 4.1: https://jira.opendaylight.org/browse/ODLPARENT-134

This version upgrades the following dependencies or plugins:

* ``maven-enforcer-plugin`` 1.4.1 → 3.0.0-M1

* ``maven-javadoc-plugin`` 3.0.0-M1 → 3.0.0

Version 3.0.1
-------------

This version fixes the following issues:

* Karaf pulls in an invalid Hibernate feature repository, breaking downstream
  dependencies pulling in the “war” feature. ``populate-local-repo`` corrects
  the repository dependency.


Version 3.0.0
-------------

Compiler settings
~~~~~~~~~~~~~~~~~

Build now show compiler warnings and deprecation warnings. This doesn't affect
the result or require any changes currently, it just makes the issues more
visible.

New Checkstyle rules
~~~~~~~~~~~~~~~~~~~~

Checkstyle has been upgraded from 7.6 to 8.4 (see the
`Checkstyle release notes`_ for details), and Sevntu from 1.21.0 to 1.24.2
(note that the latter's group identifier changed from
``com.github.sevntu.checkstyle`` to ``com.github.sevntu-checkstyle``; you
might need to update your IDE's configuration).

The following Checkstyle rules are enabled; this might require changes in
projects which enforce Checkstyle validation:

* `AvoidHidingCauseExceptionCheck`_
* `FinalClass`_: utility classes must be declared ``final``
* `HiddenField`_: fields must not be shadowed
* `HideUtilityClassConstructor`_: utility classes must hide their constructor

.. _Checkstyle release notes: http://checkstyle.sourceforge.net/releasenotes.html

.. _AvoidHidingCauseExceptionCheck: http://sevntu-checkstyle.github.io/sevntu.checkstyle/apidocs/com/github/sevntu/checkstyle/checks/coding/AvoidHidingCauseExceptionCheck.html
.. _FinalClass: http://checkstyle.sourceforge.net/config_design.html#FinalClass
.. _HiddenField: http://checkstyle.sourceforge.net/config_coding.html#HiddenField
.. _HideUtilityClassConstructor: http://checkstyle.sourceforge.net/config_design.html#HideUtilityClassConstructor

Karaf
~~~~~

Karaf has been upgraded to 4.1.3. This should be transparent for dependent
projects.

Karaf distributions
~~~~~~~~~~~~~~~~~~~

* When building a Karaf distribution using ``karaf4-parent``, projects can
  specify which archives to build: the ``karaf.archiveZip`` property will
  enable ZIP files if true, and ``karaf.archiveTarGz`` will enable
  gzip-compressed tarballs if true. By default both are enabled.

* Our Karaf distribution provides Bouncy Castle at startup. Auto-generated
  feature descriptors take this into account (they won't embed a Bouncy
  Castle dependency).

Feature removals
~~~~~~~~~~~~~~~~

* The ``odl-triemap-0.2`` feature wrapping
  ``com.github.romix:java-concurrent-hash-trie-map`` was rendered obsolete by
  YANG Tools' implementation and has been removed.

Feature additions
~~~~~~~~~~~~~~~~~

* ``odl-javassist-3`` provides Javassist in a feature.

* ``odl-jung-2.1`` provides `JUNG`_ in a feature.

.. _JUNG: http://jung.sourceforge.net/

Upstream version upgrades
~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been upgraded:

* Akka 2.4.18 → 2.5.4; release notes:

  * `Akka 2.5.0`_
  * `Akka 2.5.1`_
  * `Akka 2.5.2`_
  * `Akka 2.5.3`_
  * `Akka 2.5.4`_

* `Awaitility 2 → 3`_

* `Bouncy Castle 1.57 → 1.58`_

* `Commons Codec 1.10 → 1.11`_

* `Commons File Upload 1.3.2 → 1.3.3`_

* `Commons IO 2.5 → 2.6`_

* Eclipse JDT annotations 2.0.0 → 2.1.0

* Felix Dependency Manager 4.3.0 → 4.4.1
* Felix Dependency Manager Shell 4.0.4 → 4.0.6
* Felix Metatype 1.1.2 → 1.1.6

* `Google Truth 0.28 → 0.36`_ (with the Java 8 extensions)

* `Gson 2.7 → 2.8.2`_

* Guava 22 → 23.3 along with the associated feature name change from
  ``odl-guava-22`` to ``odl-guava-23`` (dependent packages *must* change their
  dependency); release notes:

  * `Guava 23`_
  * `Guava 23.1`_
  * `Guava 23.2`_
  * `Guava 23.3`_

* Immutables 2.4.2 → 2.5.6

* Jackson 2.3.2 → 2.8.9

* Jacoco 0.7.7 → 0.7.9; release notes:

  * `Jacoco 0.7.8`_
  * `Jacoco 0.7.9`_

* Jacoco Listeners 2.4 → 3.8

* `Javassist 3.20.0 → 3.21.0`_

* `Jettison 1.3.7 → 1.3.8`_

* `Jolokia 1.3.6 → 1.3.7`_

* `JSONassert 1.3.0 → 1.5.0`_

* `logback 1.2.2 → 1.2.3`_

* `LMAX Disruptor 3.3.6 → 3.3.7`_

* Netty 4.1.8 → 4.1.16; release notes:

  * `Netty 4.1.9`_
  * `Netty 4.1.10`_
  * `Netty 4.1.11`_
  * `Netty 4.1.12`_
  * `Netty 4.1.13`_
  * `Netty 4.1.14`_
  * `Netty 4.1.15`_
  * `Netty 4.1.16`_

* `Pax URL 2.5.2 → 2.5.3`_

* Scala 2.11.11 → 2.12.4; release notes:

  * `Scala 2.12.0`_
  * `Scala 2.12.1`_
  * `Scala 2.12.2`_
  * `Scala 2.12.3`_
  * `Scala 2.12.4`_

* Servlet API 3.0.1 → 3.1.0

* `SLF4J 1.7.21 → 1.7.25`_

* `webcohesion enunciate 2.6.0 → 2.10.1`_

* `Xtend 2.12 → 2.13`_

.. _Akka 2.5.0: http://akka.io/blog/news/2017/04/13/akka-2.5.0-released
.. _Akka 2.5.1: http://akka.io/blog/news/2017/05/02/akka-2.5.1-released
.. _Akka 2.5.2: http://akka.io/blog/news/2017/05/24/akka-2.5.2-released
.. _Akka 2.5.3: http://akka.io/blog/news/2017/06/19/akka-2.5.3-released
.. _Akka 2.5.4: http://akka.io/blog/news/2017/08/10/akka-2.5.4-released

.. _Awaitility 2 → 3: https://github.com/awaitility/awaitility/wiki/ReleaseNotes30

.. _Bouncy Castle 1.57 → 1.58: https://www.bouncycastle.org/releasenotes.html

.. _Commons Codec 1.10 → 1.11: http://www.apache.org/dist/commons/codec/RELEASE-NOTES.txt

.. _Commons File Upload 1.3.2 → 1.3.3: http://www.apache.org/dist/commons/fileupload/RELEASE-NOTES.txt

.. _Commons IO 2.5 → 2.6: http://www.apache.org/dist/commons/io/RELEASE-NOTES.txt

.. _Google Truth 0.28 → 0.36: https://github.com/google/truth/releases

.. _Gson 2.7 → 2.8.2: https://github.com/google/gson/blob/master/CHANGELOG.md

.. _Guava 23: https://github.com/google/guava/wiki/Release23
.. _Guava 23.1: https://github.com/google/guava/releases/tag/v23.1
.. _Guava 23.2: https://github.com/google/guava/releases/tag/v23.2
.. _Guava 23.3: https://github.com/google/guava/releases/tag/v23.3

.. _Jacoco 0.7.8: https://github.com/jacoco/jacoco/releases/tag/v0.7.8
.. _Jacoco 0.7.9: https://github.com/jacoco/jacoco/releases/tag/v0.7.9

.. _Javassist 3.20.0 → 3.21.0: https://github.com/jboss-javassist/javassist/compare/rel_3_20_0_ga...rel_3_21_0_ga

.. _Jettison 1.3.7 → 1.3.8: https://github.com/jettison-json/jettison/compare/jettison-1.3.7...jettison-1.3.8

.. _Jolokia 1.3.6 → 1.3.7: https://github.com/rhuss/jolokia/releases/tag/v1.3.7

.. _JSONassert 1.3.0 → 1.5.0: https://github.com/skyscreamer/JSONassert/releases

.. _logback 1.2.2 → 1.2.3: https://logback.qos.ch/news.html

.. _LMAX Disruptor 3.3.6 → 3.3.7: https://github.com/LMAX-Exchange/disruptor/releases/tag/3.3.7

.. _Netty 4.1.9: http://netty.io/news/2017/03/10/4-0-45-Final-4-1-9-Final.html
.. _Netty 4.1.10: http://netty.io/news/2017/04/30/4-0-46-Final-4-1-10-Final.html
.. _Netty 4.1.11: http://netty.io/news/2017/05/12/4-0-47-Final-4-1-11-Final.html
.. _Netty 4.1.12: http://netty.io/news/2017/06/09/4-0-48-Final-4-1-12-Final.html
.. _Netty 4.1.13: http://netty.io/news/2017/07/06/4-0-49-Final-4-1-13-Final.html
.. _Netty 4.1.14: http://netty.io/news/2017/08/03/4-0-50-Final-4-1-14-Final.html
.. _Netty 4.1.15: http://netty.io/news/2017/08/25/4-0-51-Final-4-1-15-Final.html
.. _Netty 4.1.16: http://netty.io/news/2017/09/25/4-0-52-Final-4-1-16-Final.html

.. _Pax URL 2.5.2 → 2.5.3: https://ops4j1.jira.com/browse/PAXURL-345?jql=project%20%3D%20PAXURL%20AND%20fixVersion%20%3D%202.5.3

.. _Scala 2.12.0: https://github.com/scala/scala/releases/tag/v2.12.0
.. _Scala 2.12.1: https://github.com/scala/scala/releases/tag/v2.12.1
.. _Scala 2.12.2: https://github.com/scala/scala/releases/tag/v2.12.2
.. _Scala 2.12.3: https://github.com/scala/scala/releases/tag/v2.12.3
.. _Scala 2.12.4: https://github.com/scala/scala/releases/tag/v2.12.4

.. _SLF4J 1.7.21 → 1.7.25: https://www.slf4j.org/news.html

.. _webcohesion enunciate 2.6.0 → 2.10.1: https://github.com/stoicflame/enunciate/releases

.. _Xtend 2.12 → 2.13: https://www.eclipse.org/xtend/releasenotes.html

Upstream version additions
~~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been added to dependency management:

* Commons Text, ``org.apache.commons:commons-text`` (this will allow downstreams
  to migrate from ``commons-lang3``\’s ``WordUtils``, which is deprecated)

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been removed from dependency
management (they are obsolete and unused):

* Chameleon MBeans
* Eclipse Link
* Equinox HTTP service bridge
* ``equinoxSDK381`` artifacts
* Coda Hale Metrics, which are mostly unused and should eventually be wrapped
  by InfraUtils
* ``com.google.code.findbugs:jsr305`` (which *must not* be used; this is
  enforced — ``annotations`` should be used instead)
* Felix File Install and Web Console
* Gemini Web
* Orbit
* ``org.mockito:mockito-all`` (which *must not* be used; this is enforced —
  ``mockito-core`` should be used instead)
* Spring Framework
* ``txw2``
* Xerces
* ``xml-apis``

Plugin version upgrades
~~~~~~~~~~~~~~~~~~~~~~~

The following plugins have been upgraded:

* ``org.apache.servicemix.tooling:depends-maven-plugin`` 1.3.1 → 1.4.0
* ``org.apache.felix:maven-bundle-plugin`` 2.4.0 → 3.3.0
* ``maven-compiler-plugin`` 3.6.1 → 3.7.0
* ``maven-dependency-plugin`` 3.0.1 → 3.0.2
* ``maven-enforcer-plugin`` 1.4.1 → 3.0.0-M1
* ``maven-failsafe-plugin`` 2.18.1 → 2.20.1
* ``maven-javadoc-plugin`` 2.10.4 → 3.0.0-M1
* ``maven-shade-plugin`` 2.4.3 → 3.1.0

New plugins
~~~~~~~~~~~

* The `Maven Find Duplicates`_ plugin can be enabled by setting the
  ``duplicate-finder.skip`` property to ``false``.

* The SpotBugs_ Maven plugin can now be used instead of the FindBugs plugin
  (both are available, so no change is required). To use SpotBugs, replace
  ``org.codehaus.mojo:findbugs-maven-plugin`` with
  ``com.github.spotbugs:spotbugs-maven-plugin``.

.. _Maven Find Duplicates: https://github.com/basepom/duplicate-finder-maven-plugin/

.. _SpotBugs: https://spotbugs.github.io/
