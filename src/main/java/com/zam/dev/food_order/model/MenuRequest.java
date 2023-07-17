package com.zam.dev.food_order.model;

import com.zam.dev.food_order.entity.STATUS_MENU;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {

    @NotBlank
    private String name;
    @Min(value = 1000)
    @Max(value = 1000000)
    private int price;
    @NotNull
    private STATUS_MENU status_menu;
    @NotBlank
    private String category_id;

}
