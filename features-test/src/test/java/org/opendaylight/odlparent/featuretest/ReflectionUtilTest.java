/*
 * SPDX-License-Identifier: EPL-1.0
 *
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Unit test of {@link ReflectionUtil}.
 *
 * @author Michael Vorburger.ch
 */
public class ReflectionUtilTest {

    @Test
    public void testGetClasses() {
        assertThat(ReflectionUtil.getClasses(getClass().getClassLoader(), "org.awaitility")
                .collect(Collectors.toList()))
                .containsAllOf(org.awaitility.Awaitility.class, org.awaitility.core.ConditionTimeoutException.class);
    }

    @Test
    public void testGetInnerClasses() {
        List<Class<?>> innerClasses = ReflectionUtil.getClasses(
                getClass().getClassLoader(), getClass().getPackage().getName()).collect(Collectors.toList());
        assertThat(innerClasses).containsAllOf(getClass(), InnerStaticClass.class, InnerNonStaticClass.class);
        assertThat(innerClasses.stream().anyMatch(
            clazz -> clazz.getName().endsWith(getClass().getSimpleName() + "$1"))).isTrue();
        assertThat(innerClasses).containsNoDuplicates();
    }

    @SuppressWarnings("unused")
    private final InnerNonStaticClass anoymousInnerClass = new InnerNonStaticClass() { };

    private static class InnerStaticClass { }

    private class InnerNonStaticClass { }

}
