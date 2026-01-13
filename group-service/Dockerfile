FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/group-service-0.0.1-SNAPSHOT.war /app/group-service.war
EXPOSE 2222
CMD ["java", "-jar", "group-service.war"]