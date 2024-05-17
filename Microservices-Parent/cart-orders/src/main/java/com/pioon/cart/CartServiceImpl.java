package com.pioon.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

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
    public Cart addProduct(Cart cart, Long productId){
        if (cartRepository.existsById(cart.getCartId())){
            List<Long> productIds = cart.getProductIdList();
            productIds.add(productId);
            cart.setProductIdList(productIds);
            return saveCart(cart);
        }
        else {
            throw new RuntimeException("Cart does not exist");
        }
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
    public Map<String, Object> getCartWithProduct(long id){
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {

            Map<String, Object> response = new HashMap<>();
            response.put("cartId", cart.get().getCartId());
            response.put("userId", cart.get().getUserId());
            response.put("userName", cart.get().getUserName());

            List<Product> productList = new ArrayList<>();
            for (Long x : cart.get().getProductIdList()){
                Product tempProduct = webClientBuilder.build()
                        .get()
                        .uri("http://product/product/{id}", x)
                        .retrieve()
                        .bodyToMono(Product.class)
                        .block();
                
                productList.add(tempProduct);
            }

            response.put("productList", productList);
            return response;

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

    @Override
    public boolean cartExists(List<Long> idList) {
        List<Boolean> objectCheck = new ArrayList<>();

        for (long cartId : idList){
            boolean cartExists = cartRepository.findById(cartId).isPresent();

            objectCheck.add(cartExists);
        }
        return objectCheck.stream().allMatch(Boolean::booleanValue);
    }

}
