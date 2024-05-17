package com.pioon.cart;

import com.pioon.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){ this.cartService = cartService;}

    @PostMapping("/cart")
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart){
        Cart newCart = cartService.createCart(cart);
        return new ResponseEntity<>(newCart, HttpStatus.CREATED);
    }
    @GetMapping("/cart/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable("id") long id){
        Cart cart = cartService.getCart(id);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @GetMapping("/cart/full/{id}")
    public ResponseEntity<Map<String, Object>> getCartWithProduct(@PathVariable("id") long id){
        Map<String, Object> cart = cartService.getCartWithProduct(id);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }
    @PutMapping("/cart")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart){
        Cart updatedCart = cartService.updateCart(cart);
        return new ResponseEntity<>(updatedCart,HttpStatus.OK);
    }

    @PutMapping("/cart/addProduct/{id}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable("id") long id, @RequestBody long productId){
        Cart cart = cartService.addProduct(cartService.getCart(id), productId);
        return new ResponseEntity<>(cart,HttpStatus.OK );
    }

    @GetMapping("/cart")
    public ResponseEntity<List<Cart>> getCarts(){
        List<Cart> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{id}")
    public void deleteCart(@PathVariable("id") long id){ cartService.deleteCart(id);}

    @GetMapping("/cart/check")
    public boolean checkIfProductExists(@RequestParam List<Long> idList){

        return cartService.cartExists(idList);
    }
}
