# Introduction

Restlet Client allows you to use exported tests as an input for Maven plugin which generates JUnit-like reports and allows you to hook custom URLs where to get notified before or/and after a unit test is completed.

# <a class="anchor" name="getStarted"></a>Get started in seconds

Open the project, service or scenario you want to test and click on the `Export to maven` button.

All you have to do then is download the full package, extract it and run `mvn test` in the extracted folder.

# More configuration options

## Choose the requests to run

The maven plugin takes a JSON configuration file as a parameter and runs it. That configuration file really is just an export of your repository or a subset of it.

If you want to run a specific set of tests, you can click on `export` on the bottom of the left panel of the `Requests` view, tab `Repository` and check the boxes that match the requests/scenarios etc... you want to run.

Place the downloaded JSON near the pom.xml you downloaded (see [get started](#getStarted)) and make sure to configure the pom.xml to use that JSON file (more information on that below).

## POM example

<pre class="language-markup"><code class="language-markup">&lt;project xmlns=&quot;http://maven.apache.org/POM/4.0.0&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xsi:schemaLocation=&quot;http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd&quot;&gt;
  &lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;

  &lt;groupId&gt;com.example&lt;/groupId&gt;
  &lt;artifactId&gt;my-first-api-test&lt;/artifactId&gt;
  &lt;version&gt;1.2.3&lt;/version&gt;

  &lt;build&gt;
    &lt;plugins&gt;
      &lt;plugin&gt;
        &lt;groupId&gt;com.restlet.client&lt;/groupId&gt;
        &lt;artifactId&gt;maven-plugin&lt;/artifactId&gt;
        &lt;version&gt;2.2.1&lt;/version&gt;
        &lt;executions&gt;
          &lt;execution&gt;
            &lt;phase&gt;test&lt;/phase&gt;

            &lt;goals&gt;
              &lt;goal&gt;test&lt;/goal&gt;
            &lt;/goals&gt;
            &lt;configuration&gt;
              &lt;file&gt;/path/to/json/configuration/file.json&lt;/file&gt;
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
</code>
</pre>

## Plugin configuration

This Maven plugin comes with a number of parameters to tweak its behavior.


| Name | Type | Mandatory | Default | Description
| ---- | ---- | --------- | ------- | -----------
| **file** | File | Yes | X | file path pointing to the Restlet Client export file
| **selectedEnvironment** | String | No | X | a name of an environment to be used
| **licenseKey** | String | Yes | X | A valid license key (see the "Get a license key" paragraph below)
| **stopOnFailure** | Boolean | No | False | stops processing build if an error/failure occurs
| **httpClientTimeoutInMs** | Integer | False | 60000 | Time before HTTP time-out in milliseconds
| **variables** | Properties | No | X | custom variables
| **xhrEmulation** | Boolean | No | True | adds headers like accept-*
| **beforeTest** | URL | No | X | URL where to send a notification before a test starts
| **afterTest** | URL | No | X | URL where to send a notification after a test is completed
| **begin** | URL | No | X | URL where to send a notification before first test is executed
| **end** | URL | No | X | URL where to send a notification after last test is completed


To change them, modify the tag `configuration` in your pom.xml, see a configuration example below.

<pre class="language-markup"><code class="language-markup">&lt;configuration&gt;
  &lt;file&gt;/path/to/json/configuration/file.json&lt;/file&gt;
  &lt;selectedEnvironment&gt;QA&lt;/selectedEnvironment&gt;
  &lt;licenseKey&gt;ENTER_YOUR_LICENSE_KEY_HERE&lt;/licenseKey&gt;
  &lt;stopOnFailure&gt;true&lt;/stopOnFailure&gt;
  &lt;httpClientTimeoutInMs&gt;30000&lt;/httpClientTimeoutInMs&gt;
  &lt;xhrEmulation&gt;false&lt;/xhrEmulation&gt;
  &lt;beforeTest&gt;https://my-ci-api.com/api1/notifications&lt;/beforeTest&gt;
  &lt;afterTest&gt;https://my-ci-api.com/api1/notifications&lt;/afterTest&gt;
&lt;/configuration&gt;
</code>
</pre>

## <a class="anchor" name="getLicenseKey"></a>Get a license key
At start, the Restlet Client Maven plugin checks whether the user has recorded a license key. In case the license is missing or invalid, the user is asked to get one and configure it.
Such license key is granted to users that [purchase a team plan or above](../../get-started/subscribe), and can be found in the Billing page.

Just copy/paste this value as the `licenseKey` parameter of the pom.xml file:

![Get license key](images/maven_plugin_get_license_key.png "Get license key")

## In-build API test use case

The following examples will focus on the configuration block which can be found in the previous example.

### Use a specific environment

In Restlet Client, a consistent set of variables is called an "environment". The user is able to setup several environments (for example, one for the production environment, one for the local environment, etc). If this functionality was used to build the scenario then it is possible to indicate to the `maven-plugin` which environment should be used.

For instance if a scenario is based on the `localhost` environment then the following configuration block would be used:

<pre class="language-markup"><code class="language-markup">&lt;configuration&gt;
  &lt;file&gt;${project.basedir}/test.json&lt;/file&gt;
  &lt;selectedEnvironment&gt;localhost&lt;/selectedEnvironment&gt;
&lt;/configuration&gt;
</code>
</pre>

### Override environment variables

The user can also override an environment variable.

Let's imagine that the API `port` is not the same on the test environment as on the development environment, then it is possible to override the environment `port` variable to provide the right value: `13337`.

This is done by using the following configuration:

<pre class="language-markup"><code class="language-markup">&lt;configuration&gt;
  &lt;file&gt;${project.basedir}/test.json&lt;/file&gt;
  &lt;selectedEnvironment&gt;localhost&lt;/selectedEnvironment&gt;
  &lt;variables&gt;
    &lt;property&gt;
      &lt;name&gt;port&lt;/name&gt;
      &lt;value&gt;13337&lt;/value&gt;
    &lt;/property&gt;
  &lt;/variables&gt;
&lt;/configuration&gt;
</code>
</pre>

# Notifications format

## Before/After test notifications

<pre>
  <code class="language-bash">
POST [url]
Content-Type: application/json
&hellip;
  </code>
  <code class="language-json">
{
   &ldquo;name&CloseCurlyDoubleQuote; : [test name],
   &ldquo;event&CloseCurlyDoubleQuote; : [BeforeTest|AfterTest],
   &ldquo;result&CloseCurlyDoubleQuote; : [Ok|Failure|Error] &Tab;&lt;- present only if event=afterTest
}
  </code>
</pre>

## Begin/End notifications

<pre>
  <code class="language-bash">
POST [url]
Content-Type: application/json
&hellip;
  </code>
  <code class="language-json">
{
   &ldquo;event&CloseCurlyDoubleQuote; : [Begin|End]
}
  </code>
</pre>
