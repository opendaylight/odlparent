## Example OpenDaylight Dagger-enabled artifact

This artifact shows how Dagger is an alternative to OSGi Service Component Runtime, packaged as a full
JPMS module.

There are three package involved here:

- [dagger.example.api](src/main/java/org/opendaylight/odlparent/dagger/example/api/), which it the API being
  implemented. Normally we keep these in separate artifacts to explicitly separate API from implementation. It is
  exported from the module and made available in OSGi as well.

- [dagger.example](src/main/java/org/opendaylight/odlparent/dagger/example/), which hosts the implementation. It is not
  exported from the module, nor is it available in OSGi.

- [dagger.example.dagger](src/main/java/org/opendaylight/odlparent/dagger/example/dagger/), which hosts Dagger modules
  and components, along with whatever code Dagger generates for these. This package is not exposed in OSGi.

In order to achieve this, details are quite important:

- [pom.xml](pom.xml) based on `bnd-parent`. This provides the substrate for processing OSGi annotations and
  providing correct `MANIFEST.MF` entries.

- [odl.dagger.enable](odl.dagger.enable) needs to be present to activate the Dagger profile

- [module-info.java](src/main/java/module-info.java) needs to be present and do the right thing. Most notably
  we should not `requires transitive` any Dagger-related modules if we are exporting any other package aside
  from the `.dagger` package. We do that here, as we are also exporting `example.api`, hence we use
  `requires static`.

- [bnd.bnd](bnd.bnd) file, which needs to update the default policy, so that `dagger`, `jakarta.inject` etc.
  packages are not required in OSGi.
