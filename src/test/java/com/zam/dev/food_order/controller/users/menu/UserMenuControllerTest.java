package com.zam.dev.food_order.controller.users.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class UserMenuControllerTest {

    @Autowired
    private MockMvc mc;

    User user;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    Category category;
    @Autowired
    private CategoryRepository categoryRepository;

    Menu menu;

    @Autowired
    private MenuRepository menuRepository;

    Cart cart;

    @Autowired
    CartRepository cartRepository;
    Restaurant restaurant;

    @Autowired
    private Bcrypt bcrypt;
    @BeforeEach
    void setUp(){
        initialize();
    }

    @Test
    void testFindAllMenuSuccess()throws Exception{

        mc.perform(
                get("/api/user/menu/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + user.getToken())
                        .param("page" , "0")
                        .param("size" , "10")
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            ObjectPagingResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPagingResponse<List<MenuResponse>>>() {
            });
            log.info("size , {}"  , response.getData().size());
            assertNotNull(response.getData());
            assertEquals(1 , response.getObjectPaging().getSize());
        });
    }

    @Test
    void testAddMenuToCartSuccess()throws Exception{
        CartDetailRequest request = new CartDetailRequest();
        request.setCartId(cart.getId());
        request.setMenuId(menu.getId());
        request.setQty(2);

        mc.perform(
                post("/api/user/menu/order/add")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION  , "Bearer " + user.getToken())
        ).andExpectAll(
                status().isCreated()
        ).andExpect(result -> {
            log.info(result.getResponse().getContentAsString());
        });

    }



    void initialize(){
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.deleteAll();
        menuRepository.deleteAll();

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
        cart.setRestaurant(restaurant);
        cart.setUser(user);
        cartRepository.save(cart);
    }


}