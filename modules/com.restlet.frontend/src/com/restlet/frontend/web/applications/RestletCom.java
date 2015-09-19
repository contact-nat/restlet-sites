/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.Engine;
import org.restlet.engine.application.Encoder;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.util.Series;

import com.restlet.frontend.web.services.RouterPropertiesReader;

/**
 * Application for the http://restlet.com site.
 * 
 * @author Jerome Louvel
 */
public class RestletCom extends BaseApplication {

    /**
     * {@link TemplateRoute} that scores URIs according to a regex pattern. Once
     * chosen, the URI is transmitted to the next Restlet unchanged, whereas a
     * classic {@link TemplateRoute} adjusts the base reference according to the
     * matched par of the URI.
     * 
     * @author Thierry Boileau
     * 
     */
    private static class StartsWithRoute extends TemplateRoute {
        /** The pattern to use for formatting or parsing. */
        private volatile String pattern;

        /** The internal Regex pattern. */
        private volatile Pattern regexPattern;

        /**
         * Constructor.
         * 
         * @param router
         *            the router.
         * @param next
         *            the Restlet to transmit the Request to.
         * @param pattern
         *            The regex pattern to match.
         */
        public StartsWithRoute(Router router, Restlet next, String pattern) {
            super(next);
            setRouter(router);
            this.pattern = pattern;
            this.regexPattern = Pattern.compile(this.pattern.toString());
        }

        @Override
        public float score(Request request, Response response) {
            float result = -1f;
            String remainingPart = request.getResourceRef().getRemainingPart(
                    false, isMatchingQuery());
            if (remainingPart != null) {
                Matcher matcher = regexPattern.matcher(remainingPart);
                if (matcher.lookingAt()) {
                    result = matcher.end();
                }
            }
            return result;
        }
    }

    /** The data file URI. */
    private String dataUri;

    /** Login for admin protected pages. */
    private String login;

    /** Password for admin protected pages. */
    private char[] password;

    /** The URI of the router properties file. */
    private String routerPropertiesFileReference;

    /** The root router. */
    private Router rootRouter;

    /** Login for global site authentication. */
    private String siteLogin;

    /** Password for global site authentication. */
    private char[] sitePassword;

    /** The Web files root directory URI. */
    private String wwwUri;

    /**
     * Constructor.
     * 
     * @param propertiesFileReference
     *            The Reference to the application's properties file.
     * @throws IOException
     */
    public RestletCom(String propertiesFileReference) throws IOException {
        super(propertiesFileReference);

        // By default, check the classpath.
        this.routerPropertiesFileReference = getProperty("router.uri", "clap://class/router.properties");

        this.dataUri = getProperty("data.uri");
        this.wwwUri = getProperty("www.uri");
        this.login = getProperty("admin.login");

        String str = getProperty("admin.password");
        if (str != null) {
            this.password = str.toCharArray();
        }
        this.siteLogin = getProperty("site.login");
        str = getProperty("site.password");
        if (str != null) {
            sitePassword = str.toCharArray();
        }

        // Turn off extension tunnelling because of redirections.
        this.getTunnelService().setExtensionsTunnel(false);

        // Override the default mediatype for XSD
        getMetadataService().addExtension("xsd", MediaType.APPLICATION_XML, true);
    }

    @Override
    public Restlet createInboundRoot() {
        Engine.setLogLevel(Level.FINEST);
        // Create a root router
        rootRouter = new Router(getContext());
        rootRouter.setDefaultMatchingMode(Router.MODE_FIRST_MATCH);
        updateRootRouter();

        Encoder encoder = new Encoder(getContext(), false, true, getEncoderService());

        if (siteLogin != null && sitePassword != null) {
            ChallengeAuthenticator ca = new ChallengeAuthenticator(
                    getContext(), ChallengeScheme.HTTP_BASIC, "realm");
            MapVerifier mv = new MapVerifier();
            mv.getLocalSecrets().put(siteLogin, sitePassword);
            mv.getLocalSecrets().put(login, password);
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
        return "Application for restlet.com";
    }

    /**
     * Sets up the redirections.
     * 
     * @param router
     *            The router to complete.
     * @param redirectionsFileUri
     *            The URI of the redirections file.
     */
    private void readRouter(final Router router, String redirectionsFileUri) {
        RouterPropertiesReader reader = new RouterPropertiesReader(
                redirectionsFileUri) {

            @Override
            public void handle(String source, String target, int currentMode,
                    boolean bStartsWith) {
                if (currentMode == -1) {
                    Directory dir = new Directory(getContext(), "file://" + target.toString());
                    rootRouter.attach(source.toString(), dir);
                    getLogger().fine(
                            "  attach directory: from " + dir.getRootRef()
                                    + " to " + source.toString());
                } else if (!bStartsWith) {
                    redirect(router, source.toString(), target.toString(), currentMode);
                } else {
                    redirect(router, source.toString(), target.toString(),
                            currentMode).setMatchingMode(Template.MODE_STARTS_WITH);
                }
            }
        };
        reader.read(getLogger());
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

    @Override
    public synchronized void start() throws Exception {
        super.start();
        updateRootRouter();
    }

    private void updateRootRouter() {
        rootRouter.getRoutes().clear();

        // Set up routes and redirections.
        readRouter(rootRouter, routerPropertiesFileReference);

        // "download" routing
        Router downloadRouter = new Router(getContext());
        downloadRouter.getRoutes().add(new StartsWithRoute(downloadRouter, new Directory(getContext(), this.wwwUri + "/download"), "\\/[a-zA-Z]+"));

        // Serve archives
        downloadRouter.attachDefault(new Directory(getContext(), this.dataUri + "/archive/restlet"));

        rootRouter.attach("/download", downloadRouter);
    }

}
