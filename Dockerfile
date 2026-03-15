# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY out/artifacts/URLShortner_jar/URLShortner.jar app.jar
# No compilation needed, already jar built

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/app.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port
EXPOSE 8080

# Run Spring Boot jar
ENTRYPOINT ["java", "-jar", "app.jar"]