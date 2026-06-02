## Environment variables

The application requires the following environment variables:

- DB_URL – PostgreSQL connection URL  
  Example: jdbc:postgresql://localhost:5432/url_shortener

- DB_USERNAME – database username  
  Example: postgres

- DB_PASSWORD – database password  
  Example: postgres

- JWT_SECRET – secret key for JWT token generation  
  Example: super-secret-key

## Running application

```bash
DB_URL=jdbc:postgresql://localhost:5432/url_shortener
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=secret
./gradlew bootRun