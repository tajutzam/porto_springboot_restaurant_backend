package com.zam.dev.food_order.model.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String banner;
    private long bank_number;
}
