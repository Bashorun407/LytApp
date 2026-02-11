# ----------------------------
# Stage 1: Build the Application
# ----------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies offline (speeds up rebuilds)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# ----------------------------
# Stage 2: Runtime
# ----------------------------
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Run the application with optimized JVM settings for containers
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]
