# Introduction
DHC allow you to use exported tests as an input for Maven plugin which generates JUnit like reports and allows you to hook custom URLs where to get notified before or/and after a unit test is completed.

# Maven plugin characteristics

- GroupId:	com.restlet.dhc  
- ArtifactId:	dhc-maven-plugin

## Plugin parameters

This Maven plugin comes with a number of parameters.

| Name | Type | Mandatory | Default | Description
| :---: | :---: | :---: | :---: | :---:
| **file** | File | Yes | X | file path pointing to DHC export file
| **xhrEmulation** | Boolean | No | True | adds headers like accept-*
| **variables** | Properties | No | X | custom variables
| **selectedContext** | String | No | X | a name of a context to be used
| **stopOnFailure** | Boolean | No | True | stops processing build if an error/failure occurs
| **beforeTest** | URL | No | X | URL where to send a notification before a test starts
| **afterTest** | URL | No | X | URL where to send a notification after a test is completed
| **begin** | URL | No | X | URL where to send a notification before first test is executed
| **end** | URL | No | X | URL where to send a notification after last test is completed
| **licenseKey** | String | Yes | X | A valid license key

# Notifications format

## Before/After test notifications

<pre><code class="language-bash">POST [url]
Content-Type: application/json
…
</code><code class="language-json">{
   “name” : [test name],
   “event” : [BeforeTest|AfterTest],
   “result” : [Ok|Failure|Error] 	<- present only if event=afterTest
}
</code></pre>

## Begin/End notifications

<pre><code class="language-bash">POST [url]
Content-Type: application/json
…
</code><code class="language-json">{
   “event” : [Begin|End]
}
</code></pre>

# Use cases

## Remote API test use case

Create a pom.xml file with the following content:

<pre class="language-markup"><code class="language-markup">&lt;project xmlns=&quot;http://maven.apache.org/POM/4.0.0&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xsi:schemaLocation=&quot;http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd&quot;&gt;
  &lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;

  &lt;groupId&gt;com.example&lt;/groupId&gt;
  &lt;artifactId&gt;my-first-api-test&lt;/artifactId&gt;
  &lt;version&gt;1.2.3&lt;/version&gt;

  &lt;build&gt;
     &lt;plugins&gt;
       &lt;plugin&gt;
         &lt;groupId&gt;com.restlet.dhc&lt;/groupId&gt;
         &lt;artifactId&gt;dhc-maven-plugin&lt;/artifactId&gt;
         &lt;version&gt;1.1.0.1&lt;/version&gt;
    &lt;executions&gt;
           &lt;execution&gt;
             &lt;phase&gt;test&lt;/phase&gt;

             &lt;goals&gt;
               &lt;goal&gt;test&lt;/goal&gt;
             &lt;/goals&gt;
 &lt;configuration&gt;
 &Tab;&Tab;&lt;file&gt;/home/filip/dhc-test-1.json&lt;/file&gt;
 &lt;/configuration&gt;
           &lt;/execution&gt;
         &lt;/executions&gt;
       &lt;/plugin&gt;
     &lt;/plugins&gt;
   &lt;/build&gt;

   &lt;pluginRepositories&gt;
       &lt;pluginRepository&gt;
           &lt;id&gt;nexus-public&lt;/id&gt;
           &lt;name&gt;Nexus Release Repository&lt;/name&gt;
           &lt;url&gt;http://maven.restlet.com&lt;/url&gt;
       &lt;/pluginRepository&gt;
   &lt;/pluginrepositories&gt;
&lt;/project&gt;
</code></pre>

>**Note:** Make sure you enter the appropriate version and the appropriate path to the JSON configuration file.

Launch ```> mvn install```.

## In-build API test use case

In this example, we added variables and profile.

Create a pom.xml file with the following content:

<pre class="language-markup"><code class="language-markup">&lt;project xmlns=&quot;http://maven.apache.org/POM/4.0.0&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xsi:schemaLocation=&quot;http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd&quot;&gt;
&Tab;&lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;
&Tab;&lt;groupId&gt;com.example&lt;/groupId&gt;
&Tab;&lt;artifactId&gt;my-first-api-test&lt;/artifactId&gt;
&Tab;&lt;version&gt;1.2.3&lt;/version&gt;
&Tab;&lt;name&gt;myProject&lt;/name&gt;
...

     &lt;profiles&gt;
         &lt;profile&gt;
             &lt;id&gt;it&lt;/id&gt;
             &lt;build&gt;
                 &lt;plugins&gt;
                      &hellip;.
                     &lt;plugin&gt;
                         &lt;groupId&gt;com.restlet.dhc&lt;/groupId&gt;
                         &lt;artifactId&gt;dhc-maven-plugin&lt;/artifactId&gt;
                         &lt;version&gt;1.1.0.1&lt;/version&gt;
                         &lt;executions&gt;
                             &lt;execution&gt;
                                 &lt;phase&gt;integration-test&lt;/phase&gt;

                                 &lt;goals&gt;
                                     &lt;goal&gt;test&lt;/goal&gt;
                                 &lt;/goals&gt;
                                 &lt;configuration&gt;
                                     &lt;file&gt;${project.basedir}/src/test/resources/assertions.json&lt;/file&gt;
                                     &lt;context&gt;localhost&lt;/context&gt;
                                     &lt;variables&gt;
                                     &Tab;&lt;property&gt;
                                     &Tab;&Tab;&lt;name&gt;port&lt;/name&gt;
                                     &Tab;&Tab;&lt;value&gt;13337&lt;/value&gt;
                                     &Tab;&lt;/property&gt;
                                     &lt;/variables&gt;
                                 &lt;/configuration&gt;
                             &lt;/execution&gt;
                         &lt;/executions&gt;
                     &lt;/plugin&gt;
                 &lt;/plugins&gt;
             &lt;/build&gt;
         &lt;/profile&gt;
     &lt;/profiles&gt;
&lt;/project&gt;
</code></pre>

Launch ```> mvn clean install -P it```.
