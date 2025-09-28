package com.ragav.ecommerce.product_composite_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.ragav.ecommerce.api.composite.product.ProductAggregate;
import com.ragav.ecommerce.api.composite.product.ProductCompositeService;
import com.ragav.ecommerce.product_composite_service.services.ProductCompositeServiceImpl;

/*
 * Controller to handle the composite product service requests
 * It implements the ProductCompositeService interface to provide the necessary endpoints
 * for creating, retrieving, and deleting product aggregates.
 * This controller acts as a facade to the underlying product, review, and recommendation services,
 * aggregating their responses into a single composite response.
 * @Author Rakesh Vasudevan
 * @Date 2024-10-01
 */

@RestController
public class ProductCompositeController implements ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeController.class);

    private final ProductCompositeServiceImpl productCompositeServiceImpl;

    public ProductCompositeController(ProductCompositeServiceImpl productCompositeServiceImpl) {
        this.productCompositeServiceImpl = productCompositeServiceImpl;
    }

    /**
     * Get a product aggregate.
     * 
     * @param productId ID of the product
     * @return the product aggregate
     */
    @Override
    public ProductAggregate getProduct(int productId) {
        LOG.debug("ProductCompositeController.getProduct: productId={}", productId);
        return productCompositeServiceImpl.getProduct(productId);
    }

    /**
     * Delete a product aggregate.
     * 
     * @param productId ID of the product to delete
     */
    @Override
    public void deleteProduct(int productId) {
        LOG.debug("ProductCompositeController.deleteProduct: productId={}", productId);
        productCompositeServiceImpl.deleteProduct(productId);
    }

}
