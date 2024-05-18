package com.pioon.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public Mono<ResponseEntity<Order>> saveOrder(@RequestBody Order order) {
        return orderService.createOrder(order)
                .map(newOrder -> new ResponseEntity<>(newOrder, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));
    }

    @GetMapping("/order/{id}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable("id") long id) {
        return orderService.getOrder(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/order/details/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getOrderDetails(@PathVariable("id") long id) {
        return orderService.getOrderDetails(id)
                .map(orderDetails -> new ResponseEntity<>(orderDetails, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/order")
    public Mono<ResponseEntity<Order>> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order)
                .map(updatedOrder -> new ResponseEntity<>(updatedOrder, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/order")
    public Mono<ResponseEntity<List<Order>>> getOrders() {
        return orderService.getAllOrders()
                .map(orders -> new ResponseEntity<>(orders, HttpStatus.OK));
    }

    @DeleteMapping("/order/{id}")
    public Mono<ResponseEntity<Void>> deleteOrder(@PathVariable("id") long id) {
        return orderService.deleteOrder(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
}
