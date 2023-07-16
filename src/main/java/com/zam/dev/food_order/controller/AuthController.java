package com.zam.dev.food_order.controller;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.RestaurantService;
import com.zam.dev.food_order.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
public class AuthController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/api/restaurant/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> restaurantLogin(@RequestBody LoginRequest loginRequest) {
        TokenResponse response = restaurantService.login(loginRequest);
        return WebResponse.builder()
                .data(response)
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
    }

    @PostMapping(
            path = "/api/restaurant/auth/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> restaurantRegister(@ModelAttribute RestaurantRegisterRequest request, @RequestPart("banner") MultipartFile file) {
        TokenResponse response = restaurantService.register(request, file);
        return WebResponse.builder()
                .data(response)
                .status(HttpStatus.CREATED.value())
                .message("OK")
                .build();
    }

    @PostMapping(
            path = "/api/admin/auth/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> adminLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse servletResponse) {
        servletResponse.setStatus(HttpStatus.OK.value());
        TokenResponse response = adminService.login(loginRequest);
        return WebResponse.builder()
                .data(response)
                .message("OK").status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping(
            path = "/api/admin/auth/token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> adminToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = adminService.token(request);
        return WebResponse.builder().data(tokenResponse).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PostMapping(
            path = "/api/restaurant/auth/token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> restaurantToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = restaurantService.token(request);
        return WebResponse.builder().data(tokenResponse).message("OK").status(HttpStatus.OK.value()).build();
    }
    @PostMapping(path = "/api/user/auth/login"
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> userLogin(@RequestBody LoginRequest loginRequest){
        TokenResponse response = userService.login(loginRequest);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PostMapping(value = "/api/user/auth/register" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> userRegister(@RequestBody UserRegisterRequest request){
        TokenResponse response = userService.register(request);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.CREATED.value()).build();
    }


    @PostMapping("/api/user/auth/token")
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> userCreateNewToken(@RequestBody RefreshTokenRequest request){
        TokenResponse response = userService.createNewToken(request);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }


}
