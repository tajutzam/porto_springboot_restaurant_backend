package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.*;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {

    TokenResponse register(RestaurantRegisterRequest request , MultipartFile multipartFile);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse token(RefreshTokenRequest tokenRequest);

    RestaurantResponse castToRestaurantResponse(Restaurant restaurant);

    RestaurantResponse get(Restaurant restaurant);

    RestaurantResponse update(RestaurantUpdateRequest request , Restaurant restaurant);

    RestaurantResponse updateAvatar(MultipartFile file , Restaurant restaurant);

}
