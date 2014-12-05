# First client

## Introduction

As we mentioned in the [introduction](../index.md "Part I - Introduction"),
the Restlet Framework is at the same time a client and a server
framework. For example, you can easily work with remote resources using
its HTTP client connector.

A connector in REST is a software element that enables the communication
between components, typically by implementing one side of a network
protocol. Restlet provides several implementations of client connectors
based on existing open-source projects. The
[connectors](../../core/base/connectors/index.md "Connectors")
section lists all available client and server connectors and explain how
to use and configure them.

Here we will retrieve the representation of an existing resource and
output it in the JVM console:

~~~~ {.brush: .java}
// Outputting the content of a Web page  
new ClientResource("http://restlet.com").get().write(System.out);  
~~~~

If you are running your client behind a proxy, please [check this
page](../../core/base/connectors/index.md) to
pick an HTTP client that can be configured. The internal HTTP client
doesn't support proxies at the moment.

The next example sets some preferences in your client call, like a
referrer URI:

~~~~ {.brush: .java}
// Create the client resource  
ClientResource resource = new ClientResource("http://restlet.com");  
 
// Customize the referrer property  
resource.setReferrerRef("http://www.mysite.org");  

// Write the response entity on the console
resource.get().write(System.out);  
~~~~

After those first two steps, [let's now develop a more complete Restlet
application](first-application.md "First application"),
taking advantage of the various editions of the Restlet Framework.

