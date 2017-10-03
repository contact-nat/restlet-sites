Restlet Client helps you discover your APIs by defining requests and even recording them.
Defining a single request manually is fine, defining the whole set of requests required by your API can be a boring and error prone task.
In parallel, you may have already described your API using of the current standard, such as Swagger. Why not reusing this existing documentation?

It's of course quite important to make Restlet client a tool that really ease your everyday life.
That's why we keep it open and able to import data from several formats.

We distinguish two kinds of data to import:

* contracts of APIs
* definition of requests

The rest of this section focuses on the specificities of each import.

<a class="anchor" name="import-contracts-of-api"></a>
## Import contracts of API

<a class="anchor" name="swagger-."></a>
### Swagger 2.0

The specification of the Swagger 2.0 format is available <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md" target="_blank">here</a>.

Swagger 2.0 is available under two formats: JSON and YAML. Only the former format is supported right now.

The whole Swagger description is imported into a new single root project. Here is a description of the items created at import.


Restlet Client item | Comments
--------------------|---------
root project | Single project, which name is given by the title of the Swagger documentation
services | Each service relates to a "Path" of the Swagger  documentation
requests | Every operation defined on resources gathered in the same path are located under the same service. Query parameters and headers are imported too.


<a class="anchor" name="restlet-studio"></a>
### Restlet Studio

[Restlet Studio](https://studio.restlet.com) is one the tools of the Restlet Platform. In a nutshell, this online tool allows you to fully describe the contract of your API.
Restlet Studio integrates very well with Restlet client using the `Try in Client` tool (described [here](../../studio/user-guide/platform/tryinclient)).

Restlet Client item | Comments
--------------------|---------
root project | Single project, which name is given by the name of the API in Restlet Studio.
services | Each section of the Studio defines a new service.
requests | Each operation of each resource located inside the same section are gathered under the same service. Path variables, query parameters and headers are imported too.


<a class="anchor" name="import-definition-of-requests"></a>
## Import definition  of requests

<a class="anchor" name="postman"></a>
### Postman

We estimate that Postman and Client are both valuable tools that deserve your attention. We provide the ability to import Postman collections (V2.0) into Client mainly in order to help users of Postman test Client with the data they are familiar with.

Restlet Client item | Comments
--------------------|---------
root project | Single project, which name is given by the name of the root folder in the collection. A suffix is added in order to prevent name collision.
services | As Client does not support infinite hierarchy of folders, as Postman does, subfolders are flattened as Client service.
requests | Environment variables are imported too (the environment holds the name of the project), but they are valuated. When possible, some assertions are generated too after parsing the Postman scripts.

You can refer to this <a href="http://restlet.com/company/blog/2017/08/09/the-postman-always-rings-twice/" target="_blank">blog post</a> for more details about the import rules.

<a class="anchor" name="har"></a>
### HAR

<a href="http://www.softwareishard.com/blog/har-12-spec/" target="_blank">HAR</a> is a JSON-based format able to describe HTTP requests and responses. [Major Web browsers](https://toolbox.googleapps.com/apps/har_analyzer/) propose to export all interactions in this format.
Requests are imported into a root scenario, which name is computed against the current date of import.
The main reason to use a scenario, and not a project as the other kind of import, is that a HAR log is an ordered sequence of HTTP requests/responses, so does a scenario in Restlet Client.

<a class="anchor" name="systinet-hp-technology"></a>
### Systinet (HP technology)

Restlet Client makes an <a href="https://hpln.hp.com/group/systinet?utm_source=Restlet Client" target="_blank">HP Systinet</a> integration available.

<a class="anchor" name="restlet-client-repository"></a>
### Restlet Client repository

You can export your own local repository into a JSON-based format file. Then import it into Client again.
You are assured that the imported entities are exactly the same than the one exported.

<a class="anchor" name="import-policies"></a>
## Import policies

You can choose between three different policies:

- **Update**
Imports new requests and updates existing requests only if the import contains a newer version.

- **Override**
Imports new requests and overrides all existing requests.

- **Preserve**
Imports new requests and never touches existing requests.
