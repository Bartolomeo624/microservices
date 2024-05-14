package com.pioon.order;

import com.pioon.cart.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;


    @Override
    public Order createOrder(Order order) {
        return saveOrder(order);
    }

    @Override
    public Order updateOrder(Order order) {
        if (orderRepository.existsById(order.getId())) {
            saveOrder(order);
        }
        throw new RuntimeException("Order does not exist");
    }

    @Override
    public void deleteOrder(long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        }
    }

    @Override
    public Order getOrder(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new RuntimeException("Order does not exist");
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }


    private Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

}
