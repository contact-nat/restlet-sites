package com.restlet.sites.filter;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class CacheFilterTestCase {

    @Test
    public void cache_filter_with_fixed_expiration_date_in_ISO8601_should_set_expires_header() throws ParseException {
        CacheFilter cacheFilter = new CacheFilter(null, new CacheInstruction().withExpires("@2016-12-13T10:00:00.123Z"));

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));
        response.setEntity("hello, world", MediaType.TEXT_PLAIN);

        cacheFilter.afterHandle(response.getRequest(), response);

        Assert.assertNotNull(response.getEntity().getExpirationDate());
        Assert.assertEquals(1481623200123L, response.getEntity().getExpirationDate().getTime());
    }

    @Test
    public void cache_filter_with_fixed_expiration_date_in_ISO8601_should_not_set_expires_header_on_nocache_resource() throws ParseException {
        CacheFilter cacheFilter = new CacheFilter(null, new CacheInstruction().withExpires("@2016-12-13T10:00:00.123Z"));

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/nocache/hello"));
        response.setEntity("hello, world", MediaType.TEXT_PLAIN);

        cacheFilter.afterHandle(response.getRequest(), response);

        Assert.assertNull(response.getEntity().getExpirationDate());
    }

    @Test
    public void cache_filter_with_modified_directive_should_set_expires_header() throws ParseException {
        CacheFilter cacheFilter = new CacheFilter(null, new CacheInstruction().withExpires("modified24h"));

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));
        response.setEntity("hello, world", MediaType.TEXT_PLAIN);
        Instant now = Instant.now();
        response.getEntity().setModificationDate(new Date(now.toEpochMilli()));
        cacheFilter.afterHandle(response.getRequest(), response);

        Assert.assertNotNull(response.getEntity().getExpirationDate());
        Assert.assertEquals(now.plus(24, ChronoUnit.HOURS), response.getEntity().getExpirationDate().toInstant());
    }

    @Test
    public void cache_filter_with_cache_control_should_set_cache_directives() throws ParseException {
        CacheFilter cacheFilter = new CacheFilter(null, new CacheInstruction().withCacheControl("private, max-age=120"));

        Response response = new Response(new Request(Method.GET, "http://localhost:8182/hello"));
        response.setEntity("hello, world", MediaType.TEXT_PLAIN);

        cacheFilter.afterHandle(response.getRequest(), response);

        Assert.assertNull(response.getEntity().getExpirationDate());
        Assert.assertNotNull(response.getCacheDirectives());
        Assert.assertEquals(2, response.getCacheDirectives().size());
    }

}
