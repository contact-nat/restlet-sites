/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import org.restlet.routing.Redirector;
import org.restlet.routing.VirtualHost;

import java.util.Objects;
import java.util.Optional;

public class RedirectedVirtualHost extends VirtualHost {

    private final String redirection;

    private final int mode;

    public RedirectedVirtualHost(Context context, String host, Optional<Integer> optionalPort, String redirection, int mode) {
        super(context);
        this.redirection = redirection;
        this.mode = mode;
        setHostDomain(host);
        setName(host);
        optionalPort.ifPresent(port -> {
            setHostPort(Integer.toString(port));
        });
        Redirector redirector;

        if (!redirection.toLowerCase().startsWith("http://")
                && !redirection.toLowerCase().startsWith("https://")) {
            // redirect to a new domain
            redirector = new Redirector(null, null, mode) {
                @Override
                protected Reference getTargetRef(Request request, Response response) {
                    Reference ref = new Reference(request.getResourceRef());
                    ref.setHostDomain(redirection);
                    return ref;
                }
            };
        } else {
            // redirect to a full URL
            redirector = new Redirector(null, redirection, mode);
        }
        attach(redirector);
        getLogger().info(getHostDomain() + " redirected to \"" + redirection + "\" on port " + getHostPort());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RedirectedVirtualHost) {
            RedirectedVirtualHost that = (RedirectedVirtualHost) obj;
            return this.getHostDomain().equals(that.getHostDomain())
                    && Objects.equals(this.getHostPort(), that.getHostPort())
                    && this.redirection.equals(that.redirection)
                    && this.mode == that.mode;
        }

        return false;
    }


}
