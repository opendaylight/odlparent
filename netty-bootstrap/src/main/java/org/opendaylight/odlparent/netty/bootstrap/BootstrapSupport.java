/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import org.eclipse.jdt.annotation.NonNull;

public interface BootstrapSupport {

    @NonNull Bootstrap newBootstrap();

    @NonNull ServerBootstrap newServerBootstrap();
}
