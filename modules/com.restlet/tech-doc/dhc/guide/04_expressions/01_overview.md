Expressions allow you to get and transform data from: a context, requests stored in a repository, and from the last response on/to the stored requests (if a response exists).

You can put expressions in URL, header names, header values, text body, form items values, and in assertions values. There is no limit on how many expressions you can use in one declaration. Expressions can be freely mixed with a plain text.

Ex. 1.1: an expression declarations

<pre><code>
{host}

{host}{path}

{scheme}{host}{path}

text{random()}other text{random()}
</code></pre>

You can also use built-in methods to transform values.

Ex. 1.2. calculate MD5 hash from text or JSON response body

<pre><code>
{"Saved Request #1".body.md5()}
</code></pre>
