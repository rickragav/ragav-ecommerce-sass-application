#!/bin/bash

# ====================================================================
# 🧪 Comprehensive Product API Test Suite
# Based on ProductServiceImpl validation logic
# Testing all validation scenarios and edge cases
# ====================================================================

# Configuration
BASE_URL="http://localhost:8080"
API_URL="$BASE_URL/product"
HEALTH_URL="$BASE_URL/actuator/health"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Test results
declare -a FAILED_TEST_NAMES=()

# Utility functions
print_header() {
    echo -e "${BLUE}=====================================================================${NC}"
    echo -e "${BLUE}🧪 $1${NC}"
    echo -e "${BLUE}=====================================================================${NC}"
}

print_test() {
    echo -e "${YELLOW}📋 Test $1: $2${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
    ((PASSED_TESTS++))
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
    FAILED_TEST_NAMES+=("$2")
    ((FAILED_TESTS++))
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

increment_test() {
    ((TOTAL_TESTS++))
}

# Wait for service to be ready
wait_for_service() {
    print_header "Waiting for Product Service to be Ready"
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$HEALTH_URL" > /dev/null 2>&1; then
            print_success "Product service is ready! (attempt $attempt/$max_attempts)"
            return 0
        fi
        
        print_info "Attempt $attempt/$max_attempts: Service not ready, waiting 5 seconds..."
        sleep 5
        ((attempt++))
    done
    
    print_error "Product service failed to start within $(($max_attempts * 5)) seconds" "Service Readiness"
    exit 1
}

# Test functions based on ProductServiceImpl validation logic

test_health_check() {
    increment_test
    print_test $TOTAL_TESTS "Health Check"
    
    response=$(curl -s -w "%{http_code}" "$HEALTH_URL")
    http_code="${response: -3}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Health check passed (HTTP $http_code)"
    else
        print_error "Health check failed (HTTP $http_code)" "Health Check"
    fi
}

test_create_valid_product() {
    increment_test
    print_test $TOTAL_TESTS "Create Valid Product"
    
    # Use timestamp to ensure unique product ID
    local unique_id=$((1000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"Test Product\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Valid product created successfully (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to create valid product (HTTP $http_code): $response_body" "Create Valid Product"
    fi
}

test_invalid_product_id_negative() {
    increment_test
    print_test $TOTAL_TESTS "Invalid Product ID (Negative)"
    
    local payload='{
        "productId": -1,
        "name": "Test Product",
        "weight": 100,
        "tenantId": "tenant1",
        "version": 1
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected negative product ID (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected negative product ID (HTTP $http_code): $response_body" "Invalid Product ID (Negative)"
    fi
}

test_invalid_product_id_zero() {
    increment_test
    print_test $TOTAL_TESTS "Invalid Product ID (Zero)"
    
    local payload='{
        "productId": 0,
        "name": "Test Product",
        "weight": 100,
        "tenantId": "tenant1",
        "version": 1
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected zero product ID (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected zero product ID (HTTP $http_code): $response_body" "Invalid Product ID (Zero)"
    fi
}

test_null_product_name() {
    increment_test
    print_test $TOTAL_TESTS "Null Product Name"
    
    local payload='{
        "productId": 102,
        "name": null,
        "weight": 100,
        "tenantId": "tenant1",
        "version": 1
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected null product name (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected null product name (HTTP $http_code): $response_body" "Null Product Name"
    fi
}

test_empty_product_name() {
    increment_test
    print_test $TOTAL_TESTS "Empty Product Name"
    
    local payload='{
        "productId": 103,
        "name": "",
        "weight": 100,
        "tenantId": "tenant1",
        "version": 1
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected empty product name (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected empty product name (HTTP $http_code): $response_body" "Empty Product Name"
    fi
}

test_duplicate_product_creation() {
    increment_test
    print_test $TOTAL_TESTS "Duplicate Product Creation"
    
    # First, create a product with unique ID
    local unique_id=$((2000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"Duplicate Test Product\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    curl -s "$API_URL" -X POST -H "Content-Type: application/json" -d "$payload" > /dev/null
    
    # Try to create the same product again
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "409" ]; then
        print_success "Correctly rejected duplicate product (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected duplicate product (HTTP $http_code): $response_body" "Duplicate Product Creation"
    fi
}

test_get_existing_product() {
    increment_test
    print_test $TOTAL_TESTS "Get Existing Product"
    
    # First ensure product exists with unique ID
    local unique_id=$((3000 + $(date +%s) % 9999))
    local create_payload="{
        \"productId\": $unique_id,
        \"name\": \"Get Test Product\",
        \"weight\": 150,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    curl -s "$API_URL" -X POST -H "Content-Type: application/json" -d "$create_payload" > /dev/null
    
    # Now try to get it
    response=$(curl -s -w "%{http_code}" "$API_URL/$unique_id")
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Successfully retrieved existing product (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to retrieve existing product (HTTP $http_code): $response_body" "Get Existing Product"
    fi
}

test_get_nonexistent_product() {
    increment_test
    print_test $TOTAL_TESTS "Get Non-existent Product"
    
    response=$(curl -s -w "%{http_code}" "$API_URL/99999")
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "404" ]; then
        print_success "Correctly returned 404 for non-existent product (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have returned 404 for non-existent product (HTTP $http_code): $response_body" "Get Non-existent Product"
    fi
}

test_invalid_json_payload() {
    increment_test
    print_test $TOTAL_TESTS "Invalid JSON Payload"
    
    local invalid_payload='{"productId": 106, "name": "Test", invalid json}'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$invalid_payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "400" ]; then
        print_success "Correctly rejected invalid JSON (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected invalid JSON (HTTP $http_code): $response_body" "Invalid JSON Payload"
    fi
}

test_missing_required_fields() {
    increment_test
    print_test $TOTAL_TESTS "Missing Required Fields"
    
    local payload='{
        "name": "Test Product"
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "400" ] || [ "$http_code" = "422" ]; then
        print_success "Correctly rejected missing required fields (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected missing required fields (HTTP $http_code): $response_body" "Missing Required Fields"
    fi
}

test_large_product_name() {
    increment_test
    print_test $TOTAL_TESTS "Large Product Name"
    
    local unique_id=$((4000 + $(date +%s) % 9999))
    local large_name=$(printf "A%.0s" {1..1000})  # 1000 character name
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"$large_name\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ] || [ "$http_code" = "400" ]; then
        if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
            print_success "Large product name accepted (HTTP $http_code)"
        else
            print_success "Large product name correctly rejected (HTTP $http_code)"
        fi
        print_info "Response: ${response_body:0:200}..."
    else
        print_error "Unexpected response for large product name (HTTP $http_code): $response_body" "Large Product Name"
    fi
}

test_negative_weight() {
    increment_test
    print_test $TOTAL_TESTS "Negative Weight"
    
    local unique_id=$((5000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"Negative Weight Product\",
        \"weight\": -50,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ] || [ "$http_code" = "400" ] || [ "$http_code" = "422" ]; then
        if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
            print_success "Negative weight accepted (HTTP $http_code)"
        else
            print_success "Negative weight correctly rejected (HTTP $http_code)"
        fi
        print_info "Response: $response_body"
    else
        print_error "Unexpected response for negative weight (HTTP $http_code): $response_body" "Negative Weight"
    fi
}

test_content_type_validation() {
    increment_test
    print_test $TOTAL_TESTS "Content-Type Validation"
    
    local unique_id=$((6000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"Content Type Test\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: text/plain" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "415" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected invalid content-type (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected invalid content-type (HTTP $http_code): $response_body" "Content-Type Validation"
    fi
}

test_http_methods() {
    increment_test
    print_test $TOTAL_TESTS "HTTP Method Validation"
    
    # Test unsupported method (PATCH is not implemented)
    response=$(curl -s -w "%{http_code}" -X PATCH "$API_URL/123" \
        -H "Content-Type: application/json" \
        -d '{"name": "Updated"}')
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "405" ] || [ "$http_code" = "404" ]; then
        print_success "Correctly handled unsupported HTTP method (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have returned 405 for unsupported method (HTTP $http_code): $response_body" "HTTP Method Validation"
    fi
}

test_special_characters_in_name() {
    increment_test
    print_test $TOTAL_TESTS "Special Characters in Product Name"
    
    local unique_id=$((7000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"Product with Special Characters: !@#\$%^&*(){}[]|\\\\:;\\\"'<>,.?/~\`\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Special characters in name accepted (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to handle special characters in name (HTTP $http_code): $response_body" "Special Characters in Product Name"
    fi
}

test_unicode_characters() {
    increment_test
    print_test $TOTAL_TESTS "Unicode Characters in Product Name"
    
    local unique_id=$((8000 + $(date +%s) % 9999))
    local payload="{
        \"productId\": $unique_id,
        \"name\": \"产品名称 - Produktname - 製品名 - プロダクト名\",
        \"weight\": 100,
        \"tenantId\": \"tenant1\",
        \"version\": 1
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Unicode characters in name accepted (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to handle unicode characters in name (HTTP $http_code): $response_body" "Unicode Characters in Product Name"
    fi
}

test_boundary_values() {
    increment_test
    print_test $TOTAL_TESTS "Boundary Values (Max Integer)"
    
    local payload='{
        "productId": 2147483647,
        "name": "Max Integer Product",
        "weight": 2147483647,
        "tenantId": "tenant1",
        "version": 1
    }'
    
    response=$(curl -s -w "%{http_code}" -X POST "$API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ] || [ "$http_code" = "400" ]; then
        if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
            print_success "Boundary values accepted (HTTP $http_code)"
        else
            print_success "Boundary values correctly rejected (HTTP $http_code)"
        fi
        print_info "Response: $response_body"
    else
        print_error "Unexpected response for boundary values (HTTP $http_code): $response_body" "Boundary Values"
    fi
}

# Main test execution
main() {
    print_header "Ragav E-commerce SaaS - Comprehensive Product API Test Suite"
    print_info "Testing all ProductServiceImpl validation scenarios"
    print_info "Base URL: $BASE_URL"
    echo ""
    
    # Wait for service
    wait_for_service
    echo ""
    
    # Run all tests
    print_header "Running Comprehensive Test Suite"
    
    test_health_check
    test_create_valid_product
    test_invalid_product_id_negative
    test_invalid_product_id_zero
    test_null_product_name
    test_empty_product_name
    test_duplicate_product_creation
    test_get_existing_product
    test_get_nonexistent_product
    test_invalid_json_payload
    test_missing_required_fields
    test_large_product_name
    test_negative_weight
    test_content_type_validation
    test_http_methods
    test_special_characters_in_name
    test_unicode_characters
    test_boundary_values
    
    # Print summary
    echo ""
    print_header "Test Summary"
    echo -e "${BLUE}Total Tests: $TOTAL_TESTS${NC}"
    echo -e "${GREEN}Passed: $PASSED_TESTS${NC}"
    echo -e "${RED}Failed: $FAILED_TESTS${NC}"
    
    if [ $FAILED_TESTS -gt 0 ]; then
        echo ""
        echo -e "${RED}Failed Tests:${NC}"
        for test_name in "${FAILED_TEST_NAMES[@]}"; do
            echo -e "${RED}  - $test_name${NC}"
        done
        echo ""
        echo -e "${RED}❌ Some tests failed. Please check the implementation.${NC}"
        exit 1
    else
        echo ""
        echo -e "${GREEN}🎉 All tests passed! ProductServiceImpl validation is working correctly.${NC}"
        exit 0
    fi
}

# Run main function
main "$@"