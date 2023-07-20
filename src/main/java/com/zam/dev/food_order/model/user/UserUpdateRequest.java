package com.zam.dev.food_order.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String username;
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

    @NotBlank
    @Size(
            min = 4 , max = 100
    )
    private String password;
    @NotBlank
    @Size(
            min = 11 , max =  13
    )
    private String phone_number;
    @NotBlank
    @Email
    @Size(
            min =  4 , max =  100
    )
    private String email;

}
