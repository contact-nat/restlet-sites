/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.filter;

public class CacheInstruction {
    private String expires;
    private String cacheControl;

    public CacheInstruction withExpires(String expires) {
        CacheInstruction cacheInstruction = new CacheInstruction();
        cacheInstruction.expires = expires;
        cacheInstruction.cacheControl = this.cacheControl;
        return cacheInstruction;
    }

    public CacheInstruction withCacheControl(String cacheControl) {
        CacheInstruction cacheInstruction = new CacheInstruction();
        cacheInstruction.expires = this.expires;
        cacheInstruction.cacheControl = cacheControl;
        return cacheInstruction;
    }

    public String getExpires() {
        return expires;
    }

    public String getCacheControl() {
        return cacheControl;
    }
}
