package com.pioon.review.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public Mono<ResponseEntity<Review>> saveReview(@RequestBody Review review) {
        return reviewService.createReview(review)
                .map(newReview -> new ResponseEntity<>(newReview, HttpStatus.CREATED));
    }

    @GetMapping("/review/{id}")
    public Mono<ResponseEntity<Review>> getReview(@PathVariable("id") long id) {
        return reviewService.getReview(id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK));
    }

    @GetMapping("/review/full/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getReviewWithProduct(@PathVariable("id") long id) {
        return reviewService.getReviewWithProduct(id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK));
    }

    @PutMapping("/review")
    public Mono<ResponseEntity<Review>> updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review)
                .map(updatedReview -> new ResponseEntity<>(updatedReview, HttpStatus.OK));
    }

    @GetMapping("/review")
    public Mono<ResponseEntity<List<Review>>> getReviews() {
        return reviewService.getAllReviews()
                .map(reviews -> new ResponseEntity<>(reviews, HttpStatus.OK));
    }

    @DeleteMapping("/review/{id}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable("id") long id) {
        return reviewService.deleteReview(id)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }
}
