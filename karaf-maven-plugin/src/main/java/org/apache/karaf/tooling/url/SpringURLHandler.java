/**
 *
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
package org.apache.karaf.tooling.url;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.karaf.deployer.spring.SpringTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * As org.apache.karaf.deployer.spring.SpringURLHandler needs to be run with
 * the OSGi container this class was created for use by the karaf-maven-plugin
 */
public class SpringURLHandler extends URLStreamHandler {

	private final Logger logger = LoggerFactory.getLogger(SpringURLHandler.class);

	private static String SYNTAX = "spring: spring-xml-uri";

	private URL springXmlURL;

    /**
     * Open the connection for the given URL.
     *
     * @param url the url from which to open a connection.
     * @return a connection on the specified URL.
     * @throws IOException if an error occurs or if the URL is malformed.
     */
    @Override
	public URLConnection openConnection(URL url) throws IOException {
		if (url.getPath() == null || url.getPath().trim().length() == 0) {
			throw new MalformedURLException ("Path cannot be null or empty. Syntax: " + SYNTAX );
		}
		springXmlURL = new URL(url.getPath());

		logger.debug("Spring xml URL is: [" + springXmlURL + "]");
		return new Connection(url);
	}
	
	public URL getSpringXmlURL() {
		return springXmlURL;
	}

    public class Connection extends URLConnection {

        public Connection(URL url) {
            super(url);
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public InputStream getInputStream() throws IOException {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                SpringTransformer.transform(springXmlURL, os);
                os.close();
                return new ByteArrayInputStream(os.toByteArray());
            } catch (Exception e) {
                logger.error("Error opening Spring xml url", e);
                throw (IOException) new IOException("Error opening Spring xml url").initCause(e);
            }
        }
    }

}
