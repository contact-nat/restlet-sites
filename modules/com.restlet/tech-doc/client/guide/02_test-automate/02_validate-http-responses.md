Restlet Client allows you to test the behavior of your API.
To do so, you can validate the expected characteristics of an HTTP response to your requests via _Assertions_.

Assertions allow you to verify that a certain part of the response matches your expectations. You can choose between:

* Status code
* Headers
* Body length
* Duration
* Status message
* JSON body: use this if the response returns a JSON body. You can then use [JSON path](#json-path) to verify a 
specific sub-part of the response body.
* XML body use this if the response returns an XML body. You can then use [Xpath](#xpath) to verify a specific 
sub-part of the response body.
* Body content: use this if you want to assert something on the body, it will be considered as a simple string. 

Assertions are validated in real-time, meaning that they are re-evaluated when you run the request and when you 
update them. 

![assertions](./images/assertions.png)

You can create dynamic assertions (that depend on the result of other requests) with [expressions](
./work-with-your-data/expressions), for example you can test that the header `Content-type` of the response is equal 
to the header `Accept` of the request.

Expressions unleash the full power of API testing!

<a class="anchor" name="json-path"></a>
## JSON path

The reference documentation is located [here](http://goessner.net/articles/JsonPath/ "reference documentation of JSON Path").

You can also find an online JSON Path evaluator [here](http://jsonpath.com/ "JSON Path online evaluator"). Please note that the underlying implementation may differ from the one used in Restlet Client, hence the results may differ.

Here are a few hints to help you use JSON payloads:

- The dollar sign ```$``` identifies the root object of the JSON content.

- The dot sign ```.``` allows you to get attributes of an object or to go deeper in the tree.

- The square brackets ```[]``` target arrays and allow the selection of a particular element in them, or a slice of them.

- The double dot ```..``` allows you to gather recursively all the attributes based on their name.

As we will see below the current implementation in Restlet Client mostly follows the reference, but differs on few points.

<a class="anchor" name="sample-json-path-expressions"></a>
### Sample expressions

This paragragh lists expressions and their result agains the following sample json file:

<pre class="language-json"><code class="language-json">{
  "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    }
}</code>
</pre>


Here are some sample expressions and their result.

| Expression | Value
| ---------- | -----
| $.store.bicycle.color | The value of the color attribute of bicycle (i.e. `"red"`)
| $.store.book[0] | The first "book" node in "store"
| $.store.book[1:3] $.store.book[1,2] | An array of the second and third "book" nodes in "store"
| $.store.book[-1:] | An array containing the last "book" node in "store"
| $.store.book[:2] | An array of the first and second "book" nodes in "store"
| $.store.book.[1:4:2].author | An array of the authors of the second and fourth "book" nodes in "store" (i.e.: `["Nigel Rees","Herman Melville"]`)
| $.store.book[1].* | An array of the values of all attributes of the second book (i.e. `["reference","Nigel Rees","Sayings of the Century",8.95]`)
| $.store..price | An array of the values of all attributes "price" of the "store" node (i.e. the prices of books and bicycle `[8.95,12.99,8.99,22.99,19.95]`)
| $.store.book.author | An array of the values of all attributes "author" of the "book" node (i.e. `["Nigel Rees","Evelyn Waugh","Herman Melville","J. R. R. Tolkien"]`)
| $.store.book[?(@.isbn)] | An array of the books that has an attribute "isbn" (i.e. the two last books)
| $.store.book[?(@.price<10)] | An array of the books which price is less than "10"
| $.. | An array of all child nodes of the root node, recursively
| $..category | An array of all values of the "category" nodes (i.e. `["reference","fiction","fiction","fiction"]`)
| $..category[(@.length-1)] | The value of the last "category" node


<a class="anchor" name="when-restlet-client-differs-from-the-reference-json-path"></a>
### When Restlet Client differs from the reference

<a class="anchor" name="the-star-operator"></a>
#### The "*" operator

| Expression | Reference | Restlet Client
| ---------- | --------- | --------------
| $.store.book[*].author | An array containing the authors of all books in the store | Return empty array, use `$.store.book.author` instead


<a class="anchor" name="the-double-dot-operator"></a>
#### The ".." operator

The expression `$..book` is interpreted as an array of all "book" nodes so it returns an an array containing an array, since "book" node is an array:

<pre class="language-json"><code class="language-json">[
  [
    { "category": "reference", "author": "Nigel Rees", "title": "Sayings of the Century", "price": 8.95 },
    { "category": "fiction", "author": "Evelyn Waugh", "title": "Sword of Honour", "price": 12.99 },
    { "category": "fiction", "author": "Herman Melville", "title": "Moby Dick", "isbn": "0-553-21311-3", "price": 8.99 },
    { "category": "fiction", "author": "J. R. R. Tolkien", "title": "The Lord of the Rings", "isbn": "0-395-19395-8", "price": 22.99 }
  ]
]
</code></pre>

Then the following expressions are computed differently :

| Expression | Reference | Restlet Client
| ---------- | --------- | --------------
| $..book[0] | An array containing the first "book" node | The first element of the array, that is to say the array containing all books.
| $..book[-1:] | An array containing the last book | Not supported as is, it works using (`$..book[0][-1:]`)
| $..book[(@.length-1)] | An array containing the last book | Not supported as is, it works using (`$..book[0][(@.length-1)]`)
| $..book[0,1] | An array containing the first and second book | Not supported as is, it works using (`$..book[0][0,1]`)
| $..book[:2] | An array containing the third book | Not supported as is, it works using (`$..book[0][:2]`)

<a class="anchor" name="xpath"></a>
## XPath

For more information on the <a href="http://www.w3schools.com/xsl/xpath_syntax.asp" target="_blank">XPath syntax</a>, head over to the <a href="https://www.w3.org/TR/xpath/" target="_blank">official W3C specification</a>.

You can also find an online XPath evaluator [here](https://www.freeformatter.com/xpath-tester.html "XPath online evaluator").

Note that XPath expressions have a directory-path-like syntax.

Here are a few tips to help you work with XML payloads:

- A single ```/``` selects from the root node.

- ```/list``` identifies the "list object" at the top level of the XML document. Then you can iterate over attributes and sub attributes. The language natively supports arrays.

- The slash sign ```/``` allows you to get attributes of an object or to go deeper in the tree.

- The square brackets ```[]``` enable to select a specific item in an array by its position. Please note that the index starts from 1.

- ```/text()``` allows you to get a node inner text.

- ```//``` selects nodes in the document from the current node that match the selection no matter where they are.

<a class="anchor" name="sample-xpath-expressions"></a>
### Sample expressions


<pre class="language-xml"><code class="language-xml">&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;bookstore&gt;
  &lt;book category="cooking"&gt;
    &lt;title lang="en"&gt;Everyday Italian&lt;/title&gt;
    &lt;author&gt;Giada De Laurentiis&lt;/author&gt;
    &lt;year&gt;2005&lt;/year&gt;
    &lt;price&gt;30.00&lt;/price&gt;
  &lt;/book&gt;

  &lt;book category="children"&gt;
    &lt;title lang="en"&gt;Harry Potter&lt;/title&gt;
    &lt;author&gt;J K. Rowling&lt;/author&gt;
    &lt;year&gt;2005&lt;/year&gt;
    &lt;price&gt;29.99&lt;/price&gt;
  &lt;/book&gt;

  &lt;book category="web"&gt;
    &lt;title lang="en"&gt;XQuery Kick Start&lt;/title&gt;
    &lt;author&gt;James McGovern&lt;/author&gt;
    &lt;author&gt;Per Bothner&lt;/author&gt;
    &lt;author&gt;Kurt Cagle&lt;/author&gt;
    &lt;author&gt;James Linn&lt;/author&gt;
    &lt;author&gt;Vaidyanathan Nagarajan&lt;/author&gt;
    &lt;year&gt;2003&lt;/year&gt;
    &lt;price&gt;49.99&lt;/price&gt;
  &lt;/book&gt;

  &lt;book category="web"&gt;
    &lt;title lang="en"&gt;Learning XML&lt;/title&gt;
    &lt;author&gt;Erik T. Ray&lt;/author&gt;
    &lt;year&gt;2003&lt;/year&gt;
    &lt;price&gt;39.95&lt;/price&gt;
  &lt;/book&gt;
&lt;/bookstore&gt;
</code></pre>

Here are some sample expressions and their result.

| Expression | Value
| ---------- | -----
| / | The whole document node
| /bookstore | The "bookstore" node
| /bookstore/book/title | An array of all "title" nodes of all book nodes under the bookstore element
| //title | An array of all "title" nodes whatever their position is
| //title/@lang | An array of all lang attributes of the "title" nodes
| /bookstore/book/title/text() | An array of all text values of "title" nodes of all "book" nodes under the bookstore element
| /bookstore/book[price>35]/title| The "title" nodes of all "book" nodes having price greater than 35 
| //book[last()]/title | The "title" node of the last "book" node
| //book[position() < 3] | The two first "book" nodes
| //title[@lang]] | An array of all "title" nodes with an attribute "lang"
| name(//*[1]) | The name of the first element in the document (i.e.: `bookstore`)
| count(//title) | The number of all "title" nodes  (i.e.: `4`)


<a class="anchor" name="when-restlet-client-differs-from-the-reference-xpath"></a>
### When Restlet Client differs from the reference

<a class="anchor" name="list-with-single-element"></a>
#### List with single element

This expression computes the value of the title node of the first book: `//book[last()]/title/text()`.

You expect it to return only a string value: `Learning XML`.

Unfortunately, at this time of writing, it returns an array containing a single value: `[Learning XML]`.
