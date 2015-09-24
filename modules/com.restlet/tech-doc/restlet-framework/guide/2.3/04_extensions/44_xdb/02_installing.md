First edit into your home directory a file named build.properties with:


```
# Restlet project
# xdb_11.1 specific
sqlnet.string=test
jdbc.username=RESTLET
jdbc.password=RESTLET
jdbc.sysusr=sys
jdbc.syspwd=change_on_install
```

 - __sqlnet.string__ property is a valid Oracle __SQLNet__ connect string to the target database.
 - __jdbc.{username|password}__ are a valid Oracle user name and password to be created for storing Restlet code. Note that in Oracle 11g user name and password are case sensitive by default, so use always uppercase values to work well with the __.sql__ script provided in this installer.
 - __jdbc.{sysusr|syspwd}__ are a valid OracleDBA User account used to create above user schema and other related task only performed by user who have DBA role.

Also is necessary a valid __ORACLE_HOME__ environment variable defined to a valid Oracle 11g home directory, this variable then is used to locate for example the __SQLPlus__ application.

Second create a sub-directory into Restlet __library__ directory named __oracle.xdb_11.1__, put into this new directory these Oracle libraries:


```
$ORACLE_HOME/jdbc/lib/ojdbc5.jar
$ORACLE_HOME/rdbms/jlib/xdb.jar
$ORACLE_HOME/lib/xmlparserv2.jar

```


edit a a file named __library.xml__ into above directory with this content.


```xml
<library id="xdb">
  <package name="oracle.xdb" />
  <version>11.1</version>
  <release>1</release>
  <distributions>
    <distribution id="classic" />
  </distributions>
  <homeUri>
   <a href="http://www.oracle.com/technology/tech/xml/xmldb/index.html">http://www.oracle.com/technology/tech/xml/xmldb/index.html</a>
  </homeUri>
</library>
```


edit another new file named __build.properties__ into above directory with:


```
bin.includes = META-INF/,\
 ojdbc5.jar,\
 xdb.jar,\
 xmlparserv2.jar

```

then edit the file __pom.xml__ into __modules__ directory of Restlet sources addind a new line in section __properties__ with:


```xml
<lib-xdb-version>11.1.1</lib-xdb-version>
```


To compile Restlet sources and generate all Maven artifacts and Restlet libraries go to __build__ directory and execute __ant__ without arguments.
Finally go the directory:


```
# cd build/temp/jee/dist/classic/restlet-jee-2.1snapshot/src/org.restlet.ext.xdb_11.1/resources

```

and execute __Ant__ without argument:

```
# ant

```


To start using XDB Adapter __ant__ without arguments is the only target required in a fresh database install. Other targets are provided during development stage, for example if you update the adapter code only the target __load-server-side-runtime__ is required to re-install it.


** Enabling your __XMLDB HTTP__ support


Oracle databases do not enable by default XMLDB HTTP repository access. To enable it using SQLPlus connected as SYSDBA execute:


```
SQL> exec dbms_xdb.sethttpport(8080);

```

Restart your database and test with a browser or any WebDAV enable file manager a connection to http://localhost:8080/
