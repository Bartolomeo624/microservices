package com.pioon.review.reviews;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewsByUserName(String userName);
    List<Review> findReviewsByProductName(String productName);
}
