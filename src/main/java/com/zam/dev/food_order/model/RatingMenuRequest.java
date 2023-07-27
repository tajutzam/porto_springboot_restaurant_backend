package com.zam.dev.food_order.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingMenuRequest {

    @Range(min = 1 , max = 5)
    private int rate;
    @NotBlank
    private String menuId;

}
