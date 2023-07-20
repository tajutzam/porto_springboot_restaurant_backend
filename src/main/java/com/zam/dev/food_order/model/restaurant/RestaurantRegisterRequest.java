package com.zam.dev.food_order.model.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRegisterRequest {

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
    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String firstName;
    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String lastName;
    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String address;

    @Range(min = 1000 , max = 100000000000000L)
    private long bank_number;

}
