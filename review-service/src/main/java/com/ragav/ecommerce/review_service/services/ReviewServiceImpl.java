package com.ragav.ecommerce.review_service.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ragav.ecommerce.api.core.review.Review;
import com.ragav.ecommerce.api.core.review.ReviewService;
import com.ragav.ecommerce.api.exceptions.BadRequestException;
import com.ragav.ecommerce.api.exceptions.InvalidInputException;
import com.ragav.ecommerce.review_service.mappers.ReviewMapper;
import com.ragav.ecommerce.review_service.persistence.ReviewEntity;
import com.ragav.ecommerce.review_service.persistence.ReviewRepository;
import com.ragav.ecommerce.utils.http.ServiceUtil;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository repository;

    private final ReviewMapper mapper;

    private final ServiceUtil serviceUtil;

    public ReviewServiceImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Review createReview(Review body) {
        try {
            // Validate rating range (0-10 based on dataset analysis)
            if (body.getRating() < 0 || body.getRating() > 10) {
                throw new InvalidInputException(
                        "Invalid rating: " + body.getRating() + ". Rating must be between 0 and 10.");
            }

            // Validate productId
            try {
                int productIdInt = Integer.parseInt(body.getProductId());
                if (productIdInt < 1) {
                    throw new InvalidInputException("Invalid productId: " + body.getProductId());
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Invalid productId format: " + body.getProductId());
            }

            // Validate reviewId
            if (body.getReviewId() == null || body.getReviewId().trim().isEmpty()) {
                throw new InvalidInputException("ReviewId cannot be null or empty");
            }

            // Proceed with creating the review
            LOG.debug("createReview: {}", body);

            ReviewEntity entity = mapper.apiToEntity(body);
            ReviewEntity newEntity = repository.save(entity);

            LOG.debug("createReview: created a review entity: {}", newEntity);

            return mapper.entityToApi(newEntity);

        } catch (DataIntegrityViolationException dive) {
            LOG.error("createReview failed: {}", dive.getMessage());
            throw new InvalidInputException(
                    "Duplicate key, Product Id: " + body.getProductId() + ", Review Id: " + body.getReviewId());
        } catch (Exception e) {
            LOG.error("createReview failed: {}", e.getMessage());
            throw new BadRequestException("Server Failure while creating review");
        }
    }

    @Override
    public Review getReview(String reviewId) {
        if (reviewId == null || reviewId.trim().isEmpty()) {
            throw new InvalidInputException("Invalid reviewId: " + reviewId);
        }

        ReviewEntity entity = repository.findByReviewId(reviewId)
                .orElseThrow(() -> new com.ragav.ecommerce.api.exceptions.NotFoundException(
                        "No review found for reviewId: " + reviewId));

        Review review = mapper.entityToApi(entity);
        review.setServiceAddress(serviceUtil.getServiceAddress());

        LOG.debug("getReview: found review for reviewId: {}", reviewId);

        return review;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        String productIdStr = String.valueOf(productId);
        List<ReviewEntity> entityList = repository.findByProductId(productIdStr);

        List<Review> apiList = mapper.entityListToApiList(entityList);
        apiList.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("getReviews: response size: {}", apiList.size());

        return apiList;
    }

    @Override
    public void deleteReviews(int productId) {
        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        String productIdStr = String.valueOf(productId);
        repository.deleteAll(repository.findByProductId(productIdStr));
    }

}
