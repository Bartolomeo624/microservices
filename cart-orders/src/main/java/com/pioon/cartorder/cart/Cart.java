package com.pioon.cartorder.cart;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cartId;

    private long userId;
    private String userName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cart_product_ids", joinColumns = @JoinColumn(name = "cart_id"))
    @Column(name = "product_id")
    private List<Long> productIdList;
}