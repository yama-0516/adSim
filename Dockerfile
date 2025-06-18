FROM tomcat:10.1-jdk21-temurin
COPY ROOT.war /usr/local/tomcat/webapps/
EXPOSE 8080