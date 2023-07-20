package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantRegisterRequest;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantUpdateRequest;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.RestaurantService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestaurantServiceImplTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private Bcrypt bcrypt;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private MockMultipartFile file;

    @Autowired
    private ResourceLoader resourceLoader;
    Restaurant restaurant;

    @BeforeEach
    void setUp() throws Exception {
        extracted();
        restaurantRepository.save(restaurant);
    }

    private void extracted() throws IOException {
        file = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setUsername("test");
        restaurant.setBanner("banner");
        restaurant.setToken("token");
        restaurant.setRefreshToken("refresh");
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword(bcrypt.hashPw("rahasia"));
        restaurant.setFirstName("zam");
        restaurant.setLastName("zami");
    }


    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("rahasia");
        TokenResponse response = restaurantService.login(loginRequest);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void testLoginBadRequest() {
        assertThrows(ConstraintViolationException.class, () -> {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("tajut");
            loginRequest.setPassword("");
            TokenResponse response = restaurantService.login(loginRequest);
        });
    }

    @Test
    void testLoginWrongUsernameOrPassword() {
        assertThrows(ResponseStatusException.class, () -> {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("tajut");
            loginRequest.setPassword("wrong password");
            TokenResponse response = restaurantService.login(loginRequest);
        });
    }

    @Test
    void testRegisterSuccess() {
        RestaurantRegisterRequest request = new RestaurantRegisterRequest();
        request.setUsername("username");
        request.setPassword("rahasia");
        request.setAddress("banyuwangi");
        request.setFirstName("zam zami");
        request.setLastName("tajut");
        request.setBank_number(123123123231L);
        TokenResponse tokenResponse = restaurantService.register(request, file);
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getToken());
        assertNotNull(tokenResponse.getRefreshToken());
    }


    @Test
    void testRegisterBadRequest() {
        assertThrows(ConstraintViolationException.class, () -> {
            RestaurantRegisterRequest request = new RestaurantRegisterRequest();
            request.setUsername("username");
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setLastName("tajut");
            TokenResponse tokenResponse = restaurantService.register(request, file);

        });
    }

    @Test
    void testResponseStatusCodeException() {

        assertThrows(ResponseStatusException.class, () -> {
            RestaurantRegisterRequest request = new RestaurantRegisterRequest();
            request.setUsername("test");
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setFirstName("zam zami");
            request.setLastName("tajut");
            request.setBank_number(12312L);
            TokenResponse tokenResponse = restaurantService.register(request, file);
        });
    }

    @Test
    void testUpdateSuccess() {

        RestaurantUpdateRequest request = new RestaurantUpdateRequest();
        request.setUsername("new username");
        request.setPassword("rahasia");
        request.setFirstName("zamz");
        request.setLastName("zamz");
        request.setBank_number(21312312L);
        request.setAddress("banyuwangi");
        RestaurantResponse restaurantResponse = restaurantService.update(request, restaurant);
        assertNotNull(restaurantResponse);

    }

    @Test
    void testUpdateAvatarSuccess(){
        RestaurantResponse response = restaurantService.updateAvatar(file, restaurant);
        assertNotNull(response.getBanner());
    }


    @AfterEach
    void destroy() {
        restaurantRepository.deleteAll();
    }


}
