package com.pioon.review.reviews;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review);
    Review updateReview(Review review);
    void deleteReview(long id);
    Review getReview(long id);

    List<Review> getAllReviews();
}
