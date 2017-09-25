Restlet Client allows you to manipulate data via methods. Methods can create new data or transform existing data.
The methods that transform data apply on the value of the expression they finish.

<a class="anchor" name="usage-example"></a>
## Usage example

I want to retrieve the 10 first planet names in the list of my planets in my Star wars API.
I can use the expression: `${"Star-wars API"."Get 10 planet names"."response"."body".jsonPath("$.results[:10].name")}`.

The method `jsonPath` parses the result of the expression `${"Star-wars API"."Get 10 planet names"."response"."body"}`.

<a class="anchor" name="constant"></a>
## Constants

When writing an expression like `${"toto"}`, the expression will be evaluated as:
* The value of the current environment's variable `toto` if there is one. See [how environment references work](./environments) for more details.
* If no environment variable matched, the request/scenario/service/project toto that is at root-level in your repository. See [how repository references work](./reuse-request-or-response-parts) for more details.
* The constant `toto` in any other case.

<a class="anchor" name="table-of-content"></a>
## Table of content

* [Data creation](#data-creation)
  * [Random](#random)
  * [Timestamp](#timestamp)
  * [UUID](#uuid)
* [Data transformation](#data-transformation)
  * [Base 64](#base-64)
  * [HMAC](#hmac)
  * [JSON path](#json-path)
  * [Length](#length)
  * [Lower](#lower)
  * [MD5](#md5)
  * [Sha](#sha)
  * [String](#string)
  * [Substring](#substring)
  * [Upper](#upper)

<a class="anchor" name="data-creation"></a>
## Data creation

<a class="anchor" name="random"></a>
### Random

Returns a random number in range `0 <= number < max`.

Arguments:
* max: defaults to 1000000000

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${random(50)}
Possible result: 42
  </code>
</pre>

<a class="anchor" name="timestamp"></a>
### Timestamp

Returns the number of milliseconds from January 1, 1970, 00:00:00 GMT to the date when it's evaluated.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${timestamp()}
Possible result: 1505136142950
  </code>
</pre>

<a class="anchor" name="uuid"></a>
### UUID

Returns a UUID.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${uuid()}
Possible result: f5fafd92-2298-4e72-97c9-df4dabaf27d2
  </code>
</pre>

<a class="anchor" name="data-transformation"></a>
## Data transformation

<a class="anchor" name="base-64"></a>
### Base 64

Encodes in base 64.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;username:password&quot;.base64()}
Result: dXNlcm5hbWU6cGFzc3dvcmQ=
  </code>
</pre>

<a class="anchor" name="hmac"></a>
### HMAC

Creates a [Hash-based Message Authentication Code](https://en.wikipedia.org/wiki/Hash-based_message_authentication_code).

Arguments:
* cipher: the hash function
  * Options: `SHA1`, `SHA224`, `SHA256`, `SHA384` or `SHA512`
* key: the secret key
* output: the output type
  * Options: `Base64` or `Hex`

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression:  ${&quot;test&quot;.hmac(&quot;sha256&quot;, &quot;secret&quot;, &quot;Base64&quot;)}
Result:      Aymga2LNFrM+tnkr6MYLFY2Jou46h2/Omogeu0iMCRQ=

Expression:  ${&quot;test&quot;.hmac(&quot;sha1&quot;, &quot;secret&quot;, &quot;Hex&quot;)}
Result:      1aa349585ed7ecbd3b9c486a30067e395ca4b356
  </code>
</pre>

<a class="anchor" name="json-path"></a>
### JSON path

Returns the element(s) extracted from the JSON input with the given JSON path `selector`.
See the [JSON path specification](http://goessner.net/articles/JsonPath/).

Arguments:
* selector: The JSON path selector that points to the part of the JSON to extract from the input.

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;{\&quot;titi\&quot;: \&quot;toto\&quot;}&quot;.jsonPath(&quot;$.titi&quot;)}
Result: toto
  </code>
</pre>

<a class="anchor" name="length"></a>
### Length

Computes the length of its input:
* the number of characters if the input is a string.
* the number of items in a JSON array
* the number of keys in an JSON object

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;Star-wars API&quot;.&quot;Get 2 planets&quot;.&quot;response&quot;.&quot;body&quot;}
Result: {
  &quot;count&quot;:61,
  &quot;results&quot;: [
    {&quot;name&quot;:&quot;Alderaan&quot; },
    {&quot;name&quot;:&quot;Naboo&quot; },
    {&quot;name&quot;:&quot;Hoth&quot; }
  ]
}

Expression: ${&quot;Star-wars API&quot;.&quot;Get 2 planets&quot;.&quot;response&quot;.&quot;body&quot;.length()}
Result: 2

Expression: ${&quot;Star-wars API&quot;.&quot;Get 2 planets&quot;.&quot;response&quot;.&quot;body&quot;.&quot;results&quot;.length()}
Result: 3

Expression: ${&quot;Star-wars API&quot;.&quot;Get 2 planets&quot;.&quot;response&quot;.&quot;body&quot;.&quot;results&quot;.&quot;0&quot;.&quot;name&quot;.length()}
Result: 8
  </code>
</pre>

<a class="anchor" name="lower"></a>
### Lower

Converts all the characters to lower case using the rules of the default locale.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;AbC&quot;.lower()}
Result: abc
  </code>
</pre>

<a class="anchor" name="md5"></a>
### MD5

Calculates the MD5 hash of its input.

Arguments: none

Example:

<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;toto&quot;.md5()}
Result: f71dbe52628a3f83a77ab494817525c6
  </code>
</pre>

<a class="anchor" name="sha"></a>
### Sha

Hashes its input.

Arguments:
* cipher: the hash function
  * Options: `SHA1`, `SHA224`, `SHA256`, `SHA384` or `SHA512`
* output: the output type
  * Options: `Base64` or `Hex`

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;toto&quot;.sha(&quot;SHA224&quot;, &quot;Base64&quot;)}
Result: IcBD7s1+hUI6ctrjwGKitb+g5rNc4/54jJtXpg==

Expression: ${&quot;toto&quot;.sha(&quot;SHA1&quot;, &quot;Hex&quot;)}
Result: 0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c
  </code>
</pre>

<a class="anchor" name="string"></a>
### String

Quotes its input.

Arguments:
* quote character: the character that should be used to quote the input.
  * Options: `'` and `"`
  * Default: `"`

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;toto&quot;.string()}
Result: &quot;toto&quot;

Expression: ${&quot;toto&quot;.string(&quot;\&quot;&quot;)}
Result: &quot;toto&quot;

Expression: ${&quot;toto&quot;.string(&quot;&apos;&quot;)}
Result: &apos;toto&apos;
  </code>
</pre>

<a class="anchor" name="substring"></a>
### Substring

Returns a truncated input.

Arguments:
* start: position of the first character that should be extracted
  * Default: 0
* end: position of the first character that should be omitted
  * Default: end of the input

> Note: the index of the first character is 0

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression:  ${&quot;0123456&quot;.substring(1)}
Result:      123456

Expression:  ${&quot;0123456&quot;.substring(1, 2)}
Result:      1

Expression:  ${&quot;0123456&quot;.substring(0, 4)}
Result:      0123
  </code>
</pre>

<a class="anchor" name="upper"></a>
### Upper

Converts all the characters to upper case using the rules of the default locale.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${&quot;aBc&quot;.upper()}
Result: ABC
  </code>
</pre>
