package com.ragav.ecommerce.api.composite.product;

import java.math.BigDecimal;
import java.util.List;

import com.ragav.ecommerce.api.core.product.ProductStatus;

public class ProductAggregate {
    private final int productId;
    private final String name;
    private final BigDecimal price;
    private final Integer stockQuantity;
    private final ProductStatus status;
    private final String tenantId;
    private final String imageUrlSmall;
    private final String imageUrlMedium;
    private final String imageUrlLarge;
    private final List<ReviewSummary> reviews;
    private final ServiceAddresses serviceAddresses;

    public ProductAggregate() {
        this.productId = 0;
        this.name = null;
        this.reviews = null;
        this.serviceAddresses = null;
        this.price = null;
        this.stockQuantity = null;
        this.status = ProductStatus.AVAILABLE;
        this.tenantId = null;
        this.imageUrlSmall = null;
        this.imageUrlMedium = null;
        this.imageUrlLarge = null;
    }

    public ProductAggregate(int productId, String name, List<ReviewSummary> reviews, ServiceAddresses serviceAddresses,
            BigDecimal price, Integer stockQuantity, ProductStatus status, String tenantId,
            String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.productId = productId;
        this.name = name;
        this.reviews = reviews;
        this.serviceAddresses = serviceAddresses;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status != null ? status : ProductStatus.AVAILABLE;
        this.tenantId = tenantId;
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public List<ReviewSummary> getReviews() {
        return reviews;
    }

    public ServiceAddresses getServiceAddresses() {
        return serviceAddresses;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getImageUrlSmall() {
        return imageUrlSmall;
    }

    public String getImageUrlMedium() {
        return imageUrlMedium;
    }

    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    @Override
    public String toString() {
        return "ProductAggregate [imageUrlLarge=" + imageUrlLarge + ", imageUrlMedium=" + imageUrlMedium
                + ", imageUrlSmall="
                + imageUrlSmall + ", name=" + name + ", price=" + price + ", productId=" + productId + ", reviews="
                + reviews + ", serviceAddresses=" + serviceAddresses + ", status=" + status + ", stockQuantity="
                + stockQuantity + ", tenantId=" + tenantId + "]";
    }

}
