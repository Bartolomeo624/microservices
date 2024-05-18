package com.pioon.cartorder.consumers;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductDTO {
    private long id;
    private String name;
    private float price;
}
