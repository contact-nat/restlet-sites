/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;

public class RestletChain {

    private Restlet first = null;
    private Restlet last = null;

    public RestletChain add(Restlet restlet) {
        if (first == null) {
            first = restlet;
        }

        if (last instanceof Router) {
            Router router = (Router) last;
            router.attachDefault(restlet);
        } else if (last instanceof Filter) {
            Filter filter = (Filter) last;
            filter.setNext(restlet);
        } else if (last != null) {
            throw new IllegalArgumentException("Could not chain any component after a Restlet");
        }

        last = restlet;
        return this;
    }

    public RestletChain attach(String pathTemplate, Class<? extends ServerResource> targetClass) {
        if (last instanceof Router) {
            Router router = (Router) last;
            router.attach(pathTemplate, targetClass);
        } else {
            throw new IllegalArgumentException("Add a Router before attaching a resource");
        }
        return this;
    }

    public RestletChain attach(String pathTemplate, Restlet restlet) {
        if (last instanceof Router) {
            Router router = (Router) last;
            router.attach(pathTemplate, restlet);
        } else {
            throw new IllegalArgumentException("Can attach a resource only after a Router");
        }
        return this;
    }

    public RestletChain attach(String pathTemplate, Restlet restlet, int matchingMode) {
        if (last instanceof Router) {
            Router router = (Router) last;
            router.attach(pathTemplate, restlet).setMatchingMode(matchingMode);
        } else {
            throw new IllegalArgumentException("Can attach a resource only after a Router");
        }
        return this;
    }

    public Restlet getFirst() {
        return first;
    }

    public Restlet getLast() {
        return last;
    }

}
