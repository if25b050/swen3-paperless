# Paperless

Paperless is a simple Document Management Software, which supports file uploads, tagging, ocr, generated summaries and
tokenized search.

Further a message queue is used for the communication between the backend and the workers (ocr and generative
ai).

The backend requires the db-Docker to be started.

Everything can also be started at once with:

`docker compose --profile full up --build`

## Architecture

The whole project is setup as a multiproject-monorepo, with a docker-compose.yml which can be used to either start
parts of, or the whole application.

The project contains the core backend paperless-rest, which provides the various functions to the frontend application
and uses a postgres-DB for saving metadata.

Paperless-rest further uses RustFS to save the files.

For the message queue RabbitMQ is used to communicate with the workers.


## Additional Feature

Tagging documents with labels.

Labels can be created by adding them to documents or also separately.

Labels can be used to categorize and search for documents.

Documents can have multiple Tags.

## Testing Strategy

Our testing strategy was to simple logic, like the model validations via Unit-Tests.

The general functions of the rest-services are tested mainly via Spring-Integration-Tests.