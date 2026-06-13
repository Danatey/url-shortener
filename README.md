# URL Shortener API

REST API for URL shortening with JWT authentication, PostgreSQL persistence, Flyway migrations, Docker support, and Swagger documentation.

## Features

* User registration and authentication with JWT
* URL shortening
* URL redirection by short code
* URL update support
* URL expiration support
* Click count tracking
* PostgreSQL database
* Flyway database migrations
* Swagger/OpenAPI documentation
* Docker and Docker Compose support
* Automated CI with GitHub Actions

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* PostgreSQL
* Spring Data JPA / Hibernate
* Flyway
* Gradle
* Docker
* Docker Compose

---

## Environment Variables

The application requires the following environment variables:

| Variable    | Description                        | Example                                        |
| ----------- | ---------------------------------- | ---------------------------------------------- |
| DB_URL      | PostgreSQL connection URL          | jdbc:postgresql://localhost:5432/url_shortener |
| DB_USERNAME | Database username                  | postgres                                       |
| DB_PASSWORD | Database password                  | postgres                                       |
| JWT_SECRET  | Secret key used for JWT generation | your-super-secure-32-character-secret-key      |

### Security Note

For HS256 JWT signing, the secret key should be at least 32 characters long.

Example:

```bash
JWT_SECRET=my-super-secure-secret-key-at-least-32-chars
```

---

## Running with Docker Compose

Build and start the application together with PostgreSQL:

```bash
docker-compose up --build
```

Services:

### PostgreSQL

* Host: localhost
* Port: 5432
* Database: url_shortener
* Username: test
* Password: test

### Application

* URL: http://localhost:8080

Stop containers:

```bash
docker-compose down
```

---

## Running Locally

### 1. Start PostgreSQL

Create a database:

```sql
CREATE DATABASE url_shortener;
```

### 2. Configure environment variables

```bash
export DB_URL=jdbc:postgresql://localhost:5432/url_shortener
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=my-super-secure-secret-key-at-least-32-chars
```

### 3. Run application

```bash
./gradlew bootRun
```

---

## Database Migrations

Flyway migrations run automatically on application startup.

Migration files are located in:

```text
src/main/resources/db/migration
```

---

## Swagger Documentation

After the application starts, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI documentation can be used to explore and test API endpoints.

---

## Authentication

### Register

Request:

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "username": "user",
  "password": "Password123"
}
```

Response:

```http
200 OK
```

---

### Login

Request:

```http
POST /auth/login
Content-Type: application/json
```

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

Use the returned token in the Authorization header:

```http
Authorization: Bearer <token>
```

---

## URL API

### Create Short URL

```http
POST /urls
Authorization: Bearer <token>
Content-Type: application/json
```

Example request:

```json
{
  "originalUrl": "https://example.com"
}
```

---

### Get URL by ID

```http
GET /urls/{id}
Authorization: Bearer <token>
```

---

### Update URL

```http
PUT /urls/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

Example request:

```json
{
  "originalUrl": "https://updated-example.com",
  "expiresAt": "2026-12-31T23:59:59"
}
```

---

### Partially Update URL

```http
PATCH /urls/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

Example request:

```json
{
  "expiresAt": "2026-12-31T23:59:59"
}
```

---

### Redirect by Short Code

```http
GET /{shortCode}
```

The application redirects the client to the original URL and increments the click counter.

---

## Running Tests

Run all tests:

```bash
./gradlew test
```

Generate JaCoCo coverage report:

```bash
./gradlew jacocoTestReport
```

Coverage report location:

```text
build/reports/jacoco/test/html/index.html
```

---

## GitHub Actions

The project includes a GitHub Actions workflow for continuous integration.

The workflow automatically:

* Builds the project
* Runs tests
* Validates application integrity

Workflow files are located in:

```text
.github/workflows/
```

---

## Docker Architecture

The application uses a multi-stage Docker build.

### Build Stage

* Base image: gradle:8-jdk17
* Compiles and packages the application

### Runtime Stage

* Base image: eclipse-temurin:17-jdk
* Runs the generated JAR file

---

## Project Structure

```text
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

* JWT-based authentication
* Passwords stored using BCrypt hashing
* JWT secret configured via environment variables
* Protected endpoints require Bearer authentication

---

## License

This project was created as a learning and portfolio project.
