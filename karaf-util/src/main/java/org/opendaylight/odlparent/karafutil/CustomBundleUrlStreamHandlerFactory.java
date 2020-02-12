/*
 * The NOTICE file referred to in the license statement below is available
 * as Karaf-NOTICE at the root of this project.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opendaylight.odlparent.karafutil;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import org.apache.karaf.deployer.blueprint.BlueprintURLHandler;
import org.apache.karaf.deployer.features.FeatureURLHandler;

public class CustomBundleUrlStreamHandlerFactory implements URLStreamHandlerFactory {
    private static final String MVN_URI_PREFIX = "mvn";
    private static final String WRAP_URI_PREFIX = "wrap";
    private static final String FEATURE_URI_PREFIX = "feature";
    private static final String BLUEPRINT_URI_PREFIX = "blueprint";

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        switch (protocol) {
            case MVN_URI_PREFIX:
                return new org.ops4j.pax.url.mvn.Handler();
            case WRAP_URI_PREFIX:
                return new org.ops4j.pax.url.wrap.Handler();
            case FEATURE_URI_PREFIX:
                return new FeatureURLHandler();
            case BLUEPRINT_URI_PREFIX:
                return new BlueprintURLHandler();
            default:
                return null;
        }
    }
}
