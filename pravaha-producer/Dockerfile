# # Stage 1: Build the application
# FROM eclipse-temurin:21-jdk-alpine AS builder
# WORKDIR /app

# # Copy the Maven wrapper and pom.xml first to cache dependencies
# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .

# # Ensure the wrapper is executable and download dependencies
# RUN chmod +x ./mvnw
# RUN ./mvnw dependency:go-offline -B

# # Copy the source code and build the application
# COPY src ./src
# RUN ./mvnw clean package -DskipTests

# Stage 2: Create the lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the builder stage
COPY ./target/*.jar app.jar

EXPOSE 8060
ENTRYPOINT ["java", "-jar", "app.jar"]
