/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.properties;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static java.nio.file.Files.newBufferedReader;

public class PropertiesHelper {

    public static PropertiesWrapper loadGlobalProperties(Optional<Path> configurationPath) throws IOException {
        Properties globalProperties = new Properties();
        globalProperties.put("web.server.access.logs.logger.name", "com.noelios.web.WebComponent.www");
        // Allows to retrieve the client IP address when the web server is proxied
        globalProperties.put("useForwardedForHeader", "true");

        if (configurationPath.isPresent()) {
            globalProperties.load(newBufferedReader(configurationPath.get(), Charset.defaultCharset()));
        }

        return new PropertiesWrapper(globalProperties);
    }


}
