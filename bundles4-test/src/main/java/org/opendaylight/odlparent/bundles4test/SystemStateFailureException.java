/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.opendaylight.odlparent.bundlestest.BundleDiagInfos;

@Deprecated
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class SystemStateFailureException extends org.opendaylight.odlparent.bundlestest.SystemStateFailureException {

    private static final long serialVersionUID = 1L;

    public SystemStateFailureException(String message, BundleDiagInfos bundleDiagInfos) {
        super(message, bundleDiagInfos);
    }

}
