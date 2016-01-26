# Introduction

DHC allows you to interact with REST services. It brings many different features that make your user experience better, save you precious time when debugging REST calls or sharing your requests with others.

# Install DHC by Restlet

DHC by Restlet can be used as an online service from <a href="https://dhc.restlet.com/" target="_blank">dhc.restlet.com</a> or installed within Chrome from the <a href="https://chrome.google.com/webstore/detail/dhc-resthttp-api-client/aejoelaoggembcahagimdiliamlcdmfm" target="_blank">Chrome Web Store</a>.

## DHC by Restlet online service

![DHC online service](images/dhc-online-service.jpg "DHC online service")

## DHC by Restlet Chrome application

The tool is also available in your Chrome applications through chrome://apps.

![DHC Chrome app](images/dhc-chrome-app.jpg "DHC Chrome app")

>**Note:** When installing DHC Chrome app, a confirmation message displays informing you that DHC requires permissions to "Read and change all your data on the websites you visit" and to "Communicate with cooperating websites".
- Permission to "**Read and change all your data on the websites you visit**"  
Chrome applications which need access to internet resources must declare it in their manifest. It can be a list of URLs or URL mask e.g. http://*/* allowing access to any URL. Allowing access to any URL is a primary feature of DHC, and the URL mask with wildcards is interpreted by Chrome Store as: This app "can read and change all your data on the websites you visit". Which is true, but in fact DHC does not collect your data.
- Permission to "**Communicate with cooperating websites**"  
DHC is available also as a service. This permission allows data exchange between DHC Service and DHC Chrome. This feature is disabled by default.
