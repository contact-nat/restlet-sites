/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.IOException;
import java.util.Properties;

import org.restlet.Application;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.util.StringUtils;

import com.restlet.frontend.web.WebComponent;

/**
 * Base application that describes shared behaviour.
 * 
 * @author Jerome Louvel
 */
public class BaseApplication extends Application {

    /** Application properties. */
    private Properties properties;

    /**
     * Constructor.
     * 
     * @param propertiesFileReference
     *            The Reference to the application's properties file.
     * @throws IOException
     */
    public BaseApplication(String propertiesFileReference) throws IOException {
        super();
        properties = WebComponent.getProperties(propertiesFileReference);
        getRangeService().setEnabled(false);
        getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
        getMetadataService().setDefaultLanguage(null);
        getMetadataService().addExtension("html", MediaType.TEXT_HTML, true);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
    }

    /**
     * Returns the value of the given property, or null. Empty values are set to
     * null.
     * 
     * @param property
     *            The property.
     * 
     * @return The value of the given property.
     */
    public String getProperty(String property) {
        return getProperty(property, null);
    }

    /**
     * Returns the value of the given property, or null. Empty values are set to
     * the given default value.
     * 
     * @param property
     *            The property.
     * @param defaultValue
     *            The default value.
     * @return The value of the given property.
     */
    public String getProperty(String property, String defaultValue) {
        String str = this.properties.getProperty(property);
        if (!StringUtils.isNullOrEmpty(str)) {
            return str;
        }
        return defaultValue;
    }
}
