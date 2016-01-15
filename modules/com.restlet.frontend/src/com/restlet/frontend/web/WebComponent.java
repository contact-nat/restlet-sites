/*
 * Copyright 2005-2013 Restlet. All rights reserved.
 */

package com.restlet.frontend.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.Engine;
import org.restlet.engine.application.MetadataExtension;
import org.restlet.engine.connector.HttpClientHelper;
import org.restlet.engine.local.Entity;
import org.restlet.engine.local.FileClientHelper;
import org.restlet.engine.local.FileEntity;
import org.restlet.engine.util.StringUtils;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Redirector;
import org.restlet.routing.VirtualHost;

import com.restlet.frontend.web.applications.BaseApplication;
import com.restlet.frontend.web.services.ComponentPropertiesReader;

/**
 * The web component managing the Restlet web servers.
 * 
 * Concurrency note: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You
 * should be especially careful when storing state in member variables.
 * 
 * @author Jerome Louvel
 */
public class WebComponent extends Component {
    /**
     * Returns a Properties instance loaded from the given URI.
     * 
     * @param propertiesUri
     *            The URI of the properties file.
     * @return@return A Properties instance loaded from the given URI.
     * @throws IOException
     */
    public static Properties getProperties(String propertiesUri)
            throws IOException {
        ClientResource resource = new ClientResource(propertiesUri);
        try {
            Representation rep = resource.get();

            Properties properties = new Properties();
            properties.load(rep.getStream());
            return properties;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot access to the configuration file: \"");
            stringBuilder.append(propertiesUri);
            stringBuilder.append("\"");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

    }

    private int globalPort;

    /**
     * Main method.
     * 
     * @param args
     *            Program arguments.
     */
    public static void main(String[] args) {
        try {
            // Create and start the server
            new WebComponent().start();
        } catch (Exception e) {
            System.err.println("Can't launch the web server.\nAn unexpected exception occurred:");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Constructor.
     */
    public WebComponent() throws Exception {
        super();
        Engine.getInstance().getRegisteredClients().add(0, new HttpClientHelper(null));
        getLogService().setLoggerName("com.noelios.web.WebComponent.www");

        String ipAddress = null;

        refreshHosts();

        // ------------------
        // Add the connectors
        // ------------------
        Server server = getServers().add(Protocol.HTTP, ipAddress, globalPort);
        server.getContext().getParameters().add("useForwardedForHeader", "true");

        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.FILE);
        getClients().add(Protocol.RIAP);
        getClients().add(Protocol.HTTP);
    }

    private void refreshHosts() {
        final Map<String, String> domainSynonyms = new HashMap<String, String>();

        ComponentPropertiesReader reader = new ComponentPropertiesReader("clap://class/webComponent.properties", this) {

            VirtualHost virtualHost = null;

            BaseApplication application = null;

            @Override
            public void handleRoute(String source, String target, int currentMode, boolean bStartsWith) {
                application.handleRoute(source, target, currentMode, bStartsWith);
            }

            @Override
            public void handleComponentProperty(String property, String value) {
                switch (property) {
                case "server.address":
                    // IP address to listen on
                    // ipAddress = value;
                    break;
                case "server.http.port":
                    // Port to listen on
                    globalPort = Integer.parseInt(value);
                    break;
                default:
                    domainSynonyms.put(property, value);
                    break;
                }
            }

            @Override
            public void handleHostRedirection(String source, String target, int redirectionMode, boolean bStartsWith) {
                if (!target.toLowerCase().startsWith("http://")
                        && !target.toLowerCase().startsWith("https://")) {
                    VirtualHost host = addHost(source, globalPort, new Redirector(
                            getContext().createChildContext(), null,
                            redirectionMode) {
                        @Override
                        protected Reference getTargetRef(Request request, Response response) {
                            Reference ref = new Reference(request.getResourceRef());
                            // ref.setHostDomain(getHostDomain(target, properties));
                            return ref;
                        }
                    }, domainSynonyms.get(target));
                    getHosts().add(host);
                } else {
                    getHosts().add(addRedirection(source, globalPort, target, redirectionMode, null));
                }

            }

            @Override
            public void handleVirtualHost(String domain, Integer port, String login, String password,
                    String clientDispatcher, List<MetadataExtension> extensions) {
                application = new BaseApplication("strict".equals(clientDispatcher), extensions, login, password);
                virtualHost = addHost(domain, (port == null) ? globalPort : port, application,
                        domainSynonyms.get(domain));
                getHosts().add(virtualHost);
            }
        };

        reader.read(getLogger());
    }

    /**
     * Defines a new virtual host.
     * 
     * @param host
     *            The host domain.
     * @param port
     *            The port to listen to.
     * @param application
     *            The application.
     * @param domainSynonym
     *            The facultative customized domain name.
     * @return A new virtual host.
     */
    private VirtualHost addHost(String host, int port, Restlet restlet, String domainSynonym) {
        VirtualHost result = new VirtualHost(getContext().createChildContext());
        setHostDomain(result, host, domainSynonym);
        result.setHostPort("80|" + Integer.toString(port));
        result.attach(restlet);
        result.setName(host);
        getLogger().info(result.getHostDomain() + " listens to port " + result.getHostPort());
        return result;
    }

    /**
     * Defines a new host for a redirection.
     * 
     * @param host
     *            The host domain.
     * @param port
     *            The port to listen to.
     * @param redirection
     *            The redirection.
     * @param mode
     *            The redirection mode.
     * @param domainSynonym
     *            The facultative customized domain name.
     * @return A new virtual host.
     */
    private VirtualHost addRedirection(String host, int port, String redirection, int mode, String domainSynonym) {
        VirtualHost result = new VirtualHost(getContext().createChildContext());
        setHostDomain(result, host, domainSynonym);
        result.setName(host);
        result.setHostPort("80|" + Integer.toString(port));
        result.attach(new Redirector(null, redirection, mode));
        getLogger().info(
                result.getHostDomain() + " redirected to \"" + redirection
                        + "\" on port " + result.getHostPort());

        return result;
    }

    /**
     * Sets the host's domain. Could be customized by the "domain.host"
     * property.
     * 
     * @param host
     *            The {@link VirtualHost} to update.
     * @param domain
     *            The domain name.
     * @param domainSynonym
     *            The facultative customized domain name.
     */
    private void setHostDomain(VirtualHost host, String domain, String domainSynonym) {
        if (StringUtils.isNullOrEmpty(domainSynonym)) {
            host.setHostDomain(domain);
        } else {
            host.setHostDomain(domainSynonym);
        }
        if (!domain.equals(host.getHostDomain())) {
            getLogger().info(domain + " swapped to " + host.getHostDomain());
        }

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

}