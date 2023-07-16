package com.zam.dev.food_order.service;

import com.zam.dev.food_order.model.LoginRequest;
import com.zam.dev.food_order.model.RefreshTokenRequest;
import com.zam.dev.food_order.model.TokenResponse;

public interface AdminService {


    TokenResponse login(LoginRequest loginRequest);

    TokenResponse token(RefreshTokenRequest request);

}
