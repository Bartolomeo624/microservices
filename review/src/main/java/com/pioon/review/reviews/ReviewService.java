package com.pioon.review.reviews;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    Review createReview(Review review);
    Review updateReview(Review review);
    void deleteReview(long id);
    Review getReview(long id);

    Map<String, Object> getReviewWithProduct(long id);
    List<Review> getAllReviews();
}
