# Paperless

Paperless is a document management application for uploading, organizing, searching, and processing documents. It provides a Spring Boot REST API, an Angular frontend, PostgreSQL metadata storage, RustFS object storage, RabbitMQ-based workers, and Elasticsearch-based full-text search and observability.

## Features

- Upload and manage documents
- Create and assign labels to documents
- OCR processing through a dedicated worker
- RabbitMQ message-based communication between services
- Full-text document search with Elasticsearch
- Angular Material frontend
- Docker Compose profiles for starting individual or complete environments
- OpenTelemetry tracing, metrics, and log collection with ELK visualization

## Architecture

```text
Angular Frontend
      |
      v
paperless-rest (Spring Boot REST API)
      |
      +--> PostgreSQL (document metadata)
      +--> RustFS (document files)
      +--> RabbitMQ (OCR jobs)
      +--> Elasticsearch (search index)

RabbitMQ
      |
      v
paperless-ocr (Spring Boot worker)
      |
      +--> RustFS (input/output files)
      +--> Elasticsearch (OCR text)
```

The repository is a Gradle multi-project monorepo:

| Module | Description |
| --- | --- |
| `paperless-rest` | REST API, database access, business logic, and RabbitMQ integration |
| `paperless-ocr` | OCR worker for processing queued documents |
| `paperless-common` | Shared configuration and models used by backend modules |
| `paperless-frontend` | Angular 21 frontend |

Supporting services include PostgreSQL, RabbitMQ, RustFS, Elasticsearch, Kibana, and the OpenTelemetry Collector.

## Technology Stack

- Java 25
- Spring Boot 4.1
- Gradle with Kotlin DSL
- Angular 21 and Angular Material
- TypeScript 5.9
- PostgreSQL 17
- RabbitMQ 4
- RustFS
- Elasticsearch 9 and Kibana
- Docker and Docker Compose
- OpenTelemetry

## Requirements

- Docker with Docker Compose
- Java 25 (for local Gradle execution)
- Node.js 24 or newer and npm 11 (for frontend development)

### Additional Requirements for Running the OCR Worker Locally

The OCR worker invokes system tools to convert and recognize document content. Install these tools on the host and make them available on `PATH`:

- Ghostscript
- Tesseract OCR, with the `eng`, `deu`, and `rus` language data installed

The Docker image installs these packages automatically; they are required separately when running the worker directly on your machine. Package names vary by operating system. On Debian/Ubuntu, install `ghostscript`, `tesseract-ocr`, `tesseract-ocr-eng`, `tesseract-ocr-deu`, and `tesseract-ocr-rus`.

## Configuration

Create local environment files from the provided examples:

```bash
cp paperless-common/.env.example paperless-common/.env
cp paperless-rest/.env.example paperless-rest/.env
```

Review both files and replace the example credentials before starting the application. For a service started directly on the host, configure its connection addresses to use host-accessible service endpoints (for example, `localhost` rather than a Docker Compose service name).

The REST API uses PostgreSQL for metadata and RustFS for object storage. RabbitMQ coordinates work between the REST service and the OCR worker.

## Start the Complete Application

From the repository root:

```bash
docker compose --profile full --env-file=./paperless-common/.env up --build
```

This profile starts the frontend, REST API, OCR worker, PostgreSQL, RabbitMQ, RustFS, Elasticsearch, Kibana, and OpenTelemetry Collector.

| Service | Address |
| --- | --- |
| Frontend | http://localhost:4200 |
| REST API | http://localhost:8080 |
| RabbitMQ management | http://localhost:15672 |
| RustFS console | http://localhost:9001 |
| Elasticsearch | http://localhost:9200 |
| Kibana | http://localhost:5601 |

Stop and remove the containers with:

```bash
docker compose --profile full down
```

## Docker Profiles

Docker Compose profiles allow parts of the system to be started independently:

```bash
# Database and infrastructure required by backend services
docker compose --profile services --env-file=./paperless-common/.env up --build

# Backend services and infrastructure
docker compose --profile backend --env-file=./paperless-common/.env up --build

# Frontend and database
docker compose --profile frontend --env-file=./paperless-common/.env up --build

# All application and infrastructure services
docker compose --profile full --env-file=./paperless-common/.env up --build
```

## Local Development

### Backend

Run the Gradle test suite:

```bash
./gradlew test
```

Build all Java modules:

```bash
./gradlew build
```

To run a service directly, configure its environment and execute the matching Gradle application task after starting the required infrastructure.

### Run the OCR Worker Locally

1. Install the OCR system requirements listed above and verify that `gs` and `tesseract` are available on `PATH`. Confirm that Tesseract has all required languages with `tesseract --list-langs`.
2. Create and configure `paperless-common/.env` as described above. For direct host execution, use host-accessible addresses for RabbitMQ, RustFS, and Elasticsearch.
3. Start the supporting services and ensure they are ready. For example, start the Compose backend profile, which includes the OCR container, then stop only the `ocr` service before running it locally:

   ```bash
   docker compose --profile backend --env-file=./paperless-common/.env up -d
   docker compose stop ocr
   ```

4. From the repository root, start the worker:

   ```bash
   ./gradlew :paperless-ocr:bootRun
   ```

The local worker needs access to RabbitMQ, RustFS, and Elasticsearch. Make sure its runtime configuration uses reachable host addresses and credentials matching the services.

### Frontend

```bash
cd paperless-frontend
npm install
npm start
```

The frontend development server is available at `http://localhost:4200`.

Build the frontend:

```bash
npm run build
```

Run frontend tests:

```bash
npm test
```

## Testing

- Unit tests validate model and service logic.
- Spring integration tests exercise REST endpoints and application behavior.
- Backend tests use JUnit Platform and Spring Boot test modules.
- Frontend tests use Vitest through the Angular CLI.

Run the complete backend test suite from the repository root:

```bash
./gradlew test
```

## API Collection

A Postman collection is available at:

- `Paperless API Test.postman_collection.json`

## Changelog

Release history and notable changes are documented in [CHANGELOG.md](CHANGELOG.md).
