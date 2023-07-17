package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.CartDetailRequest;
import com.zam.dev.food_order.model.CartDetailResponse;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CartDetailServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private JwtService jwtService;

    Cart cart;

    Restaurant restaurant;
    @Autowired
    private RestaurantRepository restaurantRepository;

    Category category ;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartRepository cartRepository;

    Menu menu;
    @Autowired
    private MenuRepository menuRepository;

    User user;

    @BeforeEach
    void setUp() {
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
        String token = jwtService.generateToken(restaurant);
        String refreshToken = jwtService.generateRefreshToken(restaurant);
        restaurant.setToken(token);
        restaurant.setRefreshToken(refreshToken);
        restaurant.setBanner("banner.jpg");
        restaurant.setAddress("banywuangi");
        restaurant.setUsername("test restaurant");
        restaurant.setId(UUID.randomUUID().toString());
        restaurantRepository.save(restaurant);

        category = new Category();
        category.setName("category");
        category.setId("test");
        category.setImages("test");
        categoryRepository.save(category);
        menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        menu.setName("test menu");
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setPrice(2000);
        menu.setImage("imag");
        menu.setCategory(category);
        menuRepository.save(menu);

        cart = new Cart();
        cart.setTotalPrice(0);
        cart.setStatusCart(STATUS_CART.QUEUE);
        cart.setId(UUID.randomUUID().toString());
        cart.setRestaurant(menu.getRestaurant());
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Test
    void testCreateCartDetail() {
        CartDetailRequest request = new CartDetailRequest();
        request.setQty(1);
        request.setMenuId(menu.getId());
        request.setCartId(cart.getId());
        CartDetailResponse response = cartDetailService.createOrder(request);
        assertNotNull(response.getCart().getId());
        assertNotNull(response.getMenu().getId());
    }

}