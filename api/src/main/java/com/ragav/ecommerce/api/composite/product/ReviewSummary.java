package com.ragav.ecommerce.api.composite.product;

public class ReviewSummary {
    private final String reviewId;
    private final String user;
    private final String reviewTitle;
    private final String reviewContent;
    private final int rating;

    public ReviewSummary(String reviewId, String user, String reviewTitle, String reviewContent, int rating) {
        this.reviewId = reviewId;
        this.user = user;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getUser() {
        return user;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public int getRating() {
        return rating;
    }

}
