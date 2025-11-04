## Example OpenDaylight Dagger-enabled artifact

This artifact shows how Dagger is an alternative to OSGi Service Component Runtime, packaged as a full
JPMS module.

There are three package involved here:

* [dagger.example.api](src/main/java/org/opendaylight/dagger/example/api/), which it the API being implemented.
  Normally we keep these in separate artifacts to explicitly separate API from implementation. It is exported
  from the module and made available in OSGi as well.

* [dagger.example](src/main/java/org/opendaylight/dagger/example/), which hosts the implementation. It is not
  exported from the module, nor is it available in OSGi.

* [dagger.example.dagger](src/main/java/org/opendaylight/dagger/example/dagger/), which hosts Dagger modules
  and components, along with whatever code Dagger generates for these.
