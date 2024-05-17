package com.pioon.order;

import com.pioon.cart.Cart;
import com.pioon.cart.CartRepository;
import com.pioon.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    private final CartRepository cartRepository;
    private final CartService cartService;


    @Override
    public Order createOrder(Order order) {

        Boolean result = webClientBuilder.build().get()
                .uri("http://cartOrders/cart/check",
                        uriBuilder -> uriBuilder.queryParam("idList", order.getCartIdList()).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (result){
            return saveOrder(order);
        }
        else{
            throw new IllegalArgumentException("Cart doesn't exist.");
        }
    }

    @Override
    public Order updateOrder(Order order) {
        if (orderRepository.existsById(order.getOrderId())) {
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
    public Map<String, Object> getOrderDetails(long id){
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get().getOrderId());
            response.put("orderStatus", order.get().getOrderStatus());

            List<Cart> cartList = new ArrayList<>();
            List<Map<String, Object>> carts = new ArrayList<>();

            for(Long x : order.get().getCartIdList()){

                response.putAll(cartService.getCartWithProduct(x));
            }

            return response;

        } else {
            throw new RuntimeException("Cart does not exist");
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
