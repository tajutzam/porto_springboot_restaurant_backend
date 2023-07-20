package com.zam.dev.food_order.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.admin.AdminResponse;
import com.zam.dev.food_order.model.admin.CategoryResponse;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.ObjectPageResponse;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.model.user.UserResponse;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AdminControllerTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MockMvc mc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminService adminService;
    TokenResponse tokenResponse;

    @Autowired
    private ResourceLoader resourceLoader;


    private MockMultipartFile firstFile;

    @BeforeEach
    void setUp() throws Exception {
        firstFile = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        tokenResponse = adminService.login(loginRequest);
        categoryRepository.deleteAll();
    }

    @Test
    void testGet() throws Exception {
        mc.perform(get("/api/admin/").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isOk()).andExpect(result -> {
            WebResponse<AdminResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AdminResponse>>() {
            });
            assertNotNull(response.getData());
            assertEquals("admin", response.getData().getUsername());
        });

    }

    @Test
    void testGetWithRefreshToken() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        TokenResponse tokenResponse = adminService.login(loginRequest);

        mc.perform(get("/api/admin/").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getRefreshToken())).andExpectAll(status().isUnauthorized());

    }


    @Test
    void testGetUsers() throws Exception {
        mc.perform(get("/api/admin/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isOk()).andExpect(result -> {
            ObjectPageResponse<List<UserResponse>> objectPageResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPageResponse<List<UserResponse>>>() {
            });
            assertNotNull(objectPageResponse.getData());
        });
    }

    @Test
    void testCategory() throws Exception {
        mc.perform(get("/api/admin/category").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isOk()).andExpect(result -> {
            ObjectPageResponse<List<CategoryResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPageResponse<List<CategoryResponse>>>() {
            });
            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertNotNull(response.getData());
            assertEquals("OK", response.getMessage());
        });
    }

    @Test
    void testAddCategory() throws Exception {
        mc.perform(multipart("/api/admin/category").file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA).param("name", "sip ae").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isCreated()).andExpect(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<CategoryResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
            assertNotNull(response.getData().getImages());
            assertEquals(201, response.getStatus());
        });
    }

    @Test
    void testAddCategoryUnAuthorized() throws Exception {
        mc.perform(multipart("/api/admin/category").file(new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream())).contentType(MediaType.MULTIPART_FORM_DATA).param("name", String.valueOf(System.currentTimeMillis()))).andExpectAll(status().isUnauthorized());
    }

    @Test
    void testAddCategoryBadRequest() throws Exception {
        mc.perform(multipart("/api/admin/category").file(new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream())).contentType(MediaType.MULTIPART_FORM_DATA).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isBadRequest());
    }

    @Test
    void testAddCategoryBadRequestImageNull() throws Exception {
        mc.perform(multipart("/api/admin/category")

                .contentType(MediaType.MULTIPART_FORM_DATA).param("name", String.valueOf(System.currentTimeMillis())).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isBadRequest());
    }

    @Test
    void testFindByIdCategory() throws Exception {
        Category category = new Category();
        category.setId("1");
        category.setName("category");
        category.setImages("images");
        categoryRepository.save(category);
        mc.perform(get("/api/admin/category/" + category.getId()).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isOk()).andExpect(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<CategoryResponse>>() {
            });
            assertNotNull(response);
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
        });
    }

    @Test
    void testFindCategoryByIdNotFoundBadRequest() throws Exception {
        mc.perform(get("/api/admin/category/wrongId").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isBadRequest()).andExpect(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<CategoryResponse>>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
            assertEquals(400, response.getStatus());
        });
    }

    @Test
    void testFindCategoryByIdUnauthorized() throws Exception {
        mc.perform(get("/api/admin/category/wrongId").contentType(MediaType.APPLICATION_JSON)).andExpectAll(status().isUnauthorized()).andExpect(result -> {
            WebResponse<CategoryResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<CategoryResponse>>() {
            });
            assertNotNull(response.getMessage());
            assertNull(response.getData());
            assertEquals(401, response.getStatus());
        });
    }

    @Test
    void testUpdateCategorySuccess() throws Exception {

        Category category = new Category();
        category.setId("1");
        category.setName("category");
        category.setImages("images.jpg");
        categoryRepository.save(category);
        mc.perform(multipart(HttpMethod.PUT, "/api/admin/category").file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA).param("name", String.valueOf(System.currentTimeMillis())).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken()).param("id", "1")).andExpectAll(status().isOk());
    }

    @Test
    void updateBadRequest() throws Exception {
        mc.perform(multipart(HttpMethod.PUT, "/api/admin/category").file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA).param("name", String.valueOf(System.currentTimeMillis())).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken()).param("id", "1")).andExpectAll(status().isBadRequest());
    }

    @Test
    void testUpdateCategoryUnAuthorized() throws Exception {
        mc.perform(multipart(HttpMethod.PUT, "/api/admin/category").file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA).param("name", String.valueOf(System.currentTimeMillis())).param("id", "1")).andExpectAll(status().isUnauthorized());
    }

    @Test
    void testDeleteCategory() throws Exception {

        Category category = new Category();
        category.setId("1");
        category.setName("category");
        category.setImages("images.jpg");
        categoryRepository.save(category);
        mc.perform(delete("/api/admin/category/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isAccepted()).andExpect(result -> {
            WebResponse<Integer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Integer>>() {
            });
            assertEquals(1, response.getData());
            log.info(response.getMessage());
        });
    }

    @Test
    void testDeleteCategoryNotFound() throws Exception {
        mc.perform(delete("/api/admin/category/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getToken())).andExpectAll(status().isBadRequest());
    }

    @Test
    void testDeleteCategoryUnAuthorized() throws Exception {
        mc.perform(delete("/api/admin/category/1")).andExpectAll(status().isUnauthorized());
    }

    @Test
    void testGetRestaurants()throws Exception{
        mc.perform(
                get("/api/admin/restaurant")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
                        .param("page" , "0")
                        .param("size"  , "1")
        ).andExpectAll(
                status().isOk()
        ).andExpectAll(result -> {
            ObjectPageResponse<List<RestaurantResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ObjectPageResponse<List<RestaurantResponse>>>() {
            });
            assertNotNull(response.getData());
        });
    }

    @Test
    void testGetBalanceRestaurant()throws Exception{
        restaurantRepository.deleteAll();
        for (int i = 0; i < 5 ; i++) {
            Restaurant restaurant = new Restaurant();
            restaurant.setUsername(String.valueOf(i));
            restaurant.setId(String.valueOf(i));
            restaurant.setAddress("banner");
            restaurant.setBalance(i);
            restaurant.setFirstName("firstname");
            restaurant.setLastName("lastname");
            restaurant.setToken("token");
            restaurant.setRefreshToken("refreshtoken");
            restaurant.setPassword("rahasia");
            restaurant.setBankNumber(i);
            restaurantRepository.save(restaurant);
        }
        mc.perform(
                get("/api/admin/restaurant/cash")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpectAll(result -> {
            System.out.println(result.getResponse().getContentAsString());
        });
    }

    @Test
    void testPaySuccess()throws Exception{
        restaurantRepository.deleteAll();
        Restaurant restaurant = new Restaurant();
        restaurant.setUsername("test");
        restaurant.setId("test");
        restaurant.setAddress("banner");
        restaurant.setBalance(10);
        restaurant.setFirstName("firstname");
        restaurant.setLastName("lastname");
        restaurant.setToken("token");
        restaurant.setRefreshToken("refreshtoken");
        restaurant.setPassword("rahasia");
        restaurant.setBankNumber(10);
        restaurantRepository.save(restaurant);

        mc.perform(
                put("/api/admin/restaurant/cash/test")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + tokenResponse.getToken())
        ).andExpectAll(
                status().isOk()
        ).andExpect(
                result -> {
                    System.out.println(result.getResponse().getContentAsString());
                }
        );
    }






}