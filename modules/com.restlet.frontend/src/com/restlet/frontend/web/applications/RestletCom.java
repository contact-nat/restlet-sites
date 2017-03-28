/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web.applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.Engine;
import org.restlet.engine.application.Encoder;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.log.LogFilter;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Directory;
import org.restlet.routing.Filter;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.service.CorsService;
import org.restlet.service.LogService;
import org.restlet.util.Series;

import com.restlet.frontend.objects.framework.Distribution;
import com.restlet.frontend.objects.framework.Edition;
import com.restlet.frontend.objects.framework.Version;
import com.restlet.frontend.web.resources.RestletComRefreshResource;
import com.restlet.frontend.web.services.RefreshStatusService;

import freemarker.template.Configuration;

/**
 * Application for the http://restlet.com site.
 * 
 * @author Jerome Louvel
 */
public class RestletCom extends BaseApplication implements RefreshApplication {

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

    /** Freemarker configuration object */
    private Configuration fmc;

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
        this.routerPropertiesFileReference = getProperty("router.uri",
                "clap://class/router.properties");

        this.setStatusService(new RefreshStatusService(true, this));

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
        getMetadataService().addExtension("xsd", MediaType.APPLICATION_XML,
                true);

        this.fmc = new Configuration();
        try {
            this.fmc.setDirectoryForTemplateLoading(new File(
                    new LocalReference(this.wwwUri).getFile(), ""));
        } catch (IOException e) {
            getLogger()
                    .warning(
                            "Cannot set Freemarker templates directory: "
                                    + this.wwwUri);
        }

        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);

        final CorsService corsService = new CorsService();
        corsService.setAllowingAllRequestedHeaders(true);
        corsService.setAllowedOrigins(new HashSet<String>(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);
        corsService.setSkippingResourceForCorsOptions(true);

        getServices().add(corsService);
    }

    private static final class HttpRedirectFilter extends Filter {
        private HttpRedirectFilter(Context context) {
            super(context);
        }

        @Override
        protected int beforeHandle(Request request, Response response) {
            if (request.getResourceRef().getPath().startsWith("/company/blog")) {
                // issue #134 : routes all proxied HTTP urls to HTTPS.
                Series<Header> headers = (Series<Header>) request.getHeaders();
                Protocol protocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));
                if (protocol != null) {
                    if (Protocol.HTTPS.equals(protocol)) {
                        response.redirectTemporary(request.getResourceRef());
                        response.getLocationRef().setProtocol(Protocol.HTTP);
                        return Filter.STOP;
                    }

                }
                return super.beforeHandle(request, response);
            }

            // issue #134 : routes all proxied HTTP urls to HTTPS.
            Series<Header> headers = (Series<Header>) request.getHeaders();
            Protocol protocol = Protocol.valueOf(headers.getFirstValue("X-Forwarded-Proto", true));
            if (protocol != null) {
                request.getHostRef().setProtocol(Protocol.HTTPS);
                request.getResourceRef().setProtocol(Protocol.HTTPS);
                if (request.getResourceRef().getBaseRef() != null) {
                    request.getResourceRef().getBaseRef()
                            .setProtocol(Protocol.HTTPS);
                }
                request.getRootRef().setProtocol(Protocol.HTTPS);
                if (Protocol.HTTP.equals(protocol)) {
                    response.redirectPermanent(request.getResourceRef());
                    response.getLocationRef().setProtocol(Protocol.HTTPS);
                    return Filter.STOP;
                }
            }
            return super.beforeHandle(request, response);
        }
    }

    @Override
    public Restlet createInboundRoot() {
        Engine.setLogLevel(Level.ALL);
        // Create a root router
        rootRouter = new Router(getContext()) {
            @Override
            protected TemplateRoute createRoute(final String uriPattern, final Restlet target, final int matchingMode) {
                if (uriPattern != null && uriPattern.contains("?")) {
                    final int index = uriPattern.indexOf('?');
                    return new QueryTemplateRoute(super.createRoute(uriPattern.substring(0, index), target,
                            matchingMode), new Form(uriPattern.substring(index + 1)));
                }
                return super.createRoute(uriPattern, target, matchingMode);
            }
        };
        rootRouter.setDefaultMatchingMode(Router.MODE_FIRST_MATCH);
        updateRootRouter();

        HttpRedirectFilter redirectFilter = new HttpRedirectFilter(getContext());

        if (siteLogin != null && sitePassword != null) {
            ChallengeAuthenticator ca = new ChallengeAuthenticator(
                    getContext(), ChallengeScheme.HTTP_BASIC, "realm");
            MapVerifier mv = new MapVerifier();
            mv.getLocalSecrets().put(siteLogin, sitePassword);
            mv.getLocalSecrets().put(login, password);
            ca.setVerifier(mv);
            ca.setNext(rootRouter);
            redirectFilter.setNext(ca);
        } else {
            redirectFilter.setNext(rootRouter);
        }

        Encoder encoder = new Encoder(getContext(), false, true,
                getEncoderService());
        encoder.setNext(redirectFilter);

        return encoder;
    }

    public String getDataUri() {
        return this.dataUri;
    }

    public Configuration getFmc() {
        return this.fmc;
    }

    @Override
    public String getName() {
        return "Application for restlet.com";
    }

    public String getWwwUri() {
        return this.wwwUri;
    }

    /**
     * Refreshes the list of distributions, versions, etc.
     */
    public void refresh() {
        updateRootRouter();
    }

    /**
     * Shortcut method that add a {@link CookieSetting} to the response.
     * 
     * @param response
     *            The response to complete.
     * @param name
     *            The name of the coookie.
     * @param value
     *            The value of the cookie.
     */
    private void setCookie(Response response, String name, String value) {
        response.getCookieSettings().add(
                new CookieSetting(0, name, value, "/", null));
    }

    /**
     * Sets up the redirections.
     * 
     * @param router
     *            The router to complete.
     * @param redirectionsFileUri
     *            The URI of the redirections file.
     */
    private void readRouter(Router router, String redirectionsFileUri) {
        ClientResource resource = new ClientResource(redirectionsFileUri);
        try {
            Representation rep = resource.get();
            BufferedReader br = new BufferedReader(new InputStreamReader(rep.getStream()));
            String line = null;
            int currentMode = Redirector.MODE_CLIENT_SEE_OTHER;
            while ((line = br.readLine()) != null) {
                getLogger().fine("add router instruction: " + line);
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                StringBuilder source = new StringBuilder();
                StringBuilder target = new StringBuilder();
                StringBuilder current = source;
                boolean bSource = true;
                boolean bTarget = false;
                boolean bStartsWith = false;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (Character.isWhitespace(c)) {
                        if (bSource) {
                            bSource = false;
                        } else if (bTarget) {
                            break;
                        }
                    } else if (c == '*' && bSource) {
                        bStartsWith = true;
                    } else if (!bSource && !bTarget) {
                        bTarget = true;
                        current = target;
                        current.append(c);
                    } else {
                        current.append(c);
                    }
                }
                if (source.length() > 0 && target.length() > 0) {
                    if ("setMode".equals(source.toString())) {
                        // Update the current redirection mode
                        String strMode = target.toString();
                        if ("CLIENT_PERMANENT".equals(strMode)) {
                            currentMode = Redirector.MODE_CLIENT_PERMANENT;
                        } else if ("CLIENT_FOUND".equals(strMode)) {
                            currentMode = Redirector.MODE_CLIENT_FOUND;
                        } else if ("CLIENT_SEE_OTHER".equals(strMode)) {
                            currentMode = Redirector.MODE_CLIENT_SEE_OTHER;
                        } else if ("CLIENT_TEMPORARY".equals(strMode)) {
                            currentMode = Redirector.MODE_CLIENT_TEMPORARY;
                        } else if ("REVERSE_PROXY".equals(strMode)) {
                            currentMode = Redirector.MODE_SERVER_OUTBOUND;
                        } else if ("ROUTER".equals(strMode)) {
                            currentMode = -1;
                        }
                    } else if (currentMode == -1) {
                        Directory dir = new Directory(getContext(), "file://" + target.toString());
                        rootRouter.attach(source.toString(), dir);
                        getLogger().fine("  attach directory: from " + dir.getRootRef() + " to " + source.toString());
                    } else if (!bStartsWith) {
                        redirect(router, source.toString(), target.toString(), currentMode);
                    } else {
                        redirect(router, source.toString(), target.toString(), currentMode)
                                .setMatchingMode(Template.MODE_STARTS_WITH);
                    }

                }
            }
            br.close();
        } catch (Throwable t) {

        }
    }

    /**
     * Helps to define redirections assuming that the router defines route by
     * using the {@link Template.MODE_STARTS_WITH} mode. Redirection is made by
     * default using the {@link Redirector#MODE_CLIENT_PERMANENT} mode.
     * 
     * @param router
     *            The router where to define the redirection.
     * @param from
     *            The source template.
     * @param to
     *            The target template.
     * @return The defined route.
     */
    private TemplateRoute redirect(Router router, String from, String to) {
        return redirect(router, from, to, Redirector.MODE_CLIENT_PERMANENT);
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
        getLogger().info("TBOI redirect from " + from + " to " + to);
        TemplateRoute route = router.attach(from, new Redirector(getContext(), to, mode) {
            protected void serverRedirect(Restlet next, Reference targetRef, Request request, Response response) {
                if (next == null) {
                    getLogger().warning("No next Restlet provided for server redirection to " + targetRef);
                } else {
                    // Save the base URI if it exists as we might need it for
                    // redirections
                    Reference resourceRef = request.getResourceRef();
                    Reference baseRef = resourceRef.getBaseRef();

                    // Reset the protocol and let the dispatcher handle the protocol
                    request.setProtocol(null);

                    // Update the request to cleanly go to the target URI
                    request.setResourceRef(targetRef);
                    getLogger().info("TBOI Referrer reference before " + request.getReferrerRef());
                    request.setReferrerRef("https://localhost:12003");
                    getLogger().info("TBOI Referrer reference AFTER " + request.getReferrerRef());

                    getLogger().info("TBOI Resource reference " + request.getResourceRef());

                    for (Cookie cookie : request.getCookies()) {
                        getLogger().info("TBOI cookie before "
                                + cookie.getName()
                                + " => "
                                + cookie.getValue());
                    }

                    Series<Cookie> yo = new Series<>(Cookie.class);
                    for (Cookie cookie : request.getCookies()) {
                        if ("sessionid".equals(cookie.getName())) {
                            yo.add(new Cookie(cookie.getName(), cookie.getValue()));
                        } else if ("csrftoken".equals(cookie.getName())) {
                            yo.add(new Cookie(cookie.getName(), cookie.getValue()));
                            request.getHeaders().add("X-CSRFToken", cookie.getValue());
                        }
                    }

                    request.setCookies(yo);
                    for (Cookie c : request.getCookies()) {
                        Engine.getLogger(RestletCom.class).info("TBOI cookie AFTER "
                                + c.getName()
                                + " => "
                                + c.getValue());
                    }
                    request.getAttributes().remove(HeaderConstants.ATTRIBUTE_HEADERS);
                    next.handle(request, response);

                    // Memorize Access-Control-Allow-* headers to reinject in the response
                    Series<Header> resHeaders = response.getHeaders();
                    Series<Header> newHeaders = new Series<Header>(Header.class);
                    if (resHeaders != null) {
                        for (Header h : resHeaders) {
                            if (h.getName().startsWith("Access-Control-Allow")) {
                                newHeaders.add(h.getName(), h.getValue());
                            }
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
                        int matched = rt.parse(response.getLocationRef().toString(),
                                request);

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
        refresh();
    }

    /**
     * Maintains coherency of the cookies
     * 
     * @param version
     *            The current version.
     * @param edition
     *            The current edition.
     * @param distribution
     *            The current distribution.
     * @param response
     *            The current response to update.
     */

    public void updateCookies(Version version, Edition edition, Distribution distribution, Response response) {
        setCookie(response, "branch", version.getMinorVersion());
        setCookie(response, "distribution", distribution.getFileType());
        setCookie(response, "edition", edition.getId());
        setCookie(response, "release", version.getQualifier());
        setCookie(response, "version", version.getId());
    }

    private void updateRootRouter() {
        rootRouter.getRoutes().clear();

        // Guarding access to sensitive services
        ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(),
                ChallengeScheme.HTTP_BASIC, "Admin section");
        MapVerifier verifier = new MapVerifier();
        verifier.getLocalSecrets().put(this.login, this.password);
        guard.setVerifier(verifier);
        guard.setNext(RestletComRefreshResource.class);
        rootRouter.attach("/rf-refresh", guard);

        // Set up routes and redirections.
        readRouter(rootRouter, routerPropertiesFileReference);

        // "download" routing
        Router downloadRouter = new Router(getContext());
        downloadRouter.getRoutes().clear();
        downloadRouter.getRoutes().add(
                new StartsWithRoute(downloadRouter, new Directory(getContext(), this.wwwUri + "/download"),
                        "\\/[a-zA-Z]+"));

        // Serve archives
        downloadRouter.attachDefault(new Directory(getContext(), this.dataUri
                + "/archive/restlet"));

        rootRouter.attach("/download", downloadRouter);
    }

    private static class QueryTemplateRoute extends TemplateRoute {
        private final TemplateRoute wrappedRoute;

        private final Form queryForm;

        public QueryTemplateRoute(final TemplateRoute wrappedRoute, final Form queryForm) {
            super(wrappedRoute.getNext());
            this.wrappedRoute = wrappedRoute;
            this.queryForm = queryForm;
        }

        @Override
        public float score(final Request request, final Response response) {
            final boolean matchQueryParams = request.getResourceRef().getQueryAsForm().containsAll(queryForm);
            if (matchQueryParams) {
                return wrappedRoute.score(request, response);
            }
            return 0;
        }
    }

    @Override
    public Restlet createOutboundRoot() {
        LogService logService = new LogService() {
            @Override
            protected String getDefaultResponseLogMessage(Response arg0, int arg1) {
                StringBuilder sb = new StringBuilder(super.getDefaultResponseLogMessage(arg0, arg1));
                sb.append("\n");
                sb.append("request header: " + arg0.getRequest().getHeaders());
                sb.append("\n" + sun.net.www.protocol.http.HttpURLConnection.class);
                return sb.toString();
            }
        };
        logService.setLoggerName("com.noelios.web.WebComponent.www");
        Filter filter = new LogFilter(getContext(), logService);
        filter.setNext(super.createOutboundRoot());
        return filter;
    }

}
