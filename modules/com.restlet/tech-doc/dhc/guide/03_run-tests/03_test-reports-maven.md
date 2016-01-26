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
