# API Module

This module contains shared interfaces, DTOs, and contracts used by all microservices.

## Key Features
- Java 17
- Uses Spring Boot plugin for dependency management
- No executable JAR produced (library only)
- BOM version set to 3.3.4

## Usage
Add DTOs and interfaces here to be shared across services.

## Build
This module is built automatically as part of the root project:

```
./gradlew buildAll
```

## ✅ **Implementation Status**
- ✅ **Product Service Interface**: Complete REST API contract definition
- ✅ **Product DTO**: Full product data transfer object
- ✅ **Review Service Interface**: Complete REST API contract definition
- ✅ **Review DTO**: Full review data transfer object with rating validation
- ✅ **Service Annotations**: Spring Web annotations for REST endpoints
- ✅ **Build Configuration**: Library packaging with shared dependencies

## Recent Changes (September 2025)
- ✅ **Product Service Contract**: Added complete CRUD interface (`ProductService.java`)
- ✅ **Product DTO Implementation**: Full product data model with validation support
- ✅ **Review Service Contract**: Added complete CRUD interface (`ReviewService.java`)
- ✅ **Review DTO Implementation**: Full review data model with rating validation (0-10 scale)
- ✅ **REST API Annotations**: POST, GET, DELETE mappings with proper content types
- ✅ **Library Packaging**: Optimized for shared module consumption

## Structure

```
api/
├── src/main/java/com/ragav/ecommerce/api/
│   ├── dto/          # Data Transfer Objects
│   ├── client/       # Client interfaces for service communication
│   └── constants/    # Shared constants and enums
└── build.gradle
```

## Purpose

- **DTOs**: Shared data structures for inter-service communication
- **Client Interfaces**: Define contracts for service-to-service calls
- **Constants**: Shared enums, constants, and configuration values

## Usage

This module is designed as a Java library (not a Spring Boot application) that other services can depend on:

```gradle
dependencies {
    implementation project(':api')
}
```