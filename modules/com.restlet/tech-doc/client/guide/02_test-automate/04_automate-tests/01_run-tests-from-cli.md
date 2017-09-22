Now that you know how to test your API with Restlet Client, you probably wonder how you could automate the process.
Restlet has got you covered, all your tests are exportable and runnable from CLI!

Restlet Client allows you to use exported tests as an input for our Maven plugin which generates JUnit-like reports and
allows you to configure custom URLs that will get notified before or/and after a test is completed.

<a class="anchor" name="prerequisites"></a>
## Prerequisites

The only things you will need are to install the latest Java JDK and Maven.
<!-- Should we add pointers to how to do that here ? -->

<a class="anchor" name="get-started-in-seconds"></a>
## Get started in seconds

If you want to run your tests quickly and don't need too much configuration, you can just open the project you want to
test and click on the button `Download project` in the test automation box.

![Download project button](./images/download_project_button.png)

You can now download the auto-generated pom and the project.

![Download the project and pom](./images/download_project_and_pom.png)

Put them in the same folder, open a terminal in this folder and launch `mvn clean test -DlicenseKey=<your license key>`.

<a class="anchor" name="find-your-license-key"></a>
### Find your license key

You can find the license key in your account page.

![License key location](./images/license_key_location.png)

<a class="anchor" name="get-full-control-over-your-tests"></a>
## Get full control over your tests

<a class="anchor" name="hand-pick-the-requests-to-run"></a>
### Hand-pick the requests to run

The maven plugin takes a JSON configuration file as a parameter and runs it. That configuration file really is just an
export of your repository or a subset of it.

If you want to run a specific set of requests in your test, you can use the export feature and check the boxes that
match the requests/scenarios etc... you want to run.

![Export your requests](./images/export_repository.png)

Place the downloaded JSON in the same folder as the pom.xml and make sure to configure the pom.xml to
use that JSON file (more information on that below).

<a class="anchor" name="create-your-custom-test-configuration"></a>
### Create your custom test configuration

If you want to benefit from the webhooks and/or customize the tests run more, you can configure the pom yourself.

Here is an example pom.xml, we will detail all the configurable options below.

<pre class="language-xml">
  <code class="language-xml">
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>my-star-wars-api-test</artifactId>
  <version>1.2.3</version>

  <build>
    <plugins>
      <plugin>
        <!-- The Restlet maven plugin -->
        <groupId>com.restlet.client</groupId>
        <artifactId>maven-plugin</artifactId>
        <version>2.10.0</version>
        <executions>
          <execution>
            <phase>test</phase>
            <goals>
              <goal>test</goal>
            </goals>

            <!-- The configuration goes there -->
            <configuration>
              <file>/path/to/json/configuration/file.json</file>
              <licenseKey>${myLicenseKey}</licenseKey>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <!-- The repository were the Restlet maven plugin is hosted -->
  <pluginRepositories>
    <pluginRepository>
      <id>nexus-public</id>
      <name>Nexus Release Repository</name>
      <url>http://maven.restlet.com</url>
    </pluginRepository>
  </pluginRepositories>
</project>
  </code>
</pre>

<a class="anchor" name="pom-configuration"></a>
### Pom configuration

The following attributes can be added to the pom to tweak the plugin's behavior.
They can be added two different ways:
* in the configuration tag of the pom, see example above with the attribute file
* passed to the plugin via JVM arguments. If the attribute is defined with a value `${attributeName}` in the pom, then
you can pass it a value when running the mvn command via a JVM plugin. In the case of the licenseKey in the pom above,
you can pass it like so: `mvn clean test -DmyLicenseKey=X25RyICNyBnZRxvGsf9XLZYVHGj168xTy/EApfWHt8qnMTfarfLudZzFik`

> Note: You should privilege the second option for sensitive information like the license key so that it never gets
committed along with the pom to a public place by mistake.

<a class="anchor" name="pom-attributes"></a>
### Pom attributes

Here is the full list of available customization options.

| Name | Type | Mandatory | Default | Description
| ---- | ---- | --------- | ------- | -----------
| **file** | File | Yes | X | File path pointing to the Restlet Client export file from the pom's location
| **selectedEnvironment** | String | No | X | The name of the environment to be used (make sure it was exported to the JSON file). Learn more about environments [here](../environments)
| **licenseKey** | String | Yes | X | A valid license key (see [Find your license key](#find-your-license-key))
| **stopOnFailure** | boolean | No | false | Stops processing build if an error/failure occurs
| **httpClientTimeoutInMs** | Integer | false | 60000 | Time before HTTP time-out in milliseconds
| **variables** | Properties | No | X | Custom variables see [Custom variables](#custom-variables)
| **xhrEmulation** | boolean | No | true | Mimics the Chrome extension behaviour by adding the headers the browser automatically adds like accept-*
| **followRedirects** | String | No | NONE | Indicates whether or not the Maven plugin should follow the redirections as it is possible through the Chrome extension settings in the browser. Possible values are *NONE* or *ALL*
| **beforeTest** | URL | No | X | URL where to send a notification before a test starts
| **afterTest** | URL | No | X | URL where to send a notification after a test is completed
| **begin** | URL | No | X | URL where to send a notification before the first test is executed
| **end** | URL | No | X | URL where to send a notification after the last test is executed
| **useMavenProxies** | boolean | No | false | Indicates whether the proxy configured in ~/.m2/settings.xml should be used for your test's HTTP calls or not

See the full configuration example below:

<pre class="language-xml">
  <code class="language-xml">
<configuration>
    <file>/path/to/json/configuration/file.json</file>
    <selectedEnvironment>QA</selectedEnvironment>
    <licenseKey>${myLicenseKey}</licenseKey>
    <stopOnFailure>true</stopOnFailure>
    <httpClientTimeoutInMs>30000</httpClientTimeoutInMs>
    <xhrEmulation>false</xhrEmulation>
    <followRedirects>ALL</followRedirects>
    <beforeTest>https://my-ci-api.com/api1/notifications</beforeTest>
    <afterTest>https://my-ci-api.com/api1/notifications</afterTest>
</configuration>
  </code>
</pre>

<a class="anchor" name="override-environment-variables"></a>
### Override environment variables

You can override an environment variable by adding a `variables` tag in the plugin's configuration.

Let's imagine that the API port is not the same on the test environment as on the development environment,
then it is possible to override the environment port variable to provide the right value: 13337.

This can be done by using the following configuration:

<pre class="language-xml">
  <code class="language-xml">
<configuration>
    <file>test.json</file>
    <selectedEnvironment>localhost</selectedEnvironment>
    <variables>
        <!-- The variable port's value will be overwritten to 13337 -->
        <property>
            <name>port</name>
            <value>13337</value>
        </property>
    </variables>
</configuration>
  </code>
</pre>

<a class="anchor" name="use-a-proxy"></a>
### Use a proxy

Your API may be accessible only through a proxy. In this case, Restlet provides you with simple ways to configure a
proxy for all HTTP calls made by our maven plugin.

<a class="anchor" name="use-the-system-proxy-configuration"></a>
#### Use the system proxy configuration

If the proxy is configured system-wide on the test computer, then just launch the plugin with a simple:

<pre class="language-markup"><code class="language-markup">mvn test -Djava.net.useSystemProxies=true</code></pre>

Please note that this argument is available **on recent Windows systems and on Gnome 2.x systems**,
for more information refer to the official
[Oracle documentation](https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html).

<a class="anchor" name="use-the-maven-proxy-configuration"></a>
#### Use the maven proxy configuration

The Maven plugin can reuse the Maven's installation proxy configuration, **defined in your settings.xml**, simply
by using the following configuration:

<pre class="language-markup"><code class="language-markup">
&lt;configuration&gt;
  &lt;file&gt;/test.json&lt;/file&gt;
  &lt;useMavenProxies&gt;true&lt;/useMavenProxies&gt;
&lt;/configuration&gt;
</code>
</pre>

**Please note that for now, the nonProxyHosts attribute is not supported.**

More information about Maven proxy configuration can be found on the dedicated
[documentation](https://maven.apache.org/guides/mini/guide-proxies.html).

<a class="anchor" name="jvm-proxy-configuration"></a>
#### JVM proxy configuration

The proxy configuration can also be configured at runtime using the standard JVM arguments.
For the official documentation on JVM networking arguments, please refer to
[Oracle Networking Properties](https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html).

Beware that JVM proxy configuration will be applied to *all* the JVM which will impact all the requests performed even
by other Maven plugins and/or targets.

<a class="anchor" name="http-proxy-configuration"></a>
##### HTTP proxy configuration

If your proxy is using the `http` protocol then the following arguments are available:

| Name | Default | Description
| ---- | ------- | -----------
| **http.proxyHost** | "" | The hostname, or address, of the proxy server
| **http.proxyPort** | 80 | The port number of the proxy server
| **http.proxyUser** | "" | If the proxy used basic authentication, the authentication's username
| **http.proxyPassword** | "" | If the proxy used basic authentication, the authentication's password
| **http.nonProxyHosts** | localhost&#124;127.*&#124;[::1] | **[Not supported]** Indicates the hosts that should be accessed without going through the proxy

Given the previous arguments, the maven plugin can be used:

<pre class="language-markup"><code class="language-markup">mvn test -Dhttp.proxyHost=192.168.1.127</code></pre>

<a class="anchor" name="https-proxy-configuration"></a>
##### HTTPS proxy configuration

If your proxy is using the `https` protocol then the following arguments are available:

| Name | Default | Description
| ---- | ------- | -----------
| **https.proxyHost** | "" | The hostname, or address, of the proxy server
| **https.proxyPort** | 443 | The port number of the proxy server
| **http.proxyUser** | "" | If the proxy used basic authentication, the authentication's username
| **http.proxyPassword** | "" | If the proxy used basic authentication, the authentication's password
| **http.nonProxyHosts** | localhost&#124;127.*&#124;[::1] | **[Not supported]** Indicates the hosts that should be accessed without going through the proxy

Given the previous arguments, the maven plugin can be used:

<pre class="language-markup"><code class="language-markup">mvn test -Dhttps.proxyHost=192.168.1.127</code></pre>

<a class="anchor" name="notifications-format"></a>
## Notifications format

<a class="anchor" name="before-after-test-notifications"></a>
### Before/After test notifications

<pre>
  <code class="language-json">
<!-- The URL you specified in the configuration in beforeTest/afterTest-->
POST [url]
Content-Type: application/json
Body:
{
   "name": [test name],
   "event": [BeforeTest|AfterTest],
   "result": [Ok|Failure|Error] &Tab;&lt;- present only if event=afterTest
}
  </code>
</pre>

<a class="anchor" name="begin-end-notifications"></a>
### Begin/End notifications

<pre>
  <code class="language-json">
<!-- The URL you specified in the configuration in begin/end -->
POST [url]
Content-Type: application/json
Body:
{
   "event": [Begin|End]
}
  </code>
</pre>
