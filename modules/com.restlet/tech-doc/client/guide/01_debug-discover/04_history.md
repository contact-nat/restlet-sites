# Introduction

Let's recall the aim of the discovery mode. This mode lets you focus on a single couple of tasks: setting up requests and sending them.
As said [here](saving), the discovery mode does not record automatically any intermediary versions of the edited request.
What if you want to go back to a previous version? That's where the History enters the game.

History automatically records the requests sent and the related responses.
In another words, history won't help you to recover any state of the edited request. It just record requests that you send which, in a way, is what really matters.

Every time you run a request, from anywhere in the application (either from the discovery mode of the Requests perspective, the Scenarios Perspective, or from a scenario), the request sent and the related response are stored in the so-called "History".
The history is a tab present in the Requests perspective available either on the top left or bottom pane of Restlet Client.
 
 => TODO show a screenshot.

# Browsing the history

 => TODO show a screenshot.

An entry of the history contain both sent request and received response. In addition, it contains also the duration of the request.
Entries are displayed according to the date they have been sent: the younger first, at the top of the list.

In addition, entries are grouped by URL.

# Actions

 (save to drive/req depuis RP ou SP / phrase pour le save to scenario + lien vers le 3.a bootstrap a scenario)

Quelles sont les idées fortes?

c'est un historique de réponses/requêtes

mais ça sert à alimenter le mode discovery

ou à créer/compléter des scenarios avec des requêtes.

L'utilisation typique: dans une session utilisateur, en mode discovery on conçoit des requêtes, on les lance, on affine, et puis en reprenant l'historique, on vient compléter le drive. ou alors on reviens à un point passé dans le mode discovery.





L'historique est en réalité la liste de tous les envois de requêtes et les réponses associées. L'historique est disponible depuis l'onglet en haut à gauche ou dans l'onglet en bas.

En cliquant sur une des requêtes vous basculerez en mode discovery (cf chapitre précédent).

En sélectionnant une ou plusieurs requêtes vous pourrez les sauvegarder dans un scénario.

L'historique concerne les requêtes envoyées soit unitairement, soit depuis un scénario et ce depuis les perspectives Requests et Scenarios.

Cela permet de compléter facilement un scénario depuis le mode discovery, ou de créer un scénario.

