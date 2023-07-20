package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.RefreshTokenRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.user.UserRegisterRequest;
import com.zam.dev.food_order.model.user.UserResponse;
import com.zam.dev.food_order.model.user.UserUpdateRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    PageImpl<UserResponse> findAll(int page , int size);

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse createNewToken(RefreshTokenRequest request);

    TokenResponse register(UserRegisterRequest request);

    UserResponse updateAvatar(MultipartFile multipartFile , User user);

    UserResponse getUser(User user);

    UserResponse updateUser(UserUpdateRequest request , User user);


}
