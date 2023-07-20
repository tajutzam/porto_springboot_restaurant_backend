package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.admin.AdminResponse;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.RefreshTokenRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.restaurant.CashRestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;

import java.util.List;

public interface AdminService {


    TokenResponse login(LoginRequest loginRequest);

    TokenResponse token(RefreshTokenRequest request);

    AdminResponse get(Admin admin);

    List<CashRestaurantResponse> getBalances(Admin admin);

    ObjectPagingResponse<List<RestaurantResponse>> restaurants(Admin admin , int page , int size);

    int pay(String restaurantId);


}
