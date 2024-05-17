package com.pioon.order;

import com.pioon.cart.Cart;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private  final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){ this.orderService = orderService;}

    @PostMapping("/order")
    public ResponseEntity<Order> saveOrder(@RequestBody Order order){
        Order newOrder = orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }
    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") long id){
        Order order = orderService.getOrder(id);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    @GetMapping("/order/details/{id}")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable("id") long id){
        Map<String, Object> order = orderService.getOrderDetails(id);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/order")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order){
        Order updatedOrder = orderService.updateOrder(order);
        return new ResponseEntity<>(updatedOrder,HttpStatus.OK);
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @DeleteMapping("/order/{id}")
    public void deleteOrder(@PathVariable("id") long id){ orderService.deleteOrder(id);}
}
