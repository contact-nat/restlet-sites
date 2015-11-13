# jsonPath( ‘selector’ )

see JSON Path Specification

# random([max])

Returns a random integer between ```0``` and ```MAX```. (default max = ```1000000000```)

<pre><code>
Expression:  {random()}
Result:         43242
</code></pre>

# md5()

Calculates MD5 hash from a value.

Ex.1 calculate MD5 hash from a constant value: ```123```.

<pre><code>
Expression: {"123".md5()}
Result:     202cb962ac59075b964b07152d234b70
</code></pre>

Ex. 2 calculate MD5 hash from request text body.

<pre><code>
{"Create User".request.body.md5()}
</code></pre>

# base64()

Encodes a value as Base64.

Ex. 1 basic authorization

<pre><code>
Expression: {"username:password".base64()}
Result:     dXNlcm5hbWU6cGFzc3dvcmQ=
</code></pre>

# uuid()

Generates UUID.

<pre><code>
Expression:  {uuid()}
Result:      3D130C51-FD24-43CD-9FE1-687F9E438682
</code></pre>

# string([‘quote’])

Prints a value as a quoted string. e.g. JSON String.

<pre><code>
Expression:  {"test".string()}
Result:      "test"  

Expression:  {"test".string("'")}
Result:      'test'
</code></pre>

# substring(start, end)

Prints a substring value.

<pre><code>
Expression:  {"123".substring(1)}
Result:      23  Expression:  {"123".substring(1, 2)}
Result:      2
</code></pre>

# hmac(cipher, secret, [output=Base64])

Calculates HMAC.

Arguments

cipher = SHA1 | SHA224 | SHA256 | SHA384 | SHA512  
output = Base64 | Hex

<pre><code>
Expression:  {"test".hmac("sha256", "secret", "Base64")}
Result:      Aymga2LNFrM+tnkr6MYLFY2Jou46h2/Omogeu0iMCRQ=
</code></pre>

Examples

<pre><code>
Expression:  {"test".hmac("sha1", "secret", "Hex")}
Result:      1aa349585ed7ecbd3b9c486a30067e395ca4b356
</code></pre>

# timestamp()

Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT.

<pre><code>
Expression:  {timestamp()}
Result:      1441975942686
</code></pre>

# lower()

Converts all of the characters to lower case using the rules of the default locale

<pre><code>
Expression:  {"AbC".lower()}
Result:      abc
</code></pre>

# upper()

Converts all of the characters to upper case using the rules of the default locale

<pre><code>
Expression:  {"AbC".upper()}
Result:      ABC
</code></pre>
