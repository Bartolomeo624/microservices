package com.pioon.review.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Review createReview(Review review) {

        Boolean result = webClientBuilder.build().get()
                .uri("http://product/product/check",
                        uriBuilder -> uriBuilder.queryParam("idList", review.getProductId()).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (result){
            return saveReview(review);
        }
        else{
            throw new IllegalArgumentException("Product doesn't exist.");
        }
    }

    @Override
    public Review updateReview(Review review) {
        if(reviewRepository.existsById(review.getReviewId())) {
            saveReview(review);
        }
        throw new RuntimeException("Review does not exist");
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
            throw new RuntimeException("Review does not exist");
        }
    }

    @Override
    public Map<String, Object> getReviewWithProduct(long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {

            Map<String, Object> response = new HashMap<>();


            Product product = webClientBuilder.build()
                    .get()
                    .uri("http://product/product/{id}", review.get().getProductId())
                    .retrieve()
                    .bodyToMono(Product.class)
                    .block();

            response.put("reviewId", review.get().getReviewId());
            response.put("userId", review.get().getUserId());
            response.put("stars", review.get().getStars());
            response.put("text", review.get().getText());
            response.put("userName", review.get().getUserName());
            response.put("product", product);
            return response;

        } else {
            throw new RuntimeException("Review does not exist");
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
