package com.zam.dev.food_order.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone_number;
    private String avatar;

}
