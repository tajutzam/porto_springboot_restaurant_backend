package com.zam.dev.food_order.controller.restaurant.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.transaksi.TransactionUpdateRequest;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static  org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mc;

    private Restaurant restaurant;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;


    private CartDetailRepository cartDetailRepository;
    User user;
    Cart cart;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private JwtService jwtService;
    Transaction transaction;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        cartRepository.deleteAll();
        restaurantRepository.deleteAll();


        restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setUsername("test");
        restaurant.setBanner("banner");
        String generateToken = jwtService.generateToken(restaurant);
        restaurant.setToken(generateToken);
        restaurant.setRefreshToken("refresh");
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword("rahasia");
        restaurant.setFirstName("zam");
        restaurant.setLastName("zami");
        restaurantRepository.save(restaurant);

        user = new User();
        user.setId("test");
        user.setUsername("test");
        user.setRefreshToken("refresh");
        user.setAvatar("avatar");
        user.setPassword("rahasia");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAddress("banyuwangi");
        user.setPhoneNumber("087623123123");
        user.setEmail("mohammadtajutzamzami07@gmail.com");
        String token = jwtService.generateToken(user);
        user.setToken(token);
        userRepository.save(user);

        cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUser(user);
        cart.setRestaurant(restaurant);
        cart.setStatusCart(STATUS_CART.QUEUE);
        cartRepository.save(cart);

        TransactionUpdateRequest request = new TransactionUpdateRequest();
         transaction = new Transaction();
        transaction.setStatusTransaction(STATUS_TRANSACTION.WAITING_PAYMENT);
        transaction.setUpdatedAt(Instant.now());
        transaction.setCreatedAt(Instant.now());
        transaction.setCart(cart);
        transaction.setBank_method(BANK_METHOD.BCA);
        transaction.setTotalPrice(200);
        transaction.setExpiredPayment(Instant.now());
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUser(user);
        transaction.setVaNumber("123123");
        transaction.setRestaurant(restaurant);
        transactionRepository.save(transaction);

    }

    @Test
    void testFindAllTransactionSuccess() throws Exception {
        mc.perform(
                get("/api/restaurant/transaction/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + restaurant.getToken())
                        .param("status" , STATUS_TRANSACTION.WAITING_PAYMENT.name())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            System.out.println(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testUpdateStatusSuccess()throws Exception{
        TransactionUpdateRequest request = new TransactionUpdateRequest();
        request.setStatus_transaction(STATUS_TRANSACTION.CANCELED.name());
        mc.perform(
                put("/api/restaurant/transaction/"+transaction.getId())
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + restaurant.getToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andExpect(
                result -> {
                    System.out.println(result.getResponse().getContentAsString());
                }
        );
    }

}