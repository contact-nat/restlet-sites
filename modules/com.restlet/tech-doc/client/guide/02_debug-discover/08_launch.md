Restlet Client allows you to insert a button in your website that will launch your request directly in Restlet Client.

<a class="anchor" name="instruct-restlet-client-with-a-meta-tag"></a>
## Instruct Restlet Client with a meta tag

Create an html page that contains a **meta tag** that will load Restlet Client script.

<pre class="language-markup"><code class="language-markup">&lt;meta
name=&quot;dhc-aware-page&quot;
data-indicator-element-id=&quot;&lt;id_element&gt;&quot;
data-console-debug=&quot;true&quot;
&gt;
</code></pre>

- **name** - required parameter, value should always be *dhc-aware-page*.
- **data-indicator-element-id** - optional, specifies the id of element Restlet Client will create to let the page know the availability of Restlet Client API. Default value is *dhcInfo*.
- **data-console-debug** - optional parameter, if contains true, will print debug information into browser javascript console. Default value is *false*.

<a class="anchor" name="call-window-postmessage-script"></a>
## Call window.postMessage script

To instruct Restlet Client to open a new request, your webpage must call the **window.postMessage** script with the following message object (all properties are required):

<pre class="language-javascript"><code class="language-javascript">{
      target : &quot;aejoelaoggembcahagimdiliamlcdmfm&quot;,
      type : &quot;openRequest&quot;,
      targetTab: &quot;&lt;name_of_target_tab&gt;&quot;,
      payloadType: &quot;&lt;type_of_payload&gt;&quot;,
      payload : &quot;&lt;payload&gt;&quot;
}
</code></pre>

- **target**:String - id of Restlet Client extension. Can be obtained from Restlet Client indicator element. E.g.
JSON.parse(document.getElementById(<id of  indicator element>).textContent).extensionId;
- **targetTab**:String (optional) - the name of the tab where request should be opened. If not specified, new tab with Restlet Client will be opened for each *openRequest* message. If specified, Restlet Client will try to use previously opened tab with the same  name and will create it if such tab does not exist.
- **payloadType**:String - the type of payload to load in Restlet Client. Possible values: *dhc* or *systinet*.
- **payload**:Object - payload information, specific to each payload type.

Example:

<pre class="language-javascript"><code class="language-javascript">{
    "name": "Post example on HTTP Bin",
    "headers": [
        {
            "enabled": true,
            "name": "Content-Type",
            "value": "application/json"
        }
    ],
    "type": "Request",
    "method": {
        "requestBody": true,
        "link": "",
        "name": "POST"
    },
    "body": {
        "autoSetLength": true,
        "textBody": "{\n  \"lastname\": \"Eastwood\",\n  \"firstname\": \"Clint\"\n}",
        "bodyType": "Text"
    },
    "headersType": "Form",
    "uri": {
        "path": "httpbin.org/post",
        "query": {
            "delimiter": "&",
            "items": [
                {
                    "enabled": true,
                    "name": "details",
                    "value": "full"
                }
            ]
        },
        "scheme": {
            "secure": true,
            "name": "https",
            "version": "V11"
        }
    }
}
</code></pre>

<a class="anchor" name="use-case"></a>
## Use case

Create a webpage that contains an input area pre-filled with your payload and a button to launch your request with Restlet Client in a new tab.

<pre class="language-javascript"><code class="language-javascript">&lt;!DOCTYPE html&gt;
&lt;html lang=&quot;en&quot;&gt;
&lt;head&gt;

    &lt;meta charset=&quot;UTF-8&quot;&gt;

    &lt;!--This tag instructs Restlet Client to provide api to this page--&gt;
    &lt;meta name=&quot;dhc-aware-page&quot;
          data-console-debug=&quot;true&quot;
          data-indicator-element-id=&quot;testClient&quot;&gt;

    &lt;title&gt;Client integration example&lt;/title&gt;
    &lt;style type=&quot;text/css&quot;&gt;
        input{
            width:200pt
        }
    &lt;/style&gt;
&lt;/head&gt;
&lt;script type=&quot;text/javascript&quot;&gt;

    //short hand function for sending messages to restlet-client
    function openRequestInClient(clientInfo,targetTab,payload){
        var message = {
            target:clientInfo.extensionId, //required
            type:&quot;openRequest&quot;,//required
            payloadType:&quot;dhc&quot;,//required,
            targetTab:targetTab,
            payload:JSON.parse(payload)
        };
        window.postMessage(message, document.origin);
    }

    //wait until document loads so Restlet Client has a chance to create info tag
    window.addEventListener(&quot;load&quot;,function(){
        var clientTag = document.getElementById(&quot;testClient&quot;); //lookup the tag using the id we&apos;ve provided in meta tag
        var clientStatusDisplay = document.getElementById(&quot;status&quot;);

        if(!clientTag){
            //no indicator tag - no restlet-client
            clientStatusDisplay.textContent = &quot;Restlet Client is not installed. Install Restlet Client by going to the &quot; +
                    &quot;chrome://extensions click on &apos;Enable Developer Mode&apos; then load unpacked extension, go back here and refresh the page&quot;;
            return;
        }
        //parse restlet-client info into json
        var clientInfo = JSON.parse(clientTag.textContent);
        clientStatusDisplay.textContent = &quot;Restlet Client is installed, click button below to open request in Restlet Client&quot;;

        //prepare ui

        var payload = document.createElement(&quot;textarea&quot;);
        payload.value = JSON.stringify({
            &quot;name&quot;: &quot;Post example on HTTP Bin&quot;,
            &quot;headers&quot;: [
                {
                    &quot;enabled&quot;: true,
                    &quot;name&quot;: &quot;Content-Type&quot;,
                    &quot;value&quot;: &quot;application/json&quot;
                }
            ],
            &quot;type&quot;: &quot;Request&quot;,
            &quot;method&quot;: {
                &quot;requestBody&quot;: true,
                &quot;link&quot;: &quot;&quot;,
                &quot;name&quot;: &quot;POST&quot;
            },
            &quot;body&quot;: {
                &quot;autoSetLength&quot;: true,
                &quot;textBody&quot;: &quot;{\n  \&quot;lastname\&quot;: \&quot;Eastwood\&quot;,\n  \&quot;firstname\&quot;: \&quot;Clint\&quot;\n}&quot;,
                &quot;bodyType&quot;: &quot;Text&quot;
            },
            &quot;headersType&quot;: &quot;Form&quot;,
            &quot;uri&quot;: {
                &quot;path&quot;: &quot;httpbin.org/post&quot;,
                &quot;query&quot;: {
                    &quot;delimiter&quot;: &quot;&&quot;,
                    &quot;items&quot;: [
                        {
                            &quot;enabled&quot;: true,
                            &quot;name&quot;: &quot;details&quot;,
                            &quot;value&quot;: &quot;full&quot;
                        }
                    ]
                },
                &quot;scheme&quot;: {
                    &quot;secure&quot;: true,
                    &quot;name&quot;: &quot;https&quot;,
                    &quot;version&quot;: &quot;V11&quot;
                }
            }
        },null,&quot;\t&quot;);
        var send = document.createElement(&quot;button&quot;);
        send.textContent = &quot;Open Request&quot;;


        document.body.appendChild(payload);
        document.body.appendChild(send);
        send.addEventListener(&quot;click&quot;,function(){
            openRequestInClient(clientInfo,null,payload.value);
        });



    });
&lt;/script&gt;
&lt;body&gt;


&lt;div id=&quot;status&quot;&gt;Checking Client Status&lt;/div&gt;

&lt;/body&gt;
&lt;/html&gt;
</code></pre>
