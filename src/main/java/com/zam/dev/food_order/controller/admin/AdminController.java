package com.zam.dev.food_order.controller.admin;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("adminController")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public WebResponse<Object> get(Admin admin){
        return WebResponse.builder().data(adminService.get(admin)).status(HttpStatus.OK.value()).message("OK").build();
    }



}
