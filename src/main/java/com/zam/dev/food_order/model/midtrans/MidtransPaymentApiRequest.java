package com.zam.dev.food_order.model.midtrans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MidtransPaymentApiRequest {

    @NotBlank
    private String cartId;
    @NotBlank
    @Pattern(regexp = "bca|bri", message = "Invalid value. Allowed values are: bca, bri")
    private String bank_transfer;

}
