# Use an official OpenJDK 21 base image
FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the current directory to the container's /app directory
COPY target/eureka-server-0.0.1-SNAPSHOT.jar /app/eureka-server.jar

# Expose the port your application will run on (adjust as necessary)
EXPOSE 8761

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]
