/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.odlparent.featuretest;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.ops4j.pax.exam.TestProbeBuilder;

/**
 * Utility for class path reflection.
 *
 * @author Michael Vorburger.ch
 */
@Deprecated(since = "13.1.0", forRemoval = true)
public final class ReflectionUtil {
    private ReflectionUtil() {
        // Hidden on purpose
    }

    public static void addAllClassesInSameAndSubPackageOfClass(TestProbeBuilder probe, Class<?> clazz) {
        addAllClassesInSameAndSubPackageOfPackage(probe, clazz.getPackage().getName());
    }

    public static void addAllClassesInSameAndSubPackageOfPackage(TestProbeBuilder probe, String packageName) {
        getClasses(ReflectionUtil.class.getClassLoader(), packageName).forEach(eachClass -> probe.addTest(eachClass));
    }

    /**
     * Returns all classes in the named package, and its sub-packages.
     *
     * @param classLoader class loader which needs to be inspected
     * @param packageName package name prefix
     * @return a stream of classes
     */
    public static Stream<Class<?>> getClasses(ClassLoader classLoader, String packageName) {
        try {
            ClassPath classPath = ClassPath.from(classLoader);
            // inspired by https://github.com/vorburger/ch.vorburger.minecraft.osgi/blob/master/ch.vorburger.minecraft.osgi/src/main/java/ch/vorburger/osgi/embedded/PackagesBuilder.java
            return classPath.getTopLevelClassesRecursive(packageName)
                    .stream().map(ClassPath.ClassInfo::load)
                     // to include all inner classes, including anonymous inner classes:
                    .flatMap(ReflectionUtil::getDeclaredAndAnonymousInnerClass);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to examine class loader " + classLoader, e);
        }
    }

    private static Stream<Class<?>> getDeclaredAndAnonymousInnerClass(Class<?> clazz) {
        List<Class<?>> anonymousInnerClasses = new ArrayList<>();
        anonymousInnerClasses.add(clazz); // add self; will get skipped if empty() below!
        anonymousInnerClasses.addAll(Arrays.asList(clazz.getDeclaredClasses()));
        ClassLoader classLoader = clazz.getClassLoader();
        String className = clazz.getCanonicalName();
        for (int i = 1; ; i++) {
            try {
                anonymousInnerClasses.add(classLoader.loadClass(className + "$" + i));
            } catch (ClassNotFoundException e) {
                // Last anonymous inner class found (even none), so we're done, return:
                return anonymousInnerClasses.stream();
            } catch (NoClassDefFoundError e) {
                // Oups, this class cannot be loaded, so return empty stream so that flatMap() removes it!
                return Stream.empty();
            }
        }
    }


}
