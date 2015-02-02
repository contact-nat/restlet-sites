Runtime access to an API is configured by allowing specific resource methods for specific groups.

## Configure default permissions

Default permissions allow you to define group/method permissions once and for all. Every new resource created will then inherit these default permissions.

To configure default permissions for an API, go to the **Settings** tab, and select **Default Permissions** from the left panel.

![default permissions](images/default-permissions.jpg "default permissions")

>**Note:** Select the **Anyone** checkbox to give rights to anyone including anonymous users who consume public APIs.


## Configure method level access

Select a method belonging to one of the resources in the left panel of a web API's **Overview**. Open the method's **Security** tab in the central panel.

![Security tab](images/security-tab.jpg "Security tab")

The **Security** panel lets you decide which methods are open to which member groups for this specific resource.

>**Note:** Select the **Anyone** checkbox to give rights to anyone including anonymous users who consume public APIs.

## Opening a method to Anyone

API methods can be opened to *anyone*. A method opened to *anyone* can be invoked without authentication.

To configure a method's runtime access to *anyone*, in either method-level or default permissions, check *anyone* in the appropriate menu.

> **Note:** You will need to redeploy your API in order to take into account changes in groups and permissions.

# Make your API documentation public

An API's **Overview** page contains rich documentation for consumers of the API, including **Endpoints**, **Resources**, and **Representations**.

In combination with opening your API to *anyone*, you might want to make a part of your API's **Overview** publicly accessible, even to users that do not have an APISpark account.

To see how to make your API public, please visit the [Documentation page](/technical-resources/apispark/guide/publish/publish/documentation "Documentation").
