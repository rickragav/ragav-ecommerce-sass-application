package com.ragav.ecommerce.product_composite_service.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ragav.ecommerce.api.composite.product.ProductAggregate;
import com.ragav.ecommerce.api.composite.product.ProductCompositeService;
import com.ragav.ecommerce.api.composite.product.ReviewSummary;
import com.ragav.ecommerce.api.composite.product.ServiceAddresses;
import com.ragav.ecommerce.api.core.product.Product;
import com.ragav.ecommerce.api.core.review.Review;
import com.ragav.ecommerce.api.exceptions.NotFoundException;
import com.ragav.ecommerce.utils.http.ServiceUtil;

@Service
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.debug("getProduct: retrieves a composite entity for productId: {}", productId);

        Product product = integration.getProduct(productId);

        if (product == null) {
            String message = "No product found for productId: " + productId;
            LOG.warn("getProduct: " + message);
            throw new NotFoundException(message);
        }

        List<Review> reviews = integration.getReviews(productId);

        LOG.debug("getProduct: composite entity for productId: {} found", productId);

        return createProductAggregate(product, reviews, serviceUtil.getServiceAddress());
    }

    @Override
    public void deleteProduct(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
    }

    private ProductAggregate createProductAggregate(Product product, List<Review> reviews, String serviceAddress) {
        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();

        // 2. Copy summary review info, if available

        List<ReviewSummary> reviewSummaries = (reviews == null) ? null
                : reviews.stream()
                        .map(review -> {
                            return new ReviewSummary(
                                    review.getReviewId(),
                                    "user",
                                    review.getReviewTitle(),
                                    review.getReviewText(),
                                    review.getRating());
                        })
                        .toList();

        // 3. Create info regarding the involved microservices addresses
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";

        ServiceAddresses serviceAddresses = new ServiceAddresses(
                serviceAddress, productAddress, reviewAddress);

        return new ProductAggregate(productId, name, reviewSummaries, serviceAddresses);
    }

}
