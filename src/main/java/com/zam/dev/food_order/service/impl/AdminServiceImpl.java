package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.admin.AdminResponse;
import com.zam.dev.food_order.model.other.*;
import com.zam.dev.food_order.model.restaurant.CashRestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.RestaurantService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final RestaurantRepository restaurantRepository;
    private AdminRepository adminRepository;
    private Bcrypt bcrypt;
    private JwtService jwtService;
    private ValidationService validationService;
    private RestaurantService restaurantService;
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

    private AdminResponse castToAdminResponse(Admin admin){
        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setId(admin.getId());
        adminResponse.setUsername(admin.getUsername());
        adminResponse.setFirstName(admin.getFirstName());
        adminResponse.setLastName(admin.getLastName());
        return adminResponse;
    }

    @Override
    public AdminResponse get(Admin admin) {
        return castToAdminResponse(admin);
    }

    @Override
    public List<CashRestaurantResponse> getBalances(Admin admin) {
        List<Restaurant> restaurantList = restaurantRepository.findAllByBalanceNot(0);
        return restaurantList.stream().map(this::castToRestaurantResponse).toList();
    }

    private CashRestaurantResponse castToRestaurantResponse(Restaurant restaurant){
        return CashRestaurantResponse.builder().
                id(restaurant.getId())
                        .balance(restaurant.getBalance())
                                .bank_number(restaurant.getBankNumber())
                                        .restaurant_name(restaurant.getFirstName() + " " + restaurant.getLastName())
                .build();
    }



    @Override
    public ObjectPagingResponse<List<RestaurantResponse>> restaurants(Admin admin , int page , int size) {
        if(size == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "size cant 0");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageable);
        ObjectPagingResponse<List<RestaurantResponse>> objResponse = new ObjectPagingResponse<>();
        List<RestaurantResponse> responses = restaurantPage.getContent().stream().map(restaurant -> restaurantService.castToRestaurantResponse(restaurant)).toList();
        objResponse.setData(responses);
        objResponse.setMessage("OK");
        objResponse.setStatus(HttpStatus.OK.value());
        objResponse.setObjectPaging(new ObjectPaging(restaurantPage.getNumber() , responses.size()));
        return objResponse;
    }

    @Override
    public int pay(String restaurantId) {
        restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST , "restaurant id not found"));
        return restaurantRepository.updateBalance(restaurantId);
    }

}
