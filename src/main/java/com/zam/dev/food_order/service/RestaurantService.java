package com.zam.dev.food_order.service;

import com.zam.dev.food_order.model.LoginRequest;
import com.zam.dev.food_order.model.RefreshTokenRequest;
import com.zam.dev.food_order.model.RestaurantRegisterRequest;
import com.zam.dev.food_order.model.TokenResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {

    TokenResponse register(RestaurantRegisterRequest request , MultipartFile multipartFile);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse token(RefreshTokenRequest tokenRequest);

}
