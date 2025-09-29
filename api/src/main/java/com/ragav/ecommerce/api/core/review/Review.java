package com.ragav.ecommerce.api.core.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Generic Review POJO for multi-tenant e-commerce SaaS platform.
 * 
 * Supports comprehensive user feedback system with flexible rating mechanisms.
 * Based on analysis of book recommendation dataset with real-world usage
 * patterns.
 * 
 * Dataset insights:
 * - 1,149,780+ review interactions analyzed
 * - 278,858 unique users providing feedback
 * - Rating scale: 0-10 (0 = implicit feedback, 1-10 = explicit ratings)
 * - Multi-tenant architecture with author-based tenant isolation
 * 
 * Designed for scalable review management across diverse product categories.
 */
public class Review {

    /**
     * Unique review identifier.
     * Used for review management, moderation, and analytics.
     */
    @NotNull

    private String reviewId;

    /**
     * Reference to the reviewed product.
     * Links to Product entity via productId for relationship management.
     * Supports any product type in the multi-tenant system.
     */
    @NotNull
    private String productId;

    /**
     * Reference to the user who created the review.
     * Links to User entity for user activity tracking and personalization.
     * Dataset: 278,858 unique users providing diverse feedback patterns.
     */
    @NotNull

    private Integer userId;

    /**
     * Tenant identifier for multi-tenant SaaS architecture.
     * 
     * Enables tenant-based review isolation and management.
     * Dataset insight: Authors as tenants managing their product reviews.
     * Supports review analytics per tenant for business intelligence.
     */
    @NotNull
    @Size(min = 2, max = 6)
    private String tenantId;

    // ============= RATING INFORMATION =============

    /**
     * Optional - User rating on 0-10 scale based on dataset analysis.
     * 
     * Rating semantics:
     * - 1-10: Explicit user rating (1=worst, 10=excellent)
     * 
     * Dataset distribution analysis shows this scale provides
     * granular feedback while supporting implicit interactions.
     */
    @Min(0)
    @Max(10)
    private int rating;

    /**
     * Deetailed review text from user.
     * Supports comprehensive feedback beyond numerical ratings.
     * Limited to 2000 characters for performance and storage optimization.
     */
    @Size(max = 2000)
    @NotNull
    private String reviewText;

    /**
     * Optional review title/summary.
     * Brief headline describing the review for quick scanning.
     * Enhances user experience in review listings and search.
     */
    private String reviewTitle;

    /**
     * Current review status in the system.
     * Default: ACTIVE - visible and included in calculations
     * Supports content moderation and lifecycle management.
     * See ReviewStatus enum for all possible states.
     */
    private ReviewStatus status = ReviewStatus.ACTIVE;

    /**
     * Service address where the review service instance is running.
     * Used for debugging, tracing, and monitoring in distributed systems.
     * Helps identify the source service instance handling the request.
     */
    private String serviceAddress;

    // ============= CONSTRUCTORS =============

    /**
     * Default constructor for framework compatibility.
     * Required for JSON deserialization, ORM mapping, and reflection-based
     * operations.
     */
    public Review() {
    }

    /**
     * Complete constructor for comprehensive review creation.
     * 
     * Creates fully initialized Review entity with all core attributes.
     * Used by service layer for programmatic review creation and testing.
     * 
     * @param reviewId    Unique identifier for the review
     * @param productId   Reference to the reviewed product
     * @param userId      Reference to the reviewing user
     * @param tenantId    Tenant identifier for multi-tenant isolation
     * @param rating      Optional User rating (0-10 scale, 0=implicit,
     *                    1-10=explicit)
     * @param reviewText  Detailed review content (max 2000 chars)
     * @param reviewTitle Optional review headline/summary
     * @param status      Review lifecycle status (default: ACTIVE)
     */
    public Review(String reviewId, String productId, Integer userId, String tenantId, int rating, String reviewText,
            String reviewTitle, ReviewStatus status) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewTitle = reviewTitle;
        this.status = status;
    }

    // ============= ACCESSOR METHODS =============

    /**
     * Returns the unique review identifier.
     * 
     * @return reviewId String identifier for this review
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * Sets the unique review identifier.
     * 
     * @param reviewId String identifier for this review
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Returns the product ID being reviewed.
     * 
     * @return productId String reference to the reviewed product
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the product ID being reviewed.
     * 
     * @param productId String reference to the reviewed product
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Returns the user ID of the reviewer.
     * 
     * @return userId Integer reference to the reviewing user
     */
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the tenant identifier for multi-tenant isolation.
     * 
     * @return tenantId String identifier for tenant-based segregation
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets the tenant identifier for multi-tenant isolation.
     * 
     * @param tenantId String identifier for tenant-based segregation
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Returns the user rating on 0-10 scale.
     * 
     * @return rating Integer rating (0=implicit, 1-10=explicit feedback)
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the user rating with validation constraints.
     * Must be between 0-10 inclusive based on dataset analysis.
     * 
     * @param rating Integer rating (0=implicit, 1-10=explicit feedback)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the optional detailed review text.
     * 
     * @return reviewText String content up to 2000 characters
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Sets the optional detailed review text.
     * 
     * @param reviewText String content up to 2000 characters
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    /**
     * Returns the optional review title/headline.
     * 
     * @return reviewTitle String summary for review listing
     */
    public String getReviewTitle() {
        return reviewTitle;
    }

    /**
     * Sets the optional review title/headline.
     * 
     * @param reviewTitle String summary for review listing
     */
    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    /**
     * Returns the current review status.
     * 
     * @return status ReviewStatus enum (ACTIVE, HIDDEN, DELETED, etc.)
     */
    public ReviewStatus getStatus() {
        return status;
    }

    /**
     * Sets the review status for lifecycle management.
     * 
     * @param status ReviewStatus enum (ACTIVE, HIDDEN, DELETED, etc.)
     */
    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    /**
     * Returns the service address where the review service instance is running.
     * Used for debugging, tracing, and monitoring in distributed systems.
     * Helps identify the source service instance handling the request.
     * 
     * @return serviceAddress String address of the review service instance
     */
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * Sets the service address where the review service instance is running.
     * Used for debugging, tracing, and monitoring in distributed systems.
     * Helps identify the source service instance handling the request.
     * 
     * @param serviceAddress String address of the review service instance
     */
    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    // ============= UTILITY METHODS =============

    /**
     * Returns comprehensive string representation of Review entity.
     * 
     * Includes all core attributes for debugging, logging, and development.
     * Format: Review{reviewId='xxx', productId=xxx, userId=xxx, tenantId='xxx',
     * rating=xxx, reviewText='xxx', reviewTitle='xxx', status=xxx}
     * 
     * Used for:
     * - Application logging and monitoring
     * - Development debugging and testing
     * - API response serialization (when configured)
     * - System integration tracing
     * 
     * @return String comprehensive representation of review data
     */
    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", productId=" + productId +
                ", userId=" + userId +
                ", tenantId='" + tenantId + '\'' +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                ", reviewTitle='" + reviewTitle + '\'' +
                ", status=" + status +
                '}';
    }

}
