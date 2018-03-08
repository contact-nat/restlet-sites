<a class="anchor" name="list"></a>

1. [How can I add a client certificate for HTTPS in Restlet Client?](#01 "How can I add a client certificate for HTTPS in Restlet Client?")
2. [How do I allow third-party cookies in my browser ?](#02 "How do I allow third-party cookies in my browser ?")
3. [Why can't I see my GitHub organisation when setting up my GitHub repository ?](#03 "Why can't I see my GitHub organisation when setting up my GitHub repository ?")


# <a class="anchor" name="01"></a>1. How can I add a client certificate for HTTPS in Restlet Client?

As Restlet Client relies on Chrome certificate management, you need to add your certificate to Chrome from Chrome settings:
![Chrome settings](images/chrome_settings.png "chrome settings")


[Back to list](#list "Back to list of frequently asked questions")

# <a class="anchor" name="02"></a>2. How do I allow third-party cookies in my browser ? 

Third party cookies should be enabled in your browser because we rely on a third-party provider (Auth0) for our security and user account management

1. Click the menu icon in the upper right side of the browser window.

# <a class="anchor" name="03"></a>3. Why can't I see my GitHub organisation when setting up my GitHub repository ? 

It may be because your organisation does not allow Restlet to access its resources. To allow Restlet application, you can follow [this GitHub tutorial](https://help.github.com/articles/approving-oauth-apps-for-your-organization/).