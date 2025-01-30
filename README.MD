# Product Service

## Overview

The Product Service is a Spring Boot application that provides a RESTful API for managing products. It includes features such as product creation, retrieval, and pagination. The service also integrates with an external API to fetch conversion rates.

## Technologies Used

- Java
- Spring Boot
- Spring WebFlux
- Spring Security
- Spring Data R2DBC
- Maven
- Lombok
- Swagger
- PostgreSQL

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- PostgreSQL

### Configuration

The application configuration is managed through the `application.yaml` file. You can override the default values using environment variables.
The application uses basic authentication for security with two roles (admin and customer). The credentials can be found in the `application.yaml` file.

### Swager
The path to the Swagger UI is `{baseUrl}/swagger-ui.html`. The base url port can be found in the `application.yaml` file (server.port).


## Running the Program

### Using Docker Compose

1. Ensure Docker and Docker Compose are installed on your machine.
2. Navigate to the directory containing the `docker-compose.yml` file.
3. Run the following command to start the services:

```sh
docker-compose up
```

This will start the PostgreSQL database and the application.

### Running the Java Application

1. Ensure you have Java 17 or higher installed.
2. Navigate to the root directory of the project.
3. Build the project using Maven:

```sh
mvn clean install
```

4. Run the application:

```sh
java -jar target/productservice-0.0.1-SNAPSHOT.jar
```

### Running with Maven

1. Ensure you have Maven 3.6.0 or higher installed.
2. Navigate to the root directory of the project.
3. Use the following command to run the application:

```sh
mvn spring-boot:run
```

This will start the Spring Boot application using Maven.