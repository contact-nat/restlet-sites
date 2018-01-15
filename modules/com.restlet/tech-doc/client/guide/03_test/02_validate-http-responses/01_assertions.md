Restlet Client allows you to validate the behavior of your API by creating assertions on a request.

An assertion will verify a specific part of a response based on the conditions you've defined and will indicate whether or not the conditions pass.

<!-- TOC: BEGIN -->
* [Usage example](#usage-example)
* [Assertion operators](#assertion-operators)
  * [Equality](#equality)
  * [Comparison](#comparison)
  * [Existence](#existence)
  * [Length](#length)
  * [Content](#content)
  * [Regular expression matching](#regular-expression-matching)

<!-- TOC: END -->

<a class="anchor" name="usage-example"></a>
## Usage example

I want to make sure my content-negotiation is working. I need to know whether the `Content-type` header of my HTTP response is equal to the `Accept` header of the request.

I can do that by creating the following assertion: `Header > Content-type > Equals > ${"My request"."request"."headers"."Accept"}`

> Note: the last part is an __expression__, it allows you to grab a part of the request to check it against the response. Expressions can do __much more__, please view the [full documentation](../make-your-requests-and-assertions-dynamic/expressions) on expressions for more details as to what can be achieved with them.

<a class="anchor" name="assertion-operators"></a>
## Assertion operators

Assertions rely on operators - the `Equals` part in the example above. Restlet Client has a set of operators that you
can use that are described below.

> Note: if you want a response body to be interpreted as JSON, you need to use the `JSON body` element in your assertion. If you use the `Body content` element, the body will be interpreted as a string.

<a class="anchor" name="equality"></a>
### Equality

The operators `Equals` and `Does not equal` are the simplest of all and only do an equality check.
Fortunately, they do understand JSON, meaning that if used on JSON arrays, they won't invalidate the request if the
expected and actual arrays are formatted differently.

The operator `Does not equal` is the exact opposite of `Equals`.

<a class="anchor" name="comparison"></a>
### Comparison

The comparison operators are:

* Greater than
* Greater than or equal
* Less than
* Less than or equal

Comparisons are done this way:

* On numbers: does a number comparison
* On strings: compares the string lengths
* On booleans, JSON arrays, JSON objects: compares them as strings
* On null: N/A - an error will be displayed

<a class="anchor" name="existence"></a>
### Existence

The operator `Exists` checks that the element is present in the response.

The operator `Does not exist` is the exact opposite of `Exists`.

<a class="anchor" name="length"></a>
### Length

The length operators are:

* Length equals
* Length greater than or equal
* Length less than or equal

The length is computed as follows:

* On strings: it returns the number of characters in the string
* On JSON or XML arrays: it returns the number of elements in the array
* On JSON objects: it returns the number of keys in the object
* On null, numbers, booleans: N/A - an error will be displayed
* On multi-valued headers: it returns the number of values for the header

> Note: a header can have multiple values if it is present multiple times or if it contains a comma-separated list of values

<a class="anchor" name="content"></a>
### Content

The content operators are:

* Contains
* Does not contain

They work this way:

* On strings: it checks that the element contains the given substring.
  * `Darth Vader` contains `arth`
* On JSON arrays: it checks that the element contains all of the given values. The expected values can be either a
JSON array or comma-separated values.
  * `[ "Darth Vader", "Luke Skywalker", "Leia Skywalker" ]` contains `"Darth Vader", "Luke Skywalker"`
  * `[ "Darth Vader", "Luke Skywalker", "Leia Skywalker" ]` contains `[ "Darth Vader" ]`
* On JSON objects: it checks that the element contains all of the given keys.
  * `{ "age": 41, "lightSaberColor": "red", "forcePower": "3000" }` contains `"age","lightSaberColor"`
  * `{ "age": 41, "lightSaberColor": "red", "forcePower": "3000" }` contains `[ "forcePower" ]`
* On arrays of XML nodes: it checks that the element contains all of the given nodes listed as an array of nodes.
  * `[<title lang="en">Harry Potter</title>, <title lang="en">XQuery Kick Start</title>, <title lang="en">Learning XML</title>]` contains `[<title lang="en">Harry Potter</title>, <title lang="en">Learning XML</title>]`
* On null, numbers and booleans: N/A - an error will be displayed

<a class="anchor" name="regular-expression-matching"></a>
### Regular expression matching

The operator `Matches` allows you to verify that an element matches a given regular expression.

The operator will match the element against valid JavaScript regular expressions. They can be of two forms:

* Without flags: `^Darth` will match `Darth Vader`
* With flags: `/^darth/i` will match `Darth Vader`

A more complete documentation can be found on
<a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions" target="_blank">
MDN <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i>
</a>.

If you want to learn how to craft a regular expression and test it against real-life examples, we recommend the
excellent <a href="https://regex101.com" target="_blank">
Regex101 website <i class="fa fa-external-link" style="font-size: 12px" aria-hidden="true"></i>
</a>. Just make sure you have selected the `JavaScript` flavor - in the left menu - for your regular expressions!
