# URL Shortener API

REST API for URL shortening with JWT authentication, PostgreSQL persistence, Flyway migrations, Docker support, and Swagger documentation.

---

## Features

- User registration and authentication (JWT)
- URL shortening
- URL redirection
- URL update (PUT / PATCH)
- Active URLs filtering
- Click tracking
- URL expiration support
- PostgreSQL + Flyway migrations
- Swagger/OpenAPI documentation
- Docker & Docker Compose support
- GitHub Actions CI pipeline

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT (HS256)
- PostgreSQL
- Spring Data JPA
- Hibernate
- Flyway
- Gradle
- Docker
- Docker Compose
- JUnit + Mockito
- JaCoCo

---

## Base URL

http://localhost:8080/api/v1

---

## Environment Variables

| Variable      | Description                        | Example |
|--------------|------------------------------------|---------|
| DB_URL       | PostgreSQL connection URL          | jdbc:postgresql://localhost:5432/url_shortener |
| DB_USERNAME  | Database username                  | postgres |
| DB_PASSWORD  | Database password                  | postgres |
| JWT_SECRET   | Secret key for JWT (min 32 chars)  | my-super-secure-secret-key-at-least-32-chars |
| APP_BASE_URL | Base URL for short links           | http://localhost:8080 |

---

## Running with Docker Compose

```bash
docker-compose up --build
```

### Services

PostgreSQL:
- Host: localhost
- Port: 5432
- Database: url_shortener
- Username: test
- Password: test

Application:
- http://localhost:8080

Stop containers:

```bash
docker-compose down
```

---

## Running Locally

### Start PostgreSQL

```sql
CREATE DATABASE url_shortener;
```

### Set environment variables

```bash
export DB_URL=jdbc:postgresql://localhost:5432/url_shortener
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=my-super-secure-secret-key-at-least-32-chars
```

### Run application

```bash
./gradlew bootRun
```

---

## Database Migrations

Flyway runs automatically on startup.

Location:

src/main/resources/db/migration

---

## Swagger UI

http://localhost:8080/swagger-ui/index.html

---

## Authentication

### Register

POST /api/v1/auth/register

```json
{
  "username": "user",
  "password": "Password123"
}
```

---

### Login

POST /api/v1/auth/login

```json
{
  "username": "user",
  "password": "Password123"
}
```

Response:

```json
{
  "token": "jwt-token"
}
```

Authorization header:

```
Authorization: Bearer <token>
```

---

## URL API

### Create short URL

POST /api/v1/short-url

```json
{
  "originalUrl": "https://example.com",
  "expirationDays": 5
}
```

---

### Get all URLs

GET /api/v1/short-url

---

### Get active URLs

GET /api/v1/short-url/active

Returns only non-expired URLs.

---

### Get by short code

GET /api/v1/short-url/{shortCode}

---

### Update URL (PUT)

PUT /api/v1/short-url/{id}

```json
{
  "originalUrl": "https://updated.com",
  "expiresAt": "2026-12-31T23:59:59"
}
```

---

### Patch URL

PATCH /api/v1/short-url/{id}

```json
{
  "originalUrl": "https://partial.com"
}
```

---

### Delete URL

DELETE /api/v1/short-url/{id}

---

## Redirect (Public)

GET /r/{shortCode}

- Redirects to original URL
- Increments click counter
- No authentication required

---

## Running Tests

```bash
./gradlew test
```

---

## Coverage Report

```bash
./gradlew jacocoTestReport
```

Location:

```
build/reports/jacoco/test/html/index.html
```

---

## GitHub Actions CI

Pipeline:
- Build project
- Run tests
- Validate integrity

Location:

```
.github/workflows/
```

---

## Docker Architecture

Build stage:
- Gradle 8 + Java 17

Runtime stage:
- Eclipse Temurin 17

---

## Project Structure

```
src
 ├── main
 │   ├── java
 │   └── resources
 │       ├── application.yml
 │       └── db/migration
 └── test
     └── java
```

---

## Security

- JWT authentication (HS256)
- BCrypt password hashing
- Protected endpoints via Bearer token
- Public redirect endpoint /r/{shortCode}
---
