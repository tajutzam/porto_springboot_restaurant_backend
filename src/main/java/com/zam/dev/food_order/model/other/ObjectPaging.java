package com.zam.dev.food_order.model.other;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectPaging {

    private int page;
    private int size;

}
