========================
ODL Parent release notes
========================

Version 3.1.6
-------------

This version includes the following improvements and bug fixes:

* ``karaf-plugin`` version in ``karaf4-parent`` is now provided through
  plugin management so downstreams can override it without needing
  to repeat its configuration.

* ``karaf-plugin`` no longer reads features twice when running discovery,
  speeding up the process a bit.

* ``bcprov-ext-jdk15on`` is a superset of ``bcprov-jdk15on``, so there’s no
  need to ship both; we now only ship the former. In addition, we install the
  Bouncy Castle JARs in ``lib/boot`` so that they continue to be available on
  the boot classpath (JDK 9 removes the extension mechanism which was used
  previously), and provide the corresponding bundles from the boot classpath
  instead of using separate JARs in the system repository. (See
  `ODLPARENT-183 <https://jira.opendaylight.org/browse/ODLPARENT-183>`__ and
  `ODLPARENT-185 <https://jira.opendaylight.org/browse/ODLPARENT-185>`__.)

* Dependencies pulled in by features are now checked for convergence, and
  ``karaf-plugin`` warns when it finds diverging dependencies (the same
  artifact with two different versions). Upstream-provided features are
  patched to avoid the following divergences (and upgrade some dependencies in
  the process):

  * Aries utilities 1.1.0/1.1.3 (upgraded to 1.1.3).
  * ASM 5.0.2/5.2 (upgraded to 5.2).
  * Commons Beanutils 1.8.3/1.9.3 (upgraded to 1.9.3).
  * Commons Codec 1.8/1.10 (upgraded to 1.11).
  * Commons Collections 3.2.1/3.2.2 (upgraded to 3.2.2).
  * ``javax.mail`` 1.4.4/1.4.5 (upgraded to 1.4.5).
  * Jetty 9.3.21/9.3.24 (upgraded to 9.3.24).

  (See `ODLPARENT-189 <https://jira.opendaylight.org/browse/ODLPARENT-189>`__.)

The following third-party dependency has been upgraded:

* Jackson 2.8.11.2 → 2.8.11.3 (fixing CVE-2018-14721 and CVE-2018-19362).

The following Maven plugin has been upgraded:

* ``git-commit-id`` `2.2.4 → 2.2.6 <https://github.com/git-commit-id/maven-git-commit-id-plugin/releases>`__.

Version 3.1.5
-------------

This version includes the following improvements and bug fixes:

* ``javax.activation`` is now excluded from generated features (it’s provided
  on Karaf’s boot classpath).

* Explicit GCs are disabled by default, so that calls to ``System.gc()`` are
  ignored
  (`ODLPARENT-175 <https://jira.opendaylight.org/browse/ODLPARENT-175>`__).

The following third-party dependencies have been upgraded:

* Javassist 3.23.1 → 3.24.0.

* Karaf 4.1.6 → 4.1.7, except in ``karaf4-parent`` where version 4.1.7 of the Karaf
  Maven plugin `breaks the build <https://issues.apache.org/jira/browse/KARAF-6057>`__.

* Netty `4.1.30 → 4.1.31 <https://netty.io/news/2018/10/30/4-1-31-Final.html>`__.

* Scala `2.12.7 → 2.12.8 <https://github.com/scala/scala/releases/tag/v2.12.8>`__.

* Xtend `2.14.0 → 2.16.0 <https://www.eclipse.org/xtend/releasenotes.html>`__.

The following Maven plugin has been upgraded:

* Remote Resources `1.5 → 1.6.0 <https://blogs.apache.org/maven/entry/apache-maven-remote-resources-plugin>`__.

Version 3.1.4
-------------

This version includes the following improvements:

* A new ``odl-apache-sshd`` feature encapsulates Apache SSHD, with support for
  ed25519 keys.

* Our override of ``jre.properties`` is removed; it prevented our distribution
  from working on JREs later than version 8 at all. (We don’t yet support
  version 9 or later as a whole.)

* The quick profile skips SpotBugs.

* JSR-305 annotations are now declared as optional.

* JAXB modules are filtered from generated features.

The following third-party dependencies have been upgraded:

* `BouncyCastle 1.59 → 1.60 <http://bouncycastle.org/releasenotes.html>`_
  (including fixes for CVE-2018-1000180 and CVE-2018-1000613).

* Jackson 2.8.9 → 2.8.11.20180608:

  * `2.8.10 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.8.10>`_.
  * `2.8.11 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.8.11>`_.
  * `Micro-patches <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.8>`_.

* Javassist 3.22.0-GA → 3.23.1-GA, with fixes for
  `two <https://github.com/jboss-javassist/javassist/issues/165>`_
  `issues <https://github.com/jboss-javassist/javassist/issues/171>`_
  relevant to us.

* `Karaf 4.1.5 → 4.1.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342748>`_,
  with Jetty 9.3.21.v20170918 → 9.3.24.v20180605.

* Netty 4.1.22 → 4.1.:

  * `4.1.23 <https://netty.io/news/2018/04/04/4-1-23-Final.html>`_.
  * `4.1.24 <https://netty.io/news/2018/04/19/4-1-24-Final.html>`_.
  * `4.1.25 <https://netty.io/news/2018/05/14/4-1-25-Final.html>`_.
  * `4.1.26 <https://netty.io/news/2018/07/10/4-1-26-Final.html>`_.
  * `4.1.27 <https://netty.io/news/2018/07/11/4-1-27-Final.html>`_.
  * `4.1.28 <https://netty.io/news/2018/07/27/4-1-28-Final.html>`_.
  * `4.1.29 <https://netty.io/news/2018/08/24/4-1-29-Final.html>`_.
  * `4.1.30 <https://netty.io/news/2018/09/28/4-1-30-Final.html>`_.

* `Scala 2.12.6 → 2.12.7 <https://github.com/scala/scala/releases/tag/v2.12.7>`_.

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
