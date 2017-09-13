Every request stored in your repository can be accessed through expressions
so that you can re-use its components in other requests or in assertions.

<a class="anchor" name="usage-example"></a>
## Usage example

I am testing the CRUD operations for my resource `/planets`.

I want to create a planet by POSTing on my `/planets`, then I want to verify
that my planet was created and is available.

I can create a scenario `Creation scenario` in my project `Star wars API` with a first request `Create planet` that POSTs the planet, then a second request `Verify planet existence` that will retrieve the id from the response of the first request to GET the created planet on `/planets/{planetId}`.

If the response of my first request is:

<pre class="language-json">
  <code class="language-json">
  {
    "id": "9e5d2284-94ad-11e7-bbbc-773611cab8f7",
    "name": "Tatooine",
    "rotation_period": "23",
    "orbital_period": "304",
    "diameter": "10465",
    "climate": "arid",
    "gravity": "1 standard",
    "terrain": "desert",
    "population": "200000",
  }
  </code>
</pre>

Then I can test that my planet was created by making a GET on `https://my-star-wars-api.com/planets/${"Star wars API"."Creation scenario"."Create planet"."response"."body"."id"}` and asserting the response code is 200 for example.

<a class="anchor" name="usable-elements"></a>
## Usable elements

Requests are structured like this:
<pre class="language-json">
  <code class="language-json">
  {
    {request name}: {string},
    "request": {
      "method"  : {string},
      "uri"     : {string},
      "headers" : {
        {header name}           : {header value}
        },
      "query" : {
        {query parameter name}  : {query parameter value}
      },
      "body"    : {string or object}
    },
    "response": {
      "headers"   : {object},
      "status"    : {
        "message" : {string},
        "code"    : {number}
      }
      "body"      : {string or object}
    }
  }
  </code>
</pre>

The response contains a JSON object representing the HTTP response to the last call of the request.

You can query this object as you would a standard JavaScript object, with a dotted notation.

> __Note:__ all headers names are lowercased.

> __Note:__ only the text bodies can be referenced.

This means you can reference elements of the request `Get planets` in project `Star-wars API` like follows:

- Request URI: `${"Star-wars API"."Get planets"."request"."uri"}`
- Response status code: `${"Star-wars API"."Get planets"."response"."status"."code"}`
- Response header `Content-type` value: `${"Star-wars API"."Get planets"."response"."headers"."Content-type"}`
- Response body attribute `id`: `${"Star-wars API"."Get planets"."response"."body"."id"}`