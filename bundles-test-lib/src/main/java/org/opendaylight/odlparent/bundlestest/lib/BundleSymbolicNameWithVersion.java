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
import org.eclipse.jdt.annotation.NonNull;

/**
 * Bundle's symbolic name + its version.
 *
 * @author Michael Vorburger.ch
 */
public final class BundleSymbolicNameWithVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final @NonNull String symbolicName;

    private final @NonNull String version;

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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BundleSymbolicNameWithVersion that = (BundleSymbolicNameWithVersion) obj;

        return symbolicName.equals(that.symbolicName) && version.equals(that.version);
    }

    @Override
    public String toString() {
        return symbolicName + ":" + version;
    }

}
