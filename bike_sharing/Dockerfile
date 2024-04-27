# Use the official Maven image as a parent image
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory to /app
WORKDIR /app

# Copy the entire project directory into the container
COPY . /app

# Build the Maven project and create a JAR file
RUN mvn clean package

# Use the OpenJDK image as a base image for the application
FROM openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the Maven build stage to the container
COPY --from=build /app/target/bike_sharing-*.jar /app/bike-sharing.jar

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Define the default command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "bike-sharing.jar"]