package com.zam.dev.food_order.controller.users;


import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.UserResponse;
import com.zam.dev.food_order.model.UserUpdateRequest;
import com.zam.dev.food_order.model.WebResponse;
import com.zam.dev.food_order.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @PutMapping(path = "/avatar" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> userUpdateAvatar(@RequestPart("avatar") MultipartFile file , User user){
        UserResponse userResponse = userService.updateAvatar(file, user);
        return WebResponse.builder().data(userResponse).message("OK").status(HttpStatus.OK.value()).build();
    }

    @GetMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> getUser(User user){
        UserResponse response = userService.getUser(user);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateUser(@RequestBody UserUpdateRequest request , User user){
        UserResponse userResponse = userService.updateUser(request, user);
        return WebResponse.builder().data(userResponse).message("OK").status(HttpStatus.OK.value()).build();
    }
}
