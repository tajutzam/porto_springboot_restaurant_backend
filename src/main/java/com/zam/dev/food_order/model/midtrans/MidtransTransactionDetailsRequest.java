package com.zam.dev.food_order.model.midtrans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MidtransTransactionDetailsRequest {

    @NotBlank
    private String order_id;
    @NotNull
    private int gross_amount;

}
