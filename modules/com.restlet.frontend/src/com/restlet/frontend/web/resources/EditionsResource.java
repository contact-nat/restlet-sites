package com.restlet.frontend.web.resources;

import org.restlet.resource.Get;

import com.restlet.frontend.objects.framework.EditionsList;

public interface EditionsResource {
    @Get
    public EditionsList list();
}
