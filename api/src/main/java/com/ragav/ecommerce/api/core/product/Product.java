package com.ragav.ecommerce.api.core.product;

import java.math.BigDecimal;

/**
 * Generic Product POJO for multi-tenant e-commerce SaaS platform.
 * 
 * Supports any product type with flexible tenant-based architecture.
 * 
 * Test dataset contains:
 * - 271,360 products
 * - 102,022 tenants
 * - Each tenant manages multiple products for testing multi-tenancy
 * 
 * Designed to be product-agnostic while maintaining SaaS capabilities.
 */
public class Product {

    /**
     * Unique product identifier.
     * Can be any product ID: SKU, UPC, internal ID, etc.
     * Test data: Unique IDs from dataset
     */
    private int productId;

    /**
     * Product name/title.
     * Generic product name for any e-commerce item.
     * Test data: Product names from dataset (242,135 unique titles)
     */
    private String name;

    /**
     * Product image URLs in different sizes for responsive design.
     * Supports any product imagery: electronics, clothing, consumer goods, etc.
     * Used for responsive web design and mobile optimization.
     * Test data: Product images from dataset
     */
    private String imageUrlSmall; // Thumbnail for lists/grids
    private String imageUrlMedium; // Medium size for cards/previews
    private String imageUrlLarge; // Full size for product detail pages

    /**
     * Service address for microservice identification.
     * Used by ServiceUtil for service discovery and load balancing
     */
    private String serviceAddress;

    // ============= BUSINESS FIELDS =============

    /**
     * Product price in the platform currency.
     * Supports decimal precision for accurate financial calculations
     */
    private BigDecimal price;

    /**
     * Current stock quantity available for sale.
     * Null indicates unlimited stock or stock not tracked
     */
    private Integer stockQuantity;

    /**
     * Current product status in the system.
     * Default: AVAILABLE - ready for purchase
     * See ProductStatus enum for all possible states
     */
    private ProductStatus status = ProductStatus.AVAILABLE;

    /**
     * Tenant identifier for multi-tenant SaaS architecture.
     * 
     * Represents any tenant type: sellers, vendors, brands, manufacturers, etc.
     * Test data: 102,022 tenants from dataset
     * - 363 "super tenants" (50+ products) for testing premium features
     * - Supports different tenant tiers and business models
     */
    private String tenantId;

    /**
     * Default constructor for framework usage (JPA, Jackson, etc.)
     */
    public Product() {
    }

    /**
     * Full constructor for creating a complete Product instance.
     * 
     * @param productId      Unique product identifier (any SKU/ID format)
     * @param name           Product title/name
     * @param serviceAddress Microservice address for service discovery
     * @param price          Product price with decimal precision
     * @param stockQuantity  Available stock count (null = unlimited)
     * @param status         Product availability status
     * @param tenantId       Multi-tenant identifier (seller/vendor/brand ID)
     * @param imageUrlSmall  Thumbnail image URL
     * @param imageUrlMedium Medium-sized image URL
     * @param imageUrlLarge  Full-sized image URL
     */
    public Product(int productId, String name, String serviceAddress, BigDecimal price, Integer stockQuantity,
            ProductStatus status, String tenantId, String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.productId = productId;
        this.name = name;
        this.serviceAddress = serviceAddress;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.tenantId = tenantId;
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }

    // ============= GETTERS & SETTERS =============

    /**
     * Gets the unique product identifier.
     * 
     * @return Product ID (any format: SKU, ISBN, UPC, etc.)
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the unique product identifier.
     * 
     * @param productId Product ID (should be unique across the system)
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the product name/title.
     * 
     * @return Product name (generic for any product type)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name/title.
     * 
     * @param name Product name (should not be null or empty)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the microservice address for service discovery.
     * 
     * @return Service address used by ServiceUtil
     */
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * Sets the microservice address.
     * 
     * @param serviceAddress Service address for load balancing
     */
    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    /**
     * Gets the small/thumbnail image URL.
     * 
     * @return Small image URL for lists and grids
     */
    public String getImageUrlSmall() {
        return imageUrlSmall;
    }

    /**
     * Sets the small/thumbnail image URL.
     * 
     * @param imageUrlSmall URL for thumbnail image (typically < 100px)
     */
    public void setImageUrlSmall(String imageUrlSmall) {
        this.imageUrlSmall = imageUrlSmall;
    }

    /**
     * Gets the medium-sized image URL.
     * 
     * @return Medium image URL for cards and previews
     */
    public String getImageUrlMedium() {
        return imageUrlMedium;
    }

    /**
     * Sets the medium-sized image URL.
     * 
     * @param imageUrlMedium URL for medium image (typically 200-400px)
     */
    public void setImageUrlMedium(String imageUrlMedium) {
        this.imageUrlMedium = imageUrlMedium;
    }

    /**
     * Gets the large/full-sized image URL.
     * 
     * @return Large image URL for product detail pages
     */
    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    /**
     * Sets the large/full-sized image URL.
     * 
     * @param imageUrlLarge URL for full-size image (typically > 400px)
     */
    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    /**
     * Gets the product price.
     * 
     * @return Price with decimal precision for accurate calculations
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     * 
     * @param price Product price (should be positive, null indicates no price set)
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the current stock quantity.
     * 
     * @return Available stock count (null indicates unlimited or not tracked)
     */
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the stock quantity.
     * 
     * @param stockQuantity Available stock (null = unlimited, 0 = out of stock)
     */
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Gets the current product status.
     * 
     * @return Product availability status (AVAILABLE, OUT_OF_STOCK, etc.)
     */
    public ProductStatus getStatus() {
        return status;
    }

    /**
     * Sets the product status.
     * 
     * @param status New product status (controls visibility and purchasability)
     */
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    /**
     * Gets the tenant identifier for multi-tenant architecture.
     * 
     * @return Tenant ID (seller/vendor/brand ID - any tenant type)
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets the tenant identifier.
     * 
     * @param tenantId Multi-tenant ID for data isolation and revenue tracking
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Returns a string representation of the Product.
     * Useful for logging, debugging, and development.
     * 
     * @return Formatted string containing all product fields
     */
    @Override
    public String toString() {
        return "Product [" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", status=" + status +
                ", tenantId='" + tenantId + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", imageUrlSmall='" + imageUrlSmall + '\'' +
                ", imageUrlMedium='" + imageUrlMedium + '\'' +
                ", imageUrlLarge='" + imageUrlLarge + '\'' +
                ']';
    }

}
