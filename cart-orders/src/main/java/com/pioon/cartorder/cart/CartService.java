package com.pioon.cartorder.cart;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface CartService {

    Mono<Cart> createCart(Cart cart);  // Reactive due to ProductApiClient call

    Mono<Cart> updateCart(Cart cart);  // Reactive due to ProductApiClient call

    Mono<Cart> addProduct(Cart cart, Long productId);  // Reactive due to ProductApiClient call

    Mono<Void> deleteCart(long id);  // Not using ProductApiClient, can be non-reactive

    Mono<Cart> getCart(long id);  // Not using ProductApiClient, can be non-reactive

    Mono<Map<String, Object>> getCartWithProduct(long id);  // Reactive due to ProductApiClient call

    Mono<List<Cart>> getAllCarts();  // Not using ProductApiClient, can be non-reactive

    Mono<Boolean> cartExists(long cartId);  // Not using ProductApiClient, can be non-reactive
}
