package com.zam.dev.food_order.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String username;
    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String password;

}
