package com.zam.dev.food_order.model.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashRestaurantResponse {

    private String id;
    private long bank_number;
    private String restaurant_name;
    private long balance;

}
