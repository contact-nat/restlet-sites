During the implementation phase of your API project, you'll regularly need to interact with your API to verify its behavior. You'll also need to make sure your API implementation is conform to its contract by writing integration tests.

Once the API is implemented, consumers will also need to explore the API's capabilities as they work on their client implementation.

For all of these purposes, Restlet Studio includes a "Try in Client" button, both in the editor view, and published documentation.

You can either export:

* your whole API definition : a project is created in Restlet Client with pre-defined requests for each of the API’s operations. You will then be able to set up complex scenarios that emulate real-life usages of your API.
* a single operation : Restlet Client is opened with a ready-to-go request *for the selected* API operation

If you have not yet installed Restlet Client, you will be redirected to the Google Web Store to install it.

## Try an API in Restlet Client

Try in Restlet Client from the editor:

![Try in Restlet Client from the editor](images/tryapieditor.png "Try in Restlet Client from the editor")

Try in Restlet Client from published documentation:

![Try in Restlet Client from published documentation](images/tryapidoc.png "Try in Restlet Client from published documentation")

## Try an operation in Restlet Client

Try in Restlet Client from the editor:

![Try in Restlet Client from the editor](images/tryoperationeditor.png "Try in Restlet Client from the editor")

Try in Restlet Client from published documentation:

![Try in Restlet Client from published documentation](images/tryoperationdoc.png "Try in Restlet Client from published documentation")


## Technical details

The following table gives you details about the data imported into Restlet Client.

| Restlet Studio item | Corresponding Restlet Client item | Comments
|---------------------|-----------------------------------|---------
| API | Root Project | Single project, whose name is based on the name of the API in Restlet Studio.
| Section | Service | For organization purpose.
| Operation | Request | All the operations from a Restlet Studio Section are gathered under the same Restlet Client Service. Path variables, query parameters and headers are imported too.

When you export an entire API from Studio to Client more than once, elements are matched thanks to their name so that the existing items in Client are updated (except the assertions, which are kept untouched - they are specific to Client). Please note that no items are deleted in Restlet Client (in other words, if you rename an operation in Studio the related request with the original name will be kept in Client).
