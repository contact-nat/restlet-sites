/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import com.restlet.sites.connector.StrictFileClientHelper;
import com.restlet.sites.filter.CacheFilter;
import com.restlet.sites.filter.HttpRedirectFilter;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.engine.application.Encoder;
import org.restlet.engine.application.MetadataExtension;
import org.restlet.engine.util.StringUtils;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.routing.TemplateRoute;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Base application that describes shared behaviour.
 *
 * @author Jerome Louvel
 * @author Thierry Boileau
 */
public class BaseApplication extends Application {

    private final Router rootRouter;
    public List<MetadataExtension> extensions = new ArrayList<>();
    public List<HostInstruction> instructions = new ArrayList<>();
    private HttpMode httpMode;
    private String login;
    private String password;
    private boolean strictFileClient;

    public BaseApplication(Context context) {
        super(context);

        getRangeService().setEnabled(false);
        getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
        getMetadataService().setDefaultLanguage(null);
        getMetadataService().addExtension("html", MediaType.TEXT_HTML, true);

        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.FILE);

        rootRouter = new Router();
    }

    public BaseApplication setExtensions(List<MetadataExtension> extensions) {
        this.extensions = extensions;
        if (extensions != null && !extensions.isEmpty()) {
            for (MetadataExtension metadataExtension : extensions) {
                getMetadataService().addExtension(metadataExtension.getName(), metadataExtension.getMetadata());
            }
        }
        return this;
    }

    public BaseApplication setInstructions(List<HostInstruction> instructions) {
        this.instructions = instructions;
        return this;
    }

    public BaseApplication setHttpMode(HttpMode httpMode) {
        this.httpMode = httpMode;
        return this;
    }


    public BaseApplication setLogin(String login) {
        this.login = login;
        return this;
    }

    public BaseApplication setPassword(String password) {
        this.password = password;
        return this;
    }

    public BaseApplication setStrictFileClient(boolean strictFileClient) {
        this.strictFileClient = strictFileClient;
        return this;
    }

    @Override
    public Restlet createInboundRoot() {
        rootRouter.setContext(getContext());
        instructions.forEach(this::handleRoute);

        if (strictFileClient) {
            StrictFileClientHelper.setStrictClientDispatcher(getContext());
        }

        // Add the filters
        RestletChain restletChain = new RestletChain();

        if (httpMode == HttpMode.REDIRECT_TO_HTTPS) {
            return new HttpRedirectFilter(getContext());
        }
        restletChain.add(new Encoder(getContext(), false, true, getEncoderService()));

        if (!StringUtils.isNullOrEmpty(login) && !StringUtils.isNullOrEmpty(password)) {
            ChallengeAuthenticator ca = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "realm");
            MapVerifier mv = new MapVerifier();
            mv.getLocalSecrets().put(login, password.toCharArray());
            ca.setVerifier(mv);

            restletChain.add(ca);
        }
        restletChain.add(rootRouter);

        return restletChain.getFirst();
    }

    private void handleRoute(HostInstruction hostInstruction) {
        if (hostInstruction.mode == RedirectionMode.ROUTER) {
            Directory dir = new Directory(getContext(), "file://" + hostInstruction.target);

            if (hostInstruction.directoryInstruction != null) {
                DirectoryInstruction instruction = hostInstruction.directoryInstruction;
                if (!StringUtils.isNullOrEmpty(instruction.index)) {
                    dir.setIndexName(instruction.index);
                }
                dir.setListingAllowed(instruction.listingAllowed);
                dir.setNegotiatingContent(instruction.negotiatingContent);
            }

            if (hostInstruction.cacheInstruction != null
                    && (
                    !StringUtils.isNullOrEmpty(hostInstruction.cacheInstruction.expires)
                            || StringUtils.isNullOrEmpty(hostInstruction.cacheInstruction.cacheControl)
            )) {
                CacheFilter cacheFilter = new CacheFilter(getContext(), dir, hostInstruction.cacheInstruction);
                rootRouter.attach(hostInstruction.source, cacheFilter);
            } else {
                rootRouter.attach(hostInstruction.source, dir);
            }

            getLogger().fine(format("  attach directory: from %s to %s", dir.getRootRef().toString(), hostInstruction.source));
        } else if (!hostInstruction.target.contains("*")) {
            redirect(rootRouter, hostInstruction.source, hostInstruction.target, hostInstruction.mode.redirectionMode);
        } else {
            redirect(rootRouter, hostInstruction.source, hostInstruction.target, hostInstruction.mode.redirectionMode).setMatchingMode(Template.MODE_STARTS_WITH);
        }
    }

    /**
     * Helps to define redirections assuming that the router defines route by
     * using the {@link Template#MODE_STARTS_WITH} mode.
     *
     * @param router The router where to define the redirection.
     * @param from   The source template.
     * @param to     The target template.
     * @param mode   The redirection mode (cf {@link Redirector}.
     * @return The defined route.
     */
    private TemplateRoute redirect(Router router, String from, String to, int mode) {
        Redirector redirector = new Redirector(getContext(), to, mode);
        redirector.setHeadersCleaning(true);
        TemplateRoute route = router.attach(from, redirector);
        if (to.contains("{rr}")) {
            route.setMatchingMode(Template.MODE_STARTS_WITH);
        }
        return route;
    }

    public enum RedirectionMode {
        CLIENT_PERMANENT(Redirector.MODE_CLIENT_PERMANENT),
        CLIENT_FOUND(Redirector.MODE_CLIENT_FOUND),
        CLIENT_SEE_OTHER(Redirector.MODE_CLIENT_SEE_OTHER),
        SEE_OTHER(Redirector.MODE_CLIENT_SEE_OTHER),
        CLIENT_TEMPORARY(Redirector.MODE_CLIENT_TEMPORARY),
        REVERSE_PROXY(Redirector.MODE_SERVER_OUTBOUND),
        ROUTER(-1);


        public final int redirectionMode;

        RedirectionMode(int redirectionMode) {
            this.redirectionMode = redirectionMode;
        }
    }

    public enum HttpMode {
        REDIRECT_TO_HTTPS
    }

    public static class CacheInstruction {
        public String expires;
        public String cacheControl;
    }

    public static class DirectoryInstruction {
        public String index = null;
        public boolean listingAllowed = false;
        public boolean negotiatingContent = true;
    }

    public static class HostInstruction {
        public final BaseApplication.RedirectionMode mode;
        public final String source;
        public final String target;
        public final CacheInstruction cacheInstruction;
        public final DirectoryInstruction directoryInstruction;

        public HostInstruction(BaseApplication.RedirectionMode mode, String source, String target, CacheInstruction cacheInstruction, DirectoryInstruction directoryInstruction) {
            this.mode = mode;
            this.source = source;
            this.target = target;
            this.cacheInstruction = cacheInstruction;
            this.directoryInstruction = directoryInstruction;
        }
    }
}
