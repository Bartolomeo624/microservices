package com.pioon.cartorder.cart;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> findCartByUserName(String userName);

}
