package com.pioon.cart;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    private long productId;
    private String productName;
    private float productPrice;
}