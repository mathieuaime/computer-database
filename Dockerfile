FROM tomcat:8.0.20-jre8

COPY target/*RELEASE.war /usr/local/tomcat/webapps/computer-database.war