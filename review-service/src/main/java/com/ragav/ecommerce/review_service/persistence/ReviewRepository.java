package com.ragav.ecommerce.review_service.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Integer> {

    @Transactional(readOnly = true)
    List<ReviewEntity> findByProductId(String productId);

    @Transactional(readOnly = true)
    Optional<ReviewEntity> findByReviewId(String reviewId);
}