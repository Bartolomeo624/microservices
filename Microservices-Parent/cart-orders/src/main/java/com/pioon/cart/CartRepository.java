package com.pioon.cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> findCartByUserName(String userName);

}
