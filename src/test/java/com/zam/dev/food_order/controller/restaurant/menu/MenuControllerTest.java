package com.zam.dev.food_order.controller.restaurant.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    private MockMvc mc;
    @Autowired
    private ObjectMapper objectMapper;
    TokenResponse tokenResponse;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private Bcrypt bcrypt;

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
    void setUp()throws IOException {
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
    void testFindAllMenuByStatusSuccess()throws Exception{
        mc.perform(
                get("/api/restaurant/menu/status")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .param("status" , STATUS_MENU.READY.name())
        ).andExpectAll(status().isOk())
                .andExpect(result -> {
                    WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<MenuResponse>>>() {
                    });
                    assertNotNull(response.getData());
                });
    }

    @Test
    void testFindAllMenuByStatusUnauthorized()throws Exception{
        mc.perform(
                        get("/api/restaurant/menu/status")
                                .header(HttpHeaders.AUTHORIZATION , "Bearer " + "wrong token")
                                .param("status" , STATUS_MENU.READY.name())
                ).andExpectAll(status().isUnauthorized())
                .andExpect(result -> {
                    WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<MenuResponse>>>() {
                    });
                    assertNull(response.getData());
                });
    }

    @Test
    void testUpdateStatusMenuSuccess()throws Exception{
        menuRepository.deleteAll();
        Menu menu = new Menu();
        menu.setId("123");
        menu.setName("new Menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);
        mc.perform(
            put("/api/restaurant/menu/status/"+menu.getId())
                    .param("status" , STATUS_MENU.NOT_READY.name())
                    .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<Integer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Integer>>() {
            });
            assertEquals(1 , response.getData());
        });
    }


    @Test
    void testGetMenuRestaurantSuccess()throws Exception{
        mc.perform(
                get("/api/restaurant/menu/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("page" , "0")
                        .param("size" , "1")
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            ObjectPagingResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPagingResponse<List<MenuResponse>>>() {
            });
            assertNotNull(response.getData());
            assertEquals(1  , response.getObjectPaging().getSize());
        });
    }

    @Test
    void testAddMenuSuccess() throws Exception {
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/menu/")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , String.valueOf(System.currentTimeMillis()))
                        .param("price" , String.valueOf(10000))
                        .param("status_menu"  , STATUS_MENU.READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isCreated()
        ).andExpect(result -> {
            WebResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<MenuResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
        });
    }

    @Test
    void testAddMenuBadRequest()throws Exception{
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/menu/")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , String.valueOf(System.currentTimeMillis()))
                        .param("price" , String.valueOf(1))
                        .param("status_menu"  , STATUS_MENU.READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testAddMenuUnauthorized()throws Exception{
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/menu/")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + "wrong token")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , String.valueOf(System.currentTimeMillis()))
                        .param("price" , String.valueOf(10000))
                        .param("status_menu"  , STATUS_MENU.READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void testUpdateMenuSuccess()throws Exception{

        Menu menu = new Menu();
        menu.setId("123");
        menu.setName("new Menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);

        mc.perform(
                multipart(HttpMethod.PUT , "/api/restaurant/menu/"+menu.getId())
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , String.valueOf(System.currentTimeMillis()))
                        .param("price" , String.valueOf(10000))
                        .param("status_menu"  , STATUS_MENU.NOT_READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<MenuResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertEquals(STATUS_MENU.NOT_READY.toString() , response.getData().getStatus().name());
        });
    }

    @Test
    void testUpdateMenuBadRequestValidation()throws Exception{
        Menu menu = new Menu();
        menu.setId("123");
        menu.setName("new Menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);

        mc.perform(
                multipart(HttpMethod.PUT , "/api/restaurant/menu/"+menu.getId())
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , String.valueOf(System.currentTimeMillis()))
                        .param("price" , String.valueOf(10000))
                        .param("status_menu"  , STATUS_MENU.NOT_READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testUpdateNameAlreadyDefinedOtherMenu()throws Exception{
        Menu menu = new Menu();
        menu.setId("123");
        menu.setName("new Menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);

        Menu menu1 = new Menu();
        menu1.setId("123");
        menu1.setName("new Menu1");
        menu1.setStatusMenu(STATUS_MENU.READY);
        menu1.setImage("image");
        menu1.setPrice(1000);
        menu1.setRestaurant(restaurant);
        menu1.setCategory(category);
        menuRepository.save(menu1);

        mc.perform(
                multipart(HttpMethod.PUT , "/api/restaurant/menu/"+menu.getId())
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name" , "new Menu1")
                        .param("price" , String.valueOf(10000))
                        .param("status_menu"  , STATUS_MENU.NOT_READY.toString())
                        .param("category_id" , category.getId())
        ).andExpectAll(
                status().isBadRequest()
        );
    }


}