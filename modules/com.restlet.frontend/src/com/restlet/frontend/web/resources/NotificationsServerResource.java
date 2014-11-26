package com.restlet.frontend.web.resources;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public class NotificationsServerResource extends BaseResource {

    @Get
    public Representation toHtml() {
        Map<String, Object> dataModel = new HashMap<String, Object>();
        if (getQueryValue("file") != null) {
            // Take care because we accept data that can be handled by the user.
            Reference ref = new Reference(getQueryValue("file"));
            ref.normalize();
            if (ref.toString(false, false).startsWith("/download")) {
                dataModel.put("downloadurl", ref.toString(false, false));
            }
        }
        return getHTMLTemplateRepresentation("download/notifications.html",
                dataModel);
    }
}
