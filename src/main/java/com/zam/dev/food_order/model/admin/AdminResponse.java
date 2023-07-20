package com.zam.dev.food_order.model.admin;

import com.zam.dev.food_order.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String id;


}
