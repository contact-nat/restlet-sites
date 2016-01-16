/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.web;

import com.restlet.sites.mbean.ComponentVirtualHosts;
import com.restlet.sites.properties.PropertiesWrapper;
import com.restlet.sites.properties.RestletSitesPropertiesReader;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.connector.HttpClientHelper;
import org.restlet.routing.VirtualHost;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.restlet.sites.connector.ServerConnectorHelper.createServerConnectors;
import static com.restlet.sites.connector.ServerConnectorHelper.resetServerConnectors;
import static com.restlet.sites.properties.PropertiesHelper.loadGlobalProperties;
import static com.restlet.sites.properties.PropertiesWrapper.AS_INTEGER;
import static com.restlet.sites.properties.PropertiesWrapper.AS_STRING;
import static java.util.Objects.requireNonNull;

public class WebComponent extends Component {

    private final Optional<Path> globalConfigurationPath;
    private final Path vHostsConfigurationRootPath;
    private PropertiesWrapper globalProperties;

    public WebComponent(Optional<Path> globalConfigurationPath, Path vHostsConfigurationRootPath) throws Exception {
        super();
        requireNonNull(vHostsConfigurationRootPath);

        this.globalConfigurationPath = globalConfigurationPath;
        this.vHostsConfigurationRootPath = vHostsConfigurationRootPath;

        // Set the internal HTTP client connector.
        Engine.getInstance().getRegisteredClients().add(0, new HttpClientHelper(null));
        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.FILE);
        getClients().add(Protocol.RIAP);
        getClients().add(Protocol.HTTP);

        refreshHosts();
    }

    public void refreshHosts() throws Exception {
        PropertiesWrapper newGlobalProperties = loadGlobalProperties(globalConfigurationPath);

        Optional<Integer> newGlobalPort = Optional.ofNullable(newGlobalProperties.get("web.server.port", AS_INTEGER));
        getLogService().setLoggerName(newGlobalProperties.get("web.server.access.logs.logger.name", AS_STRING));

        RestletSitesPropertiesReader rReader = new RestletSitesPropertiesReader(getContext().createChildContext(), newGlobalPort);

        for (Path path : Files.list(vHostsConfigurationRootPath).filter(p -> Files.isRegularFile(p)).collect(Collectors.toList())) {
            rReader.read(path);
        }

        if (this.globalProperties == null) {
            createServerConnectors(this, newGlobalPort, rReader.getVirtualHosts(), newGlobalProperties);
        } else {
            resetServerConnectors(this, this.globalProperties, newGlobalPort, rReader.getVirtualHosts(), newGlobalProperties);
        }

        this.globalProperties = newGlobalProperties;

        // remove current virtual hosts and add the new ones
        getHosts().clear();
        for (VirtualHost virtualHost : rReader.getVirtualHosts()) {
            getHosts().add(virtualHost);
        }
        updateHosts();
    }

    @Override
    public synchronized void start() throws Exception {
        super.start();
        // Start the JMX server, and attach the declared beans.
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.restlet.frontend.routing.virtualHosts:type=DynamicMBean");
        mbs.registerMBean(new ComponentVirtualHosts(this), name);
    }
}