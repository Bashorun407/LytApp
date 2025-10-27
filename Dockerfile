
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

# Make Maven wrapper executable and build
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/lytpay-1.0.0.jar"]