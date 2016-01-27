package com.restlet.sites.filter;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.routing.Redirector;


public class HttpRedirectFilterTestCase {

    @Test
    public void http_redirect_filter_should_not_redirect_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTP);

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));

        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.SUCCESS_OK, response.getStatus());
    }

    @Test
    public void http_redirect_filter_should_not_redirect_proxied_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTP);

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));
        response.getRequest().getHeaders().add("X-Forwarded-Proto", "HTTP");

        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.SUCCESS_OK, response.getStatus());
    }


    @Test
    public void http_redirect_filter_should_permanently_redirect_https_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTP);

        Response response = new Response(new Request(Method.GET, "https://localhost:8182/hello"));

        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_PERMANENT, response.getStatus());
    }


    @Test
    public void http_redirect_filter_should_permanently_redirect_proxied_https_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTP);

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));
        response.getRequest().getHeaders().add("X-Forwarded-Proto", "HTTPS");
        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_PERMANENT, response.getStatus());
    }


    @Test
    public void https_redirect_filter_should_permanently_redirect_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTPS);

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));

        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_PERMANENT, response.getStatus());
    }


    @Test
    public void https_redirect_filter_should_permanently_redirect_proxied_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTPS);

        Response response = new Response(new Request(Method.GET, "https://localhost:8182/hello"));
        response.getRequest().getHeaders().add("X-Forwarded-Proto", "HTTP");
        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_PERMANENT, response.getStatus());
    }

    @Test
    public void https_temporary_redirect_filter_should_temporary_redirect_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTPS, Redirector.MODE_CLIENT_TEMPORARY);

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));

        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_TEMPORARY, response.getStatus());
    }


    @Test
    public void https_temporary_redirect_filter_should_temporary_redirect_proxied_http_request() {
        HttpRedirectFilter httpRedirectFilter = new HttpRedirectFilter(null, HttpRedirectFilter.HttpMode.REDIRECT_TO_HTTPS, Redirector.MODE_CLIENT_TEMPORARY);

        Response response = new Response(new Request(Method.GET, "https://localhost:8182/hello"));
        response.getRequest().getHeaders().add("X-Forwarded-Proto", "HTTP");
        httpRedirectFilter.beforeHandle(response.getRequest(), response);
        Assert.assertEquals(Status.REDIRECTION_TEMPORARY, response.getStatus());
    }

}
