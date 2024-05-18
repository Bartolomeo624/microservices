package com.pioon.order;

import com.pioon.cart.Cart;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "active_order")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    private String orderStatus;

    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "cartId")
    private Cart cart;
}