package com.pioon.cart;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CartService {

    Cart createCart(Cart cart);

    Cart updateCart(Cart cart);

    Cart addProduct(Cart cart, Long productId);

    void deleteCart(long id);

    Cart getCart(long id);

    Map<String, Object> getCartWithProduct(long id);
    List<Cart> getAllCarts();

    boolean cartExists(List<Long> idList);
}
