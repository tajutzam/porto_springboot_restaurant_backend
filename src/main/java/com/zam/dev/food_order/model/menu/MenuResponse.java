package com.zam.dev.food_order.model.menu;

import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.admin.CategoryResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponse {

    private String id;
    private String name;
    private STATUS_MENU status;
    private String image;
    private CategoryResponse category;
    private RestaurantResponse restaurant;
    private Instant created_at;
    private Instant updated_at;


}
