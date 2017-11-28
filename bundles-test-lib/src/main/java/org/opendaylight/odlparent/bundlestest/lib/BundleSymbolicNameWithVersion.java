/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import org.osgi.framework.Version;

/**
 * Bundle's symbolic name + its version.
 *
 * @author Michael Vorburger.ch
 */
public class BundleSymbolicNameWithVersion {

    private final String symbolicName;
    private final Version version;

    public BundleSymbolicNameWithVersion(String symbolicName, Version version) {
        this.symbolicName = symbolicName;
        this.version = version;
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (symbolicName == null ? 0 : symbolicName.hashCode());
        result = prime * result + (version == null ? 0 : version.hashCode());
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
        if (symbolicName == null) {
            if (other.symbolicName != null) {
                return false;
            }
        } else if (!symbolicName.equals(other.symbolicName)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return symbolicName + ":" + version.toString();
    }

}
