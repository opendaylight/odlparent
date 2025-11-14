/*
 * Copyright (c) 2016 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import org.apache.karaf.bundle.core.BundleState;

/**
 * Karaf OSGi System State.
 *
 * @see BundleState
 *
 * @author Michael Vorburger.ch
 */
public enum SystemState {

    Booting,

    Active,

    Stopping,

    Failure

}
