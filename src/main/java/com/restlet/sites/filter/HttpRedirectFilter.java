/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Header;
import org.restlet.data.Protocol;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

public class HttpRedirectFilter extends Filter {
    public HttpRedirectFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        // routes all proxied HTTP urls to HTTPS.
        Series<Header> headers = request.getHeaders();
        Protocol protocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));
        if (protocol != null) {
            request.getHostRef().setProtocol(Protocol.HTTPS);
            request.getResourceRef().setProtocol(Protocol.HTTPS);
            if (request.getResourceRef().getBaseRef() != null) {
                request.getResourceRef().getBaseRef().setProtocol(Protocol.HTTPS);
            }
            request.getRootRef().setProtocol(Protocol.HTTPS);
            if (Protocol.HTTP.equals(protocol)) {
                response.redirectPermanent(request.getResourceRef());
                response.getLocationRef().setProtocol(Protocol.HTTPS);
                return Filter.STOP;
            }
        }
        return super.beforeHandle(request, response);
    }
}
