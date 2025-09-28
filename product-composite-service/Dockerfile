FROM eclipse-temurin:17.0.5_8-jre-focal

WORKDIR /app

# Copy the executable JAR
COPY ./build/libs/product-composite-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]