package com.restlet.sites.web;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.Directory;

import java.nio.file.Paths;
import java.util.List;

public class TryFilesDirectory extends Directory {

    private List<String> tryFiles;

    public TryFilesDirectory(Context context, String rootUri, List<String> tryFiles) {
        super(context, rootUri);
        this.tryFiles = tryFiles;
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
        if (!Status.CLIENT_ERROR_NOT_FOUND.equals(response.getStatus())
                || (!Method.GET.equals(request.getMethod()) && !Method.HEAD.equals(request.getMethod()))) {
            return;
        }
        if (tryFiles == null || tryFiles.isEmpty()) {
            return;
        }

        // let's loop over the list of fallback uris.
        for (String tryFile : tryFiles) {
            Request req = new Request(request);
            Response resp = new Response(req);
            Reference baseRef = request.getResourceRef().getBaseRef();

            // compute the new reference
            Reference newReference = new Reference(baseRef.toString());
            newReference.setPath(Paths.get(baseRef.getPath(), tryFile).toString());
            // do as if the request has been routed the same way
            newReference.setBaseRef(baseRef);
            req.setResourceRef(newReference);

            // Let's the directory server resource handles this new request.
            super.handle(req, resp);
            if (!Status.CLIENT_ERROR_NOT_FOUND.equals(resp.getStatus())) {
                response.setStatus(resp.getStatus());
                response.setEntity(resp.getEntity());
                break;
            }
        }
    }
}
