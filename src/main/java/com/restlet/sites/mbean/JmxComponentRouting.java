/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.mbean;

import java.util.Collection;

/**
 * Describes the behavior of JMX bean that give details about routing.
 *
 * @author Thierry Boileau
 */
abstract class JmxComponentRouting {

    /**
     * Returns the lists of descriptions for each route.
     *
     * @return The lists of descriptions for each route.
     */
    protected abstract Collection<String> getRoutes();

}
