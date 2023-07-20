package com.zam.dev.food_order.controller.users.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.model.transaksi.CartResponse;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.CartService;
import com.zam.dev.food_order.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class CartControllerTest {

    @Autowired
    private MockMvc mc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private TokenResponse tokenResponse;
    @Autowired
    private Bcrypt bcrypt;

    @Autowired
    private RestaurantRepository restaurantRepository;

    User user;

    Restaurant restaurant;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartService cartService;

    @BeforeEach
    void setUp(){
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("test");
        user.setPhoneNumber("asd");
        user.setEmail("test@gmail.com");
        user.setId("1");
        user.setAddress("bwi");
        user.setPassword(bcrypt.hashPw("rahasia"));
        user.setAvatar("ava");
        user.setToken(jwtService.generateToken(user));
        user.setFirstName("zam");

        user.setLastName("zami");
        user.setRefreshToken(jwtService.generateRefreshToken(user));
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
    void testCreateCart201()throws Exception{
        CartRequest request = new CartRequest();
        request.setRestaurant_id(restaurant.getId());

        mc.perform(
                post("/api/user/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isCreated()
        ).andExpect(result -> {
            WebResponse<CartResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<CartResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertEquals(201 , response.getStatus());
        });
    }

    @Test
    void testCreateCartBadRequestStillStatusNotDone()throws Exception{
        CartRequest request = new CartRequest();
        request.setRestaurant_id(restaurant.getId());
        CartResponse cartResponse = cartService.createCart(request, user);
        assertNotNull(cartResponse);


        mc.perform(
                post("/api/user/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isBadRequest()
        ).andExpect(result -> {
            log.info(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testCreateCartBadRequestValidation()throws Exception{
        mc.perform(
                post("/api/user/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CartRequest()))
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isBadRequest()
        ).andExpect(result -> {
            log.info(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testCreateCartUnauthorized()throws Exception{
        mc.perform(
                post("/api/user/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CartRequest()))
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " +"wrong token")
        ).andExpectAll(
                status().isUnauthorized()
        ).andExpect(result -> {
            log.info(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testGetCartsUser()throws Exception{
        mc.perform(
                get("/api/user/cart")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
                        .param("page" , "0")
                        .param("size" , "10")
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            ObjectPagingResponse<List<CartResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPagingResponse<List<CartResponse>>>() {
            });
            assertNotNull(response.getData());
            assertEquals(0 , response.getObjectPaging().getPage());
            assertEquals(0 , response.getObjectPaging().getSize());
        });
    }

}