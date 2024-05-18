package com.pioon.cartorder.consumers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductApiClient {
    private final WebClient.Builder webClientBuilder;

    public Mono<Boolean> checkIfProductsExist(List<Long> idList) {
        ProductIdListDTO productIdListDTO = new ProductIdListDTO();
        productIdListDTO.setProductIDs(idList);

        return webClientBuilder.build()
                .post()
                .uri("http://product/products/check")
                .bodyValue(productIdListDTO)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<ProductDTO> getProduct(long productId) {
        return webClientBuilder.build()
                .get()
                .uri("http://product/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }
}
