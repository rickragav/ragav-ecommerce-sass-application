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
- ✅ **Product-Composite APIs**: Complete composite service contracts and DTOs
- ✅ **Service Annotations**: Spring Web annotations for REST endpoints
- ✅ **Build Configuration**: Library packaging with shared dependencies

## Recent Changes (September 2025)

## 📅 **Week 1 Implementation** (Complete)
- ✅ **Product Service Interface**: Complete REST API contract definition
- ✅ **Review Service Interface**: Complete REST API contract definition  
- ✅ **Exception Handling**: Custom exceptions (BadRequest, NotFound, InvalidInput)
- ✅ **Product-Composite APIs**: Initial composite service contracts

## 📅 **Week 2 Enhancements** (Latest)
- ✅ **Enhanced ProductAggregate**: Added price, stockQuantity, status, tenantId, image URLs
- ✅ **Extended ProductCompositeService**: Added CREATE operation with POST mapping
- ✅ **Flexible Review Model**: Changed userId from int to Integer for nullable support
- ✅ **Complete Constructor Support**: Enhanced ProductAggregate constructor with all new fields
- ✅ **Better API Documentation**: Improved Swagger annotations for composite operations
- ✅ **Product Service Contract**: Added complete CRUD interface (`ProductService.java`)
- ✅ **Product DTO Implementation**: Full product data model with validation support
- ✅ **Review Service Contract**: Added complete CRUD interface (`ReviewService.java`)
- ✅ **Review DTO Implementation**: Full review data model with rating validation (0-10 scale)
- ✅ **Composite Service APIs**: Added `ProductCompositeService`, `ProductAggregate`, `ReviewSummary`, `ServiceAddresses`
- ✅ **API Composition Pattern**: Support for aggregating multiple service responses
- ✅ **REST API Annotations**: POST, GET, DELETE mappings with proper content types
- ✅ **Library Packaging**: Optimized for shared module consumption

## Structure

```
api/
├── src/main/java/com/ragav/ecommerce/api/
│   ├── core/                           # Core service interfaces and DTOs
│   │   ├── product/                    # Product service contracts
│   │   │   ├── Product.java           # Product DTO
│   │   │   └── ProductService.java    # Product service interface
│   │   └── review/                     # Review service contracts
│   │       ├── Review.java            # Review DTO  
│   │       ├── ReviewService.java     # Review service interface
│   │       └── ReviewStatus.java      # Review status enum
│   ├── composite/                      # Composite service APIs
│   │   └── product/                    # Product composite contracts
│   │       ├── ProductAggregate.java        # Aggregated product response
│   │       ├── ProductCompositeService.java # Composite service interface
│   │       ├── ReviewSummary.java           # Review summary DTO
│   │       └── ServiceAddresses.java       # Service address tracking
│   └── exceptions/                     # Shared exception classes
│       ├── BadRequestException.java
│       ├── InvalidInputException.java
│       └── NotFoundException.java
└── build.gradle
```

## Purpose

- **Core APIs**: Individual service interfaces (Product, Review)
- **Composite APIs**: Aggregated service interfaces (Product-Composite)
- **DTOs**: Shared data structures for inter-service communication
- **Exception Handling**: Common exception types for error handling
- **Service Contracts**: Interface-based architecture for loose coupling

## Key Components

### **Product APIs** (`api.core.product`)
- `Product.java`: Complete product data model with validation annotations
- `ProductService.java`: CRUD operations interface with REST mappings

### **Review APIs** (`api.core.review`)  
- `Review.java`: Review data model with rating validation support
- `ReviewService.java`: CRUD operations interface with REST mappings
- `ReviewStatus.java`: Enum for review lifecycle management

### **Composite APIs** (`api.composite.product`)
- `ProductCompositeService.java`: API composition interface
- `ProductAggregate.java`: Combined product + reviews response model
- `ReviewSummary.java`: Simplified review data for aggregation
- `ServiceAddresses.java`: Service instance tracking for debugging

### **Exception Handling** (`api.exceptions`)
- Standardized exception types across all services
- Proper HTTP status code mapping
- Consistent error response format
- **Client Interfaces**: Define contracts for service-to-service calls
- **Constants**: Shared enums, constants, and configuration values

## Usage

This module is designed as a Java library (not a Spring Boot application) that other services can depend on:

```gradle
dependencies {
    implementation project(':api')
}
```