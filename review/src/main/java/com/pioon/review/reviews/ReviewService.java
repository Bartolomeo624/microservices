package com.pioon.review.reviews;

import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

public interface ReviewService {

    Mono<Review> createReview(Review review);

    Mono<Review> updateReview(Review review);

    Mono<Void> deleteReview(long id);

    Mono<Review> getReview(long id);

    Mono<Map<String, Object>> getReviewWithProduct(long id);

    Mono<List<Review>> getAllReviews();
}
