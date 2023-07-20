package com.zam.dev.food_order.controller.restaurant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantUpdateRequest;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mc;


    TokenResponse tokenResponse;

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private Bcrypt bcrypt;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    MockMultipartFile file;

    Category category ;

    Restaurant restaurant;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() throws Exception {
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        category = new Category();
        category.setId("test");
        category.setName("test");
        category.setImages("image");
        restaurant = new Restaurant();
        restaurant.setUsername("test");
        restaurant.setId(UUID.randomUUID().toString());
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword(bcrypt.hashPw("rahasia"));
        restaurant.setBanner("banner.png");
        restaurant.setFirstName("zamz");
        restaurant.setLastName("zamz");
        String token = jwtService.generateToken(restaurant);
        String refreshToken = jwtService.generateRefreshToken(restaurant);
        restaurant.setToken(token);
        restaurant.setRefreshToken(refreshToken);
        restaurantRepository.save(restaurant);

        categoryRepository.save(category);

        for (int i = 0; i <5 ; i++) {
            Menu menu = new Menu();
            menu.setId(String.valueOf(i));
            menu.setName("menu , " + i);
            menu.setStatusMenu(STATUS_MENU.READY);
            menu.setImage("image");
            menu.setPrice(1000);
            menu.setRestaurant(restaurant);
            menu.setCategory(category);
            menuRepository.save(menu);
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("rahasia");
        tokenResponse = restaurantService.login(loginRequest);
        file = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());

    }

    @Test
    void testGetRestaurantLoginSuccess()throws Exception{
        mc.perform(
                get("/api/restaurant/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<RestaurantResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<RestaurantResponse>>() {
            });
            assertNotNull(response.getData().getId());
            assertNotNull(response.getData().getUsername());
        });
    }

    @Test
    void testGetRestaurantLoginUnauthorized()throws Exception{
        mc.perform(
                get("/api/restaurant/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " +"wrong token")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }



    @Test
    void testUpdateSuccess()throws Exception{
        RestaurantUpdateRequest request = new RestaurantUpdateRequest();
        request.setUsername("new username");
        request.setPassword("rahasia");
        request.setBank_number(234234L);
        request.setAddress("banyuwangi");
        request.setFirstName("zamz");
        request.setLastName("zamz");
        mc.perform(
                put("/api/restaurant/")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<RestaurantResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<RestaurantResponse>>() {
            });
            assertNotNull(response.getData());
            assertEquals(request.getBank_number() , response.getData().getBank_number());
        });
    }

    @Test
    void testUpdateBadRequest()throws Exception{
        RestaurantUpdateRequest request = new RestaurantUpdateRequest();
        request.setUsername("new username");
        request.setPassword("rahasia");
        request.setAddress("banyuwangi");
        request.setFirstName("zamz");
        request.setLastName("zamz");
        mc.perform(
                put("/api/restaurant/")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
        ).andExpectAll(
                status().isBadRequest()
        );
    }

}