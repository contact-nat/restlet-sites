Log service
===========

Introduction
============

The log service provides access to logging service. The implementation
is fully based on the standard logging mechanism introduced in JDK 1.4.

Being able to properly log the activity of a Web application is a common
requirement. Restlet Components know by default how to generate
Apache-like logs or even custom ones. By taking advantage of the logging
facility built in the JDK, the logger can be configured like any
standard JDK log to filter messages, reformat them or specify where to
send them. Rotation of logs is also supported; see the
[java.util.logging](http://java.sun.com/j2se/1.5.0/docs/api/java/util/logging/package-summary.html)
package for details.

Note that you can customize the logger name given to the
java.util.logging framework by modifying the Component's "logService"
property. In order to fully configure the logging, you need to declare a
configuration file by setting a system property like:

    System.setProperty("java.util.logging.config.file", "/your/path/logging.config");  

For details on the configuration file format, please check the [JDK's
LogManager](http://java.sun.com/j2se/1.5.0/docs/api/index.html?java/util/logging/LogManager.html)
class. You can also have a look at the [Restlet ${restlet-version-minor} logging
documentation](../../editions/jse/logging.md).

Default access log format
=========================

The default format follows the [W3C Extended Log File
Format](http://www.w3.org/TR/WD-logfile.html)
with the following fields used:

1.  Date (YYYY-MM-DD)
2.  Time (HH:MM:SS)
3.  Client address (IP)
4.  Remote user identifier (see RFC 1413)
5.  Server address (IP)
6.  Server port
7.  Method (GET|POST|...)
8.  Resource reference path (including the leading slash)
9.  Resource reference query (excluding the leading question mark)
10. Response status code
11. Number of bytes sent
12. Number of bytes received
13. Time to serve the request (in milliseconds)
14. Host reference
15. Client agent name
16. Referrer reference

If you use
[Analog](http://www.analog.cx/)
to generate your log reports, and if you use the default log format,
then you can simply specify this string as a value of the LOGFORMAT
command:
(%Y-%m-%d\\t%h:%n:%j\\t%S\\t%u\\t%j\\t%j\\t%j\\t%r\\t%q\\t%c\\t%b\\t%j\\t%T\\t%v\\t%B\\t%f)

For custom access log format, see the syntax to use and the list of
available variable names in
[Template](javadocs://jse/api/org/restlet/util/Resolver.html#createResolver%28org.restlet.data.Request,%20org.restlet.data.Response%29).

