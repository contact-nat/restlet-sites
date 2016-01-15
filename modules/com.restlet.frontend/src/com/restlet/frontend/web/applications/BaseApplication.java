/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.Header;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.application.Encoder;
import org.restlet.engine.application.MetadataExtension;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.local.Entity;
import org.restlet.engine.local.FileClientHelper;
import org.restlet.engine.local.FileEntity;
import org.restlet.engine.util.StringUtils;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.util.Series;

/**
 * Base application that describes shared behaviour.
 * 
 * @author Jerome Louvel
 */
public class BaseApplication extends Application {

    private String login;

    private String password;

    private Router rootRouter;

    /**
     * Constructor.
     * 
     * @throws IOException
     */
    public BaseApplication(Boolean strictClientHelper, List<MetadataExtension> extensions, String login, String password) {
        super();
        getRangeService().setEnabled(false);
        getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
        getMetadataService().setDefaultLanguage(null);
        getMetadataService().addExtension("html", MediaType.TEXT_HTML, true);
        if (extensions != null && !extensions.isEmpty()) {
            for (MetadataExtension metadataExtension : extensions) {
                getMetadataService().addExtension(metadataExtension.getName(), metadataExtension.getMetadata());
            }
        }

        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        this.login = login;
        this.password = password;

        rootRouter = new Router();
        rootRouter.setDefaultMatchingMode(Router.MODE_FIRST_MATCH);
    }

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

    @Override
    public Restlet createInboundRoot() {
        final FileClientHelper fch = new StrictFileClientHelper(null);
        Restlet r = new Restlet(getContext()) {
            @Override
            public void handle(Request request, Response response) {
                fch.handle(request, response);
            }
        };
        getContext().setClientDispatcher(r);

        if (!StringUtils.isNullOrEmpty(login) && !StringUtils.isNullOrEmpty(password)) {

        }
        rootRouter.setContext(getContext());

        Encoder encoder = new Encoder(getContext(), false, true, getEncoderService());

        if (login != null && password != null) {
            ChallengeAuthenticator ca = new ChallengeAuthenticator(
                    getContext(), ChallengeScheme.HTTP_BASIC, "realm");
            MapVerifier mv = new MapVerifier();
            mv.getLocalSecrets().put(login, password.toCharArray());
            ca.setVerifier(mv);
            ca.setNext(rootRouter);
            encoder.setNext(ca);
        } else {
            encoder.setNext(rootRouter);
        }

        return encoder;
    }

    public void handleRoute(String source, String target, int currentMode, boolean bStartsWith) {
        if (currentMode == -1) {
            Directory dir = new Directory(getContext(), "file://" + target.toString());
            rootRouter.attach(source.toString(), dir);
            getLogger().fine(
                    "  attach directory: from " + dir.getRootRef()
                            + " to " + source.toString());
        } else if (!bStartsWith) {
            redirect(rootRouter, source.toString(), target.toString(), currentMode);
        } else {
            redirect(rootRouter, source.toString(), target.toString(),
                    currentMode).setMatchingMode(Template.MODE_STARTS_WITH);
        }
    }

    /**
     * Helps to define redirections assuming that the router defines route by
     * using the {@link Template.MODE_STARTS_WITH} mode.
     * 
     * @param router
     *            The router where to define the redirection.
     * @param from
     *            The source template.
     * @param to
     *            The target template.
     * @param mode
     *            The redirection mode (cf {@link Redirector}.
     * @return The defined route.
     */
    private TemplateRoute redirect(Router router, String from, String to, int mode) {
        TemplateRoute route = router.attach(from, new Redirector(getContext(), to, mode) {
            protected void serverRedirect(Restlet next, Reference targetRef,
                    Request request, Response response) {
                if (next == null) {
                    getLogger().warning(
                            "No next Restlet provided for server redirection to " + targetRef);
                } else {
                    // Save the base URI if it exists as we might need it for redirections
                    Reference resourceRef = request.getResourceRef();
                    Reference baseRef = resourceRef.getBaseRef();

                    // Reset the protocol and let the dispatcher handle the protocol
                    request.setProtocol(null);

                    // Update the request to cleanly go to the target URI
                    request.setResourceRef(targetRef);
                    request.getAttributes().remove(HeaderConstants.ATTRIBUTE_HEADERS);
                    next.handle(request, response);

                    // Memorize Access-Control-Allow-* headers to reinject in the response
                    Series<Header> resHeaders = response.getHeaders();
                    Series<Header> newHeaders = new Series<Header>(Header.class);
                    for (Header h : resHeaders) {
                        if (h.getName().startsWith("Access-Control-Allow")) {
                            newHeaders.add(h.getName(), h.getValue());
                        }
                    }

                    // Allow for response rewriting and clean the headers
                    response.setEntity(rewrite(response.getEntity()));
                    response.getAttributes().remove(HeaderConstants.ATTRIBUTE_HEADERS);
                    request.setResourceRef(resourceRef);

                    // Reinject Access-Control-Allow-* headers
                    response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, newHeaders);

                    // In case of redirection, we may have to rewrite the redirect URI
                    if (response.getLocationRef() != null) {
                        Template rt = new Template(this.targetTemplate);
                        rt.setLogger(getLogger());
                        int matched = rt.parse(response.getLocationRef().toString(), request);

                        if (matched > 0) {
                            String remainingPart = (String) request.getAttributes().get("rr");

                            if (remainingPart != null) {
                                response.setLocationRef(baseRef.toString() + remainingPart);
                            }
                        }
                    }
                }

            }
        });
        if (to.contains("{rr}")) {
            route.setMatchingMode(Template.MODE_STARTS_WITH);
        }
        return route;
    }
}
