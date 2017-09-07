Saving a request (seule partie sans auto save donc tout le reste en auto save idée: sauvegarder quand état stable, mode draft => discovery, structuration du drive + petit mot pour dire que ça documente l’API)

# First you discover
In the discovery mode, any update erases the previous one. There is no history, only one state is kept: the last one.

This state is persisted so that you can reload Client or go back and forth to the Scenarios perspective and still recover the request as it was.

This state is updated either manually by the user when playing with the request edition form or when loading a request from the history or the drive.

Now, let's say you want to take a snapshot of the current state and make this state persistent in order to recover it anytime.
How can you achieve this? Just save it.

At any time you can save the current and stable state into the drive.

# Then you save into a drive

Let's elaborate about the notions of drive, and containers.

A drive is the highest container of entities in Restlet Client.
A drive can be local, in this case it is generally labelled "My drive".

If you subscribed to a "Team" plan, you can share your own drive. In this case, you are intronised Team owner, and you own also the shared drive.
Every member of your team are called Team members, and get their own copy of the shared drive.

Of course a drive can store requests but also other kind of containers:

* scenario: a scenario is an ordered set of requests.
* service: a service contains either scenarios or requests. The single supported order is by type, then by name.
* project: a project contains either scenarios, services or requests. The single supported order is by type, then by name.

When you save a request from the testing perspective, you can choose an existing container, or create a new one.
