FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/notification-service-0.0.1-SNAPSHOT.war /app/notification-service.war
EXPOSE 4444
CMD ["java", "-jar", "notification-service.war"]