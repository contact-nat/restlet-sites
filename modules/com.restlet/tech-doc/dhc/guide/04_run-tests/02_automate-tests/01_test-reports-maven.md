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
         &lt;version&gt;1.2.10.2&lt;/version&gt;
    &lt;executions&gt;
           &lt;execution&gt;
             &lt;phase&gt;test&lt;/phase&gt;

             &lt;goals&gt;
               &lt;goal&gt;test&lt;/goal&gt;
             &lt;/goals&gt;
 &lt;configuration&gt;
 &Tab;&Tab;&lt;file&gt;/home/filip/dhc-test-1.json&lt;/file&gt;
    &lt;licenseKey&gt;ENTER_YOUR_LICENSE_KEY_HERE&lt;/licenseKey&gt;
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
   &lt;/pluginRepositories&gt;
&lt;/project&gt;
</code></pre>

>**Note:** Make sure you enter the appropriate version and the appropriate path to the JSON configuration file.

Launch ```> mvn install```.

## In-build API test use case

The following examples will focus on the configuration block which can be found in the previous example.

### Use a specific context

In DHC, the user is able to add variables to a context. If this functionality was used to build the scenario then it is possible to indicate to the ```maven-plugin``` which context should be used.
For instance if a scenario is based on the ```localhost``` context then the following configuration block would be used:

<pre class="language-markup"><code class="language-markup"><configuration>
<file>${project.basedir}/test.json</file>
<context>localhost</context>
</configuration>
</code></pre>

### Override context variables

The user can also override a context variable.

Let's imagine that the API ```port``` is not the same on the test environment as on the development environment, then it is possible to override the context ```port``` variable to provide the right value: ```13337```. This is done by using the following configuration:

<pre class="language-markup"><code class="language-markup"><configuration>
<file>${project.basedir}/test.json</file>
<context>localhost</context>
<variables>
<property>
<name>port</name>
<value>13337</value>
</property>
</variables>
</configuration>
</code></pre>
