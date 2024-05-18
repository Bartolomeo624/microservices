package com.pioon.cartorder.consumers;

import lombok.Data;

import java.util.List;

@Data
public class ProductIdListDTO {
    private List<Long> productIDs;
}
