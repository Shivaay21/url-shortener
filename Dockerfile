# ===== Stage 1: Build the JAR inside Docker =====
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ===== Stage 2: Run the JAR =====
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the JAR from previous stage
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-it.sh .
COPY start.sh .

RUN chmod +x wait-for-it.sh start.sh

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["./start.sh"]