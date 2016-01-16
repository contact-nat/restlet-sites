/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.connector;

import com.restlet.sites.properties.PropertiesWrapper;
import com.restlet.sites.web.WebComponent;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.restlet.sites.properties.PropertiesWrapper.AS_STRING;

/**
 * Helper for handling the server connectors.
 */
public class ServerConnectorHelper {

    public static void createServerConnectors(WebComponent webComponent, Optional<Integer> optionalGlobalPort, Collection<VirtualHost> newVirtualHosts, PropertiesWrapper newProperties) throws Exception {
        PropertiesWrapper newConnectorProperties = filterConnectorProperties(newProperties);

        // add the new ports
        Set<Integer> newPorts = extractAllPorts(optionalGlobalPort, newVirtualHosts);
        newPorts.stream()
                .forEach(port -> addNewServer(webComponent, port, newConnectorProperties));
    }

    public static void resetServerConnectors(WebComponent webComponent, PropertiesWrapper currentProperties,
                                             Optional<Integer> optionalNewGlobalPort,
                                             Collection<VirtualHost> newVirtualHosts,
                                             PropertiesWrapper newProperties) throws Exception {
        PropertiesWrapper currentConnectorProperties = filterConnectorProperties(currentProperties);
        PropertiesWrapper newConnectorProperties = filterConnectorProperties(newProperties);

        // check whether the connector properties have been updated
        if (!PropertiesWrapper.equals(currentConnectorProperties, newConnectorProperties)) {
            // the connector properties have been updated: all connectors must be reset

            // first, stop all current connectors then remove them
            for (Server server : webComponent.getServers()) {
                server.stop();
            }
            webComponent.getServers().clear();

            // add the new ones
            Set<Integer> newPorts = extractAllPorts(optionalNewGlobalPort, newVirtualHosts);
            newPorts.stream()
                    .forEach(port -> addNewServer(webComponent, port, newConnectorProperties));
        } else {
            Set<Integer> newPorts = extractAllPorts(optionalNewGlobalPort, newVirtualHosts);

            // remove the useless connectors
            webComponent.getServers().stream()
                    .filter(server -> !newPorts.contains(server.getActualPort()))
                    .forEach(server -> removeServer(webComponent, server));

            // add the new ones i.e. the ones that are not currently used
            Set<Integer> currentPorts = webComponent.getServers().stream()
                    .map(Server::getActualPort)
                    .collect(Collectors.toSet());

            newPorts.stream()
                    .filter(port -> !currentPorts.contains(port))
                    .forEach(port -> addNewServer(webComponent, port, newConnectorProperties));
        }

    }

    private static void addNewServer(WebComponent webComponent, int port, PropertiesWrapper properties) {
        Server server = webComponent.getServers().add(Protocol.HTTP, port);

        // configure the connector
        properties.getPropertiesNames().stream()
                .forEach(name -> server.getContext().getParameters().add(name, properties.get(name, AS_STRING)));
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start the server for port: " + port, e);
        }
    }

    private static void removeServer(WebComponent webComponent, Server server) {
        webComponent.getServers().remove(server);
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException("Cannot stop server listening on port " + server.getActualPort(), e);
        }
    }

    private static final Predicate<String> isConnectorProperty = (String property) ->
            !"web.server.port".equals(property)
                    && !"web.server.access.logs.logger.name".equals(property);

    private static PropertiesWrapper filterConnectorProperties(PropertiesWrapper properties) {
        Properties connectorProperties = new Properties();
        properties.getPropertiesNames().stream()
                .filter(isConnectorProperty)
                .forEach(propertyName -> connectorProperties.put(propertyName, properties.get(propertyName, AS_STRING)));
        return new PropertiesWrapper(connectorProperties);
    }

    private static Set<Integer> extractAllPorts(Optional<Integer> optionalGlobalPort, Collection<VirtualHost> virtualHosts) {
        Set<Integer> ports = virtualHosts.stream()
                .map(vh -> Integer.parseInt(vh.getHostPort()))
                .collect(Collectors.toSet());
        optionalGlobalPort.ifPresent(ports::add);
        return ports;
    }
}
