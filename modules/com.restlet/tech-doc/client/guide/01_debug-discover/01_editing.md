## Introduction

=> complete the introduction perhaps

User can edit every part of the request:

 * method
 * URL (including query parameters)
 * headers
 * body

=> Insert graphic with numbered ticks

As you can notice above, each part has a dedicated area with a specific and adapted edition tool. We won't describe each of them in an exhaustive manner, since most of them are quite simple to apprehend.
We prefer to focus on some particularities.

### Method

By default, the field proposes to pick up one method among a list of the most common HTTP methods: `GET`, `POST`, `PUT`, `DELETE`, `HEAD`, `OPTIONS`, `PATCH`.

You can extend this list via the `HTTP` section of the settings.
You can add your own methods and indicate also if such methods expect or not a request body. This allows to show or to hide the "Body" editor accordingly.

=> screenshot

> Please notice that the "TRACE" method has not been added since it does not work in the context of the Chrome browser.
> The XmlHttpRequest implementation does not support this method.

### Query parameters

The query parameters can be updated directly in the URL text field or the dedicated area which presents a simple tabular form.
The right menu allows to indicate whether the name or value of the query parameters are already encoded and does not require to be encoded another time.

=> screenshot

### Headers

The headers can be edited either as simple tabular form or as a raw block of text. In the latter case, you have to comply with the <a href="https://tools.ietf.org/html/rfc7230#section-3.2" target="_blank">syntax of HTTP headers</a>.

### Body

The editor of the body part is a little bit more complex as it covers several cases and is at the same time still intuitive to use.

It guides you to manually edit:

 * simple text plain payloads
 * Json/XML/HTTP payloads with syntax coloration
 * Web forms or multipart form payloads (<a href="https://www.w3.org/TR/html401/interact/forms.html#h-17.13.4" target="_blank">cf Web forms specification</a>)

The editor of the body part also helps you also to upload files from your drives.
Please note that these contents are not persisted which means two things:

 * you will have to upload again the file after reloading the request .
 * you can't run such requests using the Maven Plugin.

## Edition helpers

Here are listed some notable helpers provided by the request editor.

Completion is available for the header names, and for the values of the following headers:

 * accept, accept-charset, accept-encoding, accept-language, accept-ranges
 * allow
 * expect
 * cache-control
 * content-encoding, content-language, content-type
 * transfer-encoding

The header "Content-type" is automatically set according to the format of current body editor. You can still override the preset value.

The header "Authorization" has a dedicated helper which supports only the "BASIC" authentication theme for the moment. This helper allows you to simply enter your username and password. It hashes these values accordingly to the specification.

When a request has been run, cf [Send a request](./sending) paragraph:

 * you can quickly add headers automatically set by the browser to the definition of the request. This is fine to mimic the browser behavior when using the maven plugin for example.
 * the "Pretty" mode of the response body reader empasizes the links parsed from the body. When clicking on a link, the URL field of the request editor is updated.
