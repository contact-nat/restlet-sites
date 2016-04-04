# Introduction

You can declare a reference on:  
- a context variable,  
- saved request data  
- and on a response to the saved request.

The reference declaration is written:
- as a string (single or double quoted),  
- or as an identifier (see grammar rule id).

If the referenced value is JSON object, then you can use ```JsonPath``` method or dot notation to get the object properties.

Ex. 1: a reference on the context variable named host

<pre class="language-javascript"><code class="language-javascript">{host}
</code></pre>

Ex. 2: a reference on the response status code on saved request named “Create User”

<pre class="language-javascript"><code class="language-javascript">{"Create User".response.status.code}
</code></pre>

This declaration is equivalent to:

<pre class="language-javascript"><code class="language-javascript">{"Create User".jsonPath("$.response.status.code")}
</code></pre>

References Look-up Order:

1. [Context](#context "Context")
2. [Repository](#repository "Repository")

# <a class="anchor" name="context"></a>Context

A context is a set of variables.  

A variable value can be:  
-  a plain text,  
- or a ```JSON object```.  

You can have as many contexts as you need and switch between them.  
Only the selected context is effective.

Ex.1: a context variables declaration

<pre class="language-javascript"><code class="language-javascript">simple_value = test text
json_value = { "a" : { "b" : "test" }}
</code></pre>

You can turn a reference into a context variable by declaring the variable name as an identifier (see [Grammar](/technical-resources/dhc/guide/expressions/grammar "Grammar rules") section) or as a string. If a variable value is a ```JSON object``` then you can also reference the ```JSON object``` properties and sub-properties.

Ex.2: a variable name as an identifier

<pre class="language-none"><code class="language-none">Context:         host = http://localhost
Expression:      {host}
Result:          http://localhost
</code></pre>

Ex.3: a variable name as a string

<pre class="language-none"><code class="language-none">Context:        secret key = 1234
Expression:     {'secret key'}
Result:         1234
</code></pre>

Ex.4: a reference on JSON value

<pre class="language-none"><code class="language-none">Context:         JSON variable = { "a" : { "b" : 1 }}

Expression:      {'JSON variable'}
Result:          { "a" : { "b" : 1 }}

Expression:      {'JSON variable'.a}
Result:          {"b" : 1 }

Expression:      {'JSON variable'.a .b}
Result:          1
</code></pre>

Ex.5: a reference on JSON value + JSON Path method

<pre class="language-none"><code class="language-none">Context:         JSON variable = { "a" : { "b" : 1 }}
Expression:      {'JSON variable'.jsonPath('$.a.b')}    // equivalent to {'JSON variable'.a.b}
Result:          1
</code></pre>

# <a class="anchor" name="repository"></a>Repository

A request stored in the repository is referenced by a path and by a name. If the history contains a response to the request then the last response is included.

The referenced repository request is represented as a ```JSON object``` containing the request and optionally the last response to the request.

<pre class="language-json"><code class="language-json">{
  "name"      : "path+name",
  "request"   : { ... },
  "response"  : { ... }
}
</code></pre>

Ex.1: a reference on the “Create User” stored request

<pre class="language-none"><code class="language-none">Expression: {"Create User"}
Result: {
            "name"     : "Create User",
            "request"  : {...},
            "response" : {...},
        }
</code></pre>

Ex.2: a reference on the “Get Ticker” stored request, found under service “Public API” under Project “Stocks”

<pre class="language-none"><code class="language-none">Expression: {Stocks."Public API"."Get Ticker"}
Result: {
            "name"     : "Get Ticker",
            "request"  : {...},
            "response" : {...},
        }
</code></pre>

## Request Reference

A stored request is represented as a ```JSON object``` and you can use the dot notation or the ```JsonPath``` method to extract a value.

Here is the request reference JSON object structure:

<pre class="language-json"><code class="language-json">{
   "uri"     : {string},
   "method"  : {string},
   "headers" : {object},
   "body"    : {string or object}
}
</code></pre>

The request body can be a plain text or a ```JSON```.

The *form* or *file* body are not supported yet.

Ex.1: stored request headers

<pre class="language-javascript"><code class="language-javascript">{"Auth Request".request.headers.authorization}
</code></pre>

>**Note:** all headers names are lowercased.

## Response Reference

Response Reference allows you to retrieve the last stored response to a request stored in the repository. A response is represented as a ```JSON object``` and you can use the dot notation or ```JsonPath method``` to get value.

Here is the response reference JSON object structure:

<pre class="language-json"><code class="language-json">{
   "status"  : {
        "message" : {string},
        "code"    : {number}
   }
   "headers" : {object},
   "body"    : {string or object}
}
</code></pre>

The referenced response body can be a plain text or a ```JSON```.

The *form* or *file* body are not supported yet.

>**Note:** all headers names are lowercased.

Ex.1: get a status code

Finds the last response on top level request named “request #1” and returns a status:

<pre class="language-none"><code class="language-none">Expression:     {"request #1".response.status}
Result:         {"code" : 200, "message" : OK }
</code></pre>

Finds the last response on/to the top level request named “request #1” and returns a status code:

<pre class="language-none"><code class="language-none">Expression:     {"request #1".response.status.code}
Result:         200
</code></pre>


Ex.2: get a value from JSON body

<pre class="language-none"><code class="language-none">Response body: { "name" : "John Doe", ... }
Expression:    {"request #1".response.body.name}
Result:        "John Doe"
</code></pre>
