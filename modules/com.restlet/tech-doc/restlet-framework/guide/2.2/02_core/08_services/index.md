Service package
===============

Introduction
============

The **org.restlet.service** package contains services used by
applications and components. This chapter lists the services hosted by
default by Component and Application instances.

Component services
==================

Here is the list of services hosted by default by an instance of
Component:

-   [Log service](log "Log service"): provide access to logging service.
-   [Status service](status "Status service"): provide common representations for exception status.

Application services
====================

Here is the list of services hosted by default by an instance of
Application :

-   [Connector service](connector "Connector service"): declare necessary client and server connectors.
-   [Decoder service](decoder "Decoder service"): automatically decode or decompress request entities.
-   [Metadata service](metadata "Metadata service"): provide access to metadata and their associated extension names.
-   [Range service](range "Range service"): automatically exposes ranges of response entities.
-   [Status service](status "Status service"): provide common representations for exception status.
-   [Task service](task "Task service"): run tasks asynchronously.
-   [Tunnel service](tunnel "Tunnel service"): tunnel method names or client preferences via query parameters.

