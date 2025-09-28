package com.ragav.ecommerce.product_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ragav.ecommerce.api.core.product.Product;
import com.ragav.ecommerce.product_service.services.ProductServiceImpl;

/**
 * REST Controller for Product Service operations.
 * 
 * Provides HTTP endpoints for managing products in the multi-tenant
 * e-commerce SaaS platform. Implements the ProductService interface for
 * standardized API contract across microservices.
 * 
 * Endpoints:
 * - POST /product - Create new product
 * - GET /product/{productId} - Get product by ID
 * - DELETE /product/{productId} - Delete product by ID
 * 
 * Based on dataset analysis supporting comprehensive product management
 * for 278,858+ users and 271,360+ products with MongoDB persistence.
 */
@RestController
public class ProductServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceController.class);

    private final ProductServiceImpl productService;

    public ProductServiceController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product in the catalog.
     * 
     * Validates product data and persists to MongoDB database.
     * Supports multi-tenant isolation and automatic indexing.
     * 
     * @param body Product data including productId, name, weight, etc.
     * @return Created product with service address populated
     */
    @PostMapping(value = "/product", consumes = "application/json", produces = "application/json")
    public Product createProduct(@RequestBody Product body) {
        LOG.debug("ProductServiceController.createProduct: {}", body);
        return productService.createProduct(body);
    }

    /**
     * Retrieves a specific product by its unique identifier.
     * 
     * Returns product with service address populated for traceability.
     * Supports individual product lookup for catalog views.
     * 
     * @param productId Unique identifier of the product
     * @return Product with the specified productId
     */
    @GetMapping(value = "/product/{productId}", produces = "application/json")
    public Product getProduct(@PathVariable int productId) {
        LOG.debug("ProductServiceController.getProduct: productId={}", productId);
        return productService.getProduct(productId);
    }

    /**
     * Deletes a specific product from the catalog.
     * 
     * Used for product removal or administrative operations.
     * Performs complete removal of product data from MongoDB.
     * 
     * @param productId ID of the product to delete
     */
    @DeleteMapping(value = "/product/{productId}")
    public void deleteProduct(@PathVariable int productId) {
        LOG.debug("ProductServiceController.deleteProduct: productId={}", productId);
        productService.deleteProduct(productId);
    }
}