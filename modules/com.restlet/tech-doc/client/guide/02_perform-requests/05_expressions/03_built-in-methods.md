# Introduction

Here is a non exhaustive list of the expressions that can be used in methods within Restlet Client.

# Methods

## jsonPath( ‘selector’ )

see JSON Path Specification

## random([max])

It returns a random integer between ```0``` and ```MAX```. (default max = ```1000000000```).

<pre class="language-none"><code class="language-none">Expression:  {random()}
Result:         43242
</code></pre>

## md5()

It calculates MD5 hash from a value.

Ex.1 calculate MD5 hash from a constant value: ```123```

<pre class="language-none"><code class="language-none">Expression: {"123".md5()}
Result:     202cb962ac59075b964b07152d234b70
</code></pre>

Ex.2 calculate MD5 hash from request text body

<pre class="language-javascript"><code class="language-javascript">{"Create User".request.body.md5()}
</code></pre>

## base64()

It encodes a value as Base64.

Ex. 1 basic authorization

<pre class="language-none"><code class="language-none">Expression: {"username:password".base64()}
Result:     dXNlcm5hbWU6cGFzc3dvcmQ=
</code></pre>

## uuid()

It generates UUID.

<pre class="language-none"><code class="language-none">Expression:  {uuid()}
Result:      3D130C51-FD24-43CD-9FE1-687F9E438682
</code></pre>

## string([‘quote’])

It prints a value as a quoted string e.g. JSON String.

<pre class="language-none"><code class="language-none">Expression:  {"test".string()}
Result:      "test"  

Expression:  {"test".string("'")}
Result:      'test'
</code></pre>

## substring(start, end)

It prints a substring value.

<pre class="language-none"><code class="language-none">Expression:  {"123".substring(1)}
Result:      23  Expression:  {"123".substring(1, 2)}
Result:      2
</code></pre>

## hmac(cipher, secret, [output=Base64])

It calculates HMAC.

### Arguments

cipher = SHA1 | SHA224 | SHA256 | SHA384 | SHA512  
output = Base64 | Hex

### Examples

<pre class="language-none"><code class="language-none">Expression:  {"test".hmac("sha256", "secret", "Base64")}
Result:      Aymga2LNFrM+tnkr6MYLFY2Jou46h2/Omogeu0iMCRQ=
</code></pre>

<pre class="language-none"><code class="language-none">Expression:  {"test".hmac("sha1", "secret", "Hex")}
Result:      1aa349585ed7ecbd3b9c486a30067e395ca4b356
</code></pre>

## timestamp()

It returns the number of milliseconds since January 1, 1970, 00:00:00 GMT.

<pre class="language-none"><code class="language-none">Expression:  {timestamp()}
Result:      1441975942686
</code></pre>

## lower()

It converts all the characters to lower case using the rules of the default locale.

<pre class="language-none"><code class="language-none">Expression:  {"AbC".lower()}
Result:      abc
</code></pre>

## upper()

It converts all the characters to upper case using the rules of the default locale.

<pre class="language-none"><code class="language-none">Expression:  {"AbC".upper()}
Result:      ABC
</code></pre>
