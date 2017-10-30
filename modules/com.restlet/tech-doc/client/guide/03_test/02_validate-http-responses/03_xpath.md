For more information on the <a href="http://www.w3schools.com/xsl/xpath_syntax.asp" target="_blank">XPath syntax <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i></a>,
head over to the <a href="https://www.w3.org/TR/xpath/" target="_blank">official W3C specification <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i></a>.

You can also find an online XPath evaluator <a href="https://www.freeformatter.com/xpath-tester.html" "title="XPath online evaluator">here <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i></a>.

Note that XPath expressions have a directory-path-like syntax.

Here are a few tips to help you work with XML payloads:

- A single ```/``` selects from the root node.

- ```/list``` identifies the "list object" at the top level of the XML document. Then you can iterate over attributes and sub attributes. The language natively supports arrays.

- The slash sign ```/``` allows you to get attributes of an object or to go deeper in the tree.

- The square brackets ```[]``` enable to select a specific item in an array by its position. Please note that the index starts from 1.

- ```/text()``` allows you to get a node inner text.

- ```//``` selects nodes in the document from the current node that match the selection no matter where they are.

<a class="anchor" name="sample-xpath-expressions"></a>
### Sample XPath expressions


<pre class="language-xml">
  <code class="language-xml">&lt;?xml version="1.0" encoding="UTF-8"?&gt;
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


<a class="anchor" name="when-restlet-client-differs-from-the-reference"></a>
### When Restlet Client differs from the reference

<a class="anchor" name="lists-with-single-element"></a>
#### Lists with single element

This expression computes the value of the title node of the first book: `//book[last()]/title/text()`.

You expect it to return only a string value: `Learning XML`.

Unfortunately, at this time of writing, it returns an array containing a single value: `[Learning XML]`.
