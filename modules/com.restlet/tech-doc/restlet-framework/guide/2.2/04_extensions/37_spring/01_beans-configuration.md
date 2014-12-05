Spring extension - Configuring Restlet beans
============================================

Passing the parent context
==========================

One frequent issue that developers encounter when configuring their
Restlet beans with Spring XML is that it is not easy to find a way to
pass the Context instance to the Restlet subclasses such as Application,
Directory or Router. What we actually need to do is to extract the
context property from the parent Restlet (typically a Component or an
Application) and pass it by reference to the constructor method.

Spring provides two mechanism to achieve this: either using the
PropertyPathFactoryBean class to create a context bean such as:

~~~~ {.brush: .java}
<!-- Restlet Component bean -->
<bean id="component" class="org.restlet.ext.spring.SpringComponent">
    ...
</bean>

<!-- Component's Context bean -->
<bean id="component.context" class="org.springframework.beans.factory.config.PropertyPathFactoryBean"/>

<!-- Application bean -->
<bean id="application" class="org.restlet.Application">
    <constructor-arg ref="component.context" />
    ...
</bean>
~~~~

The second mechanism is based on the Spring utilities schema and is
actually more compact:

~~~~ {.brush: .java}
<!-- Restlet Component bean -->
<bean id="component" class="org.restlet.ext.spring.SpringComponent">
    ...
</bean>

<!-- Application bean -->
<bean id="application" class="org.restlet.Application">
    <constructor-arg>
        <util:property-path path="component.context" />
    </constructor-arg>
    ...
</bean>
~~~~

You also have to make sure that the util namespace is properly declared
in your XML configuration header. Here is a snippet for Spring 2.5:

~~~~ {.brush: .java}
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.5.xsd">

<!-- Add you <bean/> definitions here -->

</beans>
~~~~

This utilities mechanism is quite powerful and flexible, for more
information [check this
page](http://static.springframework.org/spring/docs/2.5.x/reference/xsd-config.html#xsd-config-body-schemas-util-property-path).

