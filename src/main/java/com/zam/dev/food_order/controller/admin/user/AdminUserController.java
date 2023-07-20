package com.zam.dev.food_order.controller.admin.user;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.other.ObjectPageResponse;
import com.zam.dev.food_order.model.user.UserResponse;
import com.zam.dev.food_order.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/")
public class AdminUserController {

    private UserService userService;

    @GetMapping(value = "/users" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPageResponse<Object> getUsers(Admin admin , @RequestParam(name = "page" , defaultValue = "0" , required = false) int page , @RequestParam(name = "size" , defaultValue = "10" , required = true) int size){
        PageImpl<UserResponse> responsePage = userService.findAll(page, size);
        List<UserResponse> responses = responsePage.getContent();
        return ObjectPageResponse.builder()
                .page(responsePage.getNumber())
                .size(responses.size())
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(responses)
                .build();
    }

}
