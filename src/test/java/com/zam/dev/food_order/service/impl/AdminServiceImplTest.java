package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.AdminResponse;
import com.zam.dev.food_order.model.LoginRequest;
import com.zam.dev.food_order.model.RefreshTokenRequest;
import com.zam.dev.food_order.model.TokenResponse;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.service.AdminService;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;


    private TokenResponse tokenResponse;


    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        tokenResponse = adminService.login(loginRequest);
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        TokenResponse response = adminService.login(loginRequest);
        assertNotNull(response);

    }

    @Test
    void testLoginFailedUsernameOrPasswordBlank(){
        assertThrows(ConstraintViolationException.class , () -> {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("admin");
            loginRequest.setPassword("");
            TokenResponse response = adminService.login(loginRequest);
        });
    }

    @Test
    void testLoginFailed(){
        assertThrows(ResponseStatusException.class , () -> {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("admin");
            loginRequest.setPassword("wrong");
            TokenResponse response = adminService.login(loginRequest);
        });
    }

    @Test
    void testToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(tokenResponse.getRefreshToken());
        TokenResponse response = adminService.token(request);
        assertNotNull(response);
    }

    @Test
    void testTokenWrong(){
        assertThrows(ResponseStatusException.class , () -> {
            RefreshTokenRequest request = new RefreshTokenRequest();
            request.setRefreshToken(tokenResponse.getToken());
            TokenResponse response = adminService.token(request);
        });
    }
}