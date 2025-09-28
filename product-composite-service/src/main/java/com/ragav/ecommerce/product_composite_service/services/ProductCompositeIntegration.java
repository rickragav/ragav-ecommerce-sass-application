
package com.ragav.ecommerce.product_composite_service.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ragav.ecommerce.api.core.product.Product;
import com.ragav.ecommerce.api.core.product.ProductService;
import com.ragav.ecommerce.api.core.review.Review;
import com.ragav.ecommerce.api.core.review.ReviewService;
import com.ragav.ecommerce.api.exceptions.InvalidInputException;
import com.ragav.ecommerce.api.exceptions.NotFoundException;
import com.ragav.ecommerce.utils.http.HttpErrorInfo;

@Component
public class ProductCompositeIntegration implements ProductService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String reviewServiceUrl;

    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort) {

        this.productServiceUrl = String.format("http://%s:%d/product", productServiceHost, productServicePort);

        this.reviewServiceUrl = String.format("http://%s:%d/review", reviewServiceHost, reviewServicePort);

        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    @Override
    public Product createProduct(Product body) {
        throw new UnsupportedOperationException("Unimplemented method 'createProduct'");
    }

    @Override
    public Product getProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            LOG.debug("Will call the getProduct API on URL: {}", url);

            Product result = restTemplate.getForObject(url, Product.class);

            if (result == null) {
                LOG.debug("No product found for productId: {}", productId);
                return null;

            }

            LOG.debug("Found a product with productId: {}", result.getProductId());

            return result;

        } catch (HttpClientErrorException ex) {
            LOG.warn("Got an exception while calling getProduct API: {}", ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            String url = productServiceUrl + "/" + productId;
            LOG.debug("Will call the deleteProduct API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public Review createReview(Review body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createReview'");
    }

    @Override
    public Review getReview(String reviewId) {
        throw new UnsupportedOperationException("Unimplemented method 'getReview'");
    }

    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the getReviews API on URL: {}", url);

            List<Review> result = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
                    })
                    .getBody();

            if (result == null) {
                LOG.debug("No reviews found for productId: {}", productId);
                return null;

            }

            LOG.debug("Found {} reviews for productId: {}", result.size(), productId);

            return result;

        } catch (HttpClientErrorException ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteReviews(int productId) {
        try {
            String url = reviewServiceUrl + "?productId=" + productId;
            LOG.debug("Will call the deleteReviews API on URL: {}", url);

            restTemplate.delete(url);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (HttpStatus.resolve(ex.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(ex));

            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

}
