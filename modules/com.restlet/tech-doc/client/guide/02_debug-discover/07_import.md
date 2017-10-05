Restlet Client helps you discover your APIs by defining, saving and running requests. Editing requests by hand is fine to discover the tool and make your first steps in the product, but you probably have a complete API you would like to request and creating the requests for all its resources would be a fastidious, error-prone task. 

We want to make sure Restlet Client helps you in your everyday life and stays a modern and open tool. That's why we've made sure it can import different standard formats like Swagger - the reference in API documentation - and HAR. 

This means that you can request any API that exposes its Swagger really easily and you can convert you browser's navigation history in ready-to-launch Restlet Client requests.

We distinguish two kinds of data to import:

* API contracts
* request collections

The rest of this section focuses on the specificities of each import.

<a class="anchor" name="import-contracts-of-api"></a>
## Import API contracts

<a class="anchor" name="swagger-20"></a>
### Swagger 2.0

The specification of the Swagger 2.0 format is available <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md" target="_blank">here <i class="fa fa-external-link" aria-hidden="true"></i></a>.


Only JSON is supported at the moment, in case you only have a YAML document, you can easily 
convert it to JSON before importing it in Restlet Client. Online converters flourish on the web, 
you can use <a href="https://www.json2yaml.com/" target="_blank">here <i class="fa fa-external-link" aria-hidden="true"></i></a> for example.

Swagger 2.0 is available under two formats: JSON and YAML. Only the former format is supported right now.

The whole Swagger description is imported into a new project. Here is a description of the items created at import.


|Swagger item | Corresponding Restlet Client item | Comments
|-------------|-----------------------------------|---------
| API | Project | A project containing all the converted Swagger. Its name and description are the Swagger API title and description
| Path | Service | Each path in Swagger is converted to a service. The service is named after the base path of the Swagger Path.
| Operation | Request | All the operations of a Swagger path are converted to requests and located inside the service extracted from the said path. |

<a class="anchor" name="restlet-studio"></a>
### Restlet Studio

The full behavior is described [here](../../../studio/user-guide/platform/tryinclient).

<a class="anchor" name="import-definition-of-requests"></a>
## Import definition  of requests

<a class="anchor" name="postman"></a>
### Postman

We estimate that Postman and Restlet Client are both valuable tools that deserve your attention. We provide the ability to import Postman collections (V2.0) into Restlet Client in order to help users of Postman try out Restlet Client with the data they are familiar with.

| Postman item | Corresponding Restlet Client item | Comments
|--------------|-----------------------------------|---------
| root folder | root project | Single project, which name is given by the name of the root folder in the collection. A suffix is added in order to prevent name collisions.
| sub folders | services | As POSTman supports infinite folder depth - which Restlet Client does not - subfolders are flattened.
| requests | requests | Environment variables are imported too (the environment holds the name of the project), but they are valuated. When possible, some assertions are generated too after parsing the Postman scripts.

You can refer to this <a href="http://restlet.com/company/blog/2017/08/09/the-postman-always-rings-twice/" target="_blank">blog post <i class="fa fa-external-link" aria-hidden="true"></i></a> for more details about the import rules.

<a class="anchor" name="har-12"></a>
### HAR 1.2

<a href="http://www.softwareishard.com/blog/har-12-spec/" target="_blank">HAR <i class="fa fa-external-link" aria-hidden="true"></i></a> is a JSON-based format able to describe HTTP requests and responses. [Major Web browsers](https://toolbox.googleapps.com/apps/har_analyzer/) support to export all interactions in this format.
Requests are imported into a root scenario, which name is the date of the import - in <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601 format <i class="fa fa-external-link" aria-hidden="true"></i></a>.
The main reason to use a scenario, and not a project as the other kind of import, is that a HAR log is an __ordered__ sequence of HTTP requests/responses, so does a scenario in Restlet Client. Learn more in the dedicated tutorial: [Build an API test for your Web application](../../tutorials/test-web-api)

<a class="anchor" name="systinet-hp-technology"></a>
### Systinet (HP technology)

Restlet Client makes an <a href="https://hpln.hp.com/group/systinet?utm_source=Restlet Client" target="_blank">HP Systinet <i class="fa fa-external-link" aria-hidden="true"></i></a> integration available.

<a class="anchor" name="restlet-client-repository"></a>
### Restlet Client repository

You can export your own local repository into a JSON-based format file. Then import it into Client again.
You are assured that the imported entities are exactly the same than the one exported.

<a class="anchor" name="import-policies"></a>
## Import policies

When importing a Restlet Client repository, you can choose between three different policies:

- **Update**
Any element that does not exist in your drive - equality is checked by name - is imported. For elements that already exist, the newest is kept between the imported and the current one based on the date of last edition.  

- **Overwrite**
Imports new elements and overwrites existing ones.

- **Preserve**
Imports new elements and ignores existing ones.

> Please note that none of these policies delete anything in your drive
