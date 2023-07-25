========================
ODL Parent release notes
========================

Version 12.0.7
--------------
This is a bug-fix upgrade from version 12.0.6.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Antlr `4.12.0 → 4.13.0 <https://github.com/antlr/antlr4/releases/tag/4.13.0>`__

* asciidoctor-jdiagram 2.2.3 → 2.2.9, release notes:
  * `2.2.4 <https://github.com/asciidoctor/asciidoctorj-diagram/releases/tag/v2.2.4>`__
  * `2.2.8 <https://github.com/asciidoctor/asciidoctorj-diagram/releases/tag/v2.2.8>`__
  * `2.2.9 <https://github.com/asciidoctor/asciidoctorj-diagram/releases/tag/v2.2.9>`__

* Bouncy Castle 1.73 → 1.75, release notes:
  * `1.74 <https://www.bouncycastle.org/releasenotes.html#r1rv74>`__
  * `1.75 <https://www.bouncycastle.org/releasenotes.html#r1rv75>`__

* checker-qual 3.34.0 → 3.36.0

* Checkstyle 10.11.0 → 10.12.1, release notes:
  * `10.12.0 <https://checkstyle.org/releasenotes.html#Release_10.12.0>`__
  * `10.12.1 <https://checkstyle.org/releasenotes.html#Release_10.12.1>`__

* commons-io `2.12.0 → 2.13.0, <https://commons.apache.org/proper/commons-io/changes-report.html#a2.13.0>`__

* Dropwizard Metrics `4.2.18 → 4.2.19 <https://github.com/dropwizard/metrics/releases/tag/v4.2.19>`__

* Jersey `2.39.1 → 2.40 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.40>`__

* JUnit `5.9.3 → 5.10.0 <https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.10.0>`__

* metainf-services 1.9 → 1.11

* Mockito `5.3.1 → 5.4.0 <https://github.com/mockito/mockito/releases/tag/v5.4.0>`__

* Netty 4.1.93 → 4.1.95, release notes:
  * `4.1.94 <https://netty.io/news/2023/06/19/4-1-94-Final.html>`__
  * `4.1.95 <https://netty.io/news/2023/07/20/4-1-95-Final.html>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* depends-maven-plugin `1.4.0 → 1.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311206&version=12339645>`__

* duplicate-finder-maven-plugin 1.5.1 → 2.0.1, release notes:
  * `2.0.0 <https://github.com/basepom/duplicate-finder-maven-plugin/blob/main/CHANGES.md#200---2023-05-21>`__
  * `2.0.1 <https://github.com/basepom/duplicate-finder-maven-plugin/blob/main/CHANGES.md#201---2023-05-28>`__

* echo-maven-plugin `2.0.1 → 2.1.0 <https://github.com/Ekryd/echo-maven-plugin/releases/tag/echo-plugin-2.1.0>`__

* maven-bundle-plugin `5.1.8 → 5.1.9 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12353241>`__

* maven-clean-plugin `3.2.0 → 3.3.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317224&version=12351541>`__

* maven-invoker-plugin `3.5.1 → 3.6.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12353076>`__

* maven-project-info-reports-plugin 3.4.3 → 3.4.5, release notes:
  * `3.4.4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12353222>`__
  * `3.4.5 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12353297>`__

* maven-release-plugin 2.5.3 → 3.0.1, release notes:
  * `3.0.0-M1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12331214>`__
  * `3.0.0-M4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12348079>`__
  * `3.0.0-M5 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12346565>`__
  * `3.0.0-M6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12351336>`__
  * `3.0.0-M7 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12351828>`__
  * `3.0.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12352981>`__
  * `3.0.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317824&version=12353136>`__

* maven-remote-resources-plugin `3.0.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317825&version=12352115>`__

* maven-shade-plugin `3.4.1 → 3.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317921&version=12352951>`__

* maven-surefire-plugin `3.1.0 → 3.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12353294>`__

* spotbugs-maven-plugin `4.7.3.4 → 4.7.3.5 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.5>`__

* versions-maven-plugin `2.15.0 → 2.16.0 <https://github.com/mojohaus/versions/releases/tag/2.16.0>`__

Version 12.0.6
--------------
This is a bug-fix upgrade from version 12.0.5.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* asciidoctor-jdiagram `2.2.4 → 2.2.8 <https://github.com/asciidoctor/asciidoctorj-diagram/releases/tag/v2.2.8>`__

* checker-qual 3.33.0 → 3.34.0

* Checkstyle 10.9.3 → 10.11.0, release notes:
  * `10.10.0 <https://checkstyle.org/releasenotes.html#Release_10.10.0>`__
  * `10.11.0 <https://checkstyle.org/releasenotes.html#Release_10.11.0>`__

* commons-io `2.11.0 → 2.12.0, <https://commons.apache.org/proper/commons-io/changes-report.html#a2.12.0>`__

* Jackson `2.14.2 → 2.14.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.14.3>`__

* JUnit `5.9.2 → 5.9.3 <https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.9.3>`__

* Mockito `5.3.0 → 5.3.1 <https://github.com/mockito/mockito/releases/tag/v5.3.1>`__

* Netty 4.1.91 → 4.1.93, release notes:
  * `4.1.92 <https://netty.io/news/2023/04/25/4-1-92-Final.html>`__
  * `4.1.93 <https://netty.io/news/2023/05/25/4-1-93-Final.html>`__

* TrieMap `1.3.0 → 1.3.1 <https://github.com/PANTHEONtech/triemap/releases/tag/triemap-1.3.1>`__

* Woodstox 6.4.0 → 6.5.1, release notes:
  * `6.5.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-6.5#650-14-jan-2023>`__
  * `6.5.1 <https://github.com/FasterXML/woodstox/issues/170>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* asciidoctor-maven-plugin `2.2.2 → 2.2.3 <https://github.com/asciidoctor/asciidoctor-maven-plugin/releases/tag/asciidoctor-maven-plugin-2.2.3>`__

* builder-helper-maven-plugin `3.3.0 → 3.4.0 <https://github.com/mojohaus/build-helper-maven-plugin/releases/tag/build-helper-maven-plugin-3.4.0>`__

* cyclonedx-maven-plugin 2.7.5 → 2.7.9, release notes:
  * `2.7.6 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.6>`__
  * `2.7.7 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.7>`__
  * `2.7.8 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.8>`__
  * `2.7.9 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.9>`__

* git-commit-id-maven-plugin `5.0.0 → 6.0.0 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v6.0.0>`__

* Jacoco Maven plugin 0.8.8 → 0.8.10, release notes:
  * `0.8.9 <https://github.com/jacoco/jacoco/releases/tag/v0.8.9>`__
  * `0.8.10 <https://github.com/jacoco/jacoco/releases/tag/v0.8.10>`__

* maven-assembly-plugin 3.4.2 → 3.6.0, release notes:
  * `3.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317220&version=12352065>`__
  * `3.6.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317220&version=12352952>`__

* maven-checkstyle-plugin 3.2.1 → 3.3.0, release notes:
  * `3.2.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12352856>`__
  * `3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12353164>`__

* maven-dependency-plugin `3.5.0 → 3.6.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317227&version=12352921>`__

* maven-help-plugin `3.3.0 → 3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317522&version=12352206>`__

* maven-invoker-plugin 3.4.0 → 3.5.1, release notes:
  * `3.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12352645>`__
  * `3.5.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12352974>`__

* maven-plugin-plugin 3.7.1 → 3.8.2, release notes:
  * `3.8.1 <https://issues.apache.org/jira/projects/MPLUGIN/versions/12352545>`__
  * `3.8.2 <https://issues.apache.org/jira/projects/MPLUGIN/versions/12352508>`__

* maven-project-info-reports-plugin 3.4.1 → 3.4.3, release notes:
  * `3.4.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12352728>`__
  * `3.4.3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12352922>`__

* maven-resources-plugin `3.3.0 → 3.3.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317827&version=12352140>`__

* maven-source-plugin `3.2.1 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317924&version=12346658>`__

* maven-surefire-plugin `3.0.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12351273>`__

* versions-maven-plugin `2.14.2 → 2.15.0 <https://github.com/mojohaus/versions/releases/tag/2.15.0>`__

Version 12.0.5
--------------
This is a bug-fix upgrade from version 12.0.4.

Bug fixes
~~~~~~~~~
* cyclonedx-maven-plugin has been rolled back to version ``2.7.5`` because newer
  versions execute incredibly slowly on large dependency graphs.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Bouncy Castle `1.72 → 1.73 <https://www.bouncycastle.org/releasenotes.html#r1rv73>`__

* Mockito `5.2.0 → 5.3.0 <https://github.com/mockito/mockito/releases/tag/v5.3.0>`__

* TrieMap 1.2.0 → 1.3.0
  * `1.2.1 <https://github.com/PANTHEONtech/triemap/releases/tag/triemap-1.2.0>`__
  * `1.3.0 <https://github.com/PANTHEONtech/triemap/releases/tag/triemap-1.3.0>`__

Version 12.0.4
--------------
This is a bug-fix upgrade from version 12.0.4.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Antlr `4.11.1 → 4.12.0 <https://github.com/antlr/antlr4/releases/tag/4.12.0>`__

* checker-qual 3.30.0 → 3.33.0

* Checkstyle 10.7.0 → 10.9.3, release notes:
  * `10.8.0 <https://checkstyle.org/releasenotes.html#Release_10.8.0>`__
  * `10.8.1 <https://checkstyle.org/releasenotes.html#Release_10.8.1>`__
  * `10.9.0 <https://checkstyle.org/releasenotes.html#Release_10.9.0>`__
  * `10.9.1 <https://checkstyle.org/releasenotes.html#Release_10.9.1>`__
  * `10.9.2 <https://checkstyle.org/releasenotes.html#Release_10.9.2>`__
  * `10.9.3 <https://checkstyle.org/releasenotes.html#Release_10.9.3>`__

* Dropwizard Metrics 4.2.15 → 4.2.18, release notes:
  * `4.2.16 <https://github.com/dropwizard/metrics/releases/tag/v4.2.16>`__
  * `4.2.17 <https://github.com/dropwizard/metrics/releases/tag/v4.2.17>`__
  * `4.2.18 <https://github.com/dropwizard/metrics/releases/tag/v4.2.18>`__

* Jersey 2.38 → 2.39.1, release notes:
  * `2.39 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.39>`__
  * `2.39.1 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.39.1>`__

* Mockito `5.1.1 → 5.2.0 <https://github.com/mockito/mockito/releases/tag/v5.2.0>`__

* Netty 4.1.89 → 4.1.91, release notes:
  * `4.1.90 <https://netty.io/news/2023/03/14/4-1-90-Final.html>`__
  * `4.1.91 <https://netty.io/news/2023/04/03/4-1-91-Final.html>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* cyclonedx-maven-plugin `2.7.5 → 2.7.6 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.6>`__

* maven-compiler-plugin `3.10.1 → 3.11.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317225&version=12351444>`__

* maven-enforcer-plugin `3.2.1 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317520&version=12352877>`__

* maven-surefire-plugin 3.0.0-M8 → 3.0.0, release notes:
  * `3.0.0-M9 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12352730>`__
  * `3.0.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12352998>`__

* modernizer-maven-plugin `2.5.0 → 2.6.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.6.0>`__

* spotbugs-maven-plugin 4.7.3.0 → 4.7.3.4, release notes:
  * `4.7.3.1 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.1>`__
  * `4.7.3.2 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.2>`__
  * `4.7.3.3 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.3>`__
  * `4.7.3.4 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.4>`__

Version 12.0.3
--------------
This is a bug-fix upgrade from version 12.0.2.

Bug fixes
~~~~~~~~~
* The configuration of ``spotbugs-maven-plugin`` was incorrect in that it referenced
  ``FindBugs`` settings. This issue is reported with ``maven-3.9.0`` and later and has
  now been corrected. See `ODLPARENT-299 <https://jira.opendaylight.org/browse/ODLPARENT-299>`__
  for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* asciidoctor-jdiagram `2.2.3 → 2.2.4 <https://github.com/asciidoctor/asciidoctorj-diagram/releases/tag/v2.2.4>`__

* Checkstyle `10.6.0 → 10.7.0 <https://checkstyle.org/releasenotes.html#Release_10.7.0>`__

* Jackson `2.14.1 → 2.14.2 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.14.2>`__

* Mockito 4.11.0 → 5.1.1, release notes:
  * `5.0.0 <https://github.com/mockito/mockito/releases/tag/v5.0.0>`__
  * `5.1.0 <https://github.com/mockito/mockito/releases/tag/v5.1.0>`__
  * `5.1.1 <https://github.com/mockito/mockito/releases/tag/v5.1.1>`__

* Netty 4.1.86 → 4.1.89, release notes:
  * `4.1.87 <https://netty.io/news/2023/01/12/4-1-87-Final.html>`__
  * `4.1.88 <https://netty.io/news/2023/02/12/4-1-88-Final.html>`__
  * `4.1.89 <https://netty.io/news/2023/02/13/4-1-89-Final.html>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* cyclonedx-maven-plugin `2.7.4 → 2.7.5 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.5>`__

* echo-maven-plugin 1.3.2 → 2.0.1, release notes:
  * `2.0.0 <https://github.com/Ekryd/echo-maven-plugin/releases/tag/echo-plugin-2.0.0>`__
  * `2.0.1 <https://github.com/Ekryd/echo-maven-plugin/releases/tag/echo-plugin-2.0.1>`__

* maven-checkstyle-plugin `3.2.0 → 3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12352729>`__

* maven-dependency-plugin `3.4.0 → 3.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317227&version=12352602>`__

* maven-enforcer-plugin `3.2.0 → 3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317520&version=12352857>`__

* maven-javadoc-plugin `3.4.1 → 3.5.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12352256>`__

* maven-plugin-plugin `3.7.0 → 3.7.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317820&version=12352745>`__

* maven-surefire-plugin `3.0.0-M7 → 3.0.0-M8 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12351809>`__

Version 12.0.2
--------------
This is a security bug-fix upgrade from version 12.0.1.

New features
~~~~~~~~~~~~
* Two new features, ``odl-asm`` and ``odl-apache-spifly`` have been added.
  See `ODLPARENT-296 <https://jira.opendaylight.org/browse/ODLPARENT-296>`__
  for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle 10.4 → 10.6.0
  * `10.5.0 <https://checkstyle.org/releasenotes.html#Release_10.5.0>`__
  * `10.6.0 <https://checkstyle.org/releasenotes.html#Release_10.6.0>`__

* Dropwizard Metrics 4.2.13 → 4.2.15, release notes:
  * `4.2.14 <https://github.com/dropwizard/metrics/releases/tag/v4.2.14>`__
  * `4.2.15 <https://github.com/dropwizard/metrics/releases/tag/v4.2.15>`__

* GSON `2.10.0 → 2.10.1 <https://github.com/google/gson/releases/tag/gson-parent-2.10.1>`__

* immutables.org `2.9.2 → 2.9.3 <https://github.com/immutables/immutables/releases/tag/2.9.3>`__

* Jackson 2.13.4 → 2.14.1, release notes:
  * `2.14 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.14>`__
  * `2.14.1 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.14.1>`__

* Jetty `9.4.49.v20220914 → 9.4.50.v20221201 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.50.v20221201>`__

* Jolokia 1.7.1 → 1.7.2

* Jersey `2.37 → 2.38 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.38>`__

* JUnit `5.9.1 → 5.9.2 <https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.9.2>`__

* Karaf `4.4.2 → 4.4.3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12352267>`__

* Mockito 4.9.0 → 4.11.0, release notes:
  * `4.10.0 <https://github.com/mockito/mockito/releases/tag/v4.10.0>`__
  * `4.11.0 <https://github.com/mockito/mockito/releases/tag/v4.11.0>`__

* Netty 4.1.84 → 4.1.86, release notes:
  * `4.1.85 <https://netty.io/news/2022/11/09/4-1-85-Final.html>`__
  * `4.1.86 <https://netty.io/news/2022/12/12/4-1-86-Final.html>`__

* Sevntu 1.43.0 → 1.44.1, release notes:
  * `1.44.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.44.0>`__
  * `1.44.1 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.44.1>`__

* ThreeTen `1.7.1 → 1.7.2 <https://www.threeten.org/threeten-extra/changes-report.html#a1.7.2>`__

* xmlunit `2.9.0 → 2.9.1 <https://github.com/xmlunit/xmlunit/releases/tag/v2.9.1>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* bnd-maven-plugin `6.3.1 → 6.4.0 <https://github.com/bndtools/bnd/wiki/Changes-in-6.4.0>`__

* cyclonedx-maven-plugin 2.7.1 → 2.7.4, release notes:
  * `2.7.2 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.2>`__
  * `2.7.3 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.3>`__
  * `2.7.4 <https://github.com/CycloneDX/cyclonedx-maven-plugin/releases/tag/cyclonedx-maven-plugin-2.7.4>`__

* duplicate-finder-maven-plugin `1.5.0 → 1.5.1 <https://github.com/basepom/duplicate-finder-maven-plugin/releases/tag/duplicate-finder-maven-plugin-1.5.1>`__

* exec-maven-plugin `3.0.0 → 3.1.0 <https://github.com/mojohaus/exec-maven-plugin/releases/tag/exec-maven-plugin-3.1.0>`__

* maven-archetype-plugin `3.2.0 → 3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317122&version=12348615>`__

* maven-dependency-plugin `3.3.0 → 3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317227&version=12351068>`__

* maven-help-plugin `3.2.0 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317522&version=12345417>`__

* maven-invoker-plugin 3.2.2 → 3.4.0, release notes:
  * `3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12349728>`__
  * `3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12330828>`__

* maven-plugin-plugin 3.6.2 → 3.7.0, release notes:
  * `3.6.4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317820&version=12351222>`__
  * `3.7.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317820&version=12344367>`__

* maven-remote-resources-plugin `1.7.0 → 3.0.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317825&version=12346864>`__

* properties-maven-plugin 1.0.0 → 1.1.0

* spotbugs-maven-plugin `4.7.2.2 → 4.7.3.0 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.3.0>`__

* versions-maven-plugin 2.8.1 → 2.14.2, release notes:
  * `2.9.0 <https://github.com/mojohaus/versions/releases/tag/versions-maven-plugin-2.9.0>`__
  * `2.10.0 <https://github.com/mojohaus/versions/releases/tag/versions-maven-plugin-2.10.0>`__
  * `2.11.0 <https://github.com/mojohaus/versions/releases/tag/versions-maven-plugin-2.11.0>`__
  * `2.12.0 <https://github.com/mojohaus/versions/releases/tag/versions-maven-plugin-2.12.0>`__
  * `2.13.0 <https://github.com/mojohaus/versions/releases/tag/2.13.0>`__
  * `2.14.0 <https://github.com/mojohaus/versions/releases/tag/2.14.0>`__
  * `2.14.1 <https://github.com/mojohaus/versions/releases/tag/2.14.1>`__
  * `2.14.2 <https://github.com/mojohaus/versions/releases/tag/2.14.2>`__

Version 12.0.1
--------------
This is a security bug-fix upgrade from version 12.0.0.

Improvements and new features
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* ``junit-jupiter-params`` is now a test dependency. This allows using JUnit 5 parameterized types without further
  declarations.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle `10.3.4 → 10.4 <https://checkstyle.org/releasenotes.html#Release_10.4>`__

* Dropwizard Metrics `4.2.12 → 4.2.13 <https://github.com/dropwizard/metrics/releases/tag/v4.2.13>`__

* GSON `2.9.0 → 2.10.0 <https://github.com/google/gson/releases/tag/gson-parent-2.10.0>`__

* Jackson `2.13.4 → 2.13.4.20221013 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.13#micro-patches>`__

* Mockito `4.8.1 → 4.9.0 <https://github.com/mockito/mockito/releases/tag/v4.9.0>`__

* Netty `4.1.84 → 4.1.85 <https://netty.io/news/2022/11/09/4-1-85-Final.html>`__

* Sevntu `1.42.0 → 1.43.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.43.0>`__

* Woodstox `6.3.1 → 6.4.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-6.4#640-24-oct-2022>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-shade-plugin 3.3.0 → 3.4.1, release notes:
  * `3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317921&version=12351491>`__
  * `3.4.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317921&version=12352285>`__

* modernizer-maven-plugin `2.4.0 → 2.5.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.5.0>`__

* spotbugs-maven-plugin `4.7.2.1 → 4.7.2.2 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.2.2>`__

Version 12.0.0
--------------
This is a major upgrade from version 11, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency/plugin management:

* Declaration of ``Enunciate`` has been removed. This dependency is not used by any active downstream.

* The opt-in for generating HTML4 Javadoc documentation has been removed.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Antlr 4.9.2 → 4.11.1, release notes:
  * `4.10 <https://github.com/antlr/antlr4/releases/tag/4.10>`__
  * `4.10.1 <https://github.com/antlr/antlr4/releases/tag/4.10.1>`__
  * `4.11.0 <https://github.com/antlr/antlr4/releases/tag/4.11.0>`__
  * `4.11.1 <https://github.com/antlr/antlr4/releases/tag/4.11.1>`__

* Bouncy Castle 1.70 → 1.72, release notes:
  * `1.71 <https://www.bouncycastle.org/releasenotes.html#r1rv71>`__
  * `1.72 <https://www.bouncycastle.org/releasenotes.html#r1rv72>`__

* Checkstyle 10.3.1 → 10.3.4, release notes:
  * `10.3.2 <https://checkstyle.org/releasenotes.html#Release_10.3.2>`__
  * `10.3.3 <https://checkstyle.org/releasenotes.html#Release_10.3.3>`__
  * `10.3.4 <https://checkstyle.org/releasenotes.html#Release_10.3.4>`__

* commons-text `1.9 → 1.10.0 <https://commons.apache.org/proper/commons-text/changes-report.html#a1.10.0>`__

* Dropwizard Metrics 4.2.10 → 4.2.12, release notes:
  * `4.2.11 <https://github.com/dropwizard/metrics/releases/tag/v4.2.11>`__
  * `4.2.12 <https://github.com/dropwizard/metrics/releases/tag/v4.2.12>`__

* immutables.org 2.8.8 → 2.9.2, release notes:
  * `2.9.0 <https://github.com/immutables/immutables/milestone/76?closed=1>`__
  * `2.9.1 <https://github.com/immutables/immutables/milestone/77?closed=1>`__
  * `2.9.2 <https://github.com/immutables/immutables/milestone/78?closed=1>`__

* Jackson `2.13.3 → 2.13.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.13.4>`__

* Javassist 3.28.0-GA → 3.29.2-GA

* Jersey `2.36 → 2.37 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.37>`__

* Jetty 9.4.46.v20220331 → 9.4.49.v20220914, release notes:
  * `9.4.47.v20220610 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.47.v20220610>`__
  * `9.4.48.v20220622 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.48.v20220622>`__
  * `9.4.49.v20220914 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.49.v20220914>`__

* JUnit `5.9.0 → 5.9.1 <https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.9.1>`__

* Karaf `4.4.1 → 4.4.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12352048>`__

* Mockito 4.6.1 → 4.8.1, release notes:
  * `4.7.0 <https://github.com/mockito/mockito/releases/tag/v4.7.0>`__
  * `4.8.0 <https://github.com/mockito/mockito/releases/tag/v4.8.0>`__
  * `4.8.1 <https://github.com/mockito/mockito/releases/tag/v4.8.1>`__

* Netty 4.1.76 → 4.1.84, release notes:
  * `4.1.77 <https://netty.io/news/2022/05/06/2-1-77-Final.html>`__
  * `4.1.78 <https://netty.io/news/2022/06/14/4-1-78-Final.html>`__
  * `4.1.79 <https://netty.io/news/2022/07/11/4-1-79-Final.html>`__
  * `4.1.80 <https://netty.io/news/2022/08/26/4-1-80-Final.html>`__
  * `4.1.81 <https://netty.io/news/2022/09/08/4-1-81-Final.html>`__
  * `4.1.82 <https://netty.io/news/2022/09/13/4-1-82-Final.html>`__
  * `4.1.84 <https://netty.io/news/2022/10/11/4-1-84-Final.html>`__

* SpotBugs 4.7.1 → 4.7.3, release notes:
  * `4.7.2 <https://github.com/spotbugs/spotbugs/releases/tag/4.7.2>`__
  * `4.7.3 <https://github.com/spotbugs/spotbugs/releases/tag/4.7.3>`__

* ThreeTen `1.7.0 → 1.7.1 <https://www.threeten.org/threeten-extra/changes-report.html#a1.7.1>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* bnd-maven-plugin 6.2.0 → 6.3.1, release notes:
  * `6.3.0 <https://github.com/bndtools/bnd/wiki/Changes-in-6.3.0>`__
  * `6.3.1 <https://github.com/bndtools/bnd/wiki/Changes-in-6.3.1>`__

* maven-bundle-plugin 5.1.6 → 5.1.8, release notes:
  * `5.1.7 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12352061>`__
  * `5.1.8 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12352145>`__

* maven-checkstyle-plugin `3.1.2 → 3.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12345559>`__

* maven-jar-plugin `3.2.2 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317526&version=12351126>`__

* maven-javadoc-plugin `3.4.0 → 3.4.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12352053>`__

* maven-resources-plugin `3.2.0 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317827&version=12348676>`__

* maven-site-plugin `3.12.0 → 3.12.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317923&version=12351751>`__

* project-info-reports-plugin 3.3.0 → 3.4.1, release notes:
  * `3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12351758>`__
  * `3.4.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12352097>`__

Version 11.0.1
--------------
This is a bug-fix upgrade from version 11.0.0.

Improvements and new features
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* An alternative way of defining features has been introduced. This is hosted by the ``template-feature-parent``
  and unlike ``single-feature-parent``, only performs replacement of versions in a provided template. See
  `ODLPARENT-235 <https://jira.opendaylight.org/browse/ODLPARENT-235>`__ for more information.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle `10.3 → 10.3.1 <https://checkstyle.org/releasenotes.html#Release_10.3.1>`__

* Dropwizard Metrics `4.2.9 → 4.2.10 <https://github.com/dropwizard/metrics/releases/tag/v4.2.10>`__

* Jersey `2.35 → 2.36 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.36>`__

* Jetty 9.4.46.v20220331 → 9.4.48.v20220622, release notes:
  * `9.4.47.v20220610 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.47.v20220610>`__
  * `9.4.48.v20220622 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.48.v20220622>`__

* JUnit `5.8.2 → 5.9.0 <https://junit.org/junit5/docs/snapshot/release-notes/#release-notes-5.9.0>`__

* Karaf `4.4.0 → 4.4.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12351548>`__

* PAX-URL `2.6.10 → 2.6.11 <https://github.com/ops4j/org.ops4j.pax.url/milestone/76?closed=1>`__

* SpotBugs `4.7.0 → 4.7.1 <https://github.com/spotbugs/spotbugs/releases/tag/4.7.1>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* bnd-maven-plugin 6.2.0 → 6.3.1, release notes:
  * `6.3.0 <https://github.com/bndtools/bnd/wiki/Changes-in-6.3.0>`__
  * `6.3.1 <https://github.com/bndtools/bnd/wiki/Changes-in-6.3.1>`__

* cyclonedx-maven-plugin 2.6.2 → 2.7.1

* maven-antrun-plugin `3.0.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317121&version=12348085>`__

* maven-assebly-plugin 3.3.0 → 3.4.2, release notes:
  * `3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317220&version=12348203>`__
  * `3.4.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317220&version=12352049>`__
  * `3.4.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317220&version=12352095>`__

* maven-enforcer-plugin `3.0.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317520&version=12341008>`__

* maven-site-plugin `3.11.0 → 3.12.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317923&version=12351142>`__

* maven-surefire-plugin `3.0.0-M6 → 3.0.0-M7 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12351502>`__

* spotbugs-maven-plugin 4.7.0.0 → 4.7.1.1, release notes:
  * `4.7.1.0 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.1.0>`__
  * `4.7.1.1 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.1.1>`__

Version 11.0.0
--------------
This is a major upgrade from version 10, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Improvements and new features
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* Minimum required ``Java`` version is 17. Attempts to build a downstream project or load in a previous Java version
  will result in a failure.

* The declaration for ``argparse4j`` has been addded. See
  `ODLPARENT-289 <https://jira.opendaylight.org/browse/ODLPARENT-289>`__ for more information.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency/plugin management:

* Declaration of ``Google Truth`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-283 <https://jira.opendaylight.org/browse/ODLPARENT-283>`__ for more information.

* Declaration of ``commons-codec`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-285 <https://jira.opendaylight.org/browse/ODLPARENT-285>`__ for more information.

* Declaration of ``commons-fileupload`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-286 <https://jira.opendaylight.org/browse/ODLPARENT-286>`__ for more information.

* Declaration of ``commons-net`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-287 <https://jira.opendaylight.org/browse/ODLPARENT-287>`__ for more information.

* Declaration of ``jsonassert`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-288 <https://jira.opendaylight.org/browse/ODLPARENT-288>`__ for more information.

* Declaration of ``jung`` has been removed. This dependency is not used by any active downstream. See
  `ODLPARENT-290 <https://jira.opendaylight.org/browse/ODLPARENT-290>`__ for more information.

* Declaration of ``spring-osgi-mock`` has been removed. This dependency is not used by any active downstream.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle `10.2 → 10.3 <https://checkstyle.org/releasenotes.html#Release_10.3>`__

* Jackson `2.13.2 → 2.13.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.13.3>`__

* Jersey 2.27 → 2.35, release notes:
  * `2.28 <https://eclipse-ee4j.github.io/jersey.github.io/release-notes/2.28.html>`__
  * `2.29 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.29>`__
  * `2.29.1 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.29.1>`__
  * `2.30 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.30>`__
  * `2.30.1 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.30.1>`__
  * `2.31 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.31>`__
  * `2.32 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.32>`__
  * `2.33 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.33>`__
  * `2.34 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.34>`__
  * `2.35 <https://github.com/eclipse-ee4j/jersey/releases/tag/2.35>`__

* Karaf `4.3.7 → 4.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12349243>`__

* Mockito 4.5.1 → 4.6.1, release notes:
  * `4.6.0 <https://github.com/mockito/mockito/releases/tag/v4.6.0>`__
  * `4.6.1 <https://github.com/mockito/mockito/releases/tag/v4.6.1>`__

* SpotBugs `4.6.0 → 4.7.0 <https://github.com/spotbugs/spotbugs/releases/tag/4.7.0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* cyclonedx-maven-plugin 2.5.3 → 2.6.2

* maven-bundle-plugin `5.1.5 → 5.1.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12351722>`__

* maven-javadoc-plugin 3.1.1 → 3.4.0, release notes:
  * `3.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12345698>`__
  * `3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12346637>`__
  * `3.3.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12347807>`__
  * `3.3.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12350586>`__
  * `3.4.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12330874>`__

* maven-surefire-plugin 2.22.2 → 3.0.0-M6, release notes:
  * `3.0.0-M1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12342871>`__
  * `3.0.0-M2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12344396>`__
  * `3.0.0-M3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12342872>`__
  * `3.0.0-M4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12344668>`__
  * `3.0.0-M5 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12344612>`__
  * `3.0.0-M6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317927&version=12344613>`__

* project-info-reports-plugin `3.2.2 → 3.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12351604>`__

* spotbugs-maven-plugin `4.6.0.0 → 4.7.0.0 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-4.7.0.0>`__

Version 10.0.0
--------------
This is a major upgrade from version 9, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Log4Shell and similar vulnerabilities
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
This release addresses following security issues by its adopting ``log4j-2.17.1``,
``logback-1.2.10`` and ``pax-logging-2.0.14``:

* `CVE-2021-42550 <https://nvd.nist.gov/vuln/detail/CVE-2021-42550>`__ and related problems
  were further mitigated by reducing complexity in ``logback-1.2.10``

* `CVE-2021-44228 <https://nvd.nist.gov/vuln/detail/CVE-2021-44228>`__

* `CVE-2021-44832 <https://nvd.nist.gov/vuln/detail/CVE-2021-44832>`__

* `CVE-2021-45046 <https://nvd.nist.gov/vuln/detail/CVE-2021-45046>`__

* `CVE-2021-45105 <https://nvd.nist.gov/vuln/detail/CVE-2021-45105>`__

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency/plugin management:

* Declaration of ``blueprint-maven-plugin`` has been removed. This plugin has not seen a release in 3 years
  and its use typically leads to split container definitions. Users are advised to migrate to either
  hand-written Blueprint XML files or, preferrably, migrate to OSGi Declarative Services. See
  `ODLPARENT-267 <https://jira.opendaylight.org/browse/ODLPARENT-267>`__ for more information.

* The declaration and execution of ``maven-pmd-plugin`` has been removed. See
  `ODLPARENT-269 <https://jira.opendaylight.org/browse/ODLPARENT-269>`__ for more information.

* All ``Xtend`` dependency and plugin declarations have been removed. Xtend is currently only used by
  a single artifact in MD-SAL, hence the dependency is best managed there. Furthermore Xtend is facing
  challenges around maintainership, hence its use is discouraged. See
  `ODLPARENT-273 <https://jira.opendaylight.org/browse/ODLPARENT-273>`__ for more information.

* All ``Powermock``  dependency declarations have been removed. We have very few remaining downstream
  users, who can manage this dependency themselves. The version of ``Mockito`` we declare has facilities
  to cover all use cases previously covered only by PowerMock. See
  `ODLPARENT-275 <https://jira.opendaylight.org/browse/ODLPARENT-275>`__ for more information.

* Declarations of ``osgi.annotation``, ``osgi.cmpn`` and ``osgi.core`` have been removed. These are
  monolithic JARs providing, which are no longer shipped in OSGi Release 8. Users are advised to use
  component dependencies, such as ``org.osgi.annotation.bundle``, ``org.osgi.framework``,
  ``org.osgi.service.component.annotations`` and similar. See
  `ODLPARENT-277 <https://jira.opendaylight.org/browse/ODLPARENT-277>`__ for more information.

* The ``H2 Database`` version declaration has been removed. It is used only by as single downstream
  user, who also provides its OSGi packaging and hence it is best maintained there.

Improvements and new features
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* Minimum ``Maven`` version required to build projects using ``odlparent`` has been raised to ``3.8.3``.
  See `ODLPARENT-260 <https://jira.opendaylight.org/browse/ODLPARENT-260>`__ for more information.
* Unit tests are now using ``JUnit 5 Jupiter Engime`` for unit test execution, with ``JUnit 4`` tests
  supported by ``junit-vintage-engine``. This enables downstreams to use the much more modern JUnit 5 APIs
  as well as the corresponding Mockito integration out of the box. See
  `ODLPARENT-271 <https://jira.opendaylight.org/browse/ODLPARENT-271>`__ for more information.
* A CycloneDX SBOM is generated by default for all artifacts using ``odlparent-lite``. See
  `ODLPARENT-280 <https://jira.opendaylight.org/browse/ODLPARENT-280>`__ for more information.
* ``netty-transport-native-epoll`` is now supported on Linux/AArch64. See
  `ODLPARENT-241 <https://jira.opendaylight.org/browse/ODLPARENT-241>`__ for more information.
* A number of stale Karaf ``custom.properties`` have been removed. See
  `ODLPARENT-34 <https://jira.opendaylight.org/browse/ODLPARENT-34>`__ for more information.
* Karaf features are now tested when building with Java versions up to and including 17.
* SpotBugs analysis is now enabled when building with Java versions up to and including 17.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Bouncy Castle `1.69 → 1.70 <https://www.bouncycastle.org/releasenotes.html#r1rv70>`__

* Checkstyle 8.45.1 → 9.2.1, release notes:
  * `9.0 <https://checkstyle.org/releasenotes.html#Release_9.0>`__
  * `9.0 <https://checkstyle.org/releasenotes.html#Release_9.0.1>`__
  * `9.1 <https://checkstyle.org/releasenotes.html#Release_9.1>`__
  * `9.2 <https://checkstyle.org/releasenotes.html#Release_9.2>`__
  * `9.2.1 <https://checkstyle.org/releasenotes.html#Release_9.2.1>`__

* Dropwizard Metrics 4.1.25 → 4.2.8, release notes:
  * `4.2.0 <https://github.com/dropwizard/metrics/releases/tag/v4.2.0>`__
  * `4.2.1 <https://github.com/dropwizard/metrics/releases/tag/v4.2.1>`__
  * `4.2.2 <https://github.com/dropwizard/metrics/releases/tag/v4.2.2>`__
  * `4.2.3 <https://github.com/dropwizard/metrics/releases/tag/v4.2.3>`__
  * `4.2.4 <https://github.com/dropwizard/metrics/releases/tag/v4.2.4>`__
  * `4.2.5 <https://github.com/dropwizard/metrics/releases/tag/v4.2.5>`__
  * `4.2.6 <https://github.com/dropwizard/metrics/releases/tag/v4.2.6>`__
  * `4.2.7 <https://github.com/dropwizard/metrics/releases/tag/v4.2.7>`__
  * `4.2.8 <https://github.com/dropwizard/metrics/releases/tag/v4.2.8>`__

* GSON `2.8.9 → 2.9.0 <https://github.com/google/gson/releases/tag/gson-parent-2.9.0>`__

* Guava 30.1.1 → 31.1, release notes:
  * `31.0 <https://github.com/google/guava/releases/tag/v31.0>`__
  * `31.0.1 <https://github.com/google/guava/releases/tag/v31.0.1>`__
  * `31.1 <https://github.com/google/guava/releases/tag/v31.1>`__

* Jackson `2.16.5 → 2.16.6 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.6>`__

* Javassist 3.27.0 → 3.28.0

* Karaf 4.3.3 → 4.3.6, release notes:
  * `4.3.4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12350547>`__
  * `4.3.5 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12350856>`__
  * `4.3.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12351123>`__

* Log4J 2.14.1 → 2.17.1, release notes:
  * `2.15.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.15.0>`__
  * `2.16.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.16.0>`__
  * `2.17.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.17.0>`__
  * `2.17.1 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.17.1>`__

* Logback `1.2.5 → 1.2.10 <http://logback.qos.ch/news.html>`__

* Mockito 3.12.4 → 4.3.1, release notes:
  * `4.0.0 <https://github.com/mockito/mockito/releases/tag/v4.0.0>`__
  * `4.2.0 <https://github.com/mockito/mockito/releases/tag/v4.1.0>`__
  * `4.3.0 <https://github.com/mockito/mockito/releases/tag/v4.2.0>`__
  * `4.3.1 <https://github.com/mockito/mockito/releases/tag/v4.3.1>`__

* Netty 4.1.67 → 4.1.74, release notes:
  * `4.1.70 <https://netty.io/news/2021/10/11/4-1-70-Final.html>`__
  * `4.1.71 <https://netty.io/news/2021/12/09/4-1-71-Final.html>`__
  * `4.1.72 <https://netty.io/news/2021/12/13/4-1-72-Final.html>`__
  * `4.1.73 <https://netty.io/news/2022/01/12/4-1-73-Final.html>`__
  * `4.1.74 <https://netty.io/news/2022/02/08/4-1-74-Final.html>`__

* Sevntu `1.40.0 → 1.41.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.41.0>`__

* Woodstox 6.2.6 → 6.2.8, release notes:
  * `6.2.7 <https://github.com/FasterXML/woodstox/milestone/24?closed=1>`__
  * `6.2.8 <https://github.com/FasterXML/woodstox/milestone/26?closed=1>`__

* xmlunit 2.8.3 → 2.9.0, release notes:
  * `2.8.4 <https://github.com/xmlunit/xmlunit/releases/tag/v2.8.4>`__
  * `2.9.0 <https://github.com/xmlunit/xmlunit/releases/tag/v2.9.0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* asciidoctor-maven-plugin `2.2.1 → 2.2.2 <https://github.com/asciidoctor/asciidoctor-maven-plugin/releases/tag/asciidoctor-maven-plugin-2.2.2>`__

* bnd-maven-plugin 6.0.0 → 6.2.0, release notes:
  * `6.1.0 <https://github.com/bndtools/bnd/wiki/Changes-in-6.1.0>`__
  * `6.2.0 <https://github.com/bndtools/bnd/wiki/Changes-in-6.2.0>`__

* builder-helper-maven-plugin `3.2.0 → 3.3.0 <https://github.com/mojohaus/build-helper-maven-plugin/releases/tag/build-helper-maven-plugin-3.3.0>`__

* git-commit-id-maven-plugin `4.0.5 → 5.0.0 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v5.0.0>`__

* maven-compiler-plugin 3.8.1 → 3.10.0, release notes:
  * `3.9.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317225&version=12345214>`__
  * `3.10.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317225&version=12351256>`__

* maven-bundle-plugin 5.1.2 → 5.1.4, release notes:
  * `5.1.3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12350117>`__
  * `5.1.4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12351195>`__

* maven-jar-plugin 3.2.0 → 3.2.2, release notes:
  * `3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317526&version=12348050>`__
  * `3.2.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317526&version=12351215>`__

* maven-plugin-plugin `3.6.1 → 3.6.2 <https://maven.apache.org/docs/3.6.2/release-notes.html>`__

* maven-site-plugin `3.9.1 → 3.10.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317923&version=12348370>`__

* modernizer-maven-plugin `2.2.0 → 2.3.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.3.0>`__

* spotbugs-maven-plugin 3.12.2 → 4.5.3, release notes:
  `4.0.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#400---2020-02-15>`__
  `4.0.1 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#401---2020-03-19>`__
  `4.0.2 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#402---2020-04-15>`__
  `4.0.3 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#403---2020-05-13>`__
  `4.0.4 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#404---2020-06-09>`__
  `4.0.5 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#405---2020-06-20>`__
  `4.0.6 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#406---2020-06-23>`__
  `4.1.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#410---2020-07-30>`__
  `4.1.1 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#411---2020-07-31>`__
  `4.1.2 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#412---2020-08-18>`__
  `4.1.3 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#413---2020-09-25>`__
  `4.1.4 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#414---2020-10-15>`__
  `4.2.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#420---2020-11-28>`__
  `4.2.1 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#421---2021-02-04>`__
  `4.2.2 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#422---2021-03-03>`__
  `4.2.3 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#423---2021-04-12>`__
  `4.3.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#430---2021-07-01>`__
  `4.4.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#440---2021-08-12>`__
  `4.4.1 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#441---2021-09-07>`__
  `4.4.2 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#442---2021-10-08>`__
  `4.5.0 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#450---2021-11-05>`__
  `4.5.1 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#451---2021-12-08>`__
  `4.5.2 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#452---2021-12-13>`__
  `4.5.3 <https://github.com/spotbugs/spotbugs/blob/4.5.3/CHANGELOG.md#453---2022-01-04>`__

Version 9.0.8
-------------
This is a bug-fix upgrade from version 9.0.7.

Bug fixes
~~~~~~~~~
* The fix for `ODLPARENT-220 <https://jira.opendaylight.org/browse/ODLPARENT-220>`__ ended up
  changing behavior and packaging ``INFO.yaml`` instead of ``PROJECT_INFO.yaml``. This has now
  been fixed.

Version 9.0.7
-------------
This is a bug-fix upgrade from version 9.0.6.

Bug fixes
~~~~~~~~~
* The upgrade of ``maven-dependency-plugin`` to version 3.2.0 has caused a regression in accuracy
  of reports, with quite a few false positives being generated. The declaration has been rolled
  back to version 3.1.2 with a few tweaks to improve JDK compatibility. See
  `ODLPARENT-270 <https://jira.opendaylight.org/browse/ODLPARENT-270>`__ for more information.

Improvements
~~~~~~~~~~~~
* The declaration and invocation of ``script-maven-plugin`` has been replaced with a dedicated
  plugin providing the same functionality in a much more performant way.  See
  `ODLPARENT-220 <https://jira.opendaylight.org/browse/ODLPARENT-220>`__ for more information.

* A new parent pom.xml, ``bnd-parent``, is now available as an alternative to the existing
  ``bundle-parent``.  It has a more streamlined integration with other plugins, for example
  maven-jar-plugin, and a much healthier community. See
  `ODLPARENT-258 <https://jira.opendaylight.org/browse/ODLPARENT-258>`__ for more information.

* The definition of ``odl-karaf-feat-jdbc`` feature has been reworked to not pull in the entire
  ``enterprise`` feature repository, leading to significant savings in terms of Karaf distribution
  size for most downstream projects. See
  `ODLPARENT-266 <https://jira.opendaylight.org/browse/ODLPARENT-266>`__ for more information.

* The declaration of ``maven-compiler-plugin`` is now available in ``odlparent-lite``. This is
  improves locality, as the JDK version enforcement is already part of odlparent-lite.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* GSON 2.8.5 → 2.8.8, release notes:
  * `2.8.6 <https://github.com/google/gson/blob/master/CHANGELOG.md#version-286>`__
  * `2.8.7 <https://github.com/google/gson/blob/master/CHANGELOG.md#version-287>`__
  * `2.8.8 <https://github.com/google/gson/blob/master/CHANGELOG.md#version-288>`__

* Jolokia `1.7.0 → 1.7.1 <https://jolokia.org/changes-report.html#a1.7.1>`__

* Mockito 3.9.0 → 3.12.4, release notes:
  * `3.9.2 <https://github.com/mockito/mockito/releases/tag/v3.9.2>`__
  * `3.9.3 <https://github.com/mockito/mockito/releases/tag/v3.9.3>`__
  * `3.9.7 <https://github.com/mockito/mockito/releases/tag/v3.9.7>`__
  * `3.9.9 <https://github.com/mockito/mockito/releases/tag/v3.9.9>`__
  * `3.9.10 <https://github.com/mockito/mockito/releases/tag/v3.9.10>`__
  * `3.11.0 <https://github.com/mockito/mockito/releases/tag/v3.11.0>`__
  * `3.11.1 <https://github.com/mockito/mockito/releases/tag/v3.11.1>`__
  * `3.11.2 <https://github.com/mockito/mockito/releases/tag/v3.11.2>`__
  * `3.12.0 <https://github.com/mockito/mockito/releases/tag/v3.12.0>`__
  * `3.12.1 <https://github.com/mockito/mockito/releases/tag/v3.12.1>`__
  * `3.12.2 <https://github.com/mockito/mockito/releases/tag/v3.12.2>`__
  * `3.12.3 <https://github.com/mockito/mockito/releases/tag/v3.12.3>`__

* Netty 4.1.67 → 4.1.69, release notes:
  * `4.1.68 <https://netty.io/news/2021/09/09/4-1-68-Final.html>`__
  * `4.1.69 <https://netty.io/news/2021/10/11/4-1-69-Final.html>`__

Version 9.0.6
-------------
This is a bug-fix upgrade from version 9.0.5.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Jackson 2.11.4 → 2.12.5, release notes:
  * `2.12 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12>`__
  * `2.12.1 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.1>`__
  * `2.12.2 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.2>`__
  * `2.12.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.3>`__
  * `2.12.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.4>`__
  * `2.12.5 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12.5>`__

* Jetty 9.4.40 → 9.4.43.v20210629, release notes:
  * `9.4.41.v20210516 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.41.v20210516>`__
  * `9.4.42.v20210604 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.42.v20210604>`__
  * `9.4.43.v20210629 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.43.v20210629>`__

* Jolokia `1.6.2 → 1.7.0 <https://jolokia.org/changes-report.html#a1.7.0>`__

* Karaf `4.3.2 → 4.3.3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12350142>`__

* Pax URL 2.5.4 → 2.6.7, release notes:
  * `2.6.0 <https://github.com/ops4j/org.ops4j.pax.url/milestone/67?closed=1>`__
  * `2.6.3 <https://github.com/ops4j/org.ops4j.pax.url/milestone/68?closed=1>`__
  * `2.6.4 <https://github.com/ops4j/org.ops4j.pax.url/milestone/69?closed=1>`__
  * `2.6.6 <https://github.com/ops4j/org.ops4j.pax.url/milestone/71?closed=1>`__
  * `2.6.7 <https://github.com/ops4j/org.ops4j.pax.url/milestone/72?closed=1>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-enforcer-plugin `3.0.0-M3 → 3.0.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317520&version=12346527>`__

* asciidoctor-maven-plugin 2.1.0 → 2.2.1

Version 9.0.5
-------------
This is a minor upgrade from version 9.0.4.

Bug fixes
~~~~~~~~~
* Previous patch to address Bouncy Castle missed a critical piece for populating boot class path. This has now
  been corrected.


Version 9.0.4
-------------
This is a minor upgrade from version 9.0.3.

Bug fixes
~~~~~~~~~
* The upgrade to Bouncy Castle 1.69 turned out to be broken due to some code movement and introduction of a new
  ``bcutil-jdk15on`` artifact. It has also highlighted duplicate packaging, where Karaf's features would install
  version 1.66, while we have been on a newer version for some time. Both these issues have now ween resolved.
  See `ODLPARENT-254 <https://jira.opendaylight.org/browse/ODLPARENT-254>`__ for more information.


Version 9.0.3
-------------
This is a minor upgrade from version 9.0.2.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* awaitility `4.0.3 → 4.1.0 <https://raw.githubusercontent.com/awaitility/awaitility/master/changelog.txt>`__

* Bouncy Castle `1.68 → 1.69 <https://www.bouncycastle.org/releasenotes.html>`__

* Checkstyle 8.42 → 8.45.1, release notes:
  * `8.43 <https://checkstyle.org/releasenotes.html#Release_8.43>`__
  * `8.44 <https://checkstyle.org/releasenotes.html#Release_8.44>`__
  * `8.45 <https://checkstyle.org/releasenotes.html#Release_8.45>`__
  * `8.45.1 <https://checkstyle.org/releasenotes.html#Release_8.45.1>`__

* commons-io 2.8.0 → 2.11.0, release notes:
  * `2.9.0 <https://commons.apache.org/proper/commons-io/changes-report.html#a2.9.0>`__
  * `2.10.0 <https://commons.apache.org/proper/commons-io/changes-report.html#a2.10.0>`__
  * `2.11.0 <https://commons.apache.org/proper/commons-io/changes-report.html#a2.11.0>`__

* Dropwizard Metrics 4.1.21 → 4.1.25, release notes:
  * `4.1.22 <https://github.com/dropwizard/metrics/releases/tag/v4.1.22>`__
  * `4.1.23 <https://github.com/dropwizard/metrics/releases/tag/v4.1.23>`__
  * `4.1.24 <https://github.com/dropwizard/metrics/releases/tag/v4.1.24>`__
  * `4.1.25 <https://github.com/dropwizard/metrics/releases/tag/v4.1.25>`__

* Google Truth `1.1.2 → 1.1.3 <https://github.com/google/truth/releases/tag/release_1_1_3>`__

* logback 1.2.3 → 1.2.5, release notes:
  * `1.2.4 <https://jira.qos.ch/issues/?jql=project+%3D+LOGBACK+AND+fixVersion+%3D+1.2.4>`__
  * `1.2.5 <https://jira.qos.ch/issues/?jql=project+%3D+LOGBACK+AND+fixVersion+%3D+1.2.5>`__

* Netty 4.1.65 → 4.1.67, release notes:
  * `4.1.66 <https://netty.io/news/2021/07/16/4-1-66-Final.html>`__
  * `4.1.67 <https://netty.io/news/2021/08/16/4-1-67-Final.html>`__

* Sevntu `1.39.0 → 1.40.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.40.0>`__

* ThreeTen `1.6.0 → 1.7.0 <https://www.threeten.org/threeten-extra/changes-report.html#a1.7.0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* git-commit-id-plugin `4.0.4 → 4.0.5 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.5>`__

* Jacoco Maven plugin `0.8.6 → 0.8.7 <https://github.com/jacoco/jacoco/releases/tag/v0.8.7>`__

* maven-dependency-plugin `3.1.2 → 3.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317227&version=12348705>`__

* maven-plugin-plugin `3.6.0 → 3.6.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317820&version=12344365>`__

* project-info-reports-plugin `3.1.1 → 3.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12349521>`__

Version 9.0.2
-------------
This is a minor upgrade from version 9.0.1.

Bug fixes
~~~~~~~~~
* The upgrade to Karaf 4.3.0 caused a slight misalignment of ``org.apache.felix.metatype``
  and ``org.osgi.service.event`` versions, leading to an unnecessary rewiring of the container
  most notably during Single Feature Test. This has now been corrected.
  See `ODLPARENT-253 <https://jira.opendaylight.org/browse/ODLPARENT-253>`__ for more information.

* ``stax-utils`` component has a default dependency on ``com.bea.xml:jsr174-ri``, which is not
  resolvable from Maven Central. This artifact is not needed with modern JRE versions, hence add
  an explicit exclusion.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Jetty 9.4.38.v20210224 → 9.4.40.v20210413, release notes:
  * `9.4.39.v20210325 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.39.v20210325>`__
  * `9.4.40.v20210413 <https://github.com/eclipse/jetty.project/releases/tag/jetty-9.4.40.v20210413>`__

* Karaf `4.3.1 → 4.3.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12349968>`__

* Netty `4.1.63 → 4.1.65 <https://netty.io/news/2021/05/19/4-1-65-Final.html>`__

Version 9.0.1
-------------
This is a minor upgrade from version 9.0.0.

Bug fixes
~~~~~~~~~
The upgrade of ``duplicate-finder-maven-plugin`` ended up triggering build failures on ``.api_description``
files packaged in artifacts. This regression has now been fixed.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle `8.41 → 8.42 <https://checkstyle.org/releasenotes.html#Release_8.42>`__.

* Dropwizard Metrics 4.1.19 → 4.1.21, release notes:
  * `4.1.20 <https://github.com/dropwizard/metrics/releases/tag/v4.1.20>`__
  * `4.1.21 <https://github.com/dropwizard/metrics/releases/tag/v4.1.21>`__

* LMAX Disruptor 3.4.2 → 3.4.4, release notes:
  * `3.4.3 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.3>`__
  * `3.4.3 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.4>`__

* Woodstox `6.2.5 → 6.2.6 <https://github.com/FasterXML/woodstox/milestone/23?closed=1>`__

* XBean finder `4.18 → 4.19 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12348824>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-bundle-plugin 4.2.1 → 5.1.2, release notes:
  * `5.1.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12346016>`__
  * `5.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12350026>`__

Version 9.0.0
-------------
This is a major upgrade from version 8, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency management:

* ``javax.inject:javax.inject``. This dependency should be provided by target runtime. Furthermore we provide the same
  functionality via ``com.guicedee.services:javax.inject``, which is properly declared and is a JPMS module. See
  `ODLPARENT-246 <https://jira.opendaylight.org/browse/ODLPARENT-246>`__ for more information.

Improvements
~~~~~~~~~~~~
* The configuration of ``maven-compiler-plugin`` has been updated to expand ``javac`` warning options to include almost
  all of them.
* The default description in parent ``pom.xml`` has been updated to only include ``${project.artifactId}``, not a generic
  blurb. See `ODLPARENT-244 <https://jira.opendaylight.org/browse/ODLPARENT-244>`__ for more information.
* The ``single-feature-test`` substrate has been updated to allow Java Flight Recorder to be enabled during testing runs.
  This feature has falled into disrepair as the JFR facility has been productized.
* Configuration of various components has been updated to be deactivated when executing with JDK versions newer than 11,
  so that odlparent infrastructure works out of the box with JDK 16, albeit sacrificing some validation. Most notably
  ``SpotBugs`` and ``Single Feature Test`` are disabled, with corresponding notices displayed.
* We now expose the ability to check dependency declaration consistency via ``maven-dependency-plugin``'s ``analyze-only``
  goal. This functionality is enabled by default, but does not cause the build to fail when inconsistencies are found. It
  can be disabled by on a per-artifact basis by definining ``odlparent.dependency.skip`` property to ``true``. It can also
  be configured to fail the build on a per-artifact basis by defining ``odlparent.dependency.enforce`` property to ``true``.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Antlr 4.8-1 → 4.9.2, release notes:
  * `4.9 <https://github.com/antlr/antlr4/releases/tag/4.9>`__
  * `4.9.1 <https://github.com/antlr/antlr4/releases/tag/4.9.1>`__
  * `4.9.2 <https://github.com/antlr/antlr4/releases/tag/4.9.2>`__

* Checkstyle 8.39.0 → 8.41.1, release notes:
  * `8.40.0 <https://checkstyle.org/releasenotes.html#Release_8.40.0>`__
  * `8.41.0 <https://checkstyle.org/releasenotes.html#Release_8.41.0>`__
  * `8.41.1 <https://checkstyle.org/releasenotes.html#Release_8.41.1>`__

* commons-lang3 `3.11 → 3.12 <https://commons.apache.org/proper/commons-lang/changes-report.html#a3.12>`__

* Dropwizard Metrics 4.1.12.1 → 4.1.19, release notes:
  * `4.1.13 <https://github.com/dropwizard/metrics/releases/tag/v4.1.13>`__
  * `4.1.14 <https://github.com/dropwizard/metrics/releases/tag/v4.1.14>`__
  * `4.1.15 <https://github.com/dropwizard/metrics/releases/tag/v4.1.15>`__
  * `4.1.16 <https://github.com/dropwizard/metrics/releases/tag/v4.1.16>`__
  * `4.1.17 <https://github.com/dropwizard/metrics/releases/tag/v4.1.17>`__
  * `4.1.18 <https://github.com/dropwizard/metrics/releases/tag/v4.1.18>`__
  * `4.1.19 <https://github.com/dropwizard/metrics/releases/tag/v4.1.19>`__

* Enunciate `2.13.2 → 2.13.3 <https://github.com/stoicflame/enunciate/releases/tag/v2.13.3>`__

* Google Truth 1.1 → 1.1.2, release notes:
  * `1.1.1 <https://github.com/google/truth/releases/tag/release_1_1_1>`__
  * `1.1.2 <https://github.com/google/truth/releases/tag/release_1_1_2>`__

* Guava 29.0 → 30.1.1, release notes:
  * `30.0 <https://github.com/google/guava/releases/tag/v30.0>`__
  * `30.1 <https://github.com/google/guava/releases/tag/v30.1>`__
  * `30.1.1 <https://github.com/google/guava/releases/tag/v30.1.1>`__

* JUnit `4.13.1 → 4.13.2 <https://github.com/junit-team/junit4/blob/HEAD/doc/ReleaseNotes4.13.2.md>`__

* Karaf `4.3.0 → 4.3.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12348818>`__

* Log4J 2.13.3 → 2.14.1, release notes:
  * `2.14.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.14.0>`__
  * `2.14.1 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.14.1>`__

* Netty 4.1.59 → 4.1.63, release notes:
  * `4.1.60 <https://netty.io/news/2021/03/09/4-1-60-Final.html>`__
  * `4.1.61 <https://netty.io/news/2021/03/30/4-1-61-Final.html>`__
  * `4.1.62 <https://netty.io/news/2021/03/31/4-1-62-Final.html>`__
  * `4.1.63 <https://netty.io/news/2021/04/01/4-1-63-Final.html>`__

* Sevntu `1.38.0 → 1.39.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.39.0>`__

* ThreeTen `1.5.0 → 1.6.0 <https://www.threeten.org/threeten-extra/changes-report.html#a1.6.0>`__

* Woodstox `6.2.4 → 6.2.5 <https://github.com/FasterXML/woodstox/milestone/22?closed=1>`__

* Xtend `2.24.0 → 2.25.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2021/03/02/version-2-25-0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* Asciidoctor 1.5.7.1 → 2.1.0 (with related AsciidoctorJ upgrades)
  * `2.0.0 <https://github.com/asciidoctor/asciidoctor-maven-plugin/releases/tag/asciidoctor-maven-plugin-2.0.0>`__
  * `2.1.0 <https://github.com/asciidoctor/asciidoctor-maven-plugin/releases/tag/asciidoctor-maven-plugin-2.1.0>`__

* git-commit-id-plugin 3.0.1 → 4.0.4, release notes:
  * `4.0.0 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.0>`__
  * `4.0.1 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.1>`__
  * `4.0.2 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.2>`__
  * `4.0.3 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.3>`__
  * `4.0.4 <https://github.com/git-commit-id/git-commit-id-maven-plugin/releases/tag/v4.0.4>`__

* modernizer-maven-plugin `2.1.0 → 2.2.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.2.0>`__

Version 8.1.1
-------------
This is a minor upgrade from version 8.1.0.

Improvements
~~~~~~~~~~~~
* Support for building a `static Karaf distribution <https://karaf.apache.org/manual/latest/#_instances_and_distributions_goals>`__
  has been added. It is driven by a new parent pom.xml, ``karaf-dist-static``. For more information
  see Karaf documentation and `ODLPARENT-251 <https://jira.opendaylight.org/browse/ODLPARENT-251>`__.
* Support for `improved documentation tags <https://bugs.openjdk.java.net/browse/JDK-8068562>`__ has
  been added to default ``maven-javadoc-plugin`` configuration. See
  `ODLPARENT-252 <https://jira.opendaylight.org/browse/ODLPARENT-252>`__ for more information.
* Version declaration for ``osgi.annotation`` has been added.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* commons-net `3.7.2 → 3.8.0 <https://commons.apache.org/proper/commons-net/changes-report.html#a3.8.0>`__

* Netty `4.1.58 → 4.1.59 <https://netty.io/news/2021/02/08/4-1-59-Final.html>`__

* Woodstox `6.2.3 → 6.2.4 <https://github.com/FasterXML/woodstox/milestone/21?closed=1>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-checkstyle-plugin `3.1.1 → 3.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12347024>`__

* maven-invoker-plugin `3.2.1 → 3.2.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12346157>`__

Version 8.1.0
-------------
This is a minor upgrade from version 8.0.1, with some potentially-breaking
changes.

The most prominent is the upgrade to Karaf 4.3.0, which brings in a host of
updates to related components -- including OSGi Release 7 and Jackson 2.11.x.

Feature updates
~~~~~~~~~~~~~~~
* The ``odl-jackson-2.10`` feature and artifact were renamed to ``odl-jackson-2.11``
  to reflect the bump in Jackson version.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Bouncy Castle `1.66 → 1.68 <https://www.bouncycastle.org/releasenotes.html>`__

* Checkstyle 8.36.1 → 8.39.0, release notes:
  * `8.36.2 <https://checkstyle.org/releasenotes.html#Release_8.36.2>`__
  * `8.37.0 <https://checkstyle.org/releasenotes.html#Release_8.37.0>`__
  * `8.38.0 <https://checkstyle.org/releasenotes.html#Release_8.38.0>`__
  * `8.39.0 <https://checkstyle.org/releasenotes.html#Release_8.39.0>`__

* commons-net 3.7 → 3.7.2, release notes:
  * `3.7.1 <https://commons.apache.org/proper/commons-net/changes-report.html#a3.7.1>`__
  * `3.7.2 <https://commons.apache.org/proper/commons-net/changes-report.html#a3.7.2>`__

* enunciate `2.13.1 → 2.13.2 <https://github.com/stoicflame/enunciate/releases/tag/v2.13.2>`__

* Jackson 2.10.5 → 2.11.4, release notes:
  * `2.11.0 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.11>`__
  * `2.11.1 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.11.1>`__
  * `2.11.2 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.11.2>`__
  * `2.11.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.11.3>`__
  * `2.11.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.11.4>`__

* Karaf `4.2.10 → 4.3.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12343304>`__

* Netty 4.1.53 → 4.1.58, release notes:
  * `4.1.54 <https://netty.io/news/2020/11/11/4-1-54-Final.html>`__
  * `4.1.55 <https://netty.io/news/2020/12/08/4-1-55-Final.html>`__
  * `4.1.56 <https://netty.io/news/2020/12/17/4-1-56-Final.html>`__
  * `4.1.57 <https://netty.io/news/2021/01/12/4-1-57-Final.html>`__
  * `4.1.58 <https://netty.io/news/2021/01/13/4-1-58-Final.html>`__

* Google Truth `1.0.1 → 1.1 <https://github.com/google/truth/releases/tag/release_1_1>`__

* Powermock 2.0.7 → 2.0.9, release notes:
  * `2.0.8 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#208>`__
  * `2.0.9 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#209>`__

* Sevntu `1.37.1 → 1.38.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.38.0>`__

* XBean finder `4.17 → 4.18 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12348171>`__

* xmlunit 2.7.0 → 2.8.2, release notes:
  * `2.8.0 <https://github.com/xmlunit/xmlunit/releases/tag/v2.8.0>`__
  * `2.8.1 <https://github.com/xmlunit/xmlunit/releases/tag/v2.8.1>`__
  * `2.8.2 <https://github.com/xmlunit/xmlunit/releases/tag/v2.8.2>`__

* Xtend `2.23.0 → 2.24.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2020/12/01/version-2-24-0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* build-helper-maven-plugin `3.1.0 → 3.2.0 <https://github.com/mojohaus/build-helper-maven-plugin/issues?q=is%3Aissue+milestone%3A3.1.0+is%3Aclosed>`__

* duplicate-finder-maven-plugin `1.4.0 → 1.5.0 <https://github.com/basepom/duplicate-finder-maven-plugin/releases/tag/duplicate-finder-maven-plugin-1.5.0>`__

* exec-maven-plugin `1.6.0 → 3.0.0 <https://github.com/mojohaus/exec-maven-plugin/releases/tag/exec-maven-plugin-3.0.0>`__

* maven-pmd-plugin `3.13.0 → 3.14.0 <https://www.mail-archive.com/announce@maven.apache.org/msg00978.html>`__

* maven-resource-plugin `3.9.0 → 3.9.1 <https://www.mail-archive.com/announce@maven.apache.org/msg00972.html>`__

* maven-site-plugin `3.9.0 → 3.9.1 <https://www.mail-archive.com/announce@maven.apache.org/msg00966.html>`__

* versions-maven-plugin `2.7 → 2.8.1 <https://github.com/mojohaus/versions-maven-plugin/releases/tag/versions-maven-plugin-2.8.1>`__


Version 8.0.1
-------------
This is a minor big-fix/enhancement update from verision 8.0.0.

Improvements
~~~~~~~~~~~~
* ``javax.inject`` is now provided by the artifact from `GuicedEE <https://guicedee.com/>`__.
  This improves things a lot, as it is a proper jar (not MANIFEST.MF warning), it also is
  a JPMS module, hence can be used for linkage. This dependency is properly ``scope=provided``,
  so it should not leak into runtimes where it should not be.
  See `ODLPARENT-247 <https://jira.opendaylight.org/browse/ODLPARENT-247>`__ for details.

* Transitive dependencies of Guava are now mostly filtered from runtime, so that we do not
  require ``wrap`` for them. We still retain checker-qual, as we are actively using those
  and it is a proper bundle.
  See `ODLPARENT-248 <https://jira.opendaylight.org/browse/ODLPARENT-248>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Jackson `2.10.4 → 2.10.5 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.10.5>`__

* JaCoCo `0.8.5 → 0.8.6 <https://github.com/jacoco/jacoco/releases/tag/v0.8.6>`__

* JUnit `4.13.0 → 4.13.1 <https://github.com/junit-team/junit4/blob/HEAD/doc/ReleaseNotes4.13.1.md>`__

* Karaf `4.2.9 → 4.2.10 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12348288>`__

* Netty `4.1.52 → 4.1.53 <https://netty.io/news/2020/10/13/4-1-53-Final.html>`__

* Pax-Exam `4.13.3 → 4.13.4 <https://ops4j1.jira.com/secure/ReleaseNote.jspa?projectId=10170&version=24393>`__

* Woodstox 6.2.1 → 6.2.3, fixed issues:
  * `6.2.2 <https://github.com/FasterXML/woodstox/issues/112>`__
  * `6.2.3 <https://github.com/FasterXML/woodstox/issues/117>`__


Version 8.0.0
-------------
This is a major upgrade from version 7, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Improvements
~~~~~~~~~~~~
* ``modernizer-maven-plugin`` configuration has been updated to issue warnings for constructs
  improved in all Java versions up to and including Java 11.

* ``modernizer-maven-plugin`` is configured by default to fail the build when it issues any
  warnings. This behavior can be opted-out of on a per-artifact basis by defining
  ``odlparent.modernizer.enforce`` property to ``false``.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency management:

* ``javax.json``. This dependency is used only in Neutron project, hence this version is best
  maintained there. See `ODLPARENT-238 <https://jira.opendaylight.org/browse/ODLPARENT-238>`__
  for details.

* All ``org.eclipse.persistence`` artifacts. These dependencies are only used in Neutron,
  which actually duplicates the declarations, hence they are best maintained there.
  See `ODLPARENT-237 <https://jira.opendaylight.org/browse/ODLPARENT-237>`__ for details.

* All ``org.apache.sshd`` and ``net.i2p.crypto`` artifacts. Overriding versions does not play
  nice with Karaf's versions during ``feature:install``, causing issues when the installing
  over an SSH connection. NETCONF project is providing a repackaged version in OpenDaylight
  namespace. See `ODLPARENT-233 <https://jira.opendaylight.org/browse/ODLPARENT-233>`__ for
  details.

* ``jettison``. This dependency is used only in LISP Flow Mapping project for integration
  tests, hence this version is best maintained there.
  See `ODLPARENT-239 <https://jira.opendaylight.org/browse/ODLPARENT-239>`__ for details.

* All ``com.typesafe``, ``io.aeron``, ``org.agrona``, ``org.scala-lang`` declarations. Akka is
  removing their support for OSGi, with no working releases in their current ``2.6.x.`` branch.
  Since dealing with these requires quite a bit of dance, which needs to sit outside of odlparent POM,
  the controller project will package Akka to the extent it needs.
  See `ODLPARENT-243 <https://jira.opendaylight.org/browse/ODLPARENT-243>`__ for details.

* ``org.apache.felix.dependencymanager`` and ``org.apache.felix.dependencymanager.shell``. These
  components are ancient, having been replaced by either Blueprint or Declarative Services. The only
  project using these is AAA, hence it is best to maintain these declarations there.

Feature removals
~~~~~~~~~~~~~~~~
* ``odl-apache-sshd`` feature has been removed, mirroring the removal of related dependency
  declarations. See `ODLPARENT-233 <https://jira.opendaylight.org/browse/ODLPARENT-233>`__ for details.

* ``odl-akka-all``, ``odl-akka-scala-2.13``, ``odl-akka-system-2.5``, ``odl-akka-clustering-2.5``
  and ``odl-akka-persistence-2.5`` features. mirroring the removal of related dependency declarations.
  See `ODLPARENT-243 <https://jira.opendaylight.org/browse/ODLPARENT-243>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* awaitility `3.0.0 → 4.0.3 <https://github.com/awaitility/awaitility/wiki/ReleaseNotes40>`__

* checkstyle 8.34 → 8.36.1, release notes:
  * `8.35 <https://checkstyle.org/releasenotes.html#Release_8.35>`__
  * `8.36 <https://checkstyle.org/releasenotes.html#Release_8.36>`__
  * `8.36.1 <https://checkstyle.org/releasenotes.html#Release_8.36.1>`__

* commons-codec `1.14 → 1.15 <https://commons.apache.org/proper/commons-codec/changes-report.html#a1.15>`__

* commons-io `2.7 → 2.8.0 <https://commons.apache.org/proper/commons-io/changes-report.html#a2.8.0>`__

* commons-net `3.6 → 3.7 <https://commons.apache.org/proper/commons-net/changes-report.html#a3.7>`__

* dropwizard-metrics 4.1.9 → 4.1.12.1, release notes:
  * `4.1.10 <https://github.com/dropwizard/metrics/releases/tag/v4.1.10>`__
  * `4.1.10.1 <https://github.com/dropwizard/metrics/releases/tag/v4.1.10.1>`__
  * `4.1.11 <https://github.com/dropwizard/metrics/releases/tag/v4.1.11>`__
  * `4.1.12 <https://github.com/dropwizard/metrics/releases/tag/v4.1.12>`__
  * `4.1.12.1 <https://github.com/dropwizard/metrics/releases/tag/v4.1.12.1>`__

* Guava `28.2 → 29.0 <https://github.com/google/guava/releases/tag/v29.0>`__

* immutables.org → 2.8.8, release notes:
  * `2.8.0 <https://github.com/immutables/immutables/releases/tag/2.8.0>`__
  * `2.8.1 <https://github.com/immutables/immutables/releases/tag/2.8.1>`__
  * `2.8.2 <https://github.com/immutables/immutables/releases/tag/2.8.2>`__
  * `2.8.3 <https://github.com/immutables/immutables/releases/tag/2.8.3>`__
  * `2.8.4 <https://github.com/immutables/immutables/releases/tag/2.8.4>`__
  * `2.8.8 <https://github.com/immutables/immutables/releases/tag/2.8.8>`__

* mockito `3.3.3 → 3.5.11 <https://github.com/mockito/mockito/blob/release/3.x/doc/release-notes/official.md>`__

* Netty `4.1.51 → 4.1.52 <https://netty.io/news/2020/09/08/4-1-52-Final.html>`__

* Xtend `2.22.0 → 2.23.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2020/09/01/version-2-23-0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-archetype-plugin `3.1.2 → 3.2.0 <https://blogs.apache.org/maven/entry/apache-maven-archetype-plugin-version1>`__

* project-info-reports-plugin `3.1.0 → 3.1.1 <https://blogs.apache.org/maven/entry/apache-maven-project-info-reports1>`__

Version 7.0.5
-------------
This is a bug-fix upgrade from version 7.0.4.

Bug fixes
~~~~~~~~~
* ``odl-netty-4`` feature definition specified both ``x86_64`` and ``aarch64`` artifacts
  for ``netty-native-epoll``. This actually results only in ``aarch64`` package being
  installed, rendering epoll unavailable on ``x86_64`` architecture. This has been corrected
  by removing the ``aarch64`` package.
  See `ODLPARENT-240 <https://jira.opendaylight.org/browse/ODLPARENT-240>`__ for details.

Version 7.0.4
-------------
This is a security/bug-fix upgrade from version 7.0.3.

Bug fixes
~~~~~~~~~
* ``Single Feature Test`` setup of the JVM for Karaf container ended up using
  wrong versions of Karaf components, leading to a failure to initialize
  OSGiLocator and subsequent warnings with stack traces. This has now been
  corrected.
  See `ODLPARENT-228 <https://jira.opendaylight.org/browse/ODLPARENT-228>`__ for details.
* Pax-Exam setup interacts badly with pipes used by maven-surefire plugin,
  leading to pauses lasting around 30 seconds after SFT test success.
  This has now been worked around by using maven-surefire-plugin version 3.0.0-M5,
  with TCP sockets used for communication.
  See `ODLPARENT-179 <https://jira.opendaylight.org/browse/ODLPARENT-179>`__ for details.
* Our Jersey dependency was held back on version 2.25.1 during Neon upgrade cycle, mostly
  due to large-scale incompatibilities around JAX-RS version. We have upgraded to Karaf-4.2.8+,
  which pulls in JAX-RS 2.1, hence re-aligning to a more modern version, 2.27, is now
  feasible.
  See `ODLPARENT-208 <https://jira.opendaylight.org/browse/ODLPARENT-208>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Bouncy Castle `1.65 → 1.66 <https://www.bouncycastle.org/releasenotes.html>`__

* Checkstyle 8.32 → 8.34, release notes:
  * `8.33 <https://checkstyle.org/releasenotes.html#Release_8.33>`__
  * `8.34 <https://checkstyle.org/releasenotes.html#Release_8.34>`__

* commons-io `2.6 → 2.7 <https://commons.apache.org/proper/commons-io/changes-report.html#a2.7>`__

* Jersey 2.25.1 → 2.27, release notes:
  * `2.26 <https://eclipse-ee4j.github.io/jersey.github.io/release-notes/2.26.html>`__
  * `2.27 <https://eclipse-ee4j.github.io/jersey.github.io/release-notes/2.27.html>`__

* Karaf `4.2.8 → 4.2.9, with related upgrades <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12345539>`__

* Netty 4.1.49 → 4.1.51, release notes:
  * `4.1.50 <https://netty.io/news/2020/05/13/4-1-50-Final.html>`__
  * `4.1.51 <https://netty.io/news/2020/07/09/4-1-51-Final.html>`__

* Scala `2.13.2 → 2.13.3 <https://github.com/scala/scala/releases/tag/v2.13.3>`__

* TrieMap `1.1.0 → 1.2.0 <https://github.com/PANTHEONtech/triemap/releases/tag/triemap-1.2.0>`__

* XBean finder 4.14 → 4.17, release notes:
  * `4.15 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12345583>`__
  * `4.16 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12345584>`__
  * `4.17 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12346905>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-project-info-reports-plugin `3.0.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317821&version=12346620>`__.

* maven-shade-plugin `3.2.3 → 3.2.4 <https://blogs.apache.org/maven/entry/apache-maven-shade-plugin-version5>`__

Version 7.0.3
-------------
This is a security/bug-fix upgrade from version 7.0.2. Changes in this release
pertain strictly to Karaf packaging and do not affect other runtimes.

Bug fixes
~~~~~~~~~
* Felix SCR 2.1.16, as shipping before Karaf-4.2.9, contains a bug, which could
  lead to NullPointerException being thrown when components were examined. This
  has been rectified via upgrade to Felix SCR 2.1.20.
  See `ODLPARENT-236 <https://jira.opendaylight.org/browse/ODLPARENT-236>`__ for details.
* Karaf-4.2.8 changed packaged log4j2 version, rendering the configuration supplied
  with ``ODLPARENT-231`` inconsistent. This has led to a warning being printed in the
  Karaf console on each startup. This has now been corrected.
* Karaf-4.2.8 is packaging pax-logging-1.11.4, which embeds a a vulnerable version
  of log4j2 (2.3.0). This would render the upgrades delivered in version 7.0.2
  ineffective at runtime, potentially leading to exposure. This has been corrected
  with upgrade of pax-logging to 1.11.6, which is packaging log4j2-2.3.2.

Version 7.0.2
-------------
This is a security/bug-fix upgrade from version 7.0.1.

Improvements
~~~~~~~~~~~~
* Infrastructure for identifying confidential log messages was added, along with
  Karaf configuration update to routing such messages into a separate log file.
  See `ODLPARENT-231 <https://jira.opendaylight.org/browse/ODLPARENT-231>`__ for details.

* Netty has been disconnected from Javassist way back in its 4.1.9 release, but
  we failed to notice. This has now been rectified by ``odl-netty-4`` not depending
  on ``odl-javassist-3``.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Checkstyle `8.31 → 8.32 <https://checkstyle.org/releasenotes.html#Release_8.32>`__

* Dropwizard Metrics 4.1.5 → 4.1.9, release notes:
  * `4.1.6 <https://github.com/dropwizard/metrics/releases/tag/v4.1.6>`__
  * `4.1.7 <https://github.com/dropwizard/metrics/releases/tag/v4.1.7>`__
  * `4.1.8 <https://github.com/dropwizard/metrics/releases/tag/v4.1.8>`__
  * `4.1.9 <https://github.com/dropwizard/metrics/releases/tag/v4.1.9>`__

* Enunciate 2.12.1 → 2.13.1, release notes:
  * `2.13.0 <https://github.com/stoicflame/enunciate/releases/tag/v2.13.0>`__
  * `2.13.1 <https://github.com/stoicflame/enunciate/releases/tag/v2.13.1>`__

* Jackson 2.10.2 → 2.10.4, release notes:
  * `2.10.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.10.3>`__
  * `2.10.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.10.4>`__

* log4j2 2.13.1 → 2.13.3, release notes:
  * `2.13.2 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.13.2>`__
  * `2.13.3 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.13.3>`__

* Netty `4.1.48 → 4.1.49 <https://netty.io/news/2020/02/28/4-1-46-Final.html>`__

* Powermock `2.0.6 → 2.0.7 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#207>`__

* Scala `2.13.1 → 2.13.2 <https://github.com/scala/scala/releases/tag/v2.13.2>`__

* Woodstox 6.1.1 → 6.2.1, release notes:
  * `6.2.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-6.2#620-25-apr-2020>`__
  * `6.2.1 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-6.2#621-13-may-2020>`__

* xmlunit 2.6.3 → 2.7.0, release notes:
  * `2.6.4 <https://github.com/xmlunit/xmlunit/releases/tag/v2.6.4>`__
  * `2.7.0 <https://github.com/xmlunit/xmlunit/releases/tag/v2.7.0>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* builder-helper-maven-plugin `3.0.0 → 3.1.0 <https://github.com/mojohaus/build-helper-maven-plugin/issues?q=is%3Aissue+milestone%3A3.1.0+is%3Aclosed>`__

* duplicate-finder-maven-plugin `1.3.0 → 1.4.0 <https://github.com/basepom/duplicate-finder-maven-plugin/releases/tag/duplicate-finder-maven-plugin-1.4.0>`__

* maven-antrun-plugin `1.8 → 3.0.0 <https://blogs.apache.org/maven/entry/apache-maven-antrun-plugin-version>`__

* maven-assembly-plugin `3.2.0 → 3.3.0 <https://blogs.apache.org/maven/entry/apache-maven-assembly-plugin-version1>`__

* maven-invoker-plugin `3.2.0 → 3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317525&version=12344863>`__

* maven-remote-resources-plugin `1.6.0 → 1.7.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317825&version=12331232>`__

* maven-shade-plugin `3.2.2 → 3.2.3 <https://blogs.apache.org/maven/entry/apache-maven-shade-plugin-version4>`__

Version 7.0.1
-------------
This is a bug-fix upgrade from version 7.0.0.

Bug fixes
~~~~~~~~~
* Upgrade of ``maven-javadoc-plugin`` is causing issues in downstream javadoc jobs and therefore it has been reverted.
  See `ODLPARENT-229 <https://jira.opendaylight.org/browse/ODLPARENT-229>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Akka `2.5.30 → 2.5.31 <https://akka.io/blog/news/2020/03/31/akka-2.5.31-released>`__

* Bouncy Castle `1.64 → 1.65 <https://www.bouncycastle.org/releasenotes.html>`__

* Checkstyle `8.30 → 8.31 <https://checkstyle.org/releasenotes.html#Release_8.31>`__

* commons-lang3 `3.9 → 3.10 <https://commons.apache.org/proper/commons-lang/changes-report.html#a3.10>`__

* Xtend 1.19.0 → 1.21.0, release notes:
  * `1.20.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2019/12/03/version-2-20-0>`__
  * `1.21.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2020/03/03/version-2-21-0>`__

Version 7.0.0
-------------
This is a major upgrade from version 6, with breaking changes; downstream projects may need to make changes to upgrade
to this version.

Property removals
~~~~~~~~~~~~~~~~~
* ``enforcer.version`` and ``projectinfo`` properties were removed. These properties do not serve any legal purpose as
  the plugins referenced by them are declared in ``pluginManagement`` section.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~
The following upstream dependencies have been removed from dependency management:

* ``immutables.org/value`` without ``<classifier>annotations</classifier>``

* ``javax.xml.bind/jaxb-api``, replaced with ``jakarta.xml.bind/jakarta.xml.bind-api``

* ``com.google.inject/guice``

* ``com.mycila.guice.extensions/mycila-guice-jsr250``

* ``org.apache.shiro/shiro-core``

* ``org.apache.shiro/shiro-web``

Feature removals
~~~~~~~~~~~~~~~~
* ``odl-akka-leveldb-0.10`` feature was removed. This feature provided leveldb-backed implementation of Akka
  Persistence, which is not supported for production environments by upstream. Furthermore this feature relied on a
  custom-built binary, which we do not have a means to reproduce -- limiting our portability. The controller project,
  which is the only downstream user of persistence provides an alternative implementation, hence we are removing this
  historical baggage. See `ODLPARENT-213 <https://jira.opendaylight.org/browse/ODLPARENT-213>`__ for details.

* ``odl-caffeine-2`` feature was removed. This feature provided a ``JSR-107 JCache`` implementation, an API deemed to
  be problematic where high-performance and correctness in required.

New features
~~~~~~~~~~~~
* OSGi R6 Declarative Services enabled in Karaf. The ``scr`` feature is now part of startup features, hence Service
  Component Runtime can be used without incurring an additional refresh.
  See `ODLPARENT-227 <https://jira.opendaylight.org/browse/ODLPARENT-227>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Akka `2.5.29 → 2.5.30 <https://akka.io/blog/news/2020/03/12/akka-2.5.30-released>`__

* antl4r `4.7.2 → 4.8-1 <https://github.com/antlr/antlr4/releases/tag/4.8>`__

* Asciidoctor `1.5.7.1 → 1.5.8 <https://github.com/asciidoctor/asciidoctor/releases/tag/v1.5.8>`__

* Checkstyle 8.26 → 8.30, release notes:
  * `8.27 <https://checkstyle.org/releasenotes.html#Release_8.27>`__
  * `8.28 <https://checkstyle.org/releasenotes.html#Release_8.28>`__
  * `8.29 <https://checkstyle.org/releasenotes.html#Release_8.29>`__
  * `8.30 <https://checkstyle.org/releasenotes.html#Release_8.30>`__

* Dropwizard Metrics 4.0.5 → 4.1.5, release notes:
  * `4.1.0-rc0 <https://github.com/dropwizard/metrics/releases/tag/v4.1.0-rc0>`__
  * `4.1.0-rc2 <https://github.com/dropwizard/metrics/releases/tag/v4.1.0-rc2>`__
  * `4.1.0-rc3 <https://github.com/dropwizard/metrics/releases/tag/v4.1.0-rc3>`__
  * `4.1.1 <https://github.com/dropwizard/metrics/releases/tag/v4.1.1>`__
  * `4.1.2 <https://github.com/dropwizard/metrics/releases/tag/v4.1.2>`__
  * `4.1.3 <https://github.com/dropwizard/metrics/releases/tag/v4.1.3>`__
  * `4.1.4 <https://github.com/dropwizard/metrics/releases/tag/v4.1.4>`__

* Google Truth 0.43 → 1.0.1, release notes:
  * `0.44 <https://github.com/google/truth/releases/tag/release_0_44>`__
  * `0.45 <https://github.com/google/truth/releases/tag/release_0_45>`__
  * `0.46 <https://github.com/google/truth/releases/tag/release_0_46>`__
  * `1.0-rc1 <https://github.com/google/truth/releases/tag/release_1_0_rc1>`__
  * `1.0-rc2 <https://github.com/google/truth/releases/tag/release_1_0_rc2>`__
  * `1.0 <https://github.com/google/truth/releases/tag/release_1_0>`__
  * `1.0.1 <https://github.com/google/truth/releases/tag/release_1_0_1>`__

* Guava 27.1 → 28.2, release notes:
  * `28.0 <https://github.com/google/guava/releases/tag/v28.0>`__
  * `28.1 <https://github.com/google/guava/releases/tag/v28.1>`__
  * `28.2 <https://github.com/google/guava/releases/tag/v28.2>`__

* Javassist 3.26.0 → 3.27.0

* jdt-annotations 2.2.100 → 2.2.400

* Karaf 4.2.6 → 4.2.8, with related upgrades, release notes:
  * `4.2.7 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12345539>`__
  * `4.2.8 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12346100>`__

* log4j2 `2.13.0 → 2.13.1 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.13.1>`__

* Netty 4.1.45 → 4.1.48, release notes:
  * `4.1.46 <https://netty.io/news/2020/02/28/4-1-46-Final.html>`__
  * `4.1.47 <https://netty.io/news/2020/03/09/4-1-47-Final.html>`__
  * `4.1.48 <https://netty.io/news/2020/03/17/4-1-48-Final.html>`__

* Powermock 2.0.4 → 2.0.6, release notes:
  * `2.0.5 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#205>`__
  * `2.0.6 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#206>`__

* Scala 2.12.10 → 2.13.1, release notes:
  * `2.13.0 <https://github.com/scala/scala/releases/tag/v2.13.0>`__
  * `2.13.1 <https://github.com/scala/scala/releases/tag/v2.13.1>`__

* scala-java8-compat 0.8.0 → 0.9.1, release notes:
  * `0.9.0 <https://github.com/scala/scala-java8-compat/releases/tag/v0.9.0>`__
  * `0.9.1 <https://github.com/scala/scala-java8-compat/releases/tag/v0.9.1>`__

* Sevntu 1.36.0 → 1.37.1, release notes:
  * `1.37.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.37.0>`__
  * `1.37.1 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.37.1>`__

* woodstox-core 5.3.0 → 6.1.1

Plugin upgrades
~~~~~~~~~~~~~~~

* findbugs-slf4j `1.4.0 → 1.5.0 <https://github.com/KengoTODA/findbugs-slf4j/blob/master/CHANGELOG.md#150---2019-07-04>`__

* maven-checkstyle-plugin `3.1.0 → 3.1.1 <https://blogs.apache.org/maven/entry/apache-maven-checkstyle-plugin-version1>`__

* maven-dependency-plugin `3.1.1 → 3.1.2 <https://blogs.apache.org/maven/entry/apache-maven-dependency-plugin-version2>`__

* maven-enforcer-plugin `3.0.0-M2 → 3.0.0-M3 <https://blogs.apache.org/maven/entry/apache-maven-enforcer-version-3>`__

* maven-javadoc-plugin `3.1.1 → 3.2.0 <https://blogs.apache.org/maven/entry/apache-maven-javadoc-plugin-version1>`__

* maven-shade-plugin `3.2.1 → 3.2.2 <https://blogs.apache.org/maven/entry/apache-maven-shade-plugin-version3>`__

* modernizer-maven-plugin `2.0.0 → 2.1.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.1.0>`__

* pmd-maven-plugin `3.12.0 → 3.13.0 <https://blogs.apache.org/maven/entry/apache-maven-pmd-plugin-version2>`__

Version 6.0.5
-------------
This is a bug-fix upgrade from version 6.0.4.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~
* Akka 2.5.26 → 2.5.29, release notes:
  * `2.5.27 <https://akka.io/blog/news/2019/12/10/akka-2.5.27-released>`__
  * `2.5.29 <https://akka.io/blog/news/2020/01/28/akka-2.5.29-released>`__

* commons-codec `1.13 → 1.14 <https://commons.apache.org/proper/commons-codec/changes-report.html#a1.14>`__

* Jackson `2.9.10 → 2.9.10.20200103 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9#micro-patches>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-source-plugin `3.2.0 → 3.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317924&version=12346480>`__

Version 6.0.4
-------------
This is a bug-fix upgrade from version 6.0.3.

Bug fixes
~~~~~~~~~
* ``single-feature-test`` was using outdated repositories, including Maven Central,
  which `broke on Jan 15, 2020 <https://support.sonatype.com/hc/en-us/articles/360041287334-Central-501-HTTPS-Required>`__.
  This has been corrected.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

* JUnit 4.11 → 4.13, release notes:
  * `4.12 <https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.12.md>`__
  * `4.13 <https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.13.md>`__

* log4j2 2.11.2 → 2.13.0, release notes:
  * `2.12.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.13.0>`__
  * `2.12.1 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.12.1>`__
  * `2.13.0 <https://logging.apache.org/log4j/2.x/changes-report.html#a2.13.0>`__

* netty 4.1.42 → 4.1.45, release notes:
  * `4.1.43 <https://netty.io/news/2019/10/24/4-1-43-Final.html>`__
  * `4.1.44 <https://netty.io/news/2019/12/18/4-1-44-Final.html>`__
  * `4.1.45 <https://netty.io/news/2020/01/13/4-1-45-Final.html>`__

Plugin upgrades
~~~~~~~~~~~~~~~
* maven-assembly-plugin 2.2-beta5 → 3.2.0

* maven-archetype-plugin `3.1.1 → 3.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317122&version=12345957>`__

Version 6.0.3
-------------
This is a bug-fix upgrade from version 6.0.2.

Bug fixes
~~~~~~~~~
* The fix for `ODLPARENT-216 <https://jira.opendaylight.org/browse/ODLPARENT-216>`__ ended up
  breaking ``org.kohsuke.metainf-services`` integration. While this could be fixed in downstreams
  by adding proper </annotationProcessorPaths> entry, it is a regression from 6.0.1.

Version 6.0.2
-------------
This is a security/bug-fix upgrade from version 6.0.1.

Bug fixes
~~~~~~~~~
* ``single-feature-parent`` was setting up Karaf repositories incorrectly, leading to
  the test using unpatched Karaf resources. This has now been fixed and the test run
  is using environment equivalent to the contents of the distribution. See
  `ODLPARENT-209 <https://jira.opendaylight.org/browse/ODLPARENT-209>`__ for details.

* ``immutables.org`` integration relied on pre-JDK9 way of integration, where the annotation processor
  was just dropped as a dependency. This does not work with JDK9+ artifacts which are also explicit
  JMPS modules. Note that users are advised to switch to depending on the ``annotations``-classified
  artifact. See `ODLPARENT-216 <https://jira.opendaylight.org/browse/ODLPARENT-216>`__ for details.

* Assembled Karaf distribution did not perform proper JDK checks and allowed launching with JDK8,
  leading to a failure to install OpenDaylight components with an error stack, which confuses users
  not familiar with OSGi. The distribution now refuses to start with anything other than JDK11. See
  `ODLPARENT-218 <https://jira.opendaylight.org/browse/ODLPARENT-218>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

* Akka `2.5.25 → 2.5.26 <https://akka.io/blog/news/2019/10/17/akka-2.5.26-released>`__

* Checkstyle 8.20 → 8.26, release notes:
  * `8.21 <https://checkstyle.org/releasenotes.html#Release_8.21>`__
  * `8.22 <https://checkstyle.org/releasenotes.html#Release_8.22>`__
  * `8.23 <https://checkstyle.org/releasenotes.html#Release_8.23>`__
  * `8.24 <https://checkstyle.org/releasenotes.html#Release_8.24>`__
  * `8.25 <https://checkstyle.org/releasenotes.html#Release_8.25>`__
  * `8.26 <https://checkstyle.org/releasenotes.html#Release_8.26>`__

* H2 database `1.4.199 → 1.4.200 <http://www.h2database.com/html/changelog.html>`__

* Hamcrest `2.1 → 2.2 <https://github.com/hamcrest/JavaHamcrest/releases/tag/v2.2>`__

* JaCoCo `0.8.4 → 0.8.5 <https://github.com/jacoco/jacoco/releases/tag/v0.8.5>`__

* Karaf 4.2.2 → 4.2.6, release notes:
  * `4.2.3 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12344587>`__
  * `4.2.4 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12344856>`__
  * `4.2.5 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12345153>`__
  * `4.2.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12345365>`__

* Powermock `2.0.2 → 2.0.4 <https://github.com/powermock/powermock/blob/release/2.x/docs/release-notes/official.md#204>`__

* Sevntu `1.35.0 → 1.36.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.36.0>`__

Plugin upgrades
~~~~~~~~~~~~~~~

* maven-bundle-plugin `4.1.0 → 4.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12345491>`__

* maven-jar-plugin `3.1.2 → 3.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317526&version=12345503>`__

* maven-source-plugin `3.1.0 → 3.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317924&version=12345522>`__

* modernizer-maven-plugin `1.9.0 → 2.0.0 <https://github.com/gaul/modernizer-maven-plugin/releases/tag/modernizer-maven-plugin-2.0.0>`__

Version 6.0.1
-------------
This is a security/bug-fix upgrade from version 6.0.0.

Bug fixes
~~~~~~~~~
* ``karaf-plugin`` ignored exceptions coming from its failure to resolve ${karaf.etc} variable. This
  has now been fixed and the URL handling has been revised to fix build on Windows. See
  `ODLPARENT-214 <https://jira.opendaylight.org/browse/ODLPARENT-214>`__ for details.

* ``leveldb-jni`` jar, which has been seeded to nexus.opendaylight.org long time ago is not published
  in Maven Central. This has been resolved by repackaging this jar and publishing it from odlparent.
  See `ODLPARENT-210 <https://jira.opendaylight.org/browse/ODLPARENT-210>`__ for details.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

* Bouncy Castle `1.63 → 1.64 <http://www.bouncycastle.org/releasenotes.html>`__

* Jackson `2.9.9 → 2.9.10 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.10>`__

* Javassist 3.24.1-GA → 3.26.0-GA

* Guice 4.1.0 → 4.2.2, release notes:
  * `4.2.0 <https://github.com/google/guice/wiki/Guice42>`__
  * `4.2.1 <https://github.com/google/guice/wiki/Guice421>`__
  * `4.2.2 <https://github.com/google/guice/wiki/Guice422>`__

* Mockito 2.25.1 → 2.28.2, release notes:
  * `2.25.2 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2252>`__
  * `2.25.3 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2253>`__
  * `2.25.4 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2254>`__
  * `2.25.5 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2255>`__
  * `2.25.6 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2256>`__
  * `2.25.7 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2257>`__
  * `2.26.1 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2261>`__
  * `2.26.2 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2262>`__
  * `2.27.1 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2271>`__
  * `2.27.2 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2272>`__
  * `2.27.3 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2273>`__
  * `2.27.4 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2274>`__
  * `2.27.5 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2275>`__
  * `2.28.0 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2280>`__

* Netty 4.1.39 → 4.1.42, release notes:
  * `4.1.41 <https://netty.io/news/2019/09/12/4-1-41-Final.html>`__
  * `4.1.42 <https://netty.io/news/2019/09/25/4-1-42-Final.html>`__

* pt-triemap `1.0.6 → 1.1.0 <https://github.com/PantheonTechnologies/triemap/releases/tag/triemap-1.1.0>`__

* reactive-streams `1.0.2 → 1.0.3 <http://www.reactive-streams.org/announce-1.0.3>`__

* stax2-api `3.1.4 → 4.2 <https://github.com/FasterXML/stax2-api/blob/master/release-notes/VERSION>`__

* woodstox-core 5.0.3 → 5.3.0, release notes:
  * `5.1.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-5.1>`__
  * `5.2.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-5.2>`__
  * `5.3.0 <https://github.com/FasterXML/woodstox/wiki/Woodstox-Release-5.3>`__

Version 6.0.0
-------------
This is a major upgrade from version 5, with breaking changes; projects will
need to make changes to upgrade to this version.

Java 11 is required
~~~~~~~~~~~~~~~~~~~
This release sets ``maven.compiler.release=11`` and enforces that the JDK used to build
is Java 11+. As there may be issues with various maven plugins when faced with JDK9+
constructs and JDK11+ classes, target release can be controlled on a per-artifact
basis (i.e. target Java 10 with ``maven.compiler.release=10`` property).

This release has been validated with ``openjdk-11.0.4`` and is not supported on any lower
version. As usual, we recommend using latest available JDK/JRE for Java 11 during development
and deployment.

Checkstyle/SpotBugs/Modernizer run by default
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
With this release code artifacts always run ``maven-checkstyle-plugin``, ``spotbugs-maven-plugin``
and ``modernizer-maven-plugin``. Checkstyle and SpotBugs run in enforcing mode, i.e. will fail
build if any violations are found. Modernizer is configured to report Java 8-compatible constructs
and will not fail the build unless instructed to do so.

Behavior of each of these is controlled via a maven property on a per-artifact basis:

* ``odlparent.checkstyle.enforce`` controls checkstyle enforcement: defaults to ``true``, but can be set to ``false``
* ``odlparent.checkstyle.skip`` controls checkstyle invocation: defaults to ``false``, but can be set to ``true``
* ``odlparent.spotbugs.enforce`` controls SpotBugs enforcement: defaults to ``true``, but can be set to ``false``
* ``odlparent.spotbugs.skip`` controls SpotBugs invocation: defaults to ``false``, but can be set to ``true``
* ``odlparent.modernizer.enforce`` controls modernizer enforcement: defaults to ``false``, but can be set to ``true``
* ``odlparent.modernizer.skip`` controls modernizer invocation: defaults to ``false``, but can be set to ``true``
* ``odlparent.modernizer.target`` controls modernizer Java version: defaults to ``1.8``, but can be set to ``1.11`` or similar

Bug fixes
~~~~~~~~~

* ``blueprint container`` had ``org.apache.aries.blueprint.preemptiveShutdown`` set to false
  to enable it to work with Config Subsystem. As that component is long gone, this property has
  been removed as part of `ODLPARENT-34 <https://jira.opendaylight.org/browse/ODLPARENT-34>`__.
  Furthermore, system properties related to Config Subsystem/NETCONF integration have been removed
  as well.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been removed from dependency management:

* com.google.code.findbugs/jsr305

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

The dependency on `xmlunit-assertj` and `modernizer-maven-annotations` has been added and the following
dependencies have been upgraded:

* Akka 2.5.23 → 2.5.25, release notes:
  * `2.5.24 <https://akka.io/blog/news/2019/08/09/akka-2.5.24-released>`__
  * `2.5.25 <https://akka.io/blog/news/2019/08/20/akka-2.5.25-released>`__

* apache-sshd `2.2.0 → 2.3.0 <https://github.com/apache/mina-sshd/blob/master/docs/changes/2.3.0.md>`__

* Bouncy Castle `1.62 → 1.63 <https://www.bouncycastle.org/releasenotes.html>`__

* commons-beanutils `1.9.3 → 1.9.4 <https://www.apache.org/dist/commons/beanutils/RELEASE-NOTES.txt>`__

* commons-codec `1.12 → 1.13 <http://www.apache.org/dist/commons/codec/RELEASE-NOTES.txt>`__

* commons-text 1.6 → 1.8, release notes:
  * `1.7 <https://commons.apache.org/proper/commons-text/changes-report.html#a1.7>`__
  * `1.8 <https://commons.apache.org/proper/commons-text/changes-report.html#a1.8>`__

* Checkstyle 8.18 → 8.20, release notes:
  * `8.19 <https://checkstyle.org/releasenotes.html#Release_8.19>`__
  * `8.20 <https://checkstyle.org/releasenotes.html#Release_8.20>`__

* jackson-databind `2.9.9 → 2.9.9.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9#micro-patches>`__

* jaxb-api 2.2.8 → 2.3.0, aligning it with Karaf-provided version

* Netty 4.1.36 → 4.1.39, release notes:
  * `4.1.37 <https://netty.io/news/2019/06/28/4-1-37-Final.html>`__
  * `4.1.38 <https://netty.io/news/2019/07/24/4-1-38-Final.html>`__
  * `4.1.39 <https://netty.io/news/2019/08/13/4-1-39-Final.html>`__

* Sevntu 1.32.0 → 1.35.0, release notes:
  * `1.33.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.33.0>`__
  * `1.34.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.34.0>`__
  * `1.34.1 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.34.1>`__
  * `1.35.0 <https://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.35.0>`__

* Scala 2.12.8 → 2.12.9, release notes:
  * `2.12.9 <https://github.com/scala/scala/releases/tag/v2.12.9>`__
  * `2.12.10 <https://github.com/scala/scala/releases/tag/v2.12.10>`__

* slf4j `1.7.25 → 1.7.28 <https://www.slf4j.org/news.html>`__

* triemap `1.0.5 → 1.0.6 <https://github.com/PantheonTechnologies/triemap/releases/tag/triemap-1.0.6>`__

* typesafe/ssl-config `0.3.7 → 0.3.8 <https://github.com/lightbend/ssl-config/compare/v0.3.7...v0.3.8>`__

* Xtend 1.17.1 → 1.19.0, release notes:
  * `1.18.0 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2019/06/04/version-2-18-0>`__
  * `1.19.0 <https://www.eclipse.org/Xtext/releasenotes.html#/releasenotes/2019/09/03/version-2-19-0>`__

Plugin upgrades
~~~~~~~~~~~~~~~

* git-commit-id-plugin 2.2.6 → 3.0.1, release notes:
  * `3.0.0 <https://github.com/git-commit-id/maven-git-commit-id-plugin/releases/tag/v3.0.0>`__
  * `3.0.1 <https://github.com/git-commit-id/maven-git-commit-id-plugin/releases/tag/v3.0.1>`__

* maven-javadoc-plugin `3.1.0 → 3.1.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12345060>`__

* pmd-maven-plugin `3.11.0 → 3.12.0 <https://blogs.apache.org/maven/entry/apache-maven-pmd-plugin-version1>`__

* spotbugs-maven-plugin 3.1.11  → 3.1.12.2, release notes:
  * `3.1.12 <https://github.com/spotbugs/spotbugs-maven-plugin/compare/spotbugs-maven-plugin-3.1.11...spotbugs-maven-plugin-3.1.12>`__
  * `3.1.12.1 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-3.1.12.1>`__
  * `3.1.12.2 <https://github.com/spotbugs/spotbugs-maven-plugin/releases/tag/spotbugs-maven-plugin-3.1.12.2>`__

Version 5.0.1
-------------

This is a bug-fix upgrade from version 5.0.0.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

The following dependencies have been upgraded:

* Akka 2.5.21 → 2.5.23, release notes:
  * `2.5.22 <https://akka.io/blog/news/2019/04/03/akka-2.5.22-released>`__
  * `2.5.23 <https://akka.io/blog/news/2019/05/21/akka-2.5.23-released>`__

* asciidoctorj-diagram 1.5.12 → 1.5.16

* Bouncy Castle `1.61 → 1.62 <http://www.bouncycastle.org/releasenotes.html>`__

* commons-lang3 `3.8.1 → 3.9 <http://www.apache.org/dist/commons/lang/RELEASE-NOTES.txt>`__

* JaCoCo `0.8.3 → 0.8.4 <https://github.com/jacoco/jacoco/releases/tag/v0.8.4>`__

* Jackson `2.9.8 → 2.9.9 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.9>`__

* Jolokia 1.6.0 → 1.6.2, release notes:
  * `1.6.1 <https://jolokia.org/changes-report.html#a1.6.1>`__
  * `1.6.2 <https://jolokia.org/changes-report.html#a1.6.2>`__

* Mockito `2.25.0 → 2.25.1 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2251>`__

* Netty 4.1.34 → 4.1.36, release notes:
  * `4.1.35 <https://netty.io/news/2019/04/17/4-1-35-Final.html>`__
  * `4.1.36 <https://netty.io/news/2019/04/30/4-1-36-Final.html>`__

* PowerMock 2.0.0 → 2.0.2, release notes:
  * `2.0.1 <https://github.com/powermock/powermock/releases/tag/powermock-2.0.1>`__
  * `2.0.2 <https://github.com/powermock/powermock/releases/tag/powermock-2.0.2>`__

* Reactive Streams `1.0.1 → 1.0.2 <https://www.lightbend.com/blog/update-reactive-streams-102-released>`__

* scala-parser-combinators `1.1.1 → 1.1.2 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.1.2>`__

* Sevntu `1.32.0 → 1.33.0 <http://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.33.0>`__

* Typesafe Config `1.3.2 → 1.3.3 <https://github.com/lightbend/config/releases/tag/v1.3.3>`__

* triemap `1.0.4 → 1.0.5 <https://github.com/PantheonTechnologies/triemap/releases/tag/triemap-1.0.5>`__

* XBean finder 4.12 → 4.14, release notes:
  * `4.13 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?version=12344253&projectId=12310312>`__
  * `4.14 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310312&version=12345220>`__

* xmlunit `2.6.2 → 2.6.3 <https://github.com/xmlunit/xmlunit/releases/tag/v2.6.3>`__

* Xtend `2.17.0 → 2.17.1 <https://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2019/04/03/version-2-17-1>`__

Plugin upgrades
~~~~~~~~~~~~~~~

* maven-archetype-plugin 3.0.1 → 3.1.1, release notes:
  * `3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317122&version=12340346>`__
  * `3.1.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317122&version=12345450>`__

* maven-bundle-plugin `4.1.0 → 4.2.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310100&version=12345047>`__

* maven-checkstyle-plugin `3.1.0 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317223&version=12342397>`__

* maven-compiler-plugin `3.8.0 → 3.8.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317225&version=12343484>`__

* maven-help-plugin `3.1.1 → 3.2.0 <https://blog.soebes.de/blog/2019/04/22/apache-maven-help-plugin-version-3-dot-2-0-released/>`__

* maven-jar-plugin `3.1.1 → 3.1.2 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317526&version=12344629>`__

* maven-plugin-plugin `3.5.2 → 3.6.0 <https://blogs.apache.org/maven/entry/apache-maven-plugin-tools-version1>`__

* maven-source-plugin `3.0.1 → 3.1.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317924&version=12336941>`__

* maven-surefire-plugin 2.22.1 → 2.22.2

New plugins
~~~~~~~~~~~

* ``modernizer-maven-plugin`` declared, allowing downstreams to more easily activate it,
  and so detect code which should be updated to more modern equivalent.

New features
~~~~~~~~~~~~

* ``odl-caffeine-2`` provides a pre-packaged feature for the Caffeine caching framework,
  along with the Guava compabitility layer.

Version 5.0.0
-------------

This is a major upgrade from version 4, with breaking changes; projects will
need to make changes to upgrade to this version.

The most significant change is `ODLPARENT-198 <https://jira.opendaylight.org/browse/ODLPARENT-198>`__,
which removes JSR305 from default dependencies and does not present it at class path by default.

Deleted artifacts
~~~~~~~~~~~~~~~~~

``findbugs`` has been removed, as its only purpose was to provide FindBugs
rule definitions. Equivalent definitions are available in ``spotbugs``.

Bug fixes
~~~~~~~~~

* ``blueprint-maven-plugin`` used to scan the entire classpath, resulting in potential conflicts
  across projects. Scanning is now limited to ``${project.groupId}``, limiting conflict domain
  to single project. See `ODLPARENT-109 <https://jira.opendaylight.org/browse/ODLPARENT-109>`__.

* ``bundle-maven-plugin`` configuration ignored generated ServiceLoader service entries,
  which has now been fixed. See `ODLPARENT-197 <https://jira.opendaylight.org/browse/ODLPARENT-197>`__.

* Bundle tests are now enabled by default. See `ODLPARENT-158 <https://jira.opendaylight.org/browse/ODLPARENT-158>`__
  and `ODLPARENT-80 <https://jira.opendaylight.org/browse/ODLPARENT-80>`__ for details.

* Karaf log file rollover was not explictly set up, leading to inability to easily override
  the defaults. See `ODLPARENT-153 <https://jira.opendaylight.org/browse/ODLPARENT-153>`__ for details.

* Karaf log file used to use default maximum 16MiB file size, this has now been increased to 64MiB.
  See `ODLPARENT-154 <https://jira.opendaylight.org/browse/ODLPARENT-154>`__.

* ``features-test`` excluded opendaylight-karaf-empty's transitive dependencies, leading
  to the need to re-declare them in single-feature-parent. This re-declaration was forgotten
  in for ``bcpkix-framework-ext`` and ``bcprov-framework-ext`` bundles, which lead to them
  not being present in the local repository. See `ODLPARENT-130 <https://jira.opendaylight.org/browse/ODLPARENT-130>`__.

Upstream version removals
~~~~~~~~~~~~~~~~~~~~~~~~~

The following upstream dependencies have been removed from dependency management:

* cassandra-driver-core

* org.codehaus.enunciate/enunciate-core-annotations

* org.jboss.resteasy/jaxrs-api

* org.json/json

* org.osgi/org.osgi.compendium

Removed features
~~~~~~~~~~~~~~~~

* odl-jersey-1

* ``features-akka`` feature repository has been integrated into ``features-odlparent``

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

The following dependencies have been upgraded:

* apache-sshd `2.0.0 → 2.2.0 <https://github.com/apache/mina-sshd/compare/sshd-2.0.0...sshd-2.2.0>`__

* Aeron 1.12.0 → 1.15.3, release notes:
  * `1.13.0 <https://github.com/real-logic/aeron/releases/tag/1.13.0>`__
  * `1.14.0 <https://github.com/real-logic/aeron/releases/tag/1.14.0>`__
  * `1.15.0 <https://github.com/real-logic/aeron/releases/tag/1.15.0>`__
  * `1.15.1 <https://github.com/real-logic/aeron/releases/tag/1.15.1>`__
  * `1.15.2 <https://github.com/real-logic/aeron/releases/tag/1.15.2>`__
  * `1.15.3 <https://github.com/real-logic/aeron/releases/tag/1.15.3>`__

* Agrona 0.9.27 → 0.9.33, release notes:
  * `0.9.28 <https://github.com/real-logic/agrona/releases/tag/0.9.28>`__
  * `0.9.29 <https://github.com/real-logic/agrona/releases/tag/0.9.29>`__
  * `0.9.30 <https://github.com/real-logic/agrona/releases/tag/0.9.30>`__
  * `0.9.31 <https://github.com/real-logic/agrona/releases/tag/0.9.31>`__
  * `0.9.32 <https://github.com/real-logic/agrona/releases/tag/0.9.32>`__
  * `0.9.33 <https://github.com/real-logic/agrona/releases/tag/0.9.33>`__

* Akka 2.5.19 → 2.5.21, release notes:
  * `2.5.20 <https://akka.io/blog/news/2019/01/29/akka-2.5.20-released>`__
  * `2.5.21 <https://akka.io/blog/news/2019/02/13/akka-2.5.21-released>`__

* antl4r `4.7.1 → 4.7.2 <https://github.com/antlr/antlr4/releases/tag/4.7.2>`__

* asciidoctorj-diagram 1.5.11 → 1.5.12

* Bouncy Castle `1.60 → 1.61 <http://www.bouncycastle.org/releasenotes.html>`__

* checkstyle 8.16 → 8.18, release notes:
  * `8.17 <http://checkstyle.sourceforge.net/releasenotes.html#Release_8.17>`__
  * `8.18 <http://checkstyle.sourceforge.net/releasenotes.html#Release_8.18>`__

* commons-codec `1.11 → 1.12 <http://www.apache.org/dist/commons/codec/RELEASE-NOTES.txt>`__

* Google Error Prone 2.3.2 → 2.3.3

* Google Guava 25.1 → 27.1, release notes:
  * `26.0 <https://github.com/google/guava/releases/tag/v26.0>`__
  * `27.0 <https://github.com/google/guava/releases/tag/v27.0>`__
  * `27.0.1 <https://github.com/google/guava/releases/tag/v27.0.1>`__
  * `27.1 <https://github.com/google/guava/releases/tag/v27.1>`__

* Google Truth `0.42 → 0.43 <https://github.com/google/truth/releases/tag/release_0_43>`__

* h2 database `1.4.196 → 1.4.199 <http://www.h2database.com/html/changelog.html>`__

* Immutables `2.7.3 → 2.7.5 <https://github.com/immutables/immutables/#changelog>`__

* Javassist `3.24.0-GA → 3.24.1-GA <https://github.com/jboss-javassist/javassist/releases/tag/rel_3_24_1_ga>`__

* log4j2 `2.11.1 → 2.11.2 <https://github.com/apache/logging-log4j2/blob/log4j-2.11.2/RELEASE-NOTES.md>`__

* Mockito 2.23.4 → 2.25.0, release notes:
  * `2.24.0 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2240>`__
  * `2.25.0 <https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md#2250>`__

* Netty 4.1.32 → 4.1.34, release notes:
  * `4.1.32 <https://netty.io/news/2018/11/29/4-1-32-Final.html>`__
  * `4.1.33 <https://netty.io/news/2019/01/21/4-1-33-Final.html>`__
  * `4.1.34 <https://netty.io/news/2019/03/08/4-1-34-Final.html>`__

* OSGi 5.0.0 → 6.0.0

* Powermockito 1.7.4 → 2.0.0, release notes:
  * `2.0.0-beta.5 <https://github.com/powermock/powermock/releases/tag/powermock-2.0.0-beta.5>`__
  * `2.0.0-RC.1 <https://github.com/powermock/powermock/releases/tag/powermock-2.0.0-RC.1>`__
  * `2.0.0 <https://github.com/powermock/powermock/releases/tag/powermock-2.0.0>`__

* SpotBugs `3.1.9 → 3.1.12 <https://github.com/spotbugs/spotbugs/blob/3.1.12/CHANGELOG.md>`__

* ThreeTen `1.4.0 → 1.5.0 <https://www.threeten.org/threeten-extra/changes-report.html#a1.5.0>`__

* Xtend `2.16.0 → 2.17.0 <http://www.eclipse.org/xtend/releasenotes.html#/releasenotes/2019/03/05/version-2-17-0>`__

Plugin removals
~~~~~~~~~~~~~~~

* gmaven-plugin

* maven-findbugs-plugin

Plugin upgrades
~~~~~~~~~~~~~~~

* jacoco-maven-plugin `0.8.2 → 0.8.3 <https://github.com/jacoco/jacoco/releases/tag/v0.8.3>`__

* maven-invoker-plugin `3.1.0 → 3.2.0 <https://mail-archives.apache.org/mod_mbox/maven-announce/201901.mbox/%3Cop.zvzdg9tbkdkhrr@desktop-2khsk44%3E>`__

* maven-javadoc-plugin `3.0.1 → 3.1.0 <https://mail-archives.apache.org/mod_mbox/maven-announce/201903.mbox/%3C6064d830-474c-4b43-afef-99502c3a305a%40getmailbird.com%3E>`__

* spotbugs-maven-plugin 3.1.8 → 3.1.11

Version 4.0.9
-------------

This is a bug-fix upgrade from version 4.0.8.

Bug fixes
~~~~~~~~~

* ``karaf-plugin`` invocation in ``karaf4-parent`` caused previously
  patched features to be overwritten with their stock versions, referencing
  bundles which were not populated in the local repository. (See
  `ODLPARENT-194 <https://jira.opendaylight.org/browse/ODLPARENT-194>`__.)

* ``karaf-plugin`` version in ``karaf4-parent`` is now provided through
  plugin management so downstreams can override it without needing
  to repeat its configuration.

* ``karaf-plugin`` no longer reads features twice when running discovery,
  speeding up the process a bit.

* Recent versions of the SpotBugs Maven plugin use SLF4J 1.8 beta 2, which
  can’t use the 1.7.25 implementation we provide; we therefore provide an
  implementation of 1.8 beta 2 when SpotBugs is used. (See
  `ODLPARENT-184 <https://jira.opendaylight.org/browse/ODLPARENT-184>`__.)

New features
~~~~~~~~~~~~

* ``odl-woodstox`` wraps the Woodstox StAX implementation which is imposed on
  us by Karaf.

Version 4.0.8
-------------

This is a bug-fix and minor upstream bump upgrade from version 4.0.7.

Bug fixes
~~~~~~~~~

* ``bcprov-ext-jdk15on`` is a superset of ``bcprov-jdk15on``, so there’s no
  need to ship both; we now only ship the former. In addition, we install the
  Bouncy Castle JARs in ``lib/boot`` so that they continue to be available on
  the boot classpath (JDK 9 removes the extension mechanism which was used
  previously), and provide the corresponding bundles from the boot classpath
  instead of using separate JARs in the system repository. (See
  `ODLPARENT-183 <https://jira.opendaylight.org/browse/ODLPARENT-183>`__ and
  `ODLPARENT-185 <https://jira.opendaylight.org/browse/ODLPARENT-185>`__.)

* A dependency check has been added to ensure that we don’t run into the
  TrieMap dependency bug in 4.0.6 again.

* Dependencies pulled in by features are now checked for convergence, and
  ``karaf-plugin`` warns when it finds diverging dependencies (the same
  artifact with two different versions). Upstream-provided features are
  patched to avoid the following divergences (and upgrade some dependencies in
  the process):

  * Aries utilities 1.1.0/1.1.3 (upgraded to 1.1.3).
  * Commons Beanutils 1.8.3/1.9.3 (upgraded to 1.9.3).
  * Commons Codec 1.8/1.10 (upgraded to 1.11).
  * ``javax.mail`` 1.4.4/1.4.7 (upgraded to 1.4.7).

  (See `ODLPARENT-189 <https://jira.opendaylight.org/browse/ODLPARENT-189>`__.)

New features
~~~~~~~~~~~~

* ``odl-dropwizard-metrics`` provides Dropwizard Metrics (which are also
  available in dependency management).

* ``enunciate-maven-plugin`` is added as the replacement for
  ``maven-enunciate-plugin``.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

The following dependencies are no longer provided by the JVM, starting with
version 11, but we make them available via dependency management for projects
which need them:

* ``javax.annotation-api``.

* JAXB (``jaxb-core``, ``jaxb-impl``).

The following dependencies have been upgraded:

* Checkstyle `8.15 → 8.16 <https://checkstyle.org/releasenotes.html#Release_8.16>`__.

* Dependency Check `4.0.0 → 4.0.2 <https://github.com/jeremylong/DependencyCheck/blob/master/RELEASE_NOTES.md>`__.

* ``git-commit-id`` `2.2.5 → 2.2.6 <https://github.com/ktoso/maven-git-commit-id-plugin/releases>`__.

* Immutables 2.7.1 → 2.7.3:

  * `2.7.2 <https://github.com/immutables/immutables#272-2018-11-05>`__.
  * `2.7.3 <https://github.com/immutables/immutables#273-2018-11-10>`__.

  (2.7.4 breaks our Javadocs.)

* Jackson `2.9.7 → 2.9.8 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.8>`__.

Version 4.0.7
-------------

This is a bug-fix release, correcting the ``triemap`` import declaration.

Version 4.0.6
-------------

This is a bug-fix and minor upstream packaging upgrade from version 4.0.5.

Bug fixes
~~~~~~~~~

Single-feature-test was broken with JDK 9 and later and Karaf 4.2.2; this
release adds the additional JVM configuration needed.

Third-party dependencies
~~~~~~~~~~~~~~~~~~~~~~~~

This release adds the ``triemap`` BOM to dependency management.

Version 4.0.5
-------------

This is a bug-fix release: the Karaf Maven plugin, in version 4.2.2, is
`broken <https://issues.apache.org/jira/browse/KARAF-6057>`__ in some cases we
need in OpenDaylight; we revert to 4.2.1 in ``karaf4-parent`` to avoid this.

Version 4.0.4
-------------

This is a bug-fix release, reverting the change made in 4.0.3 to handle
building with either ``zip`` or ``tar.gz`` Karaf archives (which breaks
builds in our infrastructure, without the empty Karaf archive).

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

`This Wiki page <https://wiki.opendaylight.org/view/Neon_platform_upgrade>`__
has detailed step-by-step migration instructions.

ODL Parent 4 requires Maven 3.5.3 or later; this is needed in particular to
enable SpotBugs support with current versions of the SpotBugs plugin.

Known issues
~~~~~~~~~~~~

This release’s SpotBugs support doesn’t handle Guava 25.1 correctly, resulting
in false-positives regarding null handling; see
`ODLPARENT-161 <https://jira.opendaylight.org/browse/ODLPARENT-161>`__ for
details. Until this is fixed, the corresponding warnings are disabled, which
matches our existing FindBugs configuration (which suffers from the a variant
of this, with the same consequences).

We are planning on upgrading Akka during the 4.x cycle, even if it results in
a technically breaking upgrade. This is currently blocked on an OSGi bug in
Akka; see `Akka issue 25579 <https://github.com/akka/akka/issues/25579>`__ for
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

See `this Gerrit patch <https://git.opendaylight.org/gerrit/75699>`__ for an
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
`the SLF4J extension <http://kengotoda.github.io/findbugs-slf4j/>`__ (version
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

  * `1.8.0 <https://github.com/real-logic/aeron/releases/tag/1.8.0>`__.
  * `1.8.1 <https://github.com/real-logic/aeron/releases/tag/1.8.1>`__.
  * `1.8.2 <https://github.com/real-logic/aeron/releases/tag/1.8.2>`__.
  * `1.9.0 <https://github.com/real-logic/aeron/releases/tag/1.9.0>`__.
  * `1.9.1 <https://github.com/real-logic/aeron/releases/tag/1.9.1>`__.
  * `1.9.2 <https://github.com/real-logic/aeron/releases/tag/1.9.2>`__.
  * `1.9.3 <https://github.com/real-logic/aeron/releases/tag/1.9.3>`__.

* Agrona 0.9.12 → 0.9.21:

  * `0.9.13 <https://github.com/real-logic/agrona/releases/tag/0.9.13>`__.
  * `0.9.14 <https://github.com/real-logic/agrona/releases/tag/0.9.14>`__.
  * `0.9.15 <https://github.com/real-logic/agrona/releases/tag/0.9.15>`__.
  * `0.9.16 <https://github.com/real-logic/agrona/releases/tag/0.9.16>`__.
  * `0.9.17 <https://github.com/real-logic/agrona/releases/tag/0.9.17>`__.
  * `0.9.18 <https://github.com/real-logic/agrona/releases/tag/0.9.18>`__.
  * `0.9.19 <https://github.com/real-logic/agrona/releases/tag/0.9.19>`__.
  * `0.9.20 <https://github.com/real-logic/agrona/releases/tag/0.9.20>`__.
  * `0.9.21 <https://github.com/real-logic/agrona/releases/tag/0.9.21>`__.

* Akka 2.5.11 → 2.5.14:

  * `2.5.12 <https://akka.io/blog/news/2018/04/13/akka-2.5.12-released>`__.
  * `2.5.13 <https://akka.io/blog/news/2018/06/08/akka-2.5.13-released>`__.
  * `2.5.14 <https://akka.io/blog/news/2018/07/13/akka-2.5.14-released>`__.

* ASM 5.1 → 6.2.1 (synchronised with Karaf).

* Bouncy Castle `1.59 → 1.60 <https://www.bouncycastle.org/releasenotes.html>`__.

* Checkstyle `8.4 → 8.12 <http://checkstyle.sourceforge.net/releasenotes.html#Release_8.12>`__.

* Commons Lang `3.7 → 3.8 <http://www.apache.org/dist/commons/lang/RELEASE-NOTES.txt>`__.

* Commons Text 1.1 → 1.4:

  * `1.2 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.2.txt>`__.
  * `1.3 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.3.txt>`__.
  * `1.4 <https://commons.apache.org/proper/commons-text/release-notes/RELEASE-NOTES-1.4.txt>`__.

* Eclipse JDT annotations 2.1.150 → 2.2.0.

* EclipseLink Moxy JAXB `2.7.1 → 2.7.3 <https://www.eclipse.org/eclipselink/releases/2.7.php>`__.

* Enunciate core annotations
  `2.10.1 → 2.11.1 <https://github.com/stoicflame/enunciate/releases>`__.

* Felix Metatype 1.1.6 → 1.2.0 (synchronised with Karaf).

* Google Truth `0.40 → 0.42 <https://github.com/google/truth/releases>`__.

* Guava 23.6.1 → 25.1:

  * `23.4 <https://github.com/google/guava/releases/tag/v23.4>`__.
  * `23.5 <https://github.com/google/guava/releases/tag/v23.5>`__.
  * `23.6 <https://github.com/google/guava/releases/tag/v23.6>`__.
  * `24.0 <https://github.com/google/guava/releases/tag/v24.0>`__.
  * `24.1 <https://github.com/google/guava/releases/tag/v24.1>`__.
  * `25.0 <https://github.com/google/guava/releases/tag/v25.0>`__.
  * `25.1 <https://github.com/google/guava/releases/tag/v25.1>`__.

* Immutables `2.5.6 → 2.7.1 <https://github.com/immutables/immutables/blob/master/README.md#changelog>`__.

* Jackson 2.8.9 → 2.9.6:

  * `2.9 feature overview <https://medium.com/@cowtowncoder/jackson-2-9-features-b2a19029e9ff>`__.
  * `2.9 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9>`__.
  * `2.9.1 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.1>`__.
  * `2.9.2 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.2>`__.
  * `2.9.3 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.3>`__.
  * `2.9.4 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.4>`__.
  * `2.9.5 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.5>`__.
  * `2.9.6 <https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.9.6>`__.

* JaCoCo `0.8.1 → 0.8.2 <https://github.com/jacoco/jacoco/releases/tag/v0.8.2>`__.

* Javassist 3.22.0 → 3.23.1. This provides compatibility with Java 9 and later,
  and `fixes a file handle leak <https://github.com/jboss-javassist/javassist/issues/165>`__.

* Jettison 1.3.8 → 1.4.0.

* Jetty 9.3.21 → 9.4.11 (synchronised with Karaf):

  * `9.4.0 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00097.html>`__.
  * `9.4.1 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00100.html>`__.
  * `9.4.2 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00101.html>`__.
  * `9.4.3 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00102.html>`__.
  * `9.4.4 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00105.html>`__.
  * `9.4.5 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00107.html>`__.
  * `9.4.6 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00109.html>`__.
  * `9.4.7 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00111.html>`__.
  * `9.4.8 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00114.html>`__.
  * `9.4.9 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00117.html>`__.
  * `9.4.10 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00119.html>`__.
  * `9.4.11 <https://dev.eclipse.org/mhonarc/lists/jetty-announce/msg00122.html>`__.

* Jolokia OSGi `1.5.0 → 1.6.0 <https://jolokia.org/changes-report.html#a1.6.0>`__.

* Karaf 4.1.5 → 4.2.1:

  * `4.1.6 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342748>`__.
  * `4.2.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12311140&version=12342945>`__.

* LMAX Disruptor 3.3.10 → 3.4.1:

  * `3.4.0 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.0>`__.
  * `3.4.1 <https://github.com/LMAX-Exchange/disruptor/releases/tag/3.4.1>`__.

* META-INF services 1.7 → 1.8.

* Mockito 1.10.19 → 2.20.1; see
  `What’s new in Mockito 2 <https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2>`__
  for upgrade instructions and
  `the list of issues you might run into <https://asolntsev.github.io/en/2016/10/11/mockito-2.1/>`__.

* Netty 4.1.22 → 4.1.28:

  * `4.1.17 <http://netty.io/news/2017/11/08/4-0-53-Final-4-1-17-Final.html>`__.
  * `4.1.18 <http://netty.io/news/2017/12/11/4-0-54-Final-4-1-18-Final.html>`__.
  * `4.1.19 <http://netty.io/news/2017/12/18/4-1-19-Final.html>`__.
  * `4.1.20 <http://netty.io/news/2018/01/22/4-0-55-Final-4-1-20-Final.html>`__.
  * `4.1.21 <http://netty.io/news/2018/02/05/4-0-56-Final-4-1-21-Final.html>`__.
  * `4.1.22 <http://netty.io/news/2018/02/21/4-1-22-Final.html>`__.
  * `4.1.23 <http://netty.io/news/2018/04/04/4-1-23-Final.html>`__.
  * `4.1.24 <http://netty.io/news/2018/04/19/4-1-24-Final.html>`__.
  * `4.1.25 <http://netty.io/news/2018/05/14/4-1-25-Final.html>`__.
  * `4.1.26 <http://netty.io/news/2018/07/10/4-1-26-Final.html>`__.
  * `4.1.27 <http://netty.io/news/2018/07/11/4-1-27-Final.html>`__.
  * `4.1.28 <http://netty.io/news/2018/07/27/4-1-28-Final.html>`__.

* Pax Exam 4.11.0 → 4.12.0.

* Pax URL 2.5.3 → 2.5.4, which only fixes
  `a potential NullPointerException <https://ops4j1.jira.com/browse/PAXURL-346>`__.

* PowerMock 1.6.4 → 1.7.4:

  * `1.6.5 <https://github.com/powermock/powermock/releases/tag/powermock-1.6.5>`__.
  * `1.6.6 <https://github.com/powermock/powermock/releases/tag/powermock-1.6.6>`__.
  * `1.7.0 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.0>`__.
  * `1.7.1 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.1>`__.
  * `1.7.2 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.2>`__.
  * `1.7.3 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.3>`__.
  * `1.7.4 <https://github.com/powermock/powermock/releases/tag/powermock-1.7.4>`__.

* Scala parser combinators 1.0.7 → 1.1.1:

  * `1.1.0 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.1.0>`__.
  * `1.1.1 <https://github.com/scala/scala-parser-combinators/releases/tag/v1.1.1>`__.

* SpotBugs `3.1.0 → 3.1.6 <https://github.com/spotbugs/spotbugs/blob/3.1.6/CHANGELOG.md>`__.

* Threeten Extra `1.3.2 → 1.4 <https://github.com/ThreeTen/threeten-extra/releases>`__.

* Typesafe SSL config 0.2.2 → 0.2.4:

  * `0.2.3 <https://github.com/lightbend/ssl-config/releases/tag/v0.2.3>`__.
  * `0.2.4 <https://github.com/lightbend/ssl-config/releases/tag/v0.2.4>`__.

* Wagon HTTP
  `2.10 → 3.1.0 <https://lists.apache.org/thread.html/96024c54db7680697cb066e22a37b0ed5b4498386714a8a9ae1ec9cd@%3Cannounce.maven.apache.org%3E>`__.

* XMLUnit `1.6 → 2.6.2 <https://github.com/xmlunit/xmlunit/blob/master/RELEASE_NOTES.md>`__.

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
  `2.17 → 3.0.0 <https://mail-archives.apache.org/mod_mbox/maven-announce/201801.mbox/%3Cop.zchs68akkdkhrr%40desktop-2khsk44.mshome.net%3E>`__.

* Duplicate finder
  `1.2.1 → 1.3.0 <https://github.com/basepom/duplicate-finder-maven-plugin/blob/master/CHANGES.md>`__.

* Git commit id `2.2.4 → 2.2.5 <https://github.com/ktoso/maven-git-commit-id-plugin/releases/tag/v2.2.5>`__.

* Jacoco Maven plugin `0.8.1 → 0.8.2 <https://github.com/jacoco/jacoco/releases/tag/v0.8.2>`__.

* Javadoc `3.0.0 → 3.0.1 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12317529&version=12342283>`__.

* PMD 3.8 → 3.10.0:

  * `3.10.0 <https://issues.apache.org/jira/secure/ReleaseNote.jspa?version=12342689&styleName=Text&projectId=12317621>`__.

* Sevntu `1.29.0 → 1.32.0 <http://sevntu-checkstyle.github.io/sevntu.checkstyle/#1.32.0>`__.

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
