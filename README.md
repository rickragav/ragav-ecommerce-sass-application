# Ragav E-commerce SaaS Application

A microservice-based e-commerce platform built with Spring Boot 3.3.4 and Gradle, designed as a multi-tenant SaaS solution with integrated book recommendation capabilities.

## Modules
- **api**: Shared interfaces and DTOs for service communication.
- **utils**: Common utility classes and helpers.
- **product-service**: Product management microservice.
- **recommendation-service**: Recommendation engine microservice.
- **review-service**: Review and rating microservice.

## Build & Run
- Build all modules: `./gradlew buildAll`
- Test all modules: `./gradlew testAll`
- Clean all modules: `./gradlew cleanAll`

## Dependency Management
- All modules use Spring Boot plugin for automatic dependency management.
- Library modules (`api`, `utils`) do not produce executable JARs.
- Java 17 is required.

## Recent Changes
- Fixed Spring Boot BOM version in library modules.
- Disabled bootJar for `api` and `utils`.
- All modules build successfully with `./gradlew buildAll`.

---

See each module's README for details.

## Current Architecture

### 🏗️ Microservices Architecture
This application follows a microservices pattern with three core services:

- **product-service**: Manages product catalog and inventory management
  - Port: Default Spring Boot (8080)
  - Status: ✅ Basic setup complete, needs business logic implementation
  
- **recommendation-service**: Provides AI-powered product recommendations
  - Port: Default Spring Boot (8080) 
  - Status: ✅ Basic setup complete, ready for Python ML integration
  
- **review-service**: Handles product reviews, ratings, and user feedback
  - Port: Default Spring Boot (8080)
  - Status: ✅ Basic setup complete, needs API implementation

### 📊 Data Analytics Component
Includes comprehensive book recommendation analysis with real-world dataset:

- **Dataset**: 70MB Books.csv, 22MB Ratings.csv, 11MB Users.csv
- **Analysis Engine**: Python-based EDA with 738-line implementation
- **Visualizations**: Generated analysis charts (author, rating, user, merged analysis)
- **ML Libraries**: pandas, numpy, matplotlib, seaborn, plotly, scikit-learn

### 🔧 Technology Stack
- **Framework**: Spring Boot 3.3.4 (latest version compatible with Java 17)
- **Java Version**: 17 (LTS)
- **Build Tool**: Gradle 8.10.2 with wrapper
- **Testing**: JUnit 5 + Spring Boot Test
- **Monitoring**: Spring Boot Actuator
- **Data Analysis**: Python with ML/Data Science stack

## Prerequisites

- **Java 17 or higher** (OpenJDK recommended)
- **Gradle 8.10.2** (included via wrapper)
- **Python 3.8+** (for recommendation analysis)
- **Git** (for version control)

## Building the Application

### Build all services
```bash
./gradlew buildAll
```

### Build a specific service
```bash
./gradlew :product-service:build
./gradlew :recommendation-service:build
./gradlew :review-service:build
```

## Running Tests

### Test all services
```bash
./gradlew testAll
```

### Test a specific service
```bash
./gradlew :product-service:test
./gradlew :recommendation-service:test
./gradlew :review-service:test
```

## Running the Application

### Run a specific service
```bash
./gradlew :product-service:bootRun      # Runs on http://localhost:8080
./gradlew :recommendation-service:bootRun # Runs on http://localhost:8080
./gradlew :review-service:bootRun        # Runs on http://localhost:8080
```

> **Note**: Services currently run on default port 8080. Update `application.properties` to configure different ports for concurrent execution.

### Run Data Analysis
```bash
# Install Python dependencies
pip install -r requirements.txt

# Run comprehensive book recommendation analysis
python book_recommendation.py

# View generated analysis files
ls -la *.png                           # Generated visualizations
ls -la book_eda_results.pkl            # Saved analysis results
```

## Cleaning

### Clean all services
```bash
./gradlew cleanAll
```

### Clean a specific service
```bash
./gradlew :product-service:clean
./gradlew :recommendation-service:clean
./gradlew :review-service:clean
```

## Project Structure

```
ragav-ecommerce-sass-application/
├── api/                                # Shared API Module (Java Library)
│   ├── src/main/java/com/ragav/ecommerce/api/
│   │   ├── dto/                       # Data Transfer Objects
│   │   ├── client/                    # Client interfaces
│   │   └── constants/                 # Shared constants and enums
│   ├── build.gradle                   # Library dependencies
│   └── README.md                      # API module documentation
├── utils/                              # Shared Utils Module (Java Library)
│   ├── src/main/java/com/ragav/ecommerce/utils/
│   │   ├── validation/                # Validation utilities
│   │   ├── json/                      # JSON processing utilities
│   │   ├── date/                      # Date/time utilities
│   │   └── ...                        # Other utility packages
│   ├── build.gradle                   # Library dependencies
│   └── README.md                      # Utils module documentation
├── product-service/                    # Product Catalog Microservice
│   ├── src/main/java/                 # Java source code
│   ├── src/main/resources/            # Configuration files
│   ├── src/test/java/                 # Unit tests
│   ├── build.gradle                   # Service-specific dependencies
│   └── build/                         # Compiled artifacts
├── recommendation-service/             # AI Recommendation Microservice
│   ├── src/main/java/                 
│   ├── src/main/resources/            
│   ├── src/test/java/                 
│   ├── build.gradle                   
│   └── build/                         
├── review-service/                     # Reviews & Ratings Microservice
│   ├── src/main/java/                 
│   ├── src/main/resources/            
│   ├── src/test/java/                 
│   ├── build.gradle                   
│   └── build/                         
├── book-data/                          # ML Dataset (92MB total)
│   ├── Books.csv                      # 70MB - Book catalog
│   ├── Ratings.csv                    # 22MB - User ratings
│   ├── Users.csv                      # 11MB - User data
│   └── *.png                          # Recommendation taxonomy diagrams
├── book_recommendation.py              # 738-line Python EDA implementation
├── requirements.txt                    # Python ML dependencies
├── *.png                              # Generated analysis visualizations
├── build.gradle                       # Root project configuration
├── settings.gradle                     # Multi-module setup
├── gradlew / gradlew.bat              # Gradle wrapper scripts
└── README.md                          # This file
```

## Current Implementation Status

### ✅ Completed
- [x] Multi-module Gradle project setup
- [x] Three microservices with Spring Boot foundation
- [x] Build system with custom tasks (buildAll, testAll, cleanAll)
- [x] Basic test infrastructure
- [x] Comprehensive book recommendation dataset analysis
- [x] Python-based ML/EDA implementation
- [x] Generated data visualizations and insights

### 🚧 In Progress / Next Steps
- [ ] REST API endpoints for each service
- [ ] Database integration (JPA entities, repositories)
- [ ] Service-to-service communication
- [ ] Integration of Python ML model with recommendation-service
- [ ] Authentication and authorization
- [ ] Docker containerization
- [ ] Service discovery and configuration management
- [ ] API Gateway implementation

## Data Analysis Insights

The project includes a comprehensive analysis of book recommendation data:
- **271,379 books** with detailed metadata
- **1,149,780 ratings** from real users
- **278,858 users** with demographic information
- **Multi-tenant analysis** with authors as tenant entities
- **Generated visualizations** for business intelligence

## Development Roadmap

### Phase 1: Core API Development
```
product-service:
├── GET    /api/v1/products           # List products with pagination
├── GET    /api/v1/products/{id}      # Get product details
├── POST   /api/v1/products           # Create new product
├── PUT    /api/v1/products/{id}      # Update product
└── DELETE /api/v1/products/{id}      # Delete product

recommendation-service:
├── GET    /api/v1/recommendations/{userId}     # Get user recommendations
├── POST   /api/v1/recommendations/similar     # Similar products
└── POST   /api/v1/recommendations/retrain     # Retrain ML model

review-service:
├── GET    /api/v1/reviews/product/{productId} # Product reviews
├── POST   /api/v1/reviews                     # Submit review
├── GET    /api/v1/reviews/user/{userId}       # User's reviews
└── PUT    /api/v1/reviews/{reviewId}          # Update review
```

### Phase 2: Integration & Deployment
- Database integration (PostgreSQL/MySQL)
- Service communication (OpenFeign/RestTemplate)
- Python ML model integration via REST API
- Docker containerization
- Kubernetes deployment manifests

### Phase 3: Production Features
- API Gateway (Spring Cloud Gateway)
- Service discovery (Eureka/Consul)
- Distributed tracing (Zipkin/Jaeger)
- Monitoring & metrics (Prometheus/Grafana)
- Security (OAuth2/JWT)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.