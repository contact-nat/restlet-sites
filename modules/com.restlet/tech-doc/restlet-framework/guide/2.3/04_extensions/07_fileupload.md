Apache FileUpload extension
===========================

Introduction
============

This extension leverages the [Apache FileUpload
library](http://commons.apache.org/fileupload/)
to provide a robust, high-performance, Web-based file upload in Restlet
server-side applications.

Description
===========

This extension lets you receive files sent by POST or PUT requests and
to parse the posted entity (which is actually an instance of the
"Representation" class) and to extract a list of FileItems from it, each
item corresponding to one file uploaded in the posted request, typically
from a Web form.

For additional details, please consult the
[Javadocs](javadocs://jse/ext/org/restlet/ext/fileupload/package-summary.html).

Here is the list of dependencies for this extension:

-   [Java Servlet](http://java.sun.com/products/servlet/)
-   [Apache Commons FileUpload](http://jakarta.apache.org/commons/fileupload/)

Usage example
=============

This sample code illustrates how to upload files with the FileUpload
extension. It is composed of 3 classes:

-   a resource "MyResource" which responds to GET and POST requests,
-   an application called "MyApplication" which routes all received
    requests to the resource,
-   a component called "TestFileUpload" which creates a local HTTP
    server on port 8182 and contains only one application (one instance
    of "MyApplication") attached to the path "/testFileUpload".

Thus, each request to the following uri
"http://localhost/testFileUpload" will be handled by a new instance of
"MyResource".

The single representation of this resource is a web form with a file
select control and a submit button. It allows to set up a request with
an uploaded file that will be posted to the resource. The name of the
file select control ("fileToUpload") is referenced by the resource.

Every Resource instance handles the POST request in method "accept"
which accepts the posted entity as single parameter. The aim of the
MyResource instance is to parse the request, get the file, save it on
disk and send back its content as plain text to the client.

Here is the content of the MyResource\#accept method:

~~~~ {.brush: .java}
    @Post
    public Representation accept(Representation entity) throws Exception {
        Representation rep = null;
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
                    true)) {
                String fileName = "c:\\temp\\file.txt";

                // The Apache FileUpload project parses HTTP requests which
                // conform to RFC 1867, "Form-based File Upload in HTML". That
                // is, if an HTTP request is submitted using the POST method,
                // and with a content type of "multipart/form-data", then
                // FileUpload can parse that request, and get all uploaded files
                // as FileItem.

                // 1/ Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000240);

                // 2/ Create a new file upload handler based on the Restlet
                // FileUpload extension that will parse Restlet requests and
                // generates FileItems.
                RestletFileUpload upload = new RestletFileUpload(factory);
                List<FileItem> items;

                // 3/ Request is parsed by the handler which generates a
                // list of FileItems
                items = upload.parseRequest(getRequest());

                // Process only the uploaded item called "fileToUpload" and
                // save it on disk
                boolean found = false;
                for (final Iterator<FileItem> it = items.iterator(); it
                        .hasNext()
                        && !found;) {
                    FileItem fi = it.next();
                    if (fi.getFieldName().equals("fileToUpload")) {
                        found = true;
                        File file = new File(fileName);
                        fi.write(file);
                    }
                }

                // Once handled, the content of the uploaded file is sent
                // back to the client.
                if (found) {
                    // Create a new representation based on disk file.
                    // The content is arbitrarily sent as plain text.
                    rep = new FileRepresentation(new File(fileName),
                            MediaType.TEXT_PLAIN, 0);
                } else {
                    // Some problem occurs, sent back a simple line of text.
                    rep = new StringRepresentation("no file uploaded",
                            MediaType.TEXT_PLAIN);
                }
            }
        } else {
            // POST request with no entity.
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        return rep;
    }
~~~~

Before running this example, please add the following jars to the
classpath:

-   org.restlet (Restlet API)
-   org.restlet.ext.fileupload (Restlet extension based on the Apache FileUpload project)
-   org.apache.commons.fileupload (Apache FileUpload project)
-   org.apache.commons.io (Apache FileUpload project)
-   javax.servlet.jar (Servlet archive used by the FileUpload library)

Links
-----

-   [Apache FileUpload library](http://commons.apache.org/fileupload/)
-   [Server connectors](../core/base/connectors/#server-connectors-1)
-   [Sample code of FileUpload extension (zip file)](/learn/guide/2.1#/42-restlet.html "Usage example of FileUpload extension")

