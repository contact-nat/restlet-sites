/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.connector;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.engine.local.Entity;
import org.restlet.engine.local.FileClientHelper;
import org.restlet.engine.local.FileEntity;

import java.io.File;
import java.util.Objects;

/**
 * File client helper that does not try to infer file according to the
 * metadata.
 */
public class StrictFileClientHelper extends FileClientHelper {

    private StrictFileClientHelper() {
        super(null);
    }

    public static void setStrictClientDispatcher(Context context) {
        Objects.requireNonNull(context);
        final FileClientHelper fch = new StrictFileClientHelper();
        Restlet r = new Restlet(context) {
            @Override
            public void handle(Request request, Response response) {
                fch.handle(request, response);
            }
        };
        context.setClientDispatcher(r);
    }

    @Override
    public Entity getEntity(String decodedPath) {
        File file = new File(LocalReference.localizePath(decodedPath));
        if (file.exists()) {
            return new FileEntity(file, getMetadataService());
        }
        return new FileEntity(file, getMetadataService()) {
            @Override
            public String getBaseName() {
                return "";
            }
        };
    }
}
