/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import com.restlet.sites.connector.StrictFileClientHelper;
import com.restlet.sites.filter.CacheFilter;
import com.restlet.sites.filter.CacheInstruction;
import com.restlet.sites.filter.DirectoryInstruction;
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
import org.restlet.engine.log.LogFilter;
import org.restlet.engine.util.StringUtils;
import org.restlet.resource.Directory;
import org.restlet.routing.Redirector;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.service.LogService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        if (strictFileClient) {
            StrictFileClientHelper.setStrictClientDispatcher(getContext());
        }

        // Add the filters
        RestletChain restletChain = new RestletChain();

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

    public void addRedirection(HttpRedirectFilter.HttpMode httpMode, RedirectionMode mode, String contextPath, String target) {
        RestletChain chain = new RestletChain();

        if (httpMode != null) {
            chain.add(new HttpRedirectFilter(getContext(), httpMode));
        }

        chain.add(redirect(target, mode.redirectionMode));

        if (contextPath.endsWith("*")) {
            rootRouter.attach(contextPath.replace("*", ""), chain.getFirst()).setMatchingMode(Template.MODE_STARTS_WITH);
        } else if (target.endsWith("*")) {
            rootRouter.attach(contextPath, chain.getFirst()).setMatchingMode(Template.MODE_STARTS_WITH);
        } else if (target.endsWith("{rr}")) {
            rootRouter.attach(contextPath, chain.getFirst()).setMatchingMode(Template.MODE_STARTS_WITH);
        } else {
            rootRouter.attach(contextPath, chain.getFirst());
        }
    }

    public void addDirectory(HttpRedirectFilter.HttpMode httpMode, String contextPath, String filePath, CacheInstruction cacheInstruction, DirectoryInstruction directoryInstruction) {
        RestletChain chain = new RestletChain();

        if (httpMode != null) {
            chain.add(new HttpRedirectFilter(getContext(), httpMode));
        }
        if (cacheInstruction != null
                && (
                !StringUtils.isNullOrEmpty(cacheInstruction.getExpires())
                        || StringUtils.isNullOrEmpty(cacheInstruction.getCacheControl())
        )) {
            chain.add(new CacheFilter(getContext(), cacheInstruction));
        }

        Path path = Paths.get(filePath);
        if (!Files.isDirectory(path)) {
            getLogger().warning(format("'%s' is not a valid directory", filePath));
        }

        Directory directory;
        if (directoryInstruction != null
                && directoryInstruction.getTryFiles() != null
                && !directoryInstruction.getTryFiles().isEmpty()) {
            directory = new TryFilesDirectory(getContext(), "file://" + filePath, directoryInstruction.getTryFiles());
        } else {
            directory = new Directory(getContext(), "file://" + filePath);
        }

        if (directoryInstruction != null) {
            directory.setIndexName(directoryInstruction.getIndex());
            directory.setListingAllowed(directoryInstruction.isListingAllowed());
            directory.setNegotiatingContent(directoryInstruction.isNegotiatingContent());
        }
        chain.add(directory);

        rootRouter.attach(contextPath, chain.getFirst());
    }

    private Redirector redirect(String to, int mode) {
        Redirector redirector;

        if (to.endsWith("*")) {
            redirector = new Redirector(getContext(), to.replace("*", "{rr}"), mode);
        } else {
            redirector = new Redirector(getContext(), to, mode);
        }
        redirector.setHeadersCleaning(true);

        return redirector;
    }

    public enum RedirectionMode {
        CLIENT_PERMANENT(Redirector.MODE_CLIENT_PERMANENT),
        CLIENT_FOUND(Redirector.MODE_CLIENT_FOUND),
        CLIENT_SEE_OTHER(Redirector.MODE_CLIENT_SEE_OTHER),
        SEE_OTHER(Redirector.MODE_CLIENT_SEE_OTHER),
        CLIENT_TEMPORARY(Redirector.MODE_CLIENT_TEMPORARY),
        REVERSE_PROXY(Redirector.MODE_SERVER_OUTBOUND),
        DIRECTORY(-1),
        ROUTER(-1);

        public final int redirectionMode;

        RedirectionMode(int redirectionMode) {
            this.redirectionMode = redirectionMode;
        }
    }

    @Override
    public Restlet createOutboundRoot() {
        RestletChain chain = new RestletChain();

        chain.add(new LogFilter(getContext(), new LogService()));
        chain.add(super.createOutboundRoot());

        return chain.getFirst();
    }
}
