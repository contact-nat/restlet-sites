DHC import/export uses JSON of the structure described below.

# Container

A container is an object holding a version of the format and a list of nodes. The current format version is 3. The container is the root JSON object.

<pre><code>
{
    "version" : {number},
    "nodes"   : [{node}, ...]
}
</code></pre>

# Node

A node represents one the following entities:

- a request
- a service
- a project
- a context

Each node has an id which is UUID that never changes. You can rename an entity but the id remains the same.

A node can have a parent node optionally. The parent node is referenced by the id.

e.g. A tree of nodes

<pre><code>
Project #1
        "id"     : "2BC20C27-22F6-4E2C-8627-1FA2932D2A28",
        "type"   : "Project"

    --> Service #1
            "id"     : , "2778CB6D-4EB0-4628-A214-D276876BA494",
            "parent" : "2BC20C27-22F6-4E2C-8627-1FA2932D2A28",
            "type"   : "Service"

        --> Request #1   
                "id"     : , "DA0C7479-2538-4E65-BE2F-E62646A0A1C9",
                "parent" : "2778CB6D-4EB0-4628-A214-D276876BA494",
                "type"   :  "Request"
</code></pre>

Datetime is always encoded in ISO_8601 format. e.g. 2015-03-24T20:29:55.624+01:00

# Request

<pre><code>
{
    "id"           : {string},
    "type"         : "Request",
    "lastModified" : {datetime},

    "name"         : {string},
    "description"  : {string},

    "parent"       : {string},

    "uri"          : {uri},
    "uriEditor"    : {boolean},

    "method"       : {
        "name"        : {string},
        "requestBody" : {boolean},
        "custom"      : {boolean},
        "link"        : {string}
    },

    "headers"      : [
        {
            "name"    : {string},
            "value"   : {string},
            "enabled" : {boolean}
        }, ...
    ],
    "headersType"  : {"Raw"|"Form"},

    "body"         : {
        "bodyType" : {"Text"|"File"|"Form"},

        "textBody" : {string},
        "textBodyEditorHeight" : {number},

        "formBody" : {
            "encoding" : {
                    "application/x-www-form-urlencoded"
                    |"multipart/form-data"
                    },

            "items"    : [
                {
                    "name"    : {string},
                    "value"   : {string},
                    "type"    : {"Text"|"File"},
                    "enabled" : {boolean}
                }, ...
            ],
            "overrideContentType" : {boolean}
        }
    },

    "assertions"   : [ {assertion}, ...]
}

</code></pre>

# Request URI

<pre><code>
{
    "scheme" : {
        "name" : {"http" | "https"},
        "version" : "V11",
        "secure" : {boolean},
    },

    "path"   : {string},

    "query"  : {
        "delimiter" : {string},
        "items"     : [
            {
                "name"    : {string},
                "value"   : {string},
                "enabled" : {boolean}
            }, ...
          ]
    }
}
</code></pre>

# Request Assertion

<pre><code>
{
    "subject"    : {subject},
    "path"       : {string},
    "comparison" : {comparison},
    "value"      : {string}
    "enabled"    : {boolean}
}
</code></pre>

The subject is one of these values:

- Response
- ResponseStatus
- ResponseHeader
- ResponseJsonBody
- ResponseXmlBody
- ResponseBody

The comparison is one these values:

- IsBlank
- IsNotBlank
- Equals
- DoesNotEqual
- Contains
- DoesNotContain
- Less
- LessOrEqual
- Greater
- GreateOrEqual
- Exists
- DoesNotExist

# Project

<pre><code>
{
    "id"           : {string},
    "type"         : "Project",
    "lastModified" : {datetime},

    "parent"       : {string},

    "name"         : {string},
    "description"  : {string},
}
</code></pre>

# Service

<pre><code>
{
    "id"           : {string},
    "type"         : "Service",
    "lastModified" : {datetime},

    "parent"       : {string},

    "name"         : {string},
    "description"  : {string},
}
</code></pre>

# Context

<pre><code>
{
    "id"           : {string},
    "type"         : "Context",
    "lastModified" : {datetime},

    "name"         : {string},

    "variables"    : [
        "name"     : {string},
        "value"    : {string},
        "enabled"  : {boolean}
    ]
}
</code></pre>

# Examples

## First example: Simple GET

Simple ```GET http://www.google.com``` request without headers and without a body. Named as ```Example #1``` and stored at the top level (no project nor a service).

<pre><code>
{
    "version" : 3,
    "nodes"   : [
        {
            "id" : "2778CB6D-4EB0-4628-A214-D276876BA494",
            "lastModified" : "2015-03-28T20:50:19.165+01:00",
            "type" : "Request",
            "name" : "Example #1",
            "method" : {
                "name" : "GET",
                "link" : "http://tools.ietf.org/html/rfc7231#section-4.3.1"
            },
            "uri" : {
                "path"   : "www.google.com",
                "scheme" : {
                    "name"    : "http",
                    "version" : "V11"
                }
            }
        }
    ]
}
</code></pre>
