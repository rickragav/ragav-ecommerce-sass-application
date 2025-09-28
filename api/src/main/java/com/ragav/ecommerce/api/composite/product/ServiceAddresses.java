package com.ragav.ecommerce.api.composite.product;

public class ServiceAddresses {
    private final String productCatalogService;
    private final String productService;
    private final String reviewService;

    public ServiceAddresses(String productCatalogService, String productService, String reviewService) {
        this.productCatalogService = productCatalogService;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    public String getProductCatalogService() {
        return productCatalogService;
    }

    public String getProductService() {
        return productService;
    }

    public String getReviewService() {
        return reviewService;
    }

}
