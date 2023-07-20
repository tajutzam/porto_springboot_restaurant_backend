package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.RefreshTokenRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.restaurant.CashRestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.service.AdminService;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;


    private TokenResponse tokenResponse;

    Admin admin;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        admin =   adminRepository.findByUsername(loginRequest.getUsername()).orElse(null);
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

    @Test
    void testGetRestaurant(){
        ObjectPagingResponse<List<RestaurantResponse>> restaurants = adminService.restaurants(admin, 0, 1);
        assertNotNull(restaurants);
        assertNotNull(restaurants.getData());
    }

    @Test
    void testGetBalance(){
        for (int i = 0; i < 5 ; i++) {
            Restaurant restaurant = new Restaurant();
            restaurant.setUsername(String.valueOf(i));
            restaurant.setId(String.valueOf(i));
            restaurant.setAddress("banner");
            restaurant.setBalance(i);
            restaurant.setFirstName("firstname");
            restaurant.setLastName("lastname");
            restaurant.setToken("token");
            restaurant.setRefreshToken("refreshtoken");
            restaurant.setPassword("rahasia");
            restaurantRepository.save(restaurant);
        }

        List<CashRestaurantResponse> responses = adminService.getBalances(admin);
        assertEquals(4, responses.size());

    }

    @Test
    void testUpdateBalance(){
        for (int i = 0; i < 5 ; i++) {
            Restaurant restaurant = new Restaurant();
            restaurant.setUsername(String.valueOf(i));
            restaurant.setId(String.valueOf(i));
            restaurant.setAddress("banner");
            restaurant.setBalance(i);
            restaurant.setFirstName("firstname");
            restaurant.setLastName("lastname");
            restaurant.setToken("token");
            restaurant.setRefreshToken("refreshtoken");
            restaurant.setPassword("rahasia");
            restaurant.setBankNumber(i);
            restaurantRepository.save(restaurant);
        }
        int pay = adminService.pay("1");
        assertEquals(1 , pay);
        Restaurant restaurant = restaurantRepository.findById("1").orElse(null);
        assertEquals(0 , restaurant.getBalance());

    }


}