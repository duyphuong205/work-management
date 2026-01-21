FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/project-service-0.0.1-SNAPSHOT.war /app/project-service.war
EXPOSE 2222
CMD ["java", "-jar", "project-service.war"]