package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.LoginRequest;
import com.zam.dev.food_order.model.RefreshTokenRequest;
import com.zam.dev.food_order.model.TokenResponse;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;
    private Bcrypt bcrypt;
    private JwtService jwtService;
    private ValidationService validationService;
    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);

        Optional<Admin> optionalAdmin = adminRepository.findByUsername(loginRequest.getUsername());
        if(optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            if (bcrypt.checkPw(loginRequest.getPassword() , admin.getPassword())) {
                String token = jwtService.generateToken(admin);
                String refreshToken = jwtService.generateRefreshToken(admin);
                admin.setToken(token);
                admin.setRefreshToken(refreshToken);
                adminRepository.save(admin);
                return TokenResponse.builder()
                        .token(token).refreshToken(refreshToken)
                        .build();
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "please check your username or password");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED , "please check your username or password");
    }

    @Override
    @Transactional
    public TokenResponse token(RefreshTokenRequest request) {
        validationService.validate(request);
        Admin admin = adminRepository.findByRefreshToken(request.getRefreshToken()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your token is not valid please login again"));
        if (jwtService.isTokenValid(request.getRefreshToken() , admin)) {
            String token = jwtService.generateToken(admin);
            admin.setToken(token);
            adminRepository.save(admin);
            return TokenResponse.builder().refreshToken(request.getRefreshToken()).token(token).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "your token is not valid or expired");
    }

}
