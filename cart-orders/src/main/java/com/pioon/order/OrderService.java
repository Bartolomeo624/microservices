package com.pioon.order;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Mono<Order> createOrder(Order order);

    Mono<Order> updateOrder(Order order);

    Mono<Void> deleteOrder(long id);

    Mono<Order> getOrder(long id);

    Mono<Map<String, Object>> getOrderDetails(long id);

    Mono<List<Order>> getAllOrders();
}
