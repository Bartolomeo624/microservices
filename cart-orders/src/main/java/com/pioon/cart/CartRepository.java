package com.pioon.cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;


public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> findCartByUserName(String userName);

}
