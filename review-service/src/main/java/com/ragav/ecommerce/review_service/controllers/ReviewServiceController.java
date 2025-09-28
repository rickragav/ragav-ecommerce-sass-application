package com.ragav.ecommerce.review_service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.ragav.ecommerce.api.core.review.Review;
import com.ragav.ecommerce.api.core.review.ReviewService;
import com.ragav.ecommerce.review_service.services.ReviewServiceImpl;

/**
 * REST Controller for Review Service operations.
 * 
 * Provides HTTP endpoints for managing product reviews in the multi-tenant
 * e-commerce SaaS platform. Implements the ReviewService interface for
 * standardized API contract across microservices.
 * 
 * Endpoints:
 * - POST /review - Create new review
 * - GET /review?productId={id} - Get reviews for product
 * - DELETE /review?productId={id} - Delete reviews for product
 * 
 * Based on dataset analysis supporting 0-10 rating scale and comprehensive
 * review management for 278,858+ users and 271,360+ products.
 */
@RestController
public class ReviewServiceController implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceController.class);

    private final ReviewServiceImpl reviewService;

    public ReviewServiceController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Creates a new review for a product.
     * 
     * Validates review data and persists to MySQL database.
     * Supports 0-10 rating scale and multi-tenant isolation.
     * 
     * @param body Review data including productId, rating, text, etc.
     * @return Created review with service address populated
     */
    @Override
    public Review createReview(Review body) {
        LOG.debug("ReviewServiceController.createReview: {}", body);
        return reviewService.createReview(body);
    }

    /**
     * Retrieves a specific review by its unique identifier.
     * 
     * Returns review with service address populated for traceability.
     * Supports individual review lookup for detailed views.
     * 
     * @param reviewId Unique identifier of the review
     * @return Review with the specified reviewId
     */
    @Override
    public Review getReview(String reviewId) {
        LOG.debug("ReviewServiceController.getReview: reviewId={}", reviewId);
        return reviewService.getReview(reviewId);
    }

    /**
     * Retrieves all reviews for a specific product.
     * 
     * Returns reviews with service address populated for traceability.
     * Supports product-based filtering for review aggregation.
     * 
     * @param productId ID of the product to get reviews for
     * @return List of reviews for the specified product
     */
    @Override
    public List<Review> getReviews(int productId) {
        LOG.debug("ReviewServiceController.getReviews: productId={}", productId);
        return reviewService.getReviews(productId);
    }

    /**
     * Deletes all reviews for a specific product.
     * 
     * Used for product cleanup or administrative operations.
     * Performs cascade deletion of all related review data.
     * 
     * @param productId ID of the product to delete reviews for
     */
    @Override
    public void deleteReviews(int productId) {
        LOG.debug("ReviewServiceController.deleteReviews: productId={}", productId);
        reviewService.deleteReviews(productId);
    }
}