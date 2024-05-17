package com.pioon.order;

import com.pioon.cart.Cart;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private List<Long> cartIdList;

}
