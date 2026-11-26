# Multi-stage build for Spring Boot application
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app
COPY gradle gradle
COPY gradlew gradlew.bat ./
COPY settings.gradle.kts ./
COPY paperless-common/ ./paperless-common/
COPY paperless-ocr/ ./paperless-ocr/

# Add empty Project-Folders so the build does not fail
RUN mkdir "paperless-frontend"
RUN mkdir "paperless-rest"
RUN mkdir "paperless-gen-ai"

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Build the application
RUN ./gradlew clean paperless-ocr:build -x test

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

# install ghostscript, tesseract, and language packs (deu,eng,rus)
RUN apt-get update && apt-get install -y --no-install-recommends \
    ghostscript \
    tesseract-ocr \
    tesseract-ocr-deu \
    tesseract-ocr-eng \
    tesseract-ocr-rus \
  && rm -rf /var/lib/apt/lists*

# Copy the JAR from builder
COPY --from=builder /app/paperless-ocr/build/libs/*.jar app.jar

# Expose port (adjust if needed)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -jar app.jar --version 2>/dev/null || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
