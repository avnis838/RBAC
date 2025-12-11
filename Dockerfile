# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build
# NOTE: Replace the original line with this one.
# This specific tag (or a minor variant like 3.9.8) is known to exist on Docker Hub
# for Temurin-based Maven images.

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run (Keep this the same)
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/RBACUserManagement-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]