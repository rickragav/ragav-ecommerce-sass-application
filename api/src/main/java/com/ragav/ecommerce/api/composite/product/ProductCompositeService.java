package com.ragav.ecommerce.api.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Composite Service", description = "REST API for Product Composite Information.")
public interface ProductCompositeService {

        /**
         * Create a composite product
         * 
         */
        @Operation(summary = "Create a composite product", description = "Creates a composite product with associated reviews and recommendations")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product composite created successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product composite, object invalid"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fail") })
        @PostMapping(value = "/product-composite", consumes = "application/json", produces = "application/json")
        void createProduct(@RequestBody ProductAggregate body);

        /**
         * Get a composite product by its ID
         */
        @Operation(summary = "Get a composite product by its ID", description = "Retrieves a composite product by its ID")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product composite retrieved successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product ID, object invalid"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product composite not found") })
        @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
        ProductAggregate getProduct(
                        @PathVariable("productId") int productId);

        /**
         * * Delete a composite product by its ID
         */
        @Operation(summary = "Delete a composite product by its ID", description = "Deletes a composite products")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product composite deleted successfully"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product ID, object invalid"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fail") })
        @org.springframework.web.bind.annotation.DeleteMapping(value = "/product-composite/{productId}", produces = "application/json")
        void deleteProduct(@PathVariable int productId);
}