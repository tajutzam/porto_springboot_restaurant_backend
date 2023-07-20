package com.zam.dev.food_order.model.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MenuOnCartDetailResponse {

    private String id;
    private String images;
}
