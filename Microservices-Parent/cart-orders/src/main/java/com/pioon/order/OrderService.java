package com.pioon.order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(long id);
    Order getOrder(long id);

    Map<String, Object> getOrderDetails(long id);
    List<Order> getAllOrders();

}
