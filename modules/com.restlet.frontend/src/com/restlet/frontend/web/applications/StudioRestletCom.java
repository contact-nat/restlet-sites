/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.IOException;

import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.engine.application.Encoder;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

/**
 * Application for the http://studio.restlet.com application
 * 
 * @author Jerome Louvel
 * @author Thierry Boileau
 */
public class StudioRestletCom extends BaseApplication {
    /** The data file URI. */
    private String dataUri;

    /** Login for global site authentication. */
    private String siteLogin;

    /** Password for global site authentication. */
    private char[] sitePassword;

    /**
     * Constructor.
     * 
     * @param propertiesFileReference
     *            The Reference to the application's properties file.
     * @throws IOException
     */
    public StudioRestletCom(String propertiesFileReference) throws IOException {
        super(propertiesFileReference);

        this.dataUri = getProperty("data.uri");
        this.siteLogin = getProperty("site.login");
        String str = getProperty("site.password");
        if (str != null) {
            sitePassword = str.toCharArray();
        }
    }

    @Override
    public Restlet createInboundRoot() {
        // Create a root router
        Router rootRouter = new Router(getContext());

        // Serve repository
        Directory directory = new Directory(getContext(), this.dataUri);
        directory.setNegotiatingContent(true);
        directory.setModifiable(false);
        directory.setListingAllowed(false);
        rootRouter.attachDefault(directory);

        Encoder encoder = new Encoder(getContext(), false, true, getEncoderService());

        if (siteLogin != null && sitePassword != null) {
            ChallengeAuthenticator ca = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "realm");
            MapVerifier mv = new MapVerifier();
            mv.getLocalSecrets().put(siteLogin, sitePassword);
            ca.setVerifier(mv);
            ca.setNext(rootRouter);
            encoder.setNext(ca);
        } else {
            encoder.setNext(rootRouter);
        }

        return encoder;
    }

    @Override
    public String getName() {
        return "Application for Restlet Studio";
    }
}
