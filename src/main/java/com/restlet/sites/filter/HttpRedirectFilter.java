/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.routing.Filter;
import org.restlet.routing.Redirector;
import org.restlet.util.Series;

import java.util.Objects;

public class HttpRedirectFilter extends Filter {

    public enum HttpMode {
        NONE, REDIRECT_TO_HTTP, REDIRECT_TO_HTTPS
    }

    private final HttpMode httpMode;

    private int redirectionMode;

    public HttpRedirectFilter(Context context, HttpMode httpMode) {
        this(context, httpMode, Redirector.MODE_CLIENT_PERMANENT);
    }

    public HttpRedirectFilter(Context context, HttpMode httpMode, int redirectionMode) {
        super(context);
        Objects.requireNonNull(httpMode);
        this.httpMode = httpMode;
        this.redirectionMode = redirectionMode;
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        // routes all proxied HTTP urls to HTTPS.
        Series<Header> headers = request.getHeaders();
        Protocol proxiedProtocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));
        if (proxiedProtocol != null) {
            restoreProxiedProtocol(request, proxiedProtocol);
        }

        switch (httpMode) {
            case REDIRECT_TO_HTTP:
                if (!Protocol.HTTP.equals(request.getResourceRef().getSchemeProtocol())) {
                    return redirectClientToProtocol(response, request.getResourceRef(), Protocol.HTTP);
                }
                break;
            case REDIRECT_TO_HTTPS:
                if (!Protocol.HTTPS.equals(request.getResourceRef().getSchemeProtocol())) {
                    return redirectClientToProtocol(response, request.getResourceRef(), Protocol.HTTPS);
                }
                break;
        }

        return super.beforeHandle(request, response);
    }

    private int redirectClientToProtocol(Response response, Reference redirectReference, Protocol protocol) {
        switch (this.redirectionMode) {
            case Redirector.MODE_CLIENT_FOUND:
                response.setLocationRef(redirectReference);
                response.setStatus(Status.REDIRECTION_FOUND);
                break;
            case Redirector.MODE_CLIENT_PERMANENT:
                response.redirectPermanent(redirectReference);
                break;
            case Redirector.MODE_CLIENT_SEE_OTHER:
                response.redirectSeeOther(redirectReference);
                break;
            case Redirector.MODE_CLIENT_TEMPORARY:
                response.redirectTemporary(redirectReference);
                break;
            default:
                response.redirectPermanent(redirectReference);
                break;
        }

        response.getLocationRef().setProtocol(protocol);
        return Filter.STOP;
    }

    private void restoreProxiedProtocol(Request request, Protocol protocol) {
        if (request.getHostRef() != null) {
            request.getHostRef().setProtocol(protocol);
        }
        request.getResourceRef().setProtocol(protocol);
        if (request.getResourceRef().getBaseRef() != null) {
            request.getResourceRef().getBaseRef().setProtocol(protocol);
        }
        if (request.getRootRef() != null) {
            request.getRootRef().setProtocol(protocol);
        }
    }
}
