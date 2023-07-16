package com.zam.dev.food_order.model;

import com.zam.dev.food_order.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String id;

    private AdminResponse(){

    }

    public static AdminResponse getInstance(Admin admin) {
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setId(admin.getId());
        adminResponse.setUsername(admin.getUsername());
        adminResponse.setFirstName(admin.getFirstName());
        adminResponse.setLastName(admin.getLastName());
        return adminResponse;
    }

}
