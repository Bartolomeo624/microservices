package com.pioon.cartorder.cart;

import com.pioon.cartorder.consumers.ProductApiClient;
import com.pioon.cartorder.consumers.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductApiClient productApiClient;

    @Override
    public Mono<Cart> createCart(Cart cart) {
        return productApiClient.checkIfProductsExist(cart.getProductIdList())
                .flatMap(productsExist -> {
                    if (productsExist) {
                        return Mono.fromCallable(() -> {
                            return cartRepository.save(cart);
                        }).subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new IllegalArgumentException("Product doesn't exist."));
                    }
                });
    }

    @Override
    public Mono<Cart> updateCart(Cart cart) {
        return Mono.fromCallable(() -> cartRepository.findById(cart.getCartId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalCart -> {
                    if (optionalCart.isPresent()) {
                        return Mono.fromCallable(() -> cartRepository.save(cart))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new RuntimeException("Cart does not exist"));
                    }
                });
    }

    @Override
    public Mono<Cart> addProduct(Cart cart, Long productId) {
        return Mono.fromCallable(() -> cartRepository.findById(cart.getCartId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalCart -> {
                    if (optionalCart.isPresent()) {
                        List<Long> productIds = new ArrayList<>(optionalCart.get().getProductIdList());
                        productIds.add(productId);
                        optionalCart.get().setProductIdList(productIds);
                        return Mono.fromCallable(() -> cartRepository.save(optionalCart.get()))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new RuntimeException("Cart does not exist"));
                    }
                });
    }

    @Override
    public Mono<Void> deleteCart(long id) {
        return Mono.fromRunnable(() -> {
            if (cartRepository.existsById(id)) {
                cartRepository.deleteById(id);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Cart> getCart(long id) {
        return Mono.fromCallable(() -> cartRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart does not exist")));
    }

    @Override
    public Mono<Map<String, Object>> getCartWithProduct(long id) {
        return Mono.fromCallable(() -> cartRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalCart -> {
                    if (optionalCart.isPresent()) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("cartId", optionalCart.get().getCartId());
                        response.put("userId", optionalCart.get().getUserId());
                        response.put("userName", optionalCart.get().getUserName());

                        List<Mono<ProductDTO>> productMonos = optionalCart.get().getProductIdList().stream()
                                .map(productApiClient::getProduct)
                                .collect(Collectors.toList());

                        return Mono.zip(productMonos, objects -> Arrays.stream(objects)
                                        .map(obj -> (ProductDTO) obj)
                                        .collect(Collectors.toList()))
                                .map(productDTOList -> {
                                    response.put("productList", productDTOList);
                                    return response;
                                });
                    } else {
                        return Mono.error(new RuntimeException("Cart does not exist"));
                    }
                });
    }

    @Override
    public Mono<List<Cart>> getAllCarts() {
        return Mono.fromCallable(() -> {
            Iterable<Cart> cartsIterable = cartRepository.findAll();
            List<Cart> cartList = new ArrayList<>();
            cartsIterable.forEach(cartList::add);
            return cartList;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> cartExists(long cartId) {
        return Mono.fromCallable(() -> cartRepository.existsById(cartId))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
