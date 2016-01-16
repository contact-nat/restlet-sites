/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import org.restlet.Context;
import org.restlet.routing.VirtualHost;

import java.util.Optional;

public class RestletSiteVirtualHost extends VirtualHost {

    public final BaseApplication application;

    public RestletSiteVirtualHost(Context context, String host, Optional<Integer> optionalPort) {
        super(context);

        setHostDomain(host);
        setName(host);
        optionalPort.ifPresent(port -> setHostPort(Integer.toString(port)));
        application = new BaseApplication(getContext());
        attach(application);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RestletSiteVirtualHost) {
            RestletSiteVirtualHost that = (RestletSiteVirtualHost) obj;
            return this.getHostDomain().equals(that.getHostDomain())
                    && this.getHostPort().equals(that.getHostPort());
        }
        return false;
    }
}
