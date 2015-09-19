package com.restlet.frontend.web.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Component;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Redirector;

public abstract class ComponentPropertiesReader {

    public enum Locus {
        COMPONENT, VIRTUAL_HOSTS_REDIRECTIONS, VIRTUAL_HOST, VIRTUAL_HOST_REDIRECTIONS
    };

    private class VHost {
        String login;

        String password;

        String host;
    }

    private static Map<String, Integer> redirectionModes = new HashMap<>();
    static {
        redirectionModes.put("CLIENT_PERMANENT", Redirector.MODE_CLIENT_PERMANENT);
        redirectionModes.put("CLIENT_FOUND", Redirector.MODE_CLIENT_FOUND);
        redirectionModes.put("CLIENT_SEE_OTHER", Redirector.MODE_CLIENT_SEE_OTHER);
        redirectionModes.put("SEE_OTHER", Redirector.MODE_CLIENT_SEE_OTHER);
        redirectionModes.put("CLIENT_TEMPORARY", Redirector.MODE_CLIENT_TEMPORARY);
        redirectionModes.put("REVERSE_PROXY", Redirector.MODE_SERVER_OUTBOUND);
        redirectionModes.put("ROUTER", -1);
    }

    private Component component;

    private String routerPropertiesRef;

    private VHost vHost;

    private int currentRouterMode = Redirector.MODE_CLIENT_SEE_OTHER;

    private int currentVirtualHostMode = Redirector.MODE_CLIENT_SEE_OTHER;

    private Locus locus;

    public ComponentPropertiesReader(String routerPropertiesRef, Component component) {
        this.component = component;
        this.routerPropertiesRef = routerPropertiesRef;
        locus = Locus.COMPONENT;
    }

    public void read(Logger logger) {
        ClientResource resource = new ClientResource(routerPropertiesRef);
        BufferedReader br = null;

        int lineNumber = 0;
        try {
            Representation rep = resource.get();
            br = new BufferedReader(new InputStreamReader(rep.getStream()));

            String line = null;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                logger.fine("add router instruction: " + line);
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
                    switch (source.toString()) {
                    case "setHostMode":
                        vHost = null;
                        locus = Locus.VIRTUAL_HOSTS_REDIRECTIONS;
                        // Update the current redirection mode
                        currentVirtualHostMode = redirectionModes.get(target.toString());
                        break;
                    case "setHost":
                        vHost = new VHost();
                        vHost.host = target.toString();
                        locus = Locus.VIRTUAL_HOST;
                        break;
                    case "setMode":
                        checkVirtualHost(lineNumber, Locus.VIRTUAL_HOST, Locus.VIRTUAL_HOST_REDIRECTIONS);
                        if (locus != Locus.VIRTUAL_HOST_REDIRECTIONS) {
                            handleVirtualHost(vHost.host, vHost.login, vHost.password);
                        }
                        locus = Locus.VIRTUAL_HOST_REDIRECTIONS;
                        // Update the current redirection mode
                        currentRouterMode = redirectionModes.get(target.toString());
                        break;
                    case "login":
                        checkVirtualHost(lineNumber, Locus.VIRTUAL_HOST);
                        vHost.login = target.toString();
                        break;
                    case "password":
                        checkVirtualHost(lineNumber ,Locus.VIRTUAL_HOST);
                        vHost.password = target.toString();
                        break;
                    default:
                        switch (locus) {
                        case COMPONENT:
                            // general configuration
                            handleComponentProperty(source.toString(), target.toString());
                            break;
                        case VIRTUAL_HOSTS_REDIRECTIONS:
                            handleHostRedirection(source.toString(), target.toString(), currentVirtualHostMode,
                                    bStartsWith);
                            break;
                        case VIRTUAL_HOST:
                            handleRoute(source.toString(), target.toString(), currentRouterMode, bStartsWith);
                            break;
                        default:
                            break;
                        }

                        break;
                    }
                }
            }
        } catch (Throwable t) {
            if (lineNumber > 0) {
                logger.log(Level.SEVERE, "Cannot read properties: " + routerPropertiesRef + " at line " + lineNumber, t);
            } else {
                logger.log(Level.SEVERE, "Cannot read properties: " + routerPropertiesRef, t);
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Cannot close properties: "
                            + routerPropertiesRef, e);
                }
            }
        }
    }

    private void checkVirtualHost(int lineNumber, Locus... locii) {
        boolean found = false;
        for (Locus locus : locii) {
            if (locus == this.locus) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("Error while reading line: " + lineNumber
                    + ". Missing setHost directive before.");
        }
    };

    public abstract void handleVirtualHost(String domain, String login, String password);
    
    public abstract void handleRoute(String source, String target, int currentMode, boolean bStartsWith);

    public abstract void handleHostRedirection(String source, String target, int currentMode, boolean bStartsWith);

    public abstract void handleComponentProperty(String property, String value);

}
