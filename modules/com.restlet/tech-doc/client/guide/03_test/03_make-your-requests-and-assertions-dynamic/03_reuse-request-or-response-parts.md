Every request stored in your repository can be accessed through expressions
so that you can re-use its components in other requests or in assertions.

<a class="anchor" name="usage-example"></a>
## Usage example

I am testing the CRUD operations for my resource `/planets`.

I want to create a planet by POSTing on my `/planets`, then I want to verify
that my planet was created and is available.

I can create:

* a scenario `Creation scenario` in project `Star wars API` with a request `Create planet` that POSTs the new planet
* a second request `Verify planet existence` that will retrieve the new planet's id from the response of the first
request to GET the created planet on `/planets/{planetId}`

If the response of my first request is:

<pre class="language-json">
  <code class="language-json">
{
  &quot;id&quot;: &quot;9e5d2284-94ad-11e7-bbbc-773611cab8f7&quot;,
  &quot;name&quot;: &quot;Tatooine&quot;,
  &quot;rotation_period&quot;: &quot;23&quot;,
  &quot;orbital_period&quot;: &quot;304&quot;,
  &quot;diameter&quot;: &quot;10465&quot;,
  &quot;climate&quot;: &quot;arid&quot;,
  &quot;gravity&quot;: &quot;1 standard&quot;,
  &quot;terrain&quot;: &quot;desert&quot;,
  &quot;population&quot;: &quot;200000&quot;,
}
  </code>
</pre>

Then I can test that my planet was created by making a GET on
`https://my-star-wars-api.com/planets/${"Star wars API"."Creation scenario"."Create planet"."response"."body"."id"}`
and asserting the response code is 200 for example.

<a class="anchor" name="usable-elements"></a>
## Usable elements

Requests are structured like this:
<pre class="language-json">
  <code class="language-json">
{
  {request name}: {string},
  &quot;request&quot;: {
    &quot;method&quot;  : {string},
    &quot;uri&quot;     : {string},
    &quot;headers&quot; : {
      {header name}           : {header value}
      },
    &quot;query&quot; : {
      {query parameter name}  : {query parameter value}
    },
    &quot;body&quot;    : {string or object}
  },
  &quot;response&quot;: {
    &quot;headers&quot;   : {object},
    &quot;status&quot;    : {
      &quot;message&quot; : {string},
      &quot;code&quot;    : {number}
    }
    &quot;body&quot;      : {string or object}
  }
}
  </code>
</pre>

The response contains a JSON object representing the HTTP response to the last call of the request.

You can query it in the same way you would query a standard JavaScript object, using a dotted notation.

> __Note:__ all headers names are lowercased.

> __Note:__ only the text bodies can be referenced.

This means you can reference elements of the request `Get planets` in project `Star-wars API` as follows:

- Request URI: `${"Star-wars API"."Get planets"."request"."uri"}`
- Response status code: `${"Star-wars API"."Get planets"."response"."status"."code"}`
- Response header `Content-type` value: `${"Star-wars API"."Get planets"."response"."headers"."Content-type"}`
- Response body attribute `id`: `${"Star-wars API"."Get planets"."response"."body"."id"}`


<a class="anchor" name="going-further"></a>
## Going further

Expressions can do more, if you haven't read it yet, have a look at [this page](./expressions) to learn more.
