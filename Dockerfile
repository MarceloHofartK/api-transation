FROM openjdk:17-jdk-slim

COPY target/bank-api-0.0.1-SNAPSHOT.jar /app/bank-api.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/bank-api.jar"]