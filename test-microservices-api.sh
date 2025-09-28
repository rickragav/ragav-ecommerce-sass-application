#!/bin/bash

# ====================================================================
# 🧪 Comprehensive Microservices API Test Suite
# Testing Product Service & Review Service
# Based on service validation logic and business requirements
# ====================================================================

# Configuration
PRODUCT_BASE_URL="http://localhost:8080"
REVIEW_BASE_URL="http://localhost:8081"
PRODUCT_API_URL="$PRODUCT_BASE_URL/product"
REVIEW_API_URL="$REVIEW_BASE_URL/review"
PRODUCT_HEALTH_URL="$PRODUCT_BASE_URL/actuator/health"
REVIEW_HEALTH_URL="$REVIEW_BASE_URL/actuator/health"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
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

print_service_header() {
    echo -e "${PURPLE}=====================================================================${NC}"
    echo -e "${PURPLE}🚀 $1${NC}"
    echo -e "${PURPLE}=====================================================================${NC}"
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

# Wait for services to be ready
wait_for_service() {
    local service_name=$1
    local health_url=$2
    print_header "Waiting for $service_name to be Ready"
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$health_url" > /dev/null 2>&1; then
            print_success "$service_name is ready! (attempt $attempt/$max_attempts)"
            return 0
        fi
        
        print_info "Attempt $attempt/$max_attempts: $service_name not ready, waiting 5 seconds..."
        sleep 5
        ((attempt++))
    done
    
    print_error "$service_name failed to start within $(($max_attempts * 5)) seconds" "$service_name Readiness"
    return 1
}

# ====================================================================
# PRODUCT SERVICE TESTS
# ====================================================================

test_product_health_check() {
    increment_test
    print_test $TOTAL_TESTS "Product Service Health Check"
    
    response=$(curl -s -w "%{http_code}" "$PRODUCT_HEALTH_URL")
    http_code="${response: -3}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Product service health check passed (HTTP $http_code)"
    else
        print_error "Product service health check failed (HTTP $http_code)" "Product Health Check"
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
    
    response=$(curl -s -w "%{http_code}" -X POST "$PRODUCT_API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Valid product created successfully (HTTP $http_code)"
        print_info "Response: $response_body"
        # Store product ID for review tests
        export TEST_PRODUCT_ID=$unique_id
    else
        print_error "Failed to create valid product (HTTP $http_code): $response_body" "Create Valid Product"
    fi
}

test_get_existing_product() {
    increment_test
    print_test $TOTAL_TESTS "Get Existing Product"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping test." "Get Existing Product"
        return
    fi
    
    response=$(curl -s -w "%{http_code}" "$PRODUCT_API_URL/$TEST_PRODUCT_ID")
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Retrieved existing product successfully (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to get existing product (HTTP $http_code): $response_body" "Get Existing Product"
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
    
    response=$(curl -s -w "%{http_code}" -X POST "$PRODUCT_API_URL" \
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

# ====================================================================
# REVIEW SERVICE TESTS
# ====================================================================

test_review_health_check() {
    increment_test
    print_test $TOTAL_TESTS "Review Service Health Check"
    
    response=$(curl -s -w "%{http_code}" "$REVIEW_HEALTH_URL")
    http_code="${response: -3}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Review service health check passed (HTTP $http_code)"
    else
        print_error "Review service health check failed (HTTP $http_code)" "Review Health Check"
    fi
}

test_create_valid_review() {
    increment_test
    print_test $TOTAL_TESTS "Create Valid Review"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping review creation test." "Create Valid Review"
        return
    fi
    
    # Use timestamp to ensure unique review ID
    local unique_review_id="review-$(date +%s)"
    local payload="{
        \"reviewId\": \"$unique_review_id\",
        \"productId\": $TEST_PRODUCT_ID,
        \"userId\": 1001,
        \"tenantId\": \"tenant1\",
        \"rating\": 8,
        \"reviewText\": \"This is a great product! Highly recommend it.\",
        \"reviewTitle\": \"Excellent Product\",
        \"status\": \"ACTIVE\"
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$REVIEW_API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Valid review created successfully (HTTP $http_code)"
        print_info "Response: $response_body"
        export TEST_REVIEW_ID=$unique_review_id
    else
        print_error "Failed to create valid review (HTTP $http_code): $response_body" "Create Valid Review"
    fi
}

test_get_existing_review() {
    increment_test
    print_test $TOTAL_TESTS "Get Existing Review"
    
    if [ -z "$TEST_REVIEW_ID" ]; then
        print_error "No test review ID available. Skipping test." "Get Existing Review"
        return
    fi
    
    response=$(curl -s -w "%{http_code}" "$REVIEW_API_URL/$TEST_REVIEW_ID")
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Retrieved existing review successfully (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to get existing review (HTTP $http_code): $response_body" "Get Existing Review"
    fi
}

test_invalid_review_rating_high() {
    increment_test
    print_test $TOTAL_TESTS "Invalid Review Rating (Too High)"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping test." "Invalid Review Rating (Too High)"
        return
    fi
    
    # Use timestamp to ensure unique review ID
    local unique_review_id="review-invalid-high-$(date +%s)"
    local payload="{
        \"reviewId\": \"$unique_review_id\",
        \"productId\": $TEST_PRODUCT_ID,
        \"userId\": 1002,
        \"tenantId\": \"tenant1\",
        \"rating\": 15,
        \"reviewText\": \"Testing invalid rating\",
        \"reviewTitle\": \"Invalid Rating Test\",
        \"status\": \"ACTIVE\"
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$REVIEW_API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected high invalid rating (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected high invalid rating (HTTP $http_code): $response_body" "Invalid Review Rating (Too High)"
    fi
}

test_invalid_review_rating_negative() {
    increment_test
    print_test $TOTAL_TESTS "Invalid Review Rating (Negative)"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping test." "Invalid Review Rating (Negative)"
        return
    fi
    
    # Use timestamp to ensure unique review ID
    local unique_review_id="review-invalid-negative-$(date +%s)"
    local payload="{
        \"reviewId\": \"$unique_review_id\",
        \"productId\": $TEST_PRODUCT_ID,
        \"userId\": 1003,
        \"tenantId\": \"tenant1\",
        \"rating\": -5,
        \"reviewText\": \"Testing negative rating\",
        \"reviewTitle\": \"Negative Rating Test\",
        \"status\": \"ACTIVE\"
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$REVIEW_API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "422" ] || [ "$http_code" = "400" ]; then
        print_success "Correctly rejected negative rating (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Should have rejected negative rating (HTTP $http_code): $response_body" "Invalid Review Rating (Negative)"
    fi
}

test_review_with_long_text() {
    increment_test
    print_test $TOTAL_TESTS "Review with Long Text (Boundary Test)"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping test." "Review with Long Text"
        return
    fi
    
    # Create a review text that's exactly 2000 characters (valid boundary)
    local long_text=$(printf 'A%.0s' {1..2000})
    # Use timestamp to ensure unique review ID
    local unique_review_id="review-long-text-$(date +%s)"
    local payload="{
        \"reviewId\": \"$unique_review_id\",
        \"productId\": $TEST_PRODUCT_ID,
        \"userId\": 1004,
        \"tenantId\": \"tenant1\",
        \"rating\": 7,
        \"reviewText\": \"$long_text\",
        \"reviewTitle\": \"Long Text Review\",
        \"status\": \"ACTIVE\"
    }"
    
    response=$(curl -s -w "%{http_code}" -X POST "$REVIEW_API_URL" \
        -H "Content-Type: application/json" \
        -d "$payload")
    
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "Review with 2000 character text accepted (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Review with valid long text rejected (HTTP $http_code): $response_body" "Review with Long Text"
    fi
}

test_get_reviews_by_product() {
    increment_test
    print_test $TOTAL_TESTS "Get Reviews by Product ID"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping test." "Get Reviews by Product ID"
        return
    fi
    
    response=$(curl -s -w "%{http_code}" "$REVIEW_API_URL?productId=$TEST_PRODUCT_ID")
    http_code="${response: -3}"
    response_body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Retrieved reviews by product ID successfully (HTTP $http_code)"
        print_info "Response: $response_body"
    else
        print_error "Failed to get reviews by product ID (HTTP $http_code): $response_body" "Get Reviews by Product ID"
    fi
}

# ====================================================================
# INTEGRATION TESTS
# ====================================================================

test_product_review_integration() {
    increment_test
    print_test $TOTAL_TESTS "Product-Review Integration Test"
    
    if [ -z "$TEST_PRODUCT_ID" ]; then
        print_error "No test product ID available. Skipping integration test." "Product-Review Integration"
        return
    fi
    
    # Get product details
    product_response=$(curl -s -w "%{http_code}" "$PRODUCT_API_URL/$TEST_PRODUCT_ID")
    product_http_code="${product_response: -3}"
    
    # Get reviews for that product
    review_response=$(curl -s -w "%{http_code}" "$REVIEW_API_URL?productId=$TEST_PRODUCT_ID")
    review_http_code="${review_response: -3}"
    
    if [ "$product_http_code" = "200" ] && [ "$review_http_code" = "200" ]; then
        print_success "Product-Review integration working correctly"
        print_info "Product exists and reviews can be retrieved for it"
    else
        print_error "Product-Review integration failed (Product: $product_http_code, Reviews: $review_http_code)" "Product-Review Integration"
    fi
}

# Main test execution
main() {
    print_header "Ragav E-commerce SaaS - Comprehensive Microservices API Test Suite"
    print_info "Testing Product Service & Review Service"
    print_info "Product Service URL: $PRODUCT_BASE_URL"
    print_info "Review Service URL: $REVIEW_BASE_URL"
    echo ""
    
    # Wait for services
    local services_ready=true
    
    if ! wait_for_service "Product Service" "$PRODUCT_HEALTH_URL"; then
        services_ready=false
    fi
    
    if ! wait_for_service "Review Service" "$REVIEW_HEALTH_URL"; then
        services_ready=false
    fi
    
    if [ "$services_ready" = false ]; then
        print_error "One or more services failed to start. Exiting." "Service Startup"
        exit 1
    fi
    
    echo ""
    
    # Run Product Service Tests
    print_service_header "Product Service Tests"
    test_product_health_check
    test_create_valid_product
    test_get_existing_product
    test_invalid_product_id_negative
    
    echo ""
    
    # Run Review Service Tests
    print_service_header "Review Service Tests"
    test_review_health_check
    test_create_valid_review
    test_get_existing_review
    test_invalid_review_rating_high
    test_invalid_review_rating_negative
    test_review_with_long_text
    test_get_reviews_by_product
    
    echo ""
    
    # Run Integration Tests
    print_service_header "Integration Tests"
    test_product_review_integration
    
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
        echo -e "${RED}❌ Some tests failed. Please check the service implementations.${NC}"
        exit 1
    else
        echo ""
        echo -e "${GREEN}🎉 All tests passed! Both Product and Review services are working correctly.${NC}"
        exit 0
    fi
}

# Run main function
main "$@"