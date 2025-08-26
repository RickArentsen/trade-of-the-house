FROM ubuntu:latest
LABEL authors="ricka"
# Use OpenJDK 23 as a base image
FROM openjdk:23-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (for better caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/trade-of-the-house-0.0.1-SNAPSHOT.jar"]