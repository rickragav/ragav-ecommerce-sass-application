package com.ragav.ecommerce.api.core.review;

public enum ReviewStatus {
    ACTIVE, // Normal active review
    PENDING, // Waiting for moderation
    HIDDEN, // Hidden due to policy
    DELETED, // Soft deleted
    SPAM
}
