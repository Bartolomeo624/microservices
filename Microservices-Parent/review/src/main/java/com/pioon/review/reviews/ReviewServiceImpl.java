package com.pioon.review.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(Review review) {
        return saveReview(review);
    }

    @Override
    public Review updateReview(Review review) {
        if(reviewRepository.existsById(review.getId())) {
            saveReview(review);
        }
        throw new RuntimeException("Product does not exist");
    }

    @Override
    public void deleteReview(long id) {
        if(reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        }
    }

    @Override
    public Review getReview(long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            return review.get();
        }
        else {
            throw new RuntimeException("Product does not exist");
        }
    }

    @Override
    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    private Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
}
