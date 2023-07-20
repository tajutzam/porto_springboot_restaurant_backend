package com.zam.dev.food_order.model.transaksi;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartRequest {

    @NotBlank
    private String restaurant_id;

}
