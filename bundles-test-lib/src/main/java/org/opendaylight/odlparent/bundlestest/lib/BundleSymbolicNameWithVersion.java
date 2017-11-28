/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

/**
 * Bundle's symbolic name + its version.
 *
 * @author Michael Vorburger.ch
 */
public class BundleSymbolicNameWithVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String symbolicName;
    private final String version;

    public BundleSymbolicNameWithVersion(String symbolicName, String version) {
        this.symbolicName = requireNonNull(symbolicName, "symbolicName");
        this.version = requireNonNull(version, "version");
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + symbolicName.hashCode();
        result = prime * result + version.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BundleSymbolicNameWithVersion)) {
            return false;
        }
        BundleSymbolicNameWithVersion other = (BundleSymbolicNameWithVersion) obj;
        if (!symbolicName.equals(other.symbolicName)) {
            return false;
        }
        if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return symbolicName + ":" + version;
    }

}
