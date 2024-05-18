package com.pioon.cartorder.order;

import com.pioon.cartorder.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    public Mono<Order> createOrder(Order order) {
        return cartService.cartExists(order.getCart().getCartId())
                .flatMap(cartExists -> {
                    if (cartExists) {
                        return Mono.fromCallable(() -> orderRepository.save(order))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new IllegalArgumentException("Cart doesn't exist."));
                    }
                });
    }

    @Override
    public Mono<Order> updateOrder(Order order) {
        return Mono.fromCallable(() -> orderRepository.existsById(order.getOrderId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.fromCallable(() -> orderRepository.save(order))
                                .subscribeOn(Schedulers.boundedElastic());
                    } else {
                        return Mono.error(new RuntimeException("Order does not exist"));
                    }
                });
    }

    @Override
    public Mono<Void> deleteOrder(long id) {
        return Mono.fromCallable(() -> orderRepository.existsById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.fromRunnable(() -> orderRepository.deleteById(id))
                                .subscribeOn(Schedulers.boundedElastic())
                                .then();
                    } else {
                        return Mono.error(new RuntimeException("Order does not exist"));
                    }
                });
    }

    @Override
    public Mono<Order> getOrder(long id) {
        return Mono.fromCallable(() -> orderRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .switchIfEmpty(Mono.error(new RuntimeException("Order does not exist")));
    }

    @Override
    public Mono<Map<String, Object>> getOrderDetails(long id) {
        return Mono.fromCallable(() -> orderRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalOrder -> {
                    if (optionalOrder.isPresent()) {
                        return cartService.getCartWithProduct(optionalOrder.get().getCart().getCartId())
                                .map(cart -> {
                                    Map<String, Object> response = new HashMap<>();
                                    response.put("orderId", optionalOrder.get().getOrderId());
                                    response.put("orderStatus", optionalOrder.get().getOrderStatus());
                                    response.putAll(cart);
                                    return response;
                                });
                    } else {
                        return Mono.error(new RuntimeException("Order does not exist"));
                    }
                });
    }

    @Override
    public Mono<List<Order>> getAllOrders() {
        return Mono.fromCallable(() -> {
            Iterable<Order> ordersIterable = orderRepository.findAll();
            List<Order> orderList = new ArrayList<>();
            ordersIterable.forEach(orderList::add);
            return orderList;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
