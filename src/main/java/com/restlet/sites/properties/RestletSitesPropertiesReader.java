/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.properties;

import com.restlet.sites.filter.CacheInstruction;
import com.restlet.sites.filter.DirectoryInstruction;
import com.restlet.sites.web.RedirectedVirtualHost;
import com.restlet.sites.web.RestletSiteVirtualHost;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.engine.application.MetadataExtension;
import org.restlet.routing.VirtualHost;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

import static com.restlet.sites.filter.HttpRedirectFilter.HttpMode;
import static com.restlet.sites.web.BaseApplication.RedirectionMode;
import static java.lang.String.format;
import static org.restlet.engine.util.StringUtils.isNullOrEmpty;

public class RestletSitesPropertiesReader {

    private static final Logger logger = Logger.getLogger(RestletSitesPropertiesReader.class.getCanonicalName());
    private final Map<String, VirtualHost> vHosts = new HashMap<>();
    private final Optional<Integer> optionalGlobalPort;
    private final Context context;
    private Map<String, String> hostSynonyms = new HashMap<>();
    private RestletSiteVirtualHost vHost;
    private RedirectionMode currentRouterMode;
    private RedirectionMode currentVirtualHostMode;
    private HttpMode currentHttpMode;

    private CacheInstruction currentCacheInstruction;
    private DirectoryInstruction currentDirectoryInstruction;

    private Locus locus;

    public RestletSitesPropertiesReader(Context context, Optional<Integer> optionalGlobalPort) {
        this.optionalGlobalPort = optionalGlobalPort;
        this.context = context;
    }

    public Collection<VirtualHost> getVirtualHosts() {
        return vHosts.values();
    }

    public void read(Path vHostConfigurationPath) {

        currentRouterMode = RedirectionMode.CLIENT_SEE_OTHER;
        currentVirtualHostMode = RedirectionMode.CLIENT_SEE_OTHER;
        currentCacheInstruction = new CacheInstruction();
        currentDirectoryInstruction = new DirectoryInstruction();
        locus = Locus.VIRTUAL_HOSTS_SYNONYMS;

        int lineNumber = 0;
        try (BufferedReader br = Files.newBufferedReader(vHostConfigurationPath)) {

            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.replace("\t", " ").trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int firstSpaceIndex = line.indexOf(" ");
                String action = (firstSpaceIndex != -1) ? line.substring(0, firstSpaceIndex) : line;
                String parameter = (firstSpaceIndex != -1) ? line.substring(firstSpaceIndex).trim() : null;

                if (isNullOrEmpty(action) || isNullOrEmpty(parameter)) {
                    continue;
                }

                logger.fine("add router instruction: " + line);

                switch (action) {
                    case "setHostMode":
                        vHost = null;
                        locus = Locus.VIRTUAL_HOSTS_REDIRECTIONS;
                        // Update the current redirection mode
                        currentVirtualHostMode = RedirectionMode.valueOf(parameter);
                        break;

                    case "setHost":
                        vHost = new RestletSiteVirtualHost(context, parameter, optionalGlobalPort);
                        vHosts.put(vHost.getHostDomain(), vHost);
                        locus = Locus.VIRTUAL_HOST;
                        break;
                    case "setHostPort":
                        checkVirtualHost(lineNumber, locus);
                        vHost.setHostPort(parameter);
                        break;
                    case "setLogin":
                        checkVirtualHost(lineNumber, locus);
                        vHost.application.setLogin(parameter);
                        break;
                    case "setPassword":
                        checkVirtualHost(lineNumber, locus);
                        vHost.application.setPassword(parameter);
                        break;
                    case "setFileClient":
                        checkVirtualHost(lineNumber, locus);
                        vHost.application.setStrictFileClient("strict".equals(parameter));
                        break;
                    case "mapExtensions":
                        checkVirtualHost(lineNumber, locus);
                        mapExtensionsToMediaType(parameter);
                        break;

                    case "setMode":
                        checkVirtualHost(lineNumber, locus);
                        // Update the current redirection mode
                        currentRouterMode = RedirectionMode.valueOf(parameter);
                        currentCacheInstruction = new CacheInstruction();
                        currentDirectoryInstruction = new DirectoryInstruction();
                        break;
                    case "setHttpMode":
                        checkVirtualHost(lineNumber, locus);
                        currentHttpMode = HttpMode.valueOf(parameter);
                        break;
                    case "withCacheExpires":
                        checkVirtualHost(lineNumber, locus);
                        currentCacheInstruction = currentCacheInstruction.withExpires(parameter);
                        break;
                    case "withCacheControl":
                        checkVirtualHost(lineNumber, locus);
                        currentCacheInstruction = currentCacheInstruction.withCacheControl(parameter);
                        break;
                    case "withDirectoryIndex":
                        checkVirtualHost(lineNumber, locus);
                        currentDirectoryInstruction = currentDirectoryInstruction.withIndex(parameter);
                        break;
                    case "withDirectoryListingAllowed":
                        checkVirtualHost(lineNumber, locus);
                        currentDirectoryInstruction = currentDirectoryInstruction.withListingAllowed(Boolean.parseBoolean(parameter));
                        break;
                    case "withDirectoryNegotiatingContent":
                        checkVirtualHost(lineNumber, locus);
                        currentDirectoryInstruction = currentDirectoryInstruction.withNegotiatingContent(Boolean.parseBoolean(parameter));
                        break;
                    case "withTryFiles":
                        checkVirtualHost(lineNumber, locus);
                        currentDirectoryInstruction = currentDirectoryInstruction.withTryFiles(parameter);
                        break;
                    default:
                        if (locus == Locus.VIRTUAL_HOSTS_SYNONYMS) {
                            hostSynonyms.put(action, parameter);
                        } else {
                            handleRoute(action, parameter);
                        }
                        break;
                }
            }

            resolveSynonyms();

        } catch (Throwable t) {
            if (lineNumber == 0) {
                throw new RuntimeException(format("Cannot read properties: %s", vHostConfigurationPath.toString()), t);
            }
            throw new RuntimeException(format("Cannot read properties: %s at line %d", vHostConfigurationPath.toString(), lineNumber), t);
        }
    }

    private void handleRoute(String contextPath, String parameter) {
        switch (locus) {
            case VIRTUAL_HOSTS_REDIRECTIONS:
                vHosts.put(contextPath, new RedirectedVirtualHost(context, contextPath, optionalGlobalPort, parameter, currentVirtualHostMode.redirectionMode));
                break;
            case VIRTUAL_HOST:
                if (currentRouterMode == RedirectionMode.DIRECTORY || currentRouterMode == RedirectionMode.ROUTER) {
                    vHost.application.addDirectory(currentHttpMode, contextPath, parameter, currentCacheInstruction, currentDirectoryInstruction);
                } else {
                    vHost.application.addRedirection(currentHttpMode, currentRouterMode, contextPath, parameter);
                }
                break;
        }
    }

    private void mapExtensionsToMediaType(String target) {
        MediaType mediaType = null;
        StringTokenizer st = new StringTokenizer(target);
        while (st.hasMoreTokens()) {
            String string = st.nextToken();
            if (mediaType == null) {
                // first value is the media type
                mediaType = new MediaType(string, string);
            } else {
                // then the other ones are the extensions
                vHost.application.extensions.add(new MetadataExtension(string, mediaType));
            }
        }
    }

    private void checkVirtualHost(int lineNumber, Locus currentLocus) {
        if (Locus.VIRTUAL_HOST != currentLocus) {
            throw new RuntimeException(format("Error while reading line: %d. Missing setHost directive before.", lineNumber));
        }
    }

    public enum Locus {
        VIRTUAL_HOSTS_SYNONYMS, VIRTUAL_HOSTS_REDIRECTIONS, VIRTUAL_HOST
    }

    private void resolveSynonyms() {
        Map<String, String> newHostSynonyms = new HashMap<>();

        for (Map.Entry<String, String> entry : hostSynonyms.entrySet()) {
            String hostDomain = entry.getKey();
            VirtualHost virtualHost = vHosts.get(hostDomain);
            if (virtualHost == null) {
                newHostSynonyms.put(entry.getKey(), entry.getValue());
                continue;
            }
            String hostSynonym = entry.getValue();

            if (virtualHost instanceof RedirectedVirtualHost) {
                virtualHost.setHostDomain(hostSynonym);
                logger.info(hostDomain + " swapped to " + hostSynonym);
            } else if (virtualHost instanceof RestletSiteVirtualHost) {
                logger.info(entry.getKey() + " swapped to " + entry.getValue());
                virtualHost.setHostDomain(hostSynonym);
            }
        }
        hostSynonyms = newHostSynonyms;
    }

}
