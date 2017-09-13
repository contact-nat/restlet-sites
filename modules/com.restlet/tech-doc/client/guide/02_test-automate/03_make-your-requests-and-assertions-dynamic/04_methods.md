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
  * [Lower](#lower)
  * [MD5](#md5)
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
Expression: ${"username:password".base64()}
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
Expression:  ${"test".hmac("sha256", "secret", "Base64")}
Result:      Aymga2LNFrM+tnkr6MYLFY2Jou46h2/Omogeu0iMCRQ=

Expression:  ${"test".hmac("sha1", "secret", "Hex")}
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
Expression: ${"{\"titi\": \"toto\"}".jsonPath("$.titi")}
Result: toto
  </code>
</pre>

<a class="anchor" name="lower"></a>
### Lower

Converts all the characters to lower case using the rules of the default locale.

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${"AbC".lower()}
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
Expression: ${"toto".md5()}
Result: f71dbe52628a3f83a77ab494817525c6
  </code>
</pre>

<a class="anchor" name="string"></a>
### String

Quotes its input.

Arguments:
* quote character: the character that should be used to quote the input.
  * Options: `'` and `"`
  * Default: `"`

Arguments: none

Example:
<pre class="language-javascript">
  <code class="language-javascript">
Expression: ${"toto".string()}
Result: "toto"

Expression: ${"toto".string("\"")}
Result: "toto"

Expression: ${"toto".string("'")}
Result: 'toto'
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
Expression:  ${"0123456".substring(1)}
Result:      123456

Expression:  ${"0123456".substring(1, 2)}
Result:      1

Expression:  ${"0123456".substring(0, 4)}
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
Expression: ${"aBc".upper()}
Result: ABC
  </code>
</pre>
