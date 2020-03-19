/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class ConfidentialMarker {

    private static String CONFIDENTIAL_TAG = "CONFIDENTIAL";

    private final Marker marker;
    private static ConfidentialMarker instance = null;

    private ConfidentialMarker() {
        this.marker = MarkerFactory.getMarker(CONFIDENTIAL_TAG);
    }

    public static synchronized ConfidentialMarker getInstance() {
        if (instance == null) {
            instance = new ConfidentialMarker();
        }
        return instance;
    }

    public Marker getMarker() {
        return marker;
    }
}
