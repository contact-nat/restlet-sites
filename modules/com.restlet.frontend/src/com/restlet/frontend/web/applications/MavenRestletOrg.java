/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.File;
import java.io.IOException;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.engine.local.Entity;
import org.restlet.engine.local.FileClientHelper;
import org.restlet.engine.local.FileEntity;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

/**
 * Application for the http://maven.restlet.org repository.
 * 
 * @author Jerome Louvel
 * @author Thierry Boileau
 */
public class MavenRestletOrg extends BaseApplication {
    /** The data file URI. */
    private String dataUri;

    /**
     * File client helper that does not try to infer file according to the
     * metadata.
     * 
     * @author thboileau
     * 
     */
    private static class StrictFileClientHelper extends FileClientHelper {
        public StrictFileClientHelper(Client client) {
            super(client);
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

    /**
     * Constructor.
     * 
     * @param propertiesFileReference
     *            The Reference to the application's properties file.
     * @throws IOException
     */
    public MavenRestletOrg(String propertiesFileReference) throws IOException {
        super(propertiesFileReference);

        this.dataUri = getProperty("data.uri");

        MediaType mediaType = new MediaType("application/sha1", "Secured hash algorithm");
        getMetadataService().addExtension("sha1", mediaType);
        mediaType = new MediaType("application/md5", "Message-digest algorithm");
        getMetadataService().addExtension("MD5", mediaType);
        getMetadataService().addExtension("md5", mediaType);
    }

    @Override
    public Restlet createInboundRoot() {
        // Create a root router
        Router result = new Router(getContext());
        final FileClientHelper fch = new StrictFileClientHelper(null);
        Restlet r = new Restlet(getContext()) {
            @Override
            public void handle(Request request, Response response) {
                fch.handle(request, response);
            }
        };
        getContext().setClientDispatcher(r);

        // Serve repository
        Directory directory = new Directory(getContext(), this.dataUri + "/maven2/restlet");
        directory.setIndexName(null);
        directory.setNegotiatingContent(true);
        directory.setModifiable(false);
        directory.setDeeplyAccessible(true);
        directory.setListingAllowed(true);
        result.attachDefault(directory);

        return result;
    }

    @Override
    public String getName() {
        return "Application for maven.restlet.org";
    }
}
