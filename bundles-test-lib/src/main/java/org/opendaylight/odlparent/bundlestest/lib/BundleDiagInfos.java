/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.bundlestest.lib;

import java.io.Serializable;
import java.util.List;

/**
 * System readiness diagnostic summary information.
 *
 * @author Michael Vorburger.ch
 */
public interface BundleDiagInfos extends Serializable {

    SystemState getSystemState();

    String getFullDiagnosticText();

    String getSummaryText();

    List<String> getNokBundleStateInfoTexts();

    List<String> getOkBundleStateInfoTexts();

    List<String> getWhitelistedBundleStateInfoTexts();

}