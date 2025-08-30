# Spring Boot Hello World DB

A production-ready Spring Boot application showcasing functional routing, PostgreSQL with JPA, Flyway migrations,
OpenAPI docs, and actuator health probes.

## Tech stack

- Spring Boot 3.5.5 (Java 21)
- Spring Web (functional RouterFunction API)
- Spring Data JPA + PostgreSQL
- Flyway database migrations
- springdoc-openapi 2.x (Swagger UI)
- Spring Boot Actuator (health, probes)
- Lombok

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL 15+ (or run with containers below)

## Run locally

1) Start PostgreSQL (any option):

- With Docker/Podman
  ```bash
  # One-off DB container
  docker run -d --name hellodb \
    -e POSTGRES_DB=hellodb \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -p 5432:5432 postgres:15-alpine
  ```
- Or using the provided compose file (starts PostgreSQL):
  ```bash
  cd container
  docker-compose up -d
  ```

2) Run the app:

```bash
mvn spring-boot:run
```

App listens on http://localhost:8080

Database connection defaults (see src/main/resources/application.yml):

- URL: jdbc:postgresql://localhost:5432/hellodb?currentSchema=helloworld
- User: postgres
- Password: password
- Schema: helloworld (Flyway creates it)

## OpenAPI & Swagger UI

- Swagger UI: http://localhost:8080/swagger-ui.html (redirects to /swagger-ui/index.html)
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Data model (DTOs)

GreetingResponseDto

- id: UUID
- message: string (<= 500)
- sender: string (<= 100)
- recipient: string (<= 100)
- createdAt: ISO timestamp
- updatedAt: ISO timestamp

CreateGreetingRequestDto / UpdateGreetingRequestDto

- message (required), sender, recipient

ErrorResponseDto

- message: string

GreetingCountResponseDto

- count: number

## APIs

Hello World

- GET /api/hello — returns { message: "Hello World" }

Greetings CRUD

- POST /api/greetings — create greeting
- GET /api/greetings/{id} — get by id
- GET /api/greetings — list all
- PUT /api/greetings/{id} — update by id
- DELETE /api/greetings/{id} — delete by id

Search & filter

- GET /api/greetings/sender/{sender} — list by sender
- GET /api/greetings/recipient/{recipient} — list by recipient
- GET /api/greetings/search?message=txt — search by message contains
- GET /api/greetings/between?sender=a&recipient=b — messages between users
- GET /api/greetings/after?date=YYYY-MM-DDTHH:mm:ss — created after date (ISO)
- GET /api/greetings/latest/{sender} — latest message by sender

Utility

- HEAD /api/greetings/{id} — existence check (200 if exists, 404 if not)
- GET /api/greetings/count — total greetings count

## Curl examples

Create

```bash
curl -X POST http://localhost:8080/api/greetings \
  -H 'Content-Type: application/json' \
  -d '{
    "message": "Hello from API!",
    "sender": "alice",
    "recipient": "bob"
  }'
```

Get by id

```bash
curl http://localhost:8080/api/greetings/550e8400-e29b-41d4-a716-446655440000
```

List all

```bash
curl http://localhost:8080/api/greetings
```

Update

```bash
curl -X PUT http://localhost:8080/api/greetings/{id} \
  -H 'Content-Type: application/json' \
  -d '{
    "message": "Updated text",
    "sender": "alice",
    "recipient": "bob"
  }'
```

Delete

```bash
curl -X DELETE http://localhost:8080/api/greetings/{id}
```

Filter/search

```bash
curl http://localhost:8080/api/greetings/sender/alice
curl http://localhost:8080/api/greetings/recipient/bob
curl "http://localhost:8080/api/greetings/search?message=hello"
curl "http://localhost:8080/api/greetings/between?sender=alice&recipient=bob"
curl "http://localhost:8080/api/greetings/after?date=2025-08-31T10:15:30"
curl http://localhost:8080/api/greetings/latest/alice
```

Utility

```bash
# 200 if exists, 404 if not
curl -I http://localhost:8080/api/greetings/{id}

curl http://localhost:8080/api/greetings/count
```

## Database migrations

- Flyway runs automatically on startup
- Initial DDL/data: src/main/resources/db/migration/V1__INITIALIZE_DB.sql

## Actuator health

- /actuator/health
- /actuator/health/liveness
- /actuator/health/readiness

## Build and test

```bash
mvn clean verify
```

## Container image (optional)

Build image

```bash
docker build -t spring-boot-hello-world-db .
```

Run container (configure DB URL as needed)

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/hellodb?currentSchema=helloworld \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  spring-boot-hello-world-db
```

## Notes

- Functional routing with detailed OpenAPI annotations lives in:
    - src/main/java/.../router/HelloWorldRouter.java
    - src/main/java/.../router/GreetingsRouter.java
- Request handling/business logic lives in resource/ and service/
- All endpoints produce application/json
