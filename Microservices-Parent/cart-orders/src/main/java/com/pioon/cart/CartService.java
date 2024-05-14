package com.pioon.cart;

import java.util.List;

public interface CartService {

    Cart createCart(Cart cart);

    Cart updateCart(Cart cart);

    void deleteCart(long id);

    Cart getCart(long id);

    List<Cart> getAllCarts();

}
