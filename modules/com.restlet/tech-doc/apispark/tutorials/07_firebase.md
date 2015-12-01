# Introduction

This tutorial will show you how to create a custom web API that gives access to existing data stored in a Firebase backend. <a href="https://www.firebase.com" target="_blank">Firebase</a> is a popular Backend as a Service (BaaS) provider that powers real time HTML 5 applications.

<h1 class="iconed" id="toc_0"><i class="fa fa-flag-checkered"></i>Requirements</h1>

To follow this tutorial, you will need:

*   a web browser,
*   20 minutes of your time,
*   Your Firebase login details.

<h1 class="numbered" id="toc_1"><i>1</i>Prepare the Firebase Backend</h1>

## Create a new Firebase

Sign in to your **Firebase** account. Select the application you want to work on, or create a new one. For this tutorial, we created a new app called **MyRealtimeDB**, and with app URL **myrealtimedb**. You may need to edit the app URL if it is already taken.

After clicking on **CREATE NEW APP**, you will be directed to the app's data console. The default root data element in this app will be named after your app URL.

Now, enter a sample **Contact** by matching the tree structure illustrated in the screenshot below. Begin by adding a **Contact** element. Add a child element that will be used as the **Contact**'s ID. Then add the following fields below the ID:
*   **lastName** (string: value in quotes)
*   **firstName** (string: value in quotes)
*   **age** (integer: without quotes)

![Create your firebase](images/create-firebase-struct.png "Create your firebase")

We recommend manually adding a data element to your FireBase so that you can retrieve it later, but this is not mandatory.

As you can see, we have chosen to name our **Contact** John Smith, age 34 years old, and to give him the ID id001.

Be aware that the structure of this data will be reflected in APISpark later, namely:

*   the entity name (Contact here)
*   the entity identifier
*   the entity's properties

## Secure your Firebase

By default, operations to your Firebase are not secured. Everything is authorized. You can easily change this in the **Security** tab of the Firebase console with security rules as described below. In this case, you need to be authenticated to execute both read and write operations on data.

To do so, head to the **Security & Rules** tab and replace the default rules by those below:

<pre><code>
{
    "rules": {
      ".read": "auth != null",
      ".write": "auth != null"
      }
}
</code></pre>    

![Firebase security](images/firebase-security.png "Firebase security")

Click on the **Save Rules** button.

In order to give APISpark access to you Firebase app, you will need your secret token. Open the **Secrets** tab, click on **Show**, then **Copy** your secret token.

![Firebase secrets](images/firebase-secrets.png "Firebase secrets")

<h1 class="numbered" id="toc_2"><i>2</i>Create a Firebase Wrapper</h1>

## Create a new Firebase Wrapper

Sign in to your **APISpark** account.

Click on **+ Entity Store**.

Select **Firebase wrapper** and enter a name for your Wrapper. We named ours "myFirebase". Input a description if you like.

![Firebase secrets](images/create-firebase-wrapper.jpg "Firebase secrets")

Click **Add**. You will be taken to the **Settings** tab.

## Setup your Firebase account

In the **Security** section, click on **Firebase Account**.

Input your **Application ID** (application URL chosen in Firebase) and **Auth token**. The Auth token is only required if the corresponding Firebase is secured.

Click on the **Test** button.

![Configure your firebase app](images/configure-firebase-app.jpg "Configure your firebase app")

## Create an Entity

We will now create an entity in the APISpark **Entity Store** whose name and property names match the ones chosen in Firebase.

To do so, click on the **Overview** tab.

**Add** an Entity.

Name your **Entity**. In this tutorial example, we named our entity **Contact** and added the following properties: firstName, lastName and age.

![Add an entity](images/add-firebase-entity.jpg "Add an entity")

>**Note:** Your **Entity** elements and the **Schema** elements in Firebase do not have to match up perfectly. For this purpose, you can use the **Mapping from** attribute on the entity and its properties. This allows you to specify the name of the target element (entity or property) in Firebase.

Click on the **Deploy** button.

<h1 class="numbered" id="toc_3"><i>3</i>Export a Web API</h1>

From the Wrapper's **Overview** page, click on the actions button on the left of the **Deploy** button and select **Export web API**.

Give your new API a name. We named ours **myAPI**.

![Create a web API](images/firebase-export-api.jpg "Create a web API")

The domain will be created automatically but may not be available anymore so make sure to adjust it.

Click on **Add** to create the API. You will be taken to the API's **Overview** page.

Deploy the API by clicking the **Deploy** button.

![Deploy button](images/deploy-button2.jpg "Deploy button")

<h1 class="numbered" id="toc_4"><i>4</i>Invoke the Web API</h1>

Using a web API does not impose any particular programming language. It can even be done from a web browser. However, to test your API, APISpark offers an integration of the Swagger UI that provides a graphical user interface to perform HTTP calls.

From the **Overview** tab of your API, select the appropriate Endpoint.  
From the left panel, click on the Resource and the Method chosen and click on the **swagger** button.

![Try it out!](images/07swagger-button.jpg "Try it out!")

The Swagger UI opens in a new tab.  
Your credentials are pre-filled in the two fields on top of the screen.

![Swagger UI](images/07swagger-ui.jpg "Swagger UI")

Scroll down to the bottom of the page and click on the **Try it out!** button to invoke your API.

![Swagger Try it out button](images/07swagger-try-it-out-button.jpg "Swagger Try it out button")

Any POST requests made to the API will result in new data being created in your Firebase backend. Likewise, any data manually inserted via your Firebase data editor is visible via the custom web API.

>**Note:** APISpark lets you generate custom Client SDKs for you API. Supported environments include Java, Android, iOS and JavaScript (AJAX or Node.js).

Congratulations on completing this tutorial! If you have questions or suggestions, feel free to contact the <a href="http://support.restlet.com/" target="_blank">Help Desk</a>.
