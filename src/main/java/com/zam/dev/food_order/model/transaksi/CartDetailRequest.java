package com.zam.dev.food_order.model.transaksi;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailRequest {

    @NotBlank
    private String menuId;
    @NotBlank
    private String cartId;

    @Min(value = 1)
    @Max(value = 1000)
    private int qty;

}
