FROM openjdk:11-jre-slim
COPY target/cs1632-cicd-docker-1.2.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
