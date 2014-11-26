# Introduction

This tutorial will show you how to deploy your first API in 15 minutes or less. Head to our [User guide](technical-resources/apispark/guide "User guide") page first if you would like to learn about the main concepts behind APISpark. Now, let’s get right to it.

# Requirements

To follow this tutorial, you will need the following:

*   a web browser,
*   15 minutes of your time.

# 1. Create an Entity Store

## 1.1 Add a new Entity Store

If you have not already done so, <a href="
https://apispark.restlet.com/signin" target="_blank">sign in</a> to your APISpark account and open your **Dashboard**.

Create a new Entity Store. Click on **+ Entity Store**, select the "Full stack" **Type** and enter the **Name** "myStore".

![Create an Entity Store](images/create-entity-store.png "Create an Entity Store")

Click on the **Add** button to create the new Entity Store.

You will be taken to the new Entity Store's **Overview page**.

## 1.2 Create your data model by adding entities and properties

Click on the **Add** button next to **Entities** in the left panel to create a new Entity. Name the new Entity **Contact**.

![Add an entity](images/add-an-entity.png "Add an Entity")

To add **Properties** to an entity, select the Entity from the **Entities** menu on the left and click on **+ Add a property** in the central panel.

We are going to create three properties named *lastName*, *firstName* and *age*. Note that the *id* property was created automatically.

For each property you create, you can choose a data type, cardinality and default value. You can also decide if the property can take a *null* value.

![Add a property](images/add-a-property.png "Add a property")

## 1.3 Deploy the Entity Store

Click on the **Deploy** button to deploy your Entity Store.

![Deploy Entity Store](images/deploy-entity-store.jpg "Deploy Entity Store")

## 1.4 Add data via the Data Browser

Once your Entity Store has been deployed, you can use it to store contacts. For the purpose of this tutorial, let’s add one via the APISpark Data Browser.

Select the Contact entity from the **Entities** menu in the left panel of the **Overview**.

Click on the **Browser** tab in the central panel.

Click on the **Add** button to add a new Contact. You will be prompted to enter values for the entity's properties.

![Add a record](images/add-record.jpg "Add a record")

Click on the **Add** button. Your first contact should be displayed like so:

![](images/browser-tab.jpg)

# 2. Create a Web API

## 2.1 Export an API from the Entity Store

From the Entity Store **Overview**, click on the actions button to the right of the **Deploy** button.

![Export web API](images/export-web-api.jpg "Export web API")

Enter a name for your API (e.g. myAPI).

The domain will be created automatically but may not be available anymore so make sure to adjust it.

![Create a web API](images/domain-name-unavailable.jpg "Create a web API")

Click **Add**. You will be taken to the API’s **Overview** page.

Note that your API's **Endpoints**, **Resources**, and **Representations** have been created automatically.

The screenshot below shows some of the drop down menus opened to give you an idea of how the web API **Overview** is organized.

![structure](images/api-overview.jpg "structure")

## 2.2 Deploy the web API

Deploy the API by clicking the **Deploy** button.

The API should now be accessible online. To reach it, use the subdomain of apispark.net that you chose when you created the API.

![credentials](images/credentials.jpg "credentials")

The credentials required to invoke the API are located in the **Overview** tab, by selecting the relevant **Endpoint**. You will need to copy the **Endpoint URI**, **Login** and **Password** information for the next step.

# 3. Invoke the web API

Using a web API does not impose any particular programming language. It can even be done from a web browser. However, to test your API we recommend the use of tools such as the Chrome extensions [POSTMAN](https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm?utm_source=chrome-ntp-icon) and [DHC](http://sprintapi.com/dhcs.html) that provide a graphical user interface to perform HTTP calls. The following figure shows the call to the Contacts API with POSTMAN.

When using POSTMAN, click on the **Basic Auth** tab, fill in the **Username** and **Password** fields with the information copied from your APISpark **Endpoint**. Fill in the **Endpoint URI** and add **/contacts/** at the end of it.

Click the **Send** button.

The following figure shows the API call in POSTMAN.

![Invoke your API with Postman](images/postman.jpg "Invoke your API with Postman")

>**Note:** APISpark lets you generate custom Client SDKs for your API. Different environments are supported including Java, Android, GWT and JavaScript (AJAX or Node.js).

Congratulations on completing this tutorial! If you have questions or suggestions, feel free to contact the <a href="http://support.restlet.com/" target="_blank">Help Desk</a>.
