# Product Service

A fully-implemented microservice that manages the product catalog and inventory for the Ragav E-commerce SaaS Application. This service provides comprehensive CRUD operations with robust validation, MongoDB persistence, and Docker containerization.

## 🚀 **Status: PRODUCTION READY**

## ✅ **Implemented Features**

### **API Endpoints**
- `POST /product` - Create new product with validation
- `GET /product/{productId}` - Retrieve product by ID
- `DELETE /product/{productId}` - Delete product by ID
- `GET /actuator/health` - Health check endpoint

### **Data Validation**
- ✅ **Product ID Validation**: Must be > 0, unique across platform
- ✅ **Product Name Validation**: Non-null, non-empty, supports Unicode
- ✅ **Duplicate Prevention**: Automatic duplicate detection with unique indexes
- ✅ **Input Sanitization**: Trim whitespace, handle special characters
- ✅ **Error Handling**: Comprehensive error messages with proper HTTP status codes

### **Database Integration**
- ✅ **MongoDB 6.0.4**: Document-based persistence with Spring Data MongoDB
- ✅ **Automatic Indexing**: Unique index on `productId` created at startup
- ✅ **Optimistic Locking**: Version-based concurrency control
- ✅ **Multi-tenant Support**: Tenant isolation in data model

### **Technical Implementation**
- ✅ **Spring Boot 3.3.4**: Latest stable framework
- ✅ **Java 17**: LTS version with modern language features
- ✅ **MapStruct**: Entity-to-DTO mapping
- ✅ **Docker Support**: Multi-stage build with health checks
- ✅ **Configuration Management**: YAML-based configuration with profiles

## 🏗️ **Architecture**

### **Package Structure**
```
src/main/java/com/ragav/ecommerce/product_service/
├── ProductServiceApplication.java     # Main application class
├── config/
│   └── MongoIndexInitializer.java    # Automatic index creation
├── persistence/
│   ├── ProductEntity.java            # MongoDB document entity
│   └── ProductRepository.java        # Spring Data repository
└── services/
    └── ProductServiceImpl.java       # Business logic & REST controller
```

### **Data Model**
```java
ProductEntity {
    String id;              // MongoDB ObjectId
    Long version;           // Optimistic locking
    int productId;          // Business identifier (unique)
    String name;            // Product name
    int weight;             // Product weight
    String tenantId;        // Multi-tenant support
    // ... additional fields
}
```

## 🧪 **Testing**

### **Comprehensive Test Suite**
The service includes a comprehensive test suite with 18 test scenarios:

```bash
# Run API tests (from project root)
./test-product-api.sh
```

**Test Coverage:**
- ✅ Health check validation
- ✅ Valid product creation
- ✅ Invalid product ID rejection (negative, zero)
- ✅ Null/empty product name validation
- ✅ Duplicate product prevention
- ✅ Product retrieval and 404 handling
- ✅ JSON validation and malformed request handling
- ✅ Unicode and special character support
- ✅ Boundary value testing
- ✅ HTTP method and content-type validation

## 🐳 **Docker Support**

### **Dockerfile Features**
- **Multi-stage build** for optimized image size
- **Eclipse Temurin JRE 17** base image
- **Health check** integration
- **Spring Boot 3.x** launcher compatibility

### **Docker Compose Integration**
```yaml
services:
  product:
    build: ./product-service
    ports:
      - "8080:8080"
    depends_on:
      mongodb:
        condition: service_healthy
```

## 🔧 **Configuration**

### **Application Profiles**
- **Default Profile**: Local development with localhost MongoDB
- **Docker Profile**: Containerized environment with service discovery

### **Database Configuration**
```yaml
spring:
  data:
    mongodb:
      database: product-db
      auto-index-creation: true  # Automatic index creation
```

## 🚀 **Quick Start**

### **Docker Compose (Recommended)**
```bash
# From project root
./gradlew buildAll
docker-compose up -d
curl http://localhost:8080/actuator/health
```

### **Local Development**
```bash
# Start MongoDB locally (port 27017)
./gradlew :product-service:bootRun
```

### **API Testing**
```bash
# Create a product
curl -X POST http://localhost:8080/product \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 123,
    "name": "Test Product",
    "weight": 100,
    "tenantId": "tenant1",
    "version": 1
  }'

# Get a product
curl http://localhost:8080/product/123

# Run comprehensive tests
./test-product-api.sh
```

## 📊 **Error Handling**

The service provides detailed error responses with appropriate HTTP status codes:

- **400 Bad Request**: Invalid input data (productId < 1, null/empty name)
- **404 Not Found**: Product not found
- **415 Unsupported Media Type**: Invalid content-type
- **422 Unprocessable Entity**: Duplicate product key
- **500 Internal Server Error**: Unexpected server errors

## 🔄 **Recent Changes (September 2025)**
- ✅ **Complete CRUD Implementation**: Full product management capabilities
- ✅ **MongoDB Integration**: Document persistence with automatic indexing
- ✅ **Docker Containerization**: Production-ready deployment
- ✅ **Comprehensive Testing**: 18-scenario test suite with edge cases
- ✅ **Validation Framework**: Business logic validation with error handling
- ✅ **Configuration Management**: Profile-based configuration system
