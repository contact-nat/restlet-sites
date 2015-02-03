In the API ecosystem, there are several formats that allow you to describe an API.

The RAML and Swagger formats are more likely to be used to get the definition of an API.

With APISpark you can export your web API (or your [Descriptor](/technical-resources/apispark/guide/document/overview "Descriptor") or [Connector](/technical-resources/apispark/guide/manage/connectors "Connector") cells) to those *standard* API definition languages.

# Export to a standard API definition language

Open your API's **Overview** page and click **General information** button from the left panel.

![General information](images/general-information.jpg "General information")

From this page, you can export your API to the following format:

* RAML 0.8
* Swagger 1.2
* Swagger 2.0

> **Note:** Multiple versions of Swagger are supported.

### Obtain your API's Swagger definition

Click on one of the **Swagger** URLs to open the corresponding Swagger definition.

The **Swagger 1.2** definition for the Contacts API looks like this:

```json
{  
   "apis":[  
      {  
         "path":"/contacts"
      }
   ],
   "apiVersion":"1.1.0",
   "info":{  
      "contact":"jmichaux@restlet.com",
      "description":"My Contacts API.",
      "title":"Contacts API"
   },
   "swaggerVersion":"1.2"
}
```

# Integrate with third party applications

What's great with these formats is that there are more and more tools which understand them.

## Swagger

**Swagger** is an API description language that comes with a number of tools including Swagger UI and Swagger Code Generator. Swagger UI provides a nice HTML presentation of your API’s contract and allows you to test your API by calling it. The Swagger Code Generator generates client kits for your API in several languages.

### Access Swagger UI demo

From an API's **General information** panel, you can get the Swagger definition link to access Swagger UI.

Copy the Swagger link from the **Export your API** section and paste it in
<a href="
http://petstore.swagger.wordnik.com" target="_blank">Swagger UI demo</a>.

![Swagger URL](images/swagger-url.jpg "Swagger URL")

> **Note:** Your API documentation must be public as seen on the [Documentation](/technical-resources/apispark/guide/publish/publish/documentation "Documentation") page.

The screenshot below is an example of what the Swagger UI looks like.  

![Swagger](images/swagger-ui.jpg "Swagger")

## Postman

The *Postman* application can import RAML or Swagger definition to a Postman collection.

From the main window,

![Import in Postman](images/postman-import.png "Import in Postman")

select the **Import** menu on top of the screen to open the import popup.

![Import popup](images/postman-import-popup.png "Import popup")
