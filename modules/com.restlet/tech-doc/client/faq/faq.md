<a class="anchor" name="list"></a>

1. [How can I add a client certificate for HTTPS in Restlet Client?](#01 "How can I add a client certificate for HTTPS in Restlet Client?")
2. [How do I allow third-party cookies in my browser ?](#02 "How do I allow third-party cookies in my browser ?")


# <a class="anchor" name="01"></a>1. How can I add a client certificate for HTTPS in Restlet Client?

As Restlet Client relies on Chrome certificate management, you need to add your certificate to Chrome from Chrome settings:
![Chrome settings](images/chrome_settings.png "chrome settings")


[Back to list](#list "Back to list of frequently asked questions")

# <a class="anchor" name="02"></a>2. How do I allow third-party cookies in my browser ? 

Third party cookies should be enabled in your browser because we rely on a third-party provider (Auth0) for our security and user account management

## Chrome

1. Click the menu icon in the upper right side of the browser window.

2. Click `Settings`.

3. Click `Advanced` at the bottom of the page.

4. Click `Content settings...`

5. Click `Cookies`

6. Uncheck `Block third-party cookies`

## Firefox

1. Click the menu icon.

2. Open the Options window.

* For Windows users, click `Options`. 
* For Mac OS users, click `Preferences...`

3. Click the `Privacy & Security` panel.

4. In the `History` section, select `Firefox will: Remember history`.

## Safari

1. Click `Safari` in the menu bar.

2. Click `Preferences...`

3. Click the `Privacy panel`.

4. Uncheck `Prevent cross-site tracking`

## Internet Explorer

1. Click the cog/tools menu icon in the upper right of the window.

2. Click `Internet Options`.

3. Click the `Privacy` tab.

4. Click `Advanced`.

5. Check `Accept` under `Third-party cookies` section

6. Click `OK`

## Microsoft Edge

1. Click the menu icon in the upper right side of the browser window.

2. Click `Settings`

3. Click `View Advanced Settings`

4. Select `Don't block cookies` under `Cookies` section

