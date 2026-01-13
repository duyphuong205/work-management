FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/task-service-0.0.1-SNAPSHOT.war /app/task-service.war
EXPOSE 3333
CMD ["java", "-jar", "task-service.war"]