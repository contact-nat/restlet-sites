<a class="anchor" name="introduction"></a>
## Introduction

The request editor in Restlet Client allows you to update an HTTP request.
Each part of the request is handled by a dedicated area and adapted edition tool. 

The different parts are shown in the screenshot below.

=> TODO Insert graphic with numbered ticks

<a class="anchor" name="method"></a>
### Method

The list of proposed methods can be extended via the settings.

=> screenshot

You can add your own methods and indicate whether the method you define accepts a body or not. If you select a method without body in the request editor, the body won't be editable.

The list of the most common HTTP methods are predefined.
Please notice that the `TRACE` method has not been added since Client relies on the XmlHttpRequest API and that its implementation in Chrome does not support the `TRACE` method.

<a class="anchor" name="query-parameters"></a>
### Query parameters

The query parameters can be updated directly in the URL text field or the dedicated area which presents a simple tabular form.
The right menu allows to indicate whether the name or value of the query parameters are already encoded and does not require to be encoded another time.

=> screenshot

<a class="anchor" name="headers"></a>
### Headers

The headers can be edited either as simple tabular form or as a raw block of text. In the latter case, you have to comply with the <a href="https://tools.ietf.org/html/rfc7230#section-3.2" target="_blank">syntax of HTTP headers</a>.

<a class="anchor" name="body"></a>
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

<a class="anchor" name="edition-helpers"></a>
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
