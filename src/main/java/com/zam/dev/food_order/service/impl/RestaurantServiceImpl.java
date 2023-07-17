package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.properties.FileProperties;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.FileUploadService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.RestaurantService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {



    private RestaurantRepository restaurantRepository;

    private ValidationService validationService;

    private Bcrypt bcrypt;


    private FileProperties fileProperties;


    private FileUploadService fileUploadService;


    private JwtService jwtService;


    private ApplicationProperties applicationProperties;

    @Override
    @Transactional
    public TokenResponse register(RestaurantRegisterRequest request , MultipartFile multipartFile) {
        validationService.validate(request);

        if(multipartFile.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "please insert a image");
        }

        Optional<Restaurant> restaurantOptional = restaurantRepository.findByUsername(request.getUsername());
        if(restaurantOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already been taken");
        }

        String bannerName = fileUploadService.upload(multipartFile, "restaurant", fileProperties.getRestaurant());

        Restaurant restaurant = new Restaurant();
        restaurant.setUsername(request.getUsername());
        restaurant.setPassword(bcrypt.hashPw(request.getPassword()));
        restaurant.setFirstName(request.getFirstName());
        restaurant.setLastName(request.getLastName());
        restaurant.setAddress(request.getAddress());
        restaurant.setBanner(bannerName);
        restaurant.setId(UUID.randomUUID().toString());

        String token = jwtService.generateToken(restaurant);
        String refreshToken = jwtService.generateRefreshToken(restaurant);

        restaurant.setToken(token);
        restaurant.setRefreshToken(refreshToken);
        restaurantRepository.save(restaurant);
        return TokenResponse.builder().token(token).refreshToken(refreshToken).build();
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);
        Restaurant restaurant = restaurantRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "username or password wrong"));
        if (bcrypt.checkPw(loginRequest.getPassword() , restaurant.getPassword())) {
            String token = jwtService.generateToken(restaurant);
            String refreshToken = jwtService.generateRefreshToken(restaurant);
            restaurant.setToken(token);
            restaurant.setRefreshToken(refreshToken);
            restaurantRepository.save(restaurant);
            return TokenResponse.builder().refreshToken(refreshToken).token(token).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "username or password wrong");
    }

    @Override
    public TokenResponse token(RefreshTokenRequest tokenRequest) {

        validationService.validate(tokenRequest);

        Restaurant restaurant = restaurantRepository.findByRefreshToken(tokenRequest.getRefreshToken()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "your token is not valid"));

        if (jwtService.isTokenValid(tokenRequest.getRefreshToken() , restaurant)) {
            String token = jwtService.generateToken(restaurant);
            restaurant.setToken(token);
            restaurantRepository.save(restaurant);
            return TokenResponse.builder().refreshToken(tokenRequest.getRefreshToken()).token(token).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "your token is not valid or expired");
    }

    @Override
    public RestaurantResponse castToRestaurantResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .address(restaurant.getAddress())
                .firstName(restaurant.getFirstName())
                .lastName(restaurant.getLastName())
                .username(restaurant.getUsername())
                .banner("http://localhost:"+applicationProperties.getPort()+"/images/restaurant/"+restaurant.getBanner())
                .build();
    }

    @Override
    public RestaurantResponse get(Restaurant restaurant) {
        return castToRestaurantResponse(restaurant);
    }

}
