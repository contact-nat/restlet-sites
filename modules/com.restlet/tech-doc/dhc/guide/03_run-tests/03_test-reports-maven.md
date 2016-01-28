<!--# Introduction
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

<pre><code>
POST [url]
Content-Type: application/json
…
{
   “name” : [test name],
   “event” : [BeforeTest|AfterTest],
   “result” : [Ok|Failure|Error] 	<- present only if event=afterTest
}
</code></pre>

## Begin/End notifications

<pre><code>
POST [url]
Content-Type: application/json
…
{
   “event” : [Begin|End]
}
</code></pre>

# Use cases

## Remote API test use case

#### 1. Create a pom.xml file with the following content:

<pre lang="html"><code>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>my-first-api-test</artifactId>
  <version>1.2.3</version>

 <build>
    <plugins>
      <plugin>
        <groupId>com.restlet.dhc</groupId>
        <artifactId>dhc-maven-plugin</artifactId>
        <version>1.1</version>
   <executions>
          <execution>
            <phase>test</phase>

            <goals>
              <goal>test</goal>
            </goals>
<configuration>
		<file>/home/filip/dhc-test-1.json</file>
</configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

<pluginRepositories>
    <pluginRepository>
        <id>nexus-public</id>
        <name>Nexus Release Repository</name>
        <url>http://maven.restlet.com</url>
    </pluginRepository>
</pluginrepositories>
</project>
</code></pre>

>**Note:** Make sure you enter the appropriate version and the appropriate path to the JSON configuration file.

#### 2. Launch > mvn install.

## In-build API test use case

In this example, we added variables and profile.

#### 1. Create a pom.xml file with the following content:

<pre lang="html"><code>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.example</groupId>
	<artifactId>my-first-api-test</artifactId>
	<version>1.2.3</version>
	<name>myProject</name>
...

     <profiles>
         <profile>
             <id>it</id>
             <build>
                 <plugins>
                      ….
                     <plugin>
                         <groupId>com.restlet.dhc</groupId>
                         <artifactId>dhc-maven-plugin</artifactId>
                         <version>1.1</version>
                         <executions>
                             <execution>
                                 <phase>integration-test</phase>

                                 <goals>
                                     <goal>test</goal>
                                 </goals>
                                 <configuration>
                                     <file>${project.basedir}/src/test/resources/assertions.json</file>
                                     <context>localhost</context>
                                     <variables>
                                     	<property>
                                     		<name>port</name>
                                     		<value>13337</value>
                                     	</property>
                                     </variables>
                                 </configuration>
                             </execution>
                         </executions>
                     </plugin>
                 </plugins>
             </build>
         </profile>
     </profiles>
</project>
</code></pre>

#### 2. Launch > mvn clean install -P it
-->

# Introduction
DHC allow you to use exported tests as an input for Maven plugin which generates JUnit like reports and allows you to hook custom URLs where to get notified before or/and after a unit test is completed.
​
# Maven plugin characteristics
​
- GroupId:	com.restlet.dhc  
- ArtifactId:	dhc-maven-plugin
​
## Plugin parameters
​
This Maven plugin comes with a number of parameters.
​
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
​
# Notifications format
​
## Before/After test notifications
​
<pre><code>
POST [url]
Content-Type: application/json
…
{
   “name” : [test name],
   “event” : [BeforeTest|AfterTest],
   “result” : [Ok|Failure|Error] 	<- present only if event=afterTest
}
</code></pre>
​
## Begin/End notifications
​
<pre><code>
POST [url]
Content-Type: application/json
…
{
   “event” : [Begin|End]
}
</code></pre>
​
# Use cases
​
## Remote API test use case
​
#### 1. Create a pom.xml file with the following content:
​
<pre lang="html"><code>
&lt;project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  &lt;modelVersion>4.0.0&lt;/modelVersion>
​
  &lt;groupId>com.example&lt;/groupId>
  &lt;artifactId>my-first-api-test&lt;/artifactId>
  &lt;version>1.2.3&lt;/version>
​
 &lt;build>
    &lt;plugins>
      &lt;plugin>
        &lt;groupId>com.restlet.dhc&lt;/groupId>
        &lt;artifactId>dhc-maven-plugin&lt;/artifactId>
        &lt;version>1.1&lt;/version>
   &lt;executions>
          &lt;execution>
            &lt;phase>test&lt;/phase>
​
            &lt;goals>
              &lt;goal>test&lt;/goal>
            &lt;/goals>
&lt;configuration>
		&lt;file>/home/filip/dhc-test-1.json&lt;/file>
&lt;/configuration>
          &lt;/execution>
        &lt;/executions>
      &lt;/plugin>
    &lt;/plugins>
  &lt;/build>
​
&lt;pluginRepositories>
    &lt;pluginRepository>
        &lt;id>nexus-public&lt;/id>
        &lt;name>Nexus Release Repository&lt;/name>
        &lt;url>http://maven.restlet.com&lt;/url>
    &lt;/pluginRepository>
&lt;/pluginrepositories>
&lt;/project>
</code></pre>
​
>**Note:** Make sure you enter the appropriate version and the appropriate path to the JSON configuration file.
​
#### 2. Launch > mvn install.
​
## In-build API test use case
​
In this example, we added variables and profile.
​
#### 1. Create a pom.xml file with the following content:
​
<pre lang="html"><code>
&lt;project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	&lt;modelVersion>4.0.0&lt;/modelVersion>
	&lt;groupId>com.example&lt;/groupId>
	&lt;artifactId>my-first-api-test&lt;/artifactId>
	&lt;version>1.2.3&lt;/version>
	&lt;name>myProject&lt;/name>
...
​
     &lt;profiles>
         &lt;profile>
             &lt;id>it&lt;/id>
             &lt;build>
                 &lt;plugins>
                      ….
                     &lt;plugin>
                         &lt;groupId>com.restlet.dhc&lt;/groupId>
                         &lt;artifactId>dhc-maven-plugin&lt;/artifactId>
                         &lt;version>1.1&lt;/version>
                         &lt;executions>
                             &lt;execution>
                                 &lt;phase>integration-test&lt;/phase>
​
                                 &lt;goals>
                                     &lt;goal>test&lt;/goal>
                                 &lt;/goals>
                                 &lt;configuration>
                                     &lt;file>${project.basedir}/src/test/resources/assertions.json&lt;/file>
                                     &lt;context>localhost&lt;/context>
                                     &lt;variables>
                                     	&lt;property>
                                     		&lt;name>port&lt;/name>
                                     		&lt;value>13337&lt;/value>
                                     	&lt;/property>
                                     &lt;/variables>
                                 &lt;/configuration>
                             &lt;/execution>
                         &lt;/executions>
                     &lt;/plugin>
                 &lt;/plugins>
             &lt;/build>
         &lt;/profile>
     &lt;/profiles>
&lt;/project>
</code></pre>
​
#### 2. Launch > mvn clean install -P it
