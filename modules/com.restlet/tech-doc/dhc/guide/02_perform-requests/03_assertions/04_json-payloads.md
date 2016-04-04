When selecting **JSON Body** or **XML Body**, assertions can leverage JSON path or XPath expressions to use and check specific parts of the content. This expression language is natural and enables you to browse data graph easily.

# Use JSON path for JSON payloads

Here are a few hints to help you use JSON payloads:

- The dollar sign ```$``` identifies the root object of the JSON content. Then you can iterate over attributes and sub attributes. The language natively supports arrays.

- The dot sign ```.``` allows you to get attributes of an object. This can be done recursively according to the depth of the data graph.

- The square brackets ```[]``` target arrays and allow the selection of a particular element in them.

The following sample describes how to test the value of a list contained in arrays. The JSON body used contains a list of maps, each map has a **sources** field. This field corresponds to a list of sources and has an id attribute.

The expression **$[0].sources** corresponds to the sources of the first element. The expression **$[0].sources.id** goes further by getting all the id fields of sources. It is then possible to get just the first one.


This screenshot shows you JSON content with an array:
![JSON content with array](images/09-json-content-array.jpg "JSON content with array")

Here you can see a JSON path expression:
![JSON path expression](images/09-json-path-expression.jpg "JSON path expression")

This is the value you want to check:
![JSON content with array](images/09-json-value.jpg "JSON content with array")

>**Note:**
The value to check can be valid JSON content. DHC automatically parses it and checks if it matches with the content corresponding to the expression.

The following sample describes how to check the content of an array:
![JSON value](images/10-json-value.jpg "JSON value")

# <a class="anchor" name="xpath"></a>Use XPath for XML payloads

Here are a few tips to help you work with XML payloads:

- ```list``` identifies the root object of the XML content. Then you can iterate over attributes and sub attributes. The language natively supports arrays.

- The slash sign ```/``` allows you to get attributes of an object. This can be done recursively according to the depth of the data graph.

- The square brackets ```[]``` target arrays and allow the selection of a particular element in them.

- ```text()``` will allow you to enter text expressions.

In the following example, the expression **list/Company[1]/address/city/text()** corresponds to the city contained in the address of the first Company.

![XML value](images/xml-payload.jpg "XML value")

For more information on XML Path language, head over to the <a href="https://www.w3.org/TR/xpath/" target="_blank">W3C recommendations</a>.
