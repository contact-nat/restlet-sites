In your application development workflow, you probably rely on different environments such as development, staging and production.

Let's say that your API is deployed in staging and production environments and that you want to compare the behavior of both APIs with Restlet Client.
It's highly probable that the APIs are located on a distinct host and port: for example `staging.acme.com` and `acme.com`.
It sounds like a good idea to set up your requests using variables, doesn't it?

Restlet Client provides a powerful feature to use variables in your requests through the environments.
This topic is covered deeply [here](../test-automate/make-your-requests-and-assertions-dynamic/environments)
