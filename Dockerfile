FROM tomcat:9.0-jdk21-temurin
COPY ROOT.war /usr/local/tomcat/webapps/
EXPOSE 8080