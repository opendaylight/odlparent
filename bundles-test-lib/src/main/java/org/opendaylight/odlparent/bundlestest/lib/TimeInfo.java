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
public final class TimeInfo {

    private final long elapsedTimeInMS;
    private final long remainingTimeInMS;

    public TimeInfo(long elapsedTimeInMS, long remainingTimeInMS) {
        this.elapsedTimeInMS = elapsedTimeInMS;
        this.remainingTimeInMS = remainingTimeInMS;
    }

    public long getElapsedTimeInMS() {
        return elapsedTimeInMS;
    }

    public long getRemainingTimeInMS() {
        return remainingTimeInMS;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (elapsedTimeInMS ^ elapsedTimeInMS >>> 32);
        result = prime * result + (int) (remainingTimeInMS ^ remainingTimeInMS >>> 32);
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

        TimeInfo timeInfo = (TimeInfo) obj;

        return elapsedTimeInMS == timeInfo.elapsedTimeInMS && remainingTimeInMS == timeInfo.remainingTimeInMS;
    }

    @Override
    public String toString() {
        return "TimeInfo [elapsedTimeInMS=" + elapsedTimeInMS + ", remainingTimeInMS=" + remainingTimeInMS + "]";
    }

}
