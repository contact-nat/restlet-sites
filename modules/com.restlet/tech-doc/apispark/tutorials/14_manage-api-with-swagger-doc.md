# Introduction

Swagger is an API description language that comes with number of tools including Swagger UI and Swagger Code Generator. Swagger UI provides a nice HTML presentation of your API’s contract and allows you to test your API by calling it. Swagger Code Generator generates client kits for your API in eight languages.

APISpark dynamically generates the Swagger description of web APIs and provides the Swagger UI and Code Generator capabilities as a service. You can also import an existing Swagger description into APISpark to update a Web API contract.

# Get the Swagger definition of your API

1. Select the Descriptor of the API you want to get the Swagger definition of.

2. From the **Overview** tab, click on the **General information** button, then on the **API commons** tab in the central panel.

  ![API Commons](images/swagger-apicommons.jpg "API Commons")

  To get the address which hosts the Swagger definition of your API (1.2 or 2.0), click on the **Overview** tab. You will find it in the **Export your API** section.

  ![API Commons](images/07.jpg "API Commons")

# Import existing Swagger definitions

You can import a web API’s contract to APISpark using its Swagger definition just by providing the URL on which it is available to APISpark.

1. From an existing Descriptor page, click on the actions button on the top right of your screen and select the **Import definition** menu.

  ![API Commons](images/swagger-import-definition.jpg "API Commons")

2. Enter the URL that contains your API definition. You cannot import a definition by importing file.

After you click on the **Import** button, the representations and resources of the Swagger definition you provided the link to will be imported in your **Overview** tab.

If you import several definitions you may have conflicts, multiple resources on the same path or multiple representations with the same name. The import wizard will display a message in an orange panel and you will be free to solve the conflicts by yourself.

![API Commons](images/12.jpg "API Commons")

If you close the panel, you can still find the traces in the **Messages** section, in the **Traces** tab in the left pane.

![API Commons](images/13.jpg "API Commons")
