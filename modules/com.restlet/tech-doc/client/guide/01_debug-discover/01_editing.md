## Introduction

=> complete the introduction perhaps

User can edit every part of the request:

 * method
 * URL (including query parameters)
 * headers
 * body (according to the method the body part cannot be edited)

=> Insert graphic with numbered ticks

The list of proposed methods can be extended via the settings. This usage is a little beyond the scope of this simple vademecum and will be described => here (TODO).

The query parameters can be enter directly in the URL text field or in a simple tabular form.

The headers can be edited either as simple tabular form or as a raw block of text. I the latter case, you have to comply with the [syntax of HTTP headers](https://tools.ietf.org/html/rfc7230#section-3.2).

The editor of the body part is a little bit more complex as it covers several cases and is at the same time still intuitive to use.
It guides you to manually edit:

 * simple text plain payloads
 * Json/XML/HTTP payloads with syntax coloration
 * Web forms or multipart form payloads [cf definition](https://www.w3.org/TR/html401/interact/forms.html#h-17.13.4)

The editor of the body part also helps you also to upload files from your drives.
Please note that these contents are not persisted which means two things:

 * you will have to upload again the file after reloading the request .
 * you can't run such requests using the Maven Plugin,

## Edition helpers

Completion is available for the header names, and for the values of the following headers:

 * accept, accept-charset, accept-encoding, accept-language, accept-ranges
 * allow
 * expect
 * cache-control
 * content-encoding, content-language, content-type
 * transfer-encoding

The header "Content-type" is automatically set according to the format of current body editor. You can still override the preset value.

When a request has been run, cf "Sending a request" paragraph, you can directly add headers automatically set by the browser to the definition of the request.
This is fine to mimic the browser behavior when using the maven plugin, for example.  

When a request has been run, cf "Sending a request" paragraph, the "Pretty", mode of the response body reader empasizes the links parsed from the body.
When clicking on a link, the URL field is updated.

