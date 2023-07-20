package com.zam.dev.food_order.model.midtrans;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MidtransCustomerDetailRequest {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String first_name;
    @NotBlank
    private String last_name;
    @NotBlank
    private String phone;

}
