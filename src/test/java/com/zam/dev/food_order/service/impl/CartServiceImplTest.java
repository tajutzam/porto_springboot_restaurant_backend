package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.model.transaksi.CartResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.repository.CartRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.service.CartService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceImplTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    private User user;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        user = new User();
        user.setPhoneNumber("asd");
        user.setEmail("test@gmail.com");
        user.setId("1");
        user.setAddress("bwi");
        user.setPassword("rahasia");
        user.setAvatar("ava");
        user.setToken("token");
        user.setFirstName("zam");
        user.setUsername("test");
        user.setLastName("zami");
        userRepository.save(user);

        restaurant = new Restaurant();
        restaurant.setFirstName("okezam");
        restaurant.setPassword("rahasia");
        restaurant.setLastName("zamas");
        restaurant.setToken("token");

        restaurant.setRefreshToken("refreshToken");
        restaurant.setBanner("banner.jpg");
        restaurant.setAddress("banywuangi");
        restaurant.setUsername("test restaurant");
        restaurant.setId(UUID.randomUUID().toString());
        restaurantRepository.save(restaurant);
    }

    @Test
    void testCreateCartSuccess(){

        CartRequest request = new CartRequest();
        request.setRestaurant_id(restaurant.getId());

        CartResponse cartResponse = cartService.createCart(request, user);
        assertNotNull(cartResponse);
    }

    @Test
    void testCreateCartStillUnfinishedCart(){
        CartRequest request = new CartRequest();
        request.setRestaurant_id(restaurant.getId());

        CartResponse cartResponse = cartService.createCart(request, user);

        assertThrows(ResponseStatusException.class , () -> {
            CartRequest request1 = new CartRequest();
            request1.setRestaurant_id(restaurant.getId());

            CartResponse cartResponse2 = cartService.createCart(request1, user);
        });
    }

    @Test
    void testCreateCartRestaurantNotFound(){
       assertThrows(ResponseStatusException.class , () -> {
           CartRequest request = new CartRequest();
           request.setRestaurant_id("not found");

           CartResponse cartResponse2 = cartService.createCart(request, user);
       });
    }

    @Test
    void testCreateCartConstraintViolationExceptions(){
        assertThrows(ConstraintViolationException.class , () -> {
            CartRequest request = new CartRequest();
            CartResponse cartResponse2 = cartService.createCart(request, user);
        });
    }

    @Test
    void testGetCartUser(){

        cartRepository.deleteAll();
        CartRequest request = new CartRequest();
        request.setRestaurant_id(restaurant.getId());

        CartResponse cartResponse = cartService.createCart(request, user);
        assertNotNull(cartResponse);

        ObjectPagingResponse<List<CartResponse>> response = cartService.cartsUser(user, 0, 1);
        assertEquals(1 , response.getObjectPaging().getSize());
        assertEquals(0 , response.getObjectPaging().getPage());
        assertNotNull(response.getData());

    }



}