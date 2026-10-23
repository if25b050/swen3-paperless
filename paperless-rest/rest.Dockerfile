# Multi-stage build for Spring Boot application
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app
COPY gradle gradle
COPY gradlew gradlew.bat ./
COPY settings.gradle.kts ./
COPY paperless-common/ ./paperless-common/
COPY paperless-rest/ ./paperless-rest/

# Add empty Project-Folders so the build does not fail
RUN mkdir "paperless-frontend"


# Build the application
RUN ./gradlew clean paperless-rest:build -x test

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the JAR from builder
COPY --from=builder /app/paperless-rest/build/libs/*.jar app.jar

# Expose port (adjust if needed)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -jar app.jar --version 2>/dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
