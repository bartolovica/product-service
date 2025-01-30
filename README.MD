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
