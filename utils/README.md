# Utils Module

This module contains shared utility classes and helper functions used across all microservices.

## Structure

```
utils/
├── src/main/java/com/ragav/ecommerce/utils/
│   ├── validation/    # Validation utilities and custom validators
│   ├── json/          # JSON processing utilities
│   ├── date/          # Date/time utilities
│   └── ...            # Other utility packages
└── build.gradle
```

## Purpose

- **Validation Utils**: Common validation logic and custom validators
- **JSON Utils**: JSON processing, serialization/deserialization helpers
- **Date Utils**: Date/time formatting, parsing, and conversion utilities
- **String Utils**: String manipulation and formatting (via Apache Commons Lang)
- **Collection Utils**: Collection processing helpers (via Apache Commons Collections)

## Dependencies Included

- **Apache Commons Lang3**: String, number, and object utilities
- **Apache Commons Collections4**: Enhanced collection operations
- **Jackson**: JSON processing utilities
- **SLF4J**: Logging API
- **Jakarta Validation**: Validation annotations and API

## Usage

This module is designed as a Java library that other services can depend on:

```gradle
dependencies {
    implementation project(':utils')
}
```