Described in Augmented Backus-Naur Form (ABNF).

<pre><code>
expression       = "{" *SP declaration  *SP "}"  / "${" *SP declaration  *SP "}"  
declaration      = ( method  / index / id / string )  *("."  ( id / string / method / index))
method           = id "("  *SP [  method-arguments  ] *SP  ")"
method-arguments =  declaration *SP  *( ","  *SP declaration )
index            = id 1*("[" *SP declaration *SP "]")
id               = (ALPHA  / "_" )  *( ALPHA / DIGIT / "_")
string           = ( """ ( <any except "> / "\""  ) """ )  /  ( "'" ( <any except '> / "\'" ) "'" )
</code></pre>
