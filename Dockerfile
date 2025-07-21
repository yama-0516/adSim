
FROM gradle:jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean war -x test

FROM tomcat:11-jdk21-temurin

COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]