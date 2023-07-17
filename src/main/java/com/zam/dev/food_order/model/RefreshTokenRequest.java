package com.zam.dev.food_order.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

}
