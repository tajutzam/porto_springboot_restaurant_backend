package com.zam.dev.food_order.controller.admin;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.CategoryService;
import com.zam.dev.food_order.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;
import java.util.List;

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
