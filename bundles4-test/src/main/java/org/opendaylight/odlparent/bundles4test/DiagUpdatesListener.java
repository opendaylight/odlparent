/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

/**
 * Listener which is notified for every diag update.
 *
 * @author Michael Vorburger.ch
 */
@FunctionalInterface
public interface DiagUpdatesListener {

    void onUpdate(BundleDiagInfos bundleDiagInfos, long elapsedTimeInMS, long remainingTimeInMS);

}
