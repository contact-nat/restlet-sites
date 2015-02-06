
APISpark supports two major types of cells: Data Stores and web APIs. Data stores serve to store structured data and static files, while web APIs are RESTful interfaces that expose resources over the Web.

These two types of cells are built to work together, and APISpark provides tools that facilitate exposing Data Stores to the Web through web APIs.

In this section we look at different ways to automatically create a web API from existing Data Stores.

# Export a web API from a Data Store

You can automatically export a web API from a Data Store. If you do not already have a Data Store, checkout the [Entity Store](/technical-resources/apispark/guide/store/entity-stores/model-data "Entity Store") or the [File Store](/technical-resources/apispark/guide/store/file-stores "File Store") page to see how to build the appropriate Data Store.

The API export process can be launched from the Data Store **Overview**. Click on the action arrow and select **Export web API**.

![export custom API](images/exportfromstore.png "export custom API")

In the **Create a Web API** dialog, give your API a **Name**, **Domain**, and **Description** (optional).

The window will notify you of the availability of the the domain name.

![Create a web API](images/exportapi.png "Create a web API")

  > **Note:** When checked, **Add resources automatically** will create web API resources and representations that map towards the entities in your Entity Store. This is the recommended behavior.

# Import a data store into an existing web API

As a complement to the Export API feature, APISpark lets you import one or more Data Stores into an existing web API.

This has the added benefit of allowing you to import multiple Data Stores of potentially different types into a single web API.  

## Create a web API

If you have not already done so, create a web API from the **Dashboard** by clicking on the **+ Web API** button.

![+web API](images/web-api.jpg "+web API")

Select **Full Stack** from the **Type** drop-down menu. Give your API a **Name**, **Domain**, and **Description** (optional).
The window will notify you of the availability of the domain name.

![Create a web API](images/createapi.png "Create a web API")


## Import a Data Store

To import a Data Store into an API, navigate to the API's **Overview**. Click on the **Settings** tab. Click on the **Add** button next to **Imports** in the left panel.

![+Import](images/import.png "+Import")

Select the **Type** of cell you want to import, select the **Cell** itself and the **Version** you want to import.

### Generate resources and representations

Once you have imported a Data Store into a web API, you can automatically generate resources and representations that map to the entities in the imported Data Store.

Select an import from the left panel of the API's **Settings** tab, and click on the **Add** button to generate new resources and representations.

![Generate Resources](images/generateResources.png "Generate Resources")

If you update your Data Store, you can update the API's corresponding resources and representations by clicking on the **Update** button in the same window.

### Switch imported Entity Store version

If you create a new version of an Entity Store that is imported by a Web API (see [Versioning](/technical-resources/apispark/guide/explore/versioning "Versioning")), you can update the Web API's **Import** to switch to the new version of the Entity Store.

To switch the version of an imported Entity Store, open the importing Web API. Open the **Settings** tab, and select the imported Entity Store from the **Imports** section in the left panel.

Open the **Version** drop-down menu from the central panel and select the new version of your Entity Store.

You will be prompted to select whether or not to automatically add any new resources to your API, and to update existing ones. We recommend keeping this box checked.

Go back to the API's **Overview** to view your updated resources and representations.

> **Note:** You can only switch an imported entity store to a version which is superior to the one currently imported.
