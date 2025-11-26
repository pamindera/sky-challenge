# Sky Application

## Tech Stack
1. Java 21
2. Spring Boot 3.5
3. Gradle 8
4. PostgreSQL 17
5. Docker & Docker-compose

## Running and developing the application

Ensure Docker and Docker-compose are installed on your machine. (Tested on MacOS 26 with Docker Desktop)
The application will install its dependencies and start (it may take a few minutes on the first run)

```shell script
  docker-compose up
```

## Endpoints

API endpoints are available at: <http://localhost:8080/swagger-ui/index.html>.

There is also a bruno specification at:
```shell script
  ls sky
```

## Running commands

All commands are run inside the container. To access the container's shell, run:

```shell script
  docker exec -it backend /bin/bash
```

## Running the linter

```shell script
  docker exec -it backend ./gradlew spotlessApply
```

## Running the static code analysis

```shell script
  docker exec -it backend ./gradlew pmdMain
```

## Run the tests 
### Tech Stack
1. SpringBoot Testing framework
2. Rest Assured
3. JUnit
4. H2 Database (Since we don't use pg specific features in tests, h2 is faster, consider test containers in the future)
5. Datafaker

#### Run all tests
```shell script
  docker exec -it backend ./gradlew test
```

## Open Telemetry Tracing
Open Telemetry tracing is available at: <http://localhost:16686/search>.

## Logging
For the sake of simplicity, logs are printed to the console.
Requests, Responses, SQL Queries and Exceptions are logged using Spring Boot's default logging mechanism.
Adjust logging levels as needed.

## Packing and running the application

Build the Docker image:
```shell script
  docker buildx build . -t "sky" --no-cache
```

Run the Docker container (it might fail as no production configuration is provided)
```shell script
  docker run -p 8080:8080 sky 
```

There is also a github pipeline that will run quality tools and build the image on push to main (for demonstration purposes)

## Database Migration
Database migrations are handled using Flyway.
For the dev env, migrations are automatically applied at application startup.
For the test env, the schema is inferred automatically from the entities.

To manually apply migrations, use the following command:
```shell script
  docker exec -it backend ./gradlew flywayMigrate
```

## Notes
- Some elements in the database schema were changed to simplify the implementation.
    - Ids were changed to UUIDs
    - Table names were changed to simpler names
    - Columns sizes were adjusted to VARCHAR(255) for simplicity
    - Added created_at and updated_at timestamps to all tables
- JWT uses an asymmetric algorithm
    - For simplicity the keys are stored in the resources folder
    - In a real world application, consider using a secret management solution
- Open Telemetry is configured to use the Jaeger exporter
- Swagger is used for API documentation
- Linting is done using Spotless with Palantir's Java formatter
- Static code analysis is done using PMD with the default ruleset
