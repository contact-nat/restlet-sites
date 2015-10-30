# Introduction

DHC allows you to execute HTTP requests, but it also allows you to apply rules on calls to check that the responses received correspond to what you expect.

DHC's tests are based on *assertions*. Assertions aim at determining if calls match what is expected to be returned by the API under test.

# Define assertions
For each request an **Assertions** tab is provided to define the rules that the response of a call, to a RESTful service, needs to check to be successful.

# Assertion structure

An assertion consists of several parts:

## The part of the call the assertion applies to
The following parts are supported by DHC: status, header, JSON body, body and response. You can see that there are two “body” parts. As a matter of fact, the tool provides native support of JSON within body. This means that you can leverage JSON Patch to get data from JSON content.

## An optional parameter according to the previously selected part
For example for headers, it corresponds to the name of the header.

## The comparison to use
The matching mode can differ depending on the part of the call. Of course, standard comparisons (equality, superiority, inferiority) are always present. In addition, where header and body is concerned you can also check existence and containment.

## The value to check
This can be raw values but also expressions.

The following screenshot provides a sample use of assertions within the **Assertions** tab



If at least one test fails, the call is flagged as failed. In the same spirit as xUnit tools in different languages, DHC uses the green color for successful assertions and the red one for failures.

You will notice the ability to disable assertions if necessary using the checkbox right before the line of the assertion, as shown below:



As you can see, the assertion that failed is not taken into account anymore.

Now that you have seen the basics about assertions, let’s dive a bit more into the power of DHC to define the response element to test.

# Test response parts

The first part is obviously the status one. In this case, you can choose not only the status code but the status message as well.



Regarding response headers, DHC allows you to check both their presence and their content. In the first instance, you can simply put the name of the header in the parameter field and use the comparison “Exists” or “Does not exist”, as shown below:



Checking the header value is pretty simple, just change the comparison to ‘equals’ or ‘contains’ for example and fill the value field with the expected value.



Just as you do  for headers, you can also check either the presence or content of a payload. The first case is interesting for responses with empty payloads, typically with status code 204.



For text-based payload, structured or not, it’s possible to check if the content matches to a specific value, or contains some text, as described below.



Testing text payload can be tedious if they are only considered as text. For this reason, DHC goes further by natively supporting JSON body. The latter allows to apply specific JSON path expressions. We will focus on this feature in the next section.

Before diving into JSON payload, don’t forget the support for the response itself. This allows you to apply assertions to its latency. This is particularly useful if you want to test the maximum time for a call.


# Use JSON path for JSON payloads

When selecting JSON body, assertions can leverage JSON path expressions to use and check specific parts of the content. Stefan Goessner provides a great introduction in his article JSON Path – Xpath for JSON. This expression language is natural and enables you to browse data graph easily.

Here are the main elements:

The starting point is the expression “$” that identifies the root object of the JSON content. Then you can iterate over attributes and sub attributes. The language natively supports arrays.
The point allows you to get an attribute of an object. This can be done recursively according to the depth of the data graph.
The square brackets target arrays and allow the selection of a particular element in them.

The following sample describes how to test the value of a list contained in arrays. The JSON body used contains a list of maps, each map has a ‘sources’ field. This field corresponds to a list of sources and has an id attribute. The expression $[0].sources corresponds to the sources of the first element. The expression $[0].sources.id goes further by getting all the id fields of sources. It’s then possible to get just the first one.



Notice that the value to check can be valid JSON content. DHC automatically parses it and checks if it matches with the content corresponding to the expression. The following sample describes how to check the content of an array.


# Leverage expressions in assertions

Now we know how easy it is to define assertions and check different parts of calls. However DHC goes a step further as you can take advantage of expressions in assertions.

Imagine that you have settings in your context e.g.  the ‘contentType’ you want to use for exchanging data (both request and response).


You can then use this variable when defining your request in the Accept header, to tell the RESTful service that you expect JSON content. To test this behavior, you can add an assertion to check the content of the Content-Type response header and to be sure that JSON content is actually received.



Another possibility is to rely on the result of previous calls. DHC lets you reference the executed calls through expressions. For this to be possible, these calls need to have been saved at least once. The tool will automatically look for the corresponding last call in the history. To do this, it is important to follow this pattern: {“PROJECT-NAME”.”SERVICE-NAME”.”CALL-NAME”}. You can then reference any part such asresponse body or headers. For example, to get the value of the id attribute in the response body, simply use the expression response.body.id, as described below



Having reviewed the capabilities of DHC regarding testing, let’s try it out with a real world RESTful service
