/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

/**
 * Value Object for elapsed and remaining time.
 *
 * @author Michael Vorburger.ch
 */
public record TimeInfo(long elapsedTimeInMS, long remainingTimeInMS) {
    @Deprecated(since = "13.0.5", forRemoval = true)
    public long getElapsedTimeInMS() {
        return elapsedTimeInMS;
    }

    @Deprecated(since = "13.0.5", forRemoval = true)
    public long getRemainingTimeInMS() {
        return remainingTimeInMS;
    }

    @Override
    public String toString() {
        return "TimeInfo [elapsedTimeInMS=" + elapsedTimeInMS + ", remainingTimeInMS=" + remainingTimeInMS + "]";
    }
}
