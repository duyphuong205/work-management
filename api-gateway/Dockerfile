FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/api-gateway-0.0.1-SNAPSHOT.war /app/api-gateway.war
EXPOSE 5555
CMD ["java", "-jar", "api-gateway.war"]