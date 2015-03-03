# Cell concept
APISpark relies on the concept of cells. A cell is a basic software package that can interact with other imported cells.

# Cell types
From APISpark, you can create two types of cells: Data Stores (Entity Stores or Files Stores) and web APIs.

Web APIs import Data Stores and can be invoked by users.

![Cell concept](images/cell-concept.jpg "Cell concept")

## Web APIs  
An *API* (Application Programming Interface) is an available web business once deployed via HTTP and exposing data.

You can create a [*web API*](/technical-resources/apispark/guide/create/overview "Web API") cell, which will be hosted on the APISpark platform. If you do not need to host your web API, you can create a web API  [*Descriptor*](/technical-resources/apispark/guide/document/overview "Descriptor"), which is an APISpark cell that documents your web API. In order to bring additional features to your existing web API hosted outside of APISpark, you can also create a [*Connector*](/technical-resources/apispark/guide/manage/connectors "Connector") cell.

## Data Stores  
Two types of Data Stores can be created. *Entity Stores* for structured data (equivalent to a database) and *File Stores* for static files (text documents, images, videos or style sheets).

# Cell characteristics

Each cell created is given a unique identifier on APISpark prefixed by its category (APIs or stores), and followed by its version number. Here is the identifier structure:  
`/<category>/<cell identifier>/versions/<version number>/`  
e.g. `https://apispark.restlet.com/apis/3373/versions/1/`  
This identifier will be requested by the APISpark HelpDesk team when assisting you.

> **Note:** You cannot directly invoke a DataStore, you need to first expose it through a web API which imports this store (see [Invoke a web API](/technical-resources/apispark/guide/publish/publish/invocation "Invoke an API")). A web API can invoke several stores.
