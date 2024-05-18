package com.pioon.cartorder.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart")
    public Mono<ResponseEntity<Cart>> saveCart(@RequestBody Cart cart) {
        return cartService.createCart(cart)
                .map(newCart -> new ResponseEntity<>(newCart, HttpStatus.CREATED))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));
    }

    @GetMapping("/cart/{id}")
    public Mono<ResponseEntity<Cart>> getCart(@PathVariable("id") long id) {
        return cartService.getCart(id)
                .map(cart -> new ResponseEntity<>(cart, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/cart/full/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getCartWithProduct(@PathVariable("id") long id) {
        return cartService.getCartWithProduct(id)
                .map(cart -> new ResponseEntity<>(cart, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/cart")
    public Mono<ResponseEntity<Cart>> updateCart(@RequestBody Cart cart) {
        return cartService.updateCart(cart)
                .map(updatedCart -> new ResponseEntity<>(updatedCart, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/cart/addProduct/{id}")
    public Mono<ResponseEntity<Cart>> addProductToCart(@PathVariable("id") long id, @RequestBody long productId) {
        return cartService.getCart(id)
                .flatMap(cart -> cartService.addProduct(cart, productId))
                .map(updatedCart -> new ResponseEntity<>(updatedCart, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/cart")
    public Mono<ResponseEntity<List<Cart>>> getCarts() {
        return cartService.getAllCarts()
                .map(carts -> new ResponseEntity<>(carts, HttpStatus.OK));
    }

    @DeleteMapping("/cart/{id}")
    public Mono<ResponseEntity<Void>> deleteCart(@PathVariable("id") long id) {
        return cartService.deleteCart(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
}
