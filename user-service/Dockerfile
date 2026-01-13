FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.war /app/user-service.war
EXPOSE 1111
CMD ["java", "-jar", "user-service.war"]