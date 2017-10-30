The reference documentation is located
<a href="http://goessner.net/articles/JsonPath/" title="reference documentation of JSON Path" target="_blank">
here <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i>
</a>.

You can also find an online JSON Path evaluator <a href="http://jsonpath.com/" title="JSON Path online evaluator">
here <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i>
</a>.
Please note that the underlying implementation may differ from the one used in Restlet Client, hence the results may differ.

Here are a few hints to help you use JSON payloads:

- The dollar sign ```$``` identifies the root object of the JSON content.

- The dot sign ```.``` allows you to get attributes of an object or to go deeper in the tree.

- The square brackets ```[]``` target arrays and allow the selection of a particular element in them, or a slice of them.

- The double dot ```..``` allows you to gather recursively all the attributes based on their name.

As we will see below the current implementation in Restlet Client mostly follows the reference, but differs on few points.

<a class="anchor" name="sample-json-path-expressions"></a>
### Sample JSON path expressions

This paragragh lists expressions and their result agains the following sample json file:

<pre class="language-json">
  <code class="language-json">
{
  &quot;store&quot;: {
        &quot;book&quot;: [
            {
                &quot;category&quot;: &quot;reference&quot;,
                &quot;author&quot;: &quot;Nigel Rees&quot;,
                &quot;title&quot;: &quot;Sayings of the Century&quot;,
                &quot;price&quot;: 8.95
            },
            {
                &quot;category&quot;: &quot;fiction&quot;,
                &quot;author&quot;: &quot;Evelyn Waugh&quot;,
                &quot;title&quot;: &quot;Sword of Honour&quot;,
                &quot;price&quot;: 12.99
            },
            {
                &quot;category&quot;: &quot;fiction&quot;,
                &quot;author&quot;: &quot;Herman Melville&quot;,
                &quot;title&quot;: &quot;Moby Dick&quot;,
                &quot;isbn&quot;: &quot;0-553-21311-3&quot;,
                &quot;price&quot;: 8.99
            },
            {
                &quot;category&quot;: &quot;fiction&quot;,
                &quot;author&quot;: &quot;J. R. R. Tolkien&quot;,
                &quot;title&quot;: &quot;The Lord of the Rings&quot;,
                &quot;isbn&quot;: &quot;0-395-19395-8&quot;,
                &quot;price&quot;: 22.99
            }
        ],
        &quot;bicycle&quot;: {
            &quot;color&quot;: &quot;red&quot;,
            &quot;price&quot;: 19.95
        }
    }
}
  </code>
</pre>


Here are some sample expressions and their result.

| Expression | Value
| ---------- | -----
| $.store.bicycle.color | The value of the color attribute of bicycle (i.e. `"red"`)
| $.store.book[0] | The first "book" node in "store"
| $.store.book[1:3] $.store.book[1,2] | An array of the second and third "book" nodes in "store"
| $.store.book[-1:] | An array containing the last "book" node in "store"
| $.store.book[:2] | An array of the first and second "book" nodes in "store"
| $.store.book[1:4:2].author | An array of the authors of the second and forth "book" nodes in "store" (i.e.: `["Evelyn Waugh","J. R. R. Tolkien"]`)
| $.store.book[1].* | An array of the values of all attributes of the second book (i.e. `["fiction","Evelyn Waugh","Sword of Honour",12.99]`)
| $.store..price | An array of the values of all attributes "price" of the "store" node (i.e. the prices of books and bicycle `[8.95,12.99,8.99,22.99,19.95]`)
| $.store.book.author | An array of the values of all attributes "author" of the "book" node (i.e. `["Nigel Rees","Evelyn Waugh","Herman Melville","J. R. R. Tolkien"]`)
| $.store.book[?(@.isbn)] | An array of the books that has an attribute "isbn" (i.e. the two last books)
| $.store.book[?(@.price<10)] | An array of the books which price is less than "10"
| $.. | An array of all child nodes of the root node, recursively
| $..category | An array of all values of the "category" nodes (i.e. `["reference","fiction","fiction","fiction"]`)
| $..category[(@.length-1)] | The value of the last "category" node

<a class="anchor" name="when-restlet-client-differs-from-the-reference"></a>
### When Restlet Client differs from the reference

<a class="anchor" name="the-star-operator"></a>
#### The star operator

| Expression | Reference | Restlet Client
| ---------- | --------- | --------------
| $.store.book[*].author | An array containing the authors of all books in the store | Return empty array, use `$.store.book.author` instead


<a class="anchor" name="the-double-dot-operator"></a>
#### The double dot operator

The expression `$..book` is interpreted as an array of all "book" nodes so it returns an an array containing an array, since "book" node is an array:

<pre class="language-json">
  <code class="language-json">
[
  [
    { &quot;category&quot;: &quot;reference&quot;, &quot;author&quot;: &quot;Nigel Rees&quot;, &quot;title&quot;: &quot;Sayings of the Century&quot;, &quot;price&quot;: 8.95 },
    { &quot;category&quot;: &quot;fiction&quot;, &quot;author&quot;: &quot;Evelyn Waugh&quot;, &quot;title&quot;: &quot;Sword of Honour&quot;, &quot;price&quot;: 12.99 },
    { &quot;category&quot;: &quot;fiction&quot;, &quot;author&quot;: &quot;Herman Melville&quot;, &quot;title&quot;: &quot;Moby Dick&quot;, &quot;isbn&quot;: &quot;0-553-21311-3&quot;, &quot;price&quot;: 8.99 },
    { &quot;category&quot;: &quot;fiction&quot;, &quot;author&quot;: &quot;J. R. R. Tolkien&quot;, &quot;title&quot;: &quot;The Lord of the Rings&quot;, &quot;isbn&quot;: &quot;0-395-19395-8&quot;, &quot;price&quot;: 22.99 }
  ]
]
  </code>
</pre>

Then the following expressions are computed differently :

| Expression | Reference | Restlet Client
| ---------- | --------- | --------------
| $..book[0] | An array containing the first "book" node | The first element of the array, that is to say the array containing all books.
| $..book[-1:] | An array containing the last book | Not supported as is, it works using (`$..book[0][-1:]`)
| $..book[(@.length-1)] | An array containing the last book | Not supported as is, it works using (`$..book[0][(@.length-1)]`)
| $..book[0,1] | An array containing the first and second book | Not supported as is, it works using (`$..book[0][0,1]`)
| $..book[:2] | An array containing the third book | Not supported as is, it works using (`$..book[0][:2]`)
