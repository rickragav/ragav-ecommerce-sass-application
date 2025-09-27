# API Module

This module contains shared interfaces, DTOs, and contracts used by all microservices.

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