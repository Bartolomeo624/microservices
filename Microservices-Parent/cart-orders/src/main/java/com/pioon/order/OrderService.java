package com.pioon.order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(long id);
    Order getOrder(long id);
    List<Order> getAllOrders();
}
