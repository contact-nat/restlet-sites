<a class="anchor" name="introduction"></a>
## Introduction

Now that you have sent a few requests, you might want to remember exactly how the request or its response looked like. You can do exactly that with the history. The history is accessible from the left panel of the Requests perspective and shows last requests sent by URL. 

Clicking on an item in the history loads it in the request panel, you can then edit it and send a modified version if you want.

The history saves the requests and responses of all the requests sent in Restlet Client, whether they are sent in the Requests perspective or Scenarios perspective, as part of a scenario or alone doesn't matter, it is stored. 

From within the history list, users can load the selected entry into the requested editor. Related response is loaded as well.
Take care, the request that was edited previously has now join the abysses and nobody will take it back.

![global picture](images/restlet-client-history.png)

<a class="anchor" name="browsing-the-history"></a>
## Browsing the history

An entry of the history contains both sent request and received response. In addition, it contains the duration of the request.
Entries are displayed according to the date they have been sent: the last sent first.

In addition, entries are grouped by URL which means that any successive requests to the same urls represent only one entry in the list. This helps keep the history synthetic.

<a class="anchor" name="save-requests-to-drive"></a>
## Save requests to drive

You can also save a set of requests into the drive, using the save to repository feature.

Just select a list of requests then specify where they should be stored. 
You can put them in your drive or a shared drive if you belong to a team (see [collaborating](./collaborating)), in an existing request container or create a new one. 