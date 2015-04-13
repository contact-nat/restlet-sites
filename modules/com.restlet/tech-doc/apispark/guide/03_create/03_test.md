# Requirements

In order to test and debug a web API, a certain number of requirements must be met.

The API must be deployed. Please visit the [Cell deployment](/technical-resources/apispark/guide/explore/cell-deployment "Cell deployment") page if you do not know how to deploy an API.  
You need access credentials if the API if not open to *Anyone*. Please visit the [Invocation](/technical-resources/apispark/guide/publish/publish/invocation "Invocation") page if you do not know how to obtain and view your API access credentials.

# Invoke your web API

Using a web API does not impose any particular programming language. It can even be done from a web browser. However, to test your API we recommend the use of tools such as the Swagger UI (which you can launch directly from APISpark), the Chrome extension POSTMAN or DHC that provide a graphical user interface to perform HTTP calls.

## Invoke your web API from APISpark with the Swagger UI

APISpark embeds the Swagger UI to allow you to call out the various resources and methods of your API directly from the platform.

From the **Overview** tab of your API, select the appropriate Endpoint.  
From the left panel, click on the Resource and the Method chosen and click on the **swagger** button.

![Try it out!](images/swagger-button.jpg "Try it out!")

The Swagger UI opens in a new tab.  
Your credentials are pre-filled in the two fields on top of the screen.

![Swagger UI](images/swagger-ui.jpg "Swagger UI")

You can select the list of resources and methods available from your API, along with sample data that you will need i.e. to send to a PUT or POST method.  
You can also specify the query parameters that you want to pass to the call.  
Scroll down to the bottom of the page and click on the **Try it out!** button to invoke your API.

![Swagger Try it out button](images/swagger-try-it-out-button.jpg "Swagger Try it out button")


## Invoke your web API with POSTMAN

When using POSTMAN, click on the **Basic Auth** tab, fill in the **Username** and **Password** fields with the information copied from your APISpark **Endpoints**.  
Fill in the **Endpoint URI** and add the name of a resource e.g. `/contacts/ `at the end of it. (E.g. `https://employeedirectory.apispark.net/v1/contacts`).  
To retrieve the list of contacts in JSON, click on the **Headers** button on the far right and input the **Accept** command in the **Header** field and write **application/json** in the **Value** field opposite.  
Click on the **Send** button.

![POSTMAN](images/postman.jpg "POSTMAN")

# Check your messages

The web API's **Messages** tab displays messages pertaining to different aspects of your API's life.

This is a great place to go to when testing your API.

![Messages](images/messages.jpg "Messages")

## Messages

The **Messages** section in the left panel contains general messages pertaining to APISpark internal process such as version creation, and deployment.

## Traces

The **Traces** section contains detailed log messages pertaining to processes such as deployment.

If one such process were to result in an error, the **Traces** section can provide useful information as to why the process failed.

## Latest API calls

The **Latest API calls** section lists the latest HTTP calls made to a deployed web API.
