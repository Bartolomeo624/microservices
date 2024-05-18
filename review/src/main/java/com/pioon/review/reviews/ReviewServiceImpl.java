package com.pioon.review.reviews;

import com.pioon.review.consumers.ProductApiClient;
import com.pioon.review.consumers.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductApiClient productApiClient;

    @Override
    public Mono<Review> createReview(Review review) {
        return productApiClient.checkIfProductsExist(Collections.singletonList(review.getProductId()))
                .flatMap(productExists -> {
                    if (productExists) {
                        return Mono.fromCallable(() -> reviewRepository.save(review))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new IllegalArgumentException("Product doesn't exist."));
                    }
                });
    }

    @Override
    public Mono<Review> updateReview(Review review) {
        return Mono.fromCallable(() -> reviewRepository.existsById(review.getReviewId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.fromCallable(() -> reviewRepository.save(review))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new RuntimeException("Review does not exist"));
                    }
                });
    }

    @Override
    public Mono<Void> deleteReview(long id) {
        return Mono.fromCallable(() -> reviewRepository.existsById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.fromRunnable(() -> reviewRepository.deleteById(id))
                                .subscribeOn(Schedulers.boundedElastic())
                                .then();
                    } else {
                        return Mono.error(new RuntimeException("Review does not exist"));
                    }
                });
    }

    @Override
    public Mono<Review> getReview(long id) {
        return Mono.fromCallable(() -> reviewRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalReview -> Mono.justOrEmpty(optionalReview))
                .switchIfEmpty(Mono.error(new RuntimeException("Review does not exist")));
    }

    @Override
    public Mono<Map<String, Object>> getReviewWithProduct(long id) {
        return Mono.fromCallable(() -> reviewRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalReview -> {
                    if (optionalReview.isPresent()) {
                        return productApiClient.getProduct(optionalReview.get().getProductId())
                                .map(product -> {
                                    Map<String, Object> response = new HashMap<>();
                                    Review review = optionalReview.get();
                                    response.put("reviewId", review.getReviewId());
                                    response.put("userId", review.getUserId());
                                    response.put("stars", review.getStars());
                                    response.put("text", review.getText());
                                    response.put("userName", review.getUserName());
                                    response.put("product", product);
                                    return response;
                                });
                    } else {
                        return Mono.error(new RuntimeException("Review does not exist"));
                    }
                });
    }

    @Override
    public Mono<List<Review>> getAllReviews() {
        return Mono.fromCallable(() -> {
            Iterable<Review> reviewsIterable = reviewRepository.findAll();
            List<Review> reviewList = new ArrayList<>();
            reviewsIterable.forEach(reviewList::add);
            return reviewList;
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
