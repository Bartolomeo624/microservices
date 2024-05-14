package com.pioon.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Cart createCart(Cart cart) {
        //check if product exists
        Boolean result = webClientBuilder.build().get()
                .uri("http://product/product/check",
                        uriBuilder -> uriBuilder.queryParam("idList", cart.getProductIdList()).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (result){
            return saveCart(cart);
        }
        else{
            throw new IllegalArgumentException("Product doesn't exist.");
        }
    }

    @Override
    public Cart updateCart(Cart cart) {
        if (cartRepository.existsById(cart.getCartId())) {
            saveCart(cart);
        }
        throw new RuntimeException("Cart does not exist");
    }

    @Override
    public void deleteCart(long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
        }
    }


    @Override
    public Cart getCart(long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            throw new RuntimeException("Cart does not exist");
        }
    }

    @Override
    public List<Cart> getAllCarts() {
        return (List<Cart>) cartRepository.findAll();
    }

    private Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

}
