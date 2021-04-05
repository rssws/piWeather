FROM openjdk:15-jdk-alpine

WORKDIR /app

EXPOSE 31415

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
