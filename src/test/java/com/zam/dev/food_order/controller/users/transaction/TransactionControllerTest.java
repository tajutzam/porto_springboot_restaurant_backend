package com.zam.dev.food_order.controller.users.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.midtrans.MidtransPaymentApiRequest;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.transaksi.TransactionCreateResponse;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static  org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mc;

    @Autowired
    private ObjectMapper objectMapper;

    User user;

    Restaurant restaurant;
    Menu menu;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    Category category;
    Cart cart;
    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        cartRepository.deleteAll();
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        menuRepository.deleteAll();
        cartDetailRepository.deleteAll();
        transactionRepository.deleteAll();
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

        restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setUsername("test");
        restaurant.setBanner("banner");
        restaurant.setToken("token");
        restaurant.setRefreshToken("refresh");
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword("rahasia");
        restaurant.setFirstName("zam");
        restaurant.setLastName("zami");
        restaurantRepository.save(restaurant);

        category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName("new");
        category.setImages("images");
        categoryRepository.save(category);


        menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        menu.setImage("image");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setName("name menu");
        menu.setPrice(20000);
        menu.setCategory(category);
        menu.setRestaurant(restaurant);
        menuRepository.save(menu);

        cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUser(user);
        cart.setRestaurant(restaurant);
        cart.setStatusCart(STATUS_CART.QUEUE);
        cartRepository.save(cart);


        CartDetailRequest request = new CartDetailRequest();
        request.setQty(2);
        request.setMenuId(menu.getId());
        request.setCartId(cart.getId());

        cartDetailService.createOrder(request);
    }



    @Test
    void testCreateTransaction()throws Exception{
        Thread.sleep(1000L);
        MidtransPaymentApiRequest request = new MidtransPaymentApiRequest();
        request.setBank_transfer("bca");
        request.setCartId(cart.getId());
        mc.perform(
                post("/api/user/transaction/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andExpect(result -> {
            WebResponse<TransactionCreateResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TransactionCreateResponse>>() {
            });
            assertEquals(HttpStatus.CREATED.value() , response.getStatus());
            System.out.println(response.getData().toString());
        });
    }

    @Test
    void testFindAllTransaction()throws Exception{
        mc.perform(
                get("/api/user/transaction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            System.out.println(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testFindByIdTransaction() throws Exception {
        MidtransPaymentApiRequest request = new MidtransPaymentApiRequest();
        request.setBank_transfer("bca");
        request.setCartId(cart.getId());

        TransactionCreateResponse response = transactionService.createTransaction(user, request);

        mc.perform(
                get("/api/user/transaction/" + response.getOrder_id())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            System.out.println(result.getResponse().getContentAsString());
        });
    }

}