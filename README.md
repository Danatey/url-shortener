# URL Shortener API

REST API for URL shortening with JWT authentication, PostgreSQL persistence, Flyway migrations, Docker support, Swagger documentation, and Testcontainers-based integration testing.

---

## Features

* User registration and authentication (JWT)
* URL shortening
* URL redirection
* URL update (PUT / PATCH)
* Active URLs filtering
* Click tracking
* URL expiration support
* PostgreSQL persistence
* Flyway database migrations
* Swagger/OpenAPI documentation
* Docker & Docker Compose support
* Testcontainers integration testing
* GitHub Actions CI pipeline

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT (HS256)
* PostgreSQL
* Spring Data JPA
* Hibernate
* Flyway
* Gradle
* Docker
* Docker Compose
* Testcontainers
* JUnit 5
* Mockito
* JaCoCo

---

## Base URL

http://localhost:8080/api/v1

---

## Environment Variables

| Variable     | Description                                | Example                                        |
| ------------ | ------------------------------------------ | ---------------------------------------------- |
| DB_URL       | PostgreSQL connection URL                  | jdbc:postgresql://localhost:5432/url_shortener |
| DB_USERNAME  | Database username                          | postgres                                       |
| DB_PASSWORD  | Database password                          | postgres                                       |
| JWT_SECRET   | Secret key for JWT (minimum 32 characters) | my-super-secure-secret-key-at-least-32-chars   |
| APP_BASE_URL | Base URL used for generated short links    | http://localhost:8080                          |

---

## Running with Docker Compose

Start application and database:

```bash
docker-compose up --build
```

### Services

#### PostgreSQL

* Host: localhost
* Port: 5432
* Database: url_shortener
* Username: test
* Password: test

#### Application

* http://localhost:8080

Stop containers:

```bash
docker-compose down
```

---

## Running Locally

### Create Database

```sql
CREATE DATABASE url_shortener;
```

### Configure Environment Variables

Linux / macOS:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/url_shortener
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=my-super-secure-secret-key-at-least-32-chars
export APP_BASE_URL=http://localhost:8080
```

Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/url_shortener"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_SECRET="my-super-secure-secret-key-at-least-32-chars"
$env:APP_BASE_URL="http://localhost:8080"
```

### Run Application

```bash
./gradlew bootRun
```

Windows:

```powershell
gradlew.bat bootRun
```

---

## Database Migrations

Flyway migrations run automatically on application startup.

Migration scripts location:

```text
src/main/resources/db/migration
```

---

## Swagger UI

Open:

```text
http://localhost:8080/swagger-ui/index.html
```

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

Use token in requests:

```text
Authorization: Bearer <token>
```

---

## URL API

### Create Short URL

POST /api/v1/short-url

```json
{
  "originalUrl": "https://example.com",
  "expirationDays": 5
}
```

---

### Get All URLs

GET /api/v1/short-url

---

### Get Active URLs

GET /api/v1/short-url/active

Returns only non-expired URLs.

---

### Get URL By Short Code

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

## Redirect Endpoint (Public)

GET /r/{shortCode}

Behavior:

* Redirects to original URL
* Increments click counter
* No authentication required

Generated short links follow the format:

```text
{APP_BASE_URL}/r/{shortCode}
```

Example:

```text
http://localhost:8080/r/abc12345
```

---

## Running Tests

Integration tests use Testcontainers and automatically start a temporary PostgreSQL container.

### Requirements

Docker must be installed and running.

### Run Tests

Linux / macOS:

```bash
./gradlew test
```

Windows:

```powershell
gradlew.bat test
```

---

## Code Coverage Report

Generate coverage report:

```bash
./gradlew jacocoTestReport
```

Coverage report location:

```text
build/reports/jacoco/test/html/index.html
```

---

## GitHub Actions CI

The CI pipeline automatically:

1. Builds the project
2. Runs unit tests
3. Runs integration tests using Testcontainers
4. Generates JaCoCo reports
5. Verifies project integrity

Workflow location:

```text
.github/workflows/
```

---

## Docker Architecture

### Build Stage

* Gradle 8
* Java 17

### Runtime Stage

* Eclipse Temurin 17

---

## Project Structure

```text
src
├── main
│   ├── java
│   └── resources
│       ├── application.yml
│       └── db
│           └── migration
└── test
    └── java
```

---

## Security

* JWT authentication (HS256)
* BCrypt password hashing
* Protected endpoints secured by Bearer Token
* Public redirect endpoint `/r/{shortCode}`

---
