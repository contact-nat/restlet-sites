/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites.mbean;

import com.restlet.sites.web.BaseApplication;
import org.eclipse.jetty.util.StringUtil;
import org.restlet.Restlet;
import org.restlet.engine.util.StringUtils;
import org.restlet.resource.Directory;
import org.restlet.routing.*;

import java.util.HashMap;
import java.util.Map;

class MBeanHelper {

    private static final Map<Integer, String> redirectionModes;

    static {
        redirectionModes = new HashMap<>();
        redirectionModes.put(Redirector.MODE_CLIENT_FOUND, "CLIENT_FOUND");
        redirectionModes.put(Redirector.MODE_CLIENT_PERMANENT, "CLIENT_PERMANENT");
        redirectionModes.put(Redirector.MODE_CLIENT_SEE_OTHER, "CLIENT_SEE_OTHER");
        redirectionModes.put(Redirector.MODE_CLIENT_TEMPORARY, "CLIENT_TEMPORARY");
        redirectionModes.put(Redirector.MODE_SERVER_INBOUND, "SERVER_INBOUND");
        redirectionModes.put(Redirector.MODE_SERVER_OUTBOUND, "SERVER_OUTBOUND");
    }

    public static String describe(VirtualHost virtualHost) {
        return describe((TemplateRoute) virtualHost.getRoutes().get(0));
    }

    public static Object describeVirtualHostApplication(VirtualHost virtualHost) {
        Restlet restlet = getNextFilteredRestlet(virtualHost.getRoutes().get(0));
        if (restlet instanceof BaseApplication) {
            StringBuilder sb = new StringBuilder();
            BaseApplication app = (BaseApplication) restlet;
            Router router = getNextRouter(app.getInboundRoot());
            if (router != null) {
                for (Route route : router.getRoutes()) {
                    sb.append(describe((TemplateRoute) route)).append("\n");
                }
            }
            return sb.toString();
        }
        return describe((TemplateRoute) virtualHost.getRoutes().get(0));
    }

    public static String describe(TemplateRoute route) {
        StringBuilder sb = new StringBuilder();

        if (route.getTemplate()!= null && !StringUtils.isNullOrEmpty(route.getTemplate().getPattern())) {
            sb.append(route.getTemplate().getPattern());
            sb.append(" => ");
        }

        Restlet next = getNextFilteredRestlet(route);
        if (next instanceof Redirector) {
            Redirector redirector = (Redirector) next;
            sb.append("redirection to ");
            sb.append(redirector.getTargetTemplate());
            sb.append(" using mode ");
            sb.append(redirectionModes.get(redirector.getMode()));
        } else if (next instanceof BaseApplication) {
            BaseApplication application = (BaseApplication) next;
            Router router = getNextRouter(application.getInboundRoot());
            if (router.getRoutes().size() == 1) {
                sb.append(describe((TemplateRoute) router.getRoutes().get(0)));
            } else {
                sb.append("web site with several routes (check \"listRoutes\" operations)");
            }
        } else if (next instanceof Directory) {
            Directory directory = (Directory) next;
            sb.append("directory ");
            sb.append(directory.getRootRef().toString());
        } else {
            sb.append(" ");
            sb.append(next.getClass().getName());
        }

        return sb.toString();
    }

    /**
     * Returns the first Restlet instance which is not a Filter.
     */
    private static Restlet getNextFilteredRestlet(Restlet restlet) {
        if (restlet instanceof Filter) {
            return getNextFilteredRestlet(((Filter) restlet).getNext());
        }
        return restlet;
    }

    /**
     * Returns the first Restlet instance which is a router.
     */
    private static Router getNextRouter(Restlet restlet) {
        if (restlet instanceof Router) {
            return (Router) restlet;
        }
        if (restlet instanceof Filter) {
            return getNextRouter(((Filter) restlet).getNext());
        }
        return null;
    }
}
