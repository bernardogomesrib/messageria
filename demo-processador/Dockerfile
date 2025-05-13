# Use Maven as the builder image
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the container
COPY . /app

# Package the application into a JAR file
RUN mvn clean package -DskipTests


# Set the working directory inside the container


# Expose the application port
EXPOSE 8081

# Run the application

#<artifactId>demo</artifactId>
#<version>0.0.1-SNAPSHOT</version>
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]