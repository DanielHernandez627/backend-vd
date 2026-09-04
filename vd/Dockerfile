# Stage 1: Build Spring Boot JAR with Gradle
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy project files
COPY . .

# Grant execution permissions to gradlew and build bootJar
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

# Stage 2: Runtime JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built JAR from stage 1
COPY --from=build /app/build/libs/*.jar app.jar

# Expose HTTP port for Spring Boot API
EXPOSE 8080

# Run Spring Boot Application
ENTRYPOINT ["java", "-jar", "app.jar"]
