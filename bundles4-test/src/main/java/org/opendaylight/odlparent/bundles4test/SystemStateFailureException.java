/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundles4test;

/**
 * Exception with details about the {@link SystemState#Failure}.
 *
 * @author Michael Vorburger.ch
 */
public class SystemStateFailureException extends Exception {
    private static final long serialVersionUID = 1L;

    public SystemStateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemStateFailureException(String message) {
        super(message);
    }

}
