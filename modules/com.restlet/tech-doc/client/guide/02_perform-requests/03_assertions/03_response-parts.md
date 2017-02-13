The first part is obviously the status one. In this case, you can choose not only the status code but the status message as well.

![Status code or message](images/02-status-code.jpg "Status code or message")

Regarding response headers, Restlet Client allows you to check both their presence and their content. In the first instance, you can simply put the name of the header in the parameter field and use the **Exists** or **Does not exist** comparison operators, as shown below:

![Does not exist](images/04-does-not-exist.jpg "Does not exist")

Checking the header value is pretty simple, just change the comparison operator to **Equals** or **Contains** for example and fill the value field with the expected value.

![Contains](images/05-contains.jpg "Contains")

Just as you do for headers, you can also check either the presence or content of a payload. The first case is interesting for responses with empty payloads, typically with status code 204.

On this screenshpt, the response payload is empty:
![No content](images/06-no-content.jpg "No content")

Here we check that the response payload is actually empty:
![Empty response payload](images/06-empty-response-payload.jpg "Empty response payload")


For text-based payload, structured or not, it is possible to check if the content matches to a specific value, or contains some text, as described below.

The response payload is not empty:
![Response payload is not empty](images/07-response-payload-not-empty.jpg "Response payload is not empty")

Here we check if the keyword *Restlet* is present in the response payload:
![Keyword in response payload](images/07-keyword-in-response-payload.jpg "Keyword in response payload")

Testing text payload can be tedious if they are only considered as text. For this reason, Restlet Client goes further by natively supporting JSON body. The latter allows to apply specific JSON path expressions. We will focus on this feature in the next section.

Before diving into JSON payload, do not forget the support for the response itself. This allows you to apply assertions to its latency. This is particularly useful if you want to test the maximum time for a call.

![Latency](images/08-latency.jpg "Latency")
