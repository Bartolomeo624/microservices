package com.pioon.consumers;

import lombok.Data;

import java.util.List;

@Data
public class ProductIdListDTO {
    private List<Long> productIDs;
}
