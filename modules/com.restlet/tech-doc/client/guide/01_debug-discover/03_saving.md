<a class="anchor" name="first-you-discover"></a>
## First you discover
In the discovery mode, any update erases the previous one. There is no history, only one state is kept: the last one.

This state is persisted so that you can reload Client or go back and forth to the Scenarios perspective and still recover the request as it was.

This state is updated either manually by the user when playing with the request edition form or when loading a request from the history or the drive.

The save action works like a `save as`:

* the first time, it saves the request with the name you must provide
* the next time, you can either perform a `save as` by specifying a name that doesn't already exist in the target location or update the request by selecting it

At any time you can save the current and stable state into the drive.

=> perhaps a simple but global workflow diagram

<a class="anchor" name="then-you-save-into-a-drive"></a>
## Then you save into a drive

Let's elaborate about the notions of drive, and containers.


<a class="anchor" name="drive"></a>
### Drive

The repository contains all your requests and the requests of your teams.
The requests are split in drives, yours - `My drive` - and one for each of the teams you belong to. The last ones are known as `shared drives`.

If you are the owner of a team, all the projects -more on projects below - you share can be seen by your team members. 
They appear on their side in a `Shared drive` which name is the email address linked to your Restlet account. 

Of course a drive can be local, in this case it is generally labelled `My drive`.

If you subscribed to a "Team" plan, you can share your own drive. In this case, you are intronised Team owner, and you own also the shared drive.
Every member of your team are called Team members, and get their own copy of the shared drive.

<a class="anchor" name="request-containers"></a>
### Request containers

Of course a drive can store requests but also other kind of containers:

* scenario: a scenario is an ordered set of requests. It is very helpful to define a sequence of requests and play it again, and again, and again ([learn more about scenarios](../test/run-request-sets)). You can change the order at will in the scenario's overview. Just click on the arrows to move a request up or down.
* service: a service contains either scenarios or requests. Children entities are ordered by type, then by name.
* project: a project contains either scenarios, services or requests. Children entities are ordered by type, then by name.

Each entity has a name and a description. The description field accepts markdown so that you get powerful documentation tools.

When you save a request from the testing perspective, you can choose an existing container, or create a new one.

> Note about the order of children:
> The children entities of a container are run in the same order as presented by the user interface.
