Pluggable authenticators
========================

Introduction
============

The aim of the document is to explain how to support its own challenge
scheme. Let's say you have defined your own challenge scheme called
"MySCHEME". Basically, the server's response to unauthenticated request
will contain a WWW-Authenticate header as follow:

    WWW-Authentication: MySCHEME realm=”<realm>”

Definition of the custom challenge scheme
-----------------------------------------

You will first need to declare your own ChallengeScheme, certainly as a
static member:

    public static ChallengeScheme MySCHEME = new ChallengeScheme("This is my own challenge scheme", "MySCHEME");

Definition of the custom authentication helper
----------------------------------------------

This helper is responsible to cope with server and/or client sides
requirements:

-   generate the right response ("WWW-Authenticate" header) and parse
    the client request ("Authorization" header) on server side,
-   generate the right request ("Authorization" header) and parse of the
    server's response ("WWW-Authenticate" header) on client side.

This helper need to declare that it supports a given challenge scheme,
and need to be registered by the engine.

The registration can be done manually:

    Engine.getInstance().getRegisteredAuthenticators().add(new MyCustomAuthenticationHelper());

or "magically" by creating a service file located in the
"META-INF/services" and called
"org.restlet.engine.security.AuthenticatorHelper". It contains only one
line of text which is the full path of the the helper class.

Then, the helper must declare its support of a ChallengeScheme. This is
done in the constructor:

        public MyCustomAuthenticationHelper() {
            super(ChallengeScheme.CUSTOM, false, true);
        }

The two boolean values correspond to the support of the client and
server sides of the authentication: in the sample code above, this
helper supports only the server side of the challenge scheme.

The support of server side authentication is denoted by the
implementation of the following methods:

-   formatRawRequest(ChallengeWriter, ChallengeRequest, Response,
    Series\<Parameter\>) which is responsible to generate a correct
    "WWW-Authenticate" header by writing to the given instance of
    ChallengeWriter. By default, nothing is done which results in the
    following header:

<!-- -->

    WWW-Authentication: MySCHEME

In order to add the realm, proceed as follow:

        public void formatRawRequest(ChallengeWriter cw, 
                                     ChallengeRequest challenge,
                                     Response response,
                                     Series<Parameter> httpHeaders) throws IOException {
            if (challenge.getRealm() != null) {
                cw.appendQuotedChallengeParameter("realm", challenge.getRealm());
            }
        }

-   parseResponse(ChallengeResponse, Request, Series\<Parameter\>) which
    let the helper complete the given instance of ChallengeResponse (it
    typically parses the ChallengeResponse\#.getRawValue

The support of client side authentication is denoted by the
implementation of the following methods:

-   formatRawResponse(ChallengeWriter, ChallengeResponse, Request,
    Series\<Parameter\>) which generates the right "Authorization"
    header by using the given instance of ChallengeWriter
-   parseRequest(ChallengeRequest, Response, Series\<Parameter\>) which
    parses the response sent by the server. It especially parses the
    ChallengeRequest\#getRawValue.

