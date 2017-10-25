In your application development workflow, you probably rely on different environments such as development, staging and production.

Let's say that your API is deployed in staging and production environments and that you want to compare the behavior of both APIs with Restlet Client.
The APIs probably run on different hosts and ports, for example `staging.acme.com` and `acme.com`.
It would be better to create only one request and parameterize it to target the different environments, wouldn't it ?

Restlet Client provides a powerful feature to use variables in your requests through the environments.
This topic is covered deeply [here](../test/make-your-requests-and-assertions-dynamic/environments).
