
FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM tomcat:10-jdk21-temurin

COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]