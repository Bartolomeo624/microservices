package com.pioon.review.reviews;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService){ this.reviewService = reviewService;}

    @PostMapping("/review")
    public ResponseEntity<Review> saveReview(@RequestBody Review review){
        Review newReview = reviewService.createReview(review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<Review> getReview(@PathVariable("id") long id){
        Review review = reviewService.getReview(id);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }
    @PutMapping("/review")
    public ResponseEntity<Review> updateReview(@RequestBody Review review){
        Review updatedReview = reviewService.updateReview(review);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @GetMapping("/review")
    public ResponseEntity<List<Review>> getReviews(){
        List<Review> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @DeleteMapping("/review/{id}")
    public void deleteReview(@PathVariable("id") long id){
        reviewService.deleteReview(id);
    }
}
