Sending a request is good, but sometimes you will want to send several in one go. We've got you covered on that front.
Any request container (project, service, scenario) in your repository can be run either from its overview (right-pane)
or directly from the repository tree in the `Scenarios` perspective. Just click on the play button!

<a class="anchor" name="differences-between-scenarios-and-other-request-containers"></a>
## Differences between scenarios and other request containers

<a class="anchor" name="request-order"></a>
### Request order

Especially if you are performing tests, you might want to send your requests in a specific order. If it is your case,
then all you need to do is create a scenario. Requests in scenarios can be reordered to your wishes unlike projects and
services which will run your requests in alphabetical order of their names.

<a class="anchor" name="validation"></a>
### Validation

If you have tried running a scenario, you may have noticed green/yellow/red labels indicating its
success/warning/failure to run.
This is computed for each request from the status code or via [HTTP response validation](./validate-http-responses).
Then the request container is tagged as success if all its children were successful. 

Note that scenarios should be functionally atomic as unlike other containers, they will stop as soon as a child request 
fails.

<a class="anchor" name="manage-your-scenarios"></a>
## Manage your scenarios

Restlet Client provides a set of features to help you create and update your scenarios faster.

* From history: select requests in the history then click on '<i class="fa fa-database" aria-hidden="true"></i> Save 
to repository'.
* From HAR: click on `Import > Import HAR 1.2` and let the app guide you. HAR can be exported from chrome dev tools 
in the network tab by right-clicking the history and selecting `Save as HAR with content` so you can play with your 
API with Chrome then import the corresponding scenario in Restlet Client!
* Add existing requests: if you have already played with an API and want to create multiple scenarios from the same 
requests, you can click on the contextual menu next to the scenario's name and select `Add existing requests`. You can
select some requests in your repository that will be copied to your scenario.
* Extract to scenario: this method is exactly the same as the previous one except that here you start from the 
container of your request collection and create a new scenario from its requests. Open the contextual menu next to
your service/project and click on `Extract to scenario` to create a new scenario with copies of the requests you'll 
select from the container.
