/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.mbean;

import com.restlet.sites.web.WebComponent;
import org.restlet.routing.Route;
import org.restlet.routing.TemplateRoute;
import org.restlet.routing.VirtualHost;

import javax.management.*;
import java.util.*;

import static com.restlet.sites.properties.PropertiesWrapper.AS_STRING;

/**
 * JMX bean that lists details about the virtual hosts of a worker.
 *
 * @author Thierry Boileau
 */
public class ComponentVirtualHosts extends JmxComponentRouting implements DynamicMBean {
    /**
     * The worker component.
     */
    private final WebComponent component;
    private Map<String, Route> virtualHostRoutesMap;

    /**
     * Constructor.
     *hours
     * @param component The component.
     */
    public ComponentVirtualHosts(WebComponent component) {
        super();
        this.component = component;
    }

    @Override
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException, ReflectionException {
        TemplateRoute route = (TemplateRoute) virtualHostRoutesMap.get(attribute);
        if (route != null) {
            return MBeanHelper.describe(route);
        }

        return null;
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        AttributeList result = new AttributeList();

        for (String attribute : attributes) {
            TemplateRoute route = (TemplateRoute) virtualHostRoutesMap.get(attribute);
            if (route != null) {
                result.add(MBeanHelper.describe(route));
            }
        }

        return result;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        Collection<String> names = getRoutes();
        MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[names.size()];
        Iterator<String> it = names.iterator();
        for (int i = 0; i < attrs.length; i++) {
            String name = it.next();
            attrs[i] = new MBeanAttributeInfo(name, String.class.getCanonicalName(),
                    "Property " + name, true, false, false);
        }

        MBeanOperationInfo refreshHosts = new MBeanOperationInfo("refreshHosts",
                "Is in charge to reload the whole configuration and reset the web servers.",
                new MBeanParameterInfo[0],
                "type",
                MBeanOperationInfo.ACTION_INFO,
                null);

        MBeanParameterInfo paramDomainName = new MBeanParameterInfo("domainName", String.class.getCanonicalName(), "", null);
        MBeanParameterInfo paramPort = new MBeanParameterInfo("port", String.class.getCanonicalName(), "", null);

        MBeanParameterInfo[] params = {paramDomainName, paramPort};

        MBeanOperationInfo describe = new MBeanOperationInfo("describeVirtualHostApplication",
                "Describe the virtual host",
                params,
                "list",
                MBeanOperationInfo.INFO,
                null);

        MBeanOperationInfo listRoutes = new MBeanOperationInfo("listRoutes",
                "List all routes of the virtual host",
                params,
                "list",
                MBeanOperationInfo.INFO,
                null);

        MBeanOperationInfo[] opers = {refreshHosts, describe, listRoutes};
        return new MBeanInfo(this.getClass().getName(),
                "Component virtual hosts MBean", attrs, null, opers, null);
    }

    @Override
    protected Collection<String> getRoutes() {
        virtualHostRoutesMap = new HashMap<>();

        SortedSet<String> names = new TreeSet<>();
        for (VirtualHost virtualHost : component.getHosts()) {
            for (Route r : virtualHost.getRoutes()) {
                StringBuilder sb = new StringBuilder();
                sb.append(virtualHost.getHostScheme()).append("://");
                sb.append(virtualHost.getHostDomain()).append(":");
                sb.append(virtualHost.getHostPort());

                names.add(sb.toString());
                virtualHostRoutesMap.put(sb.toString(), r);
            }
        }
        return names;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        switch (actionName) {
            case "refreshHosts":
                try {
                    component.refreshHosts();
                } catch (Exception e) {
                    throw new RuntimeException("Cannot refresh the list of virtual hosts", e);
                }
                return "OK";
            case "describeVirtualHostApplication": {
                if (params.length < 1) {
                    return "wrong number of parameters";
                }
                String virtualHostName = AS_STRING.convert(params[0]);
                String port = (params.length > 1) ? AS_STRING.convert(params[1]) : null;

                return component.getHosts().stream()
                        .filter(virtualHost -> Objects.equals(virtualHost.getHostDomain(), virtualHostName)
                                && (port == null || port.equals(virtualHost.getHostPort())))
                        .findFirst().map(MBeanHelper::describe).orElse("Nothing");
            }
            case "listRoutes": {
                if (params.length < 1) {
                    return "wrong number of parameters";
                }
                String virtualHostName = AS_STRING.convert(params[0]);
                String port = (params.length > 1) ? AS_STRING.convert(params[1]) : null;

                return component.getHosts().stream()
                        .filter(virtualHost -> Objects.equals(virtualHost.getHostDomain(), virtualHostName)
                                && (port == null || port.equals(virtualHost.getHostPort())))
                        .findFirst().map(MBeanHelper::describeVirtualHostApplication)
                        .orElse("Nothing");
            }
        }
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException {
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

}
