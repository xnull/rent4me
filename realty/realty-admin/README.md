spring-mvc-java8-web-app-template
=================================

Template project for web-application development using Spring MVC and Java

Starter template project for thos who want to start experimenting with Java 8 & Spring MVC & Hibernate & SSP(Scala Server pages).

You need to have Java 8+ installed on your PC in order to compile example.

Project uses Apache Maven as build and dependency management tool.

It has 4 modules:
1. common - module with common information
2. data - module with Data Access Layer & Service Layer
3. web - module that will be deployed into tomcat
4. web-app-launcher - is a helper module that's used for using ide-based Launcher (see Launcher class).

In order to create war execute following command: mvn -P=build clean package

P.S. I prefer to use IntelliJ IDEA IDE - simply import parent project as maven project(select parent pom.xml).
For Eclipse/netbeans users: In project web-app-launcher, class name.dargiri.Launcher, change value of static variable:
"MULTI_MODULE_DEFAULT_PATH" to full path to "web" module.
