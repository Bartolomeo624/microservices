package com.pioon.cart;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private long productId;
    private String productName;
    private float productPrice;
}
