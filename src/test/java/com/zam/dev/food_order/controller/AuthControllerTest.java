package com.zam.dev.food_order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.RefreshTokenRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.user.UserRegisterRequest;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.AdminService;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.RestaurantService;
import com.zam.dev.food_order.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


    @Autowired
    private MockMvc mc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdminService adminService;
    TokenResponse tokenResponse;

    TokenResponse restaurantTokenResponse;

    TokenResponse userTokenResponse;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;

    private MockMultipartFile file;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserService userService;

    @Autowired
    private Bcrypt bcrypt;

    @Autowired
    private JwtService jwtService;

    User user;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() throws Exception {
        restaurantRepository.deleteAll();
        file =  new MockMultipartFile("banner", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setUsername("test");
        restaurant.setBanner("banner");
        restaurant.setToken("token");
        restaurant.setRefreshToken("refresh");
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword(bcrypt.hashPw("rahasia"));
        restaurant.setFirstName("zam");
        restaurant.setLastName("zami");

        restaurantRepository.save(restaurant);
        user = new User();
        user.setPhoneNumber("asd");
        user.setEmail("test@gmail.com");
        user.setId("1");
        user.setAddress("bwi");
        user.setPassword(bcrypt.hashPw("rahasia"));
        user.setAvatar("ava");
        user.setToken("token");
        user.setFirstName("zam");
        user.setUsername("test");
        user.setLastName("zami");
        userRepository.save(user);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        tokenResponse = adminService.login(loginRequest);
        loginRequest.setUsername("test");
        restaurantTokenResponse = restaurantService.login(loginRequest);
        loginRequest.setUsername("test");
        userTokenResponse = userService.login(loginRequest);
    }



    @Test
    void testAdminLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("rahasia");
        mc.perform(post("/api/admin/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isOk()).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getRefreshToken());
            assertEquals(200, response.getStatus());
            assertEquals("OK", response.getMessage());
        });
    }

    @Test
    void testAdminLoginBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        mc.perform(post("/api/admin/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isBadRequest()).andExpect(result -> {
            WebResponse<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Object>>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getMessage());
            assertEquals(400, response.getStatus());
        });
    }

    @Test
    void testAdninLoginUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrong");
        mc.perform(post("/api/admin/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isUnauthorized()).andExpect(result -> {
            WebResponse<Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Object>>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getMessage());
            assertEquals(401, response.getStatus());
        });
    }

    @Test
    void testAdminToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(tokenResponse.getRefreshToken());
        mc.perform(post("/api/admin/auth/token").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk()).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getToken());
            assertEquals(200, response.getStatus());
        });
    }

    @Test
    void testAdminTokenFailed() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(tokenResponse.getToken());
        mc.perform(post("/api/admin/auth/token").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isUnauthorized()).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNull(response.getData());
        });
    }

    @Test
    void testRestaurantLoginSuccess()throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("rahasia");
        mc.perform(
                post("/api/restaurant/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isOk()

        ).andExpect(
                result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                    assertNotNull(response.getData());
                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getRefreshToken());
                }
        );
    }

    @Test
    void testRestaurantLoginBadRequest()throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("restaurant");
        mc.perform(
                post("/api/restaurant/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testRestaurantLoginUsernameOrPasswordWrong()throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("restaurant");
        loginRequest.setPassword("wrong");
        mc.perform(
                post("/api/restaurant/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isBadRequest()

        ).andExpect(
                result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                    assertNull(response.getData());
                    assertEquals(400 , response.getStatus());
                }
        );
    }

    @Test
    void testRestaurantRegisterSuccess()throws Exception{
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/auth/register")
                        .file(file)
                        .param("firstName" , "Deuz")
                        .param("lastName" , "Toko")
                        .param("address" , "banyuwangi")
                        .param("password" , "rahasia")
                        .param("username" , "Deuz")
                        .param("bank_number" , "2000000")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpectAll(
                status().isCreated()
        ).andExpect(
                result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getRefreshToken());
                }
        );
    }

    @Test
    void testRestaurantRegisterBadRequestValidation()throws Exception{
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/auth/register")
                        .file(file)
                        .param("firstName" , "Deuz")
                        .param("lastName" , "Toko")
                        .param("address" , "banyuwangi")
                        .param("username" , "Deuz")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testRestaurantRegisterBadRequestUsernameAlreadyBeenTaken()throws Exception{
        mc.perform(
                multipart(HttpMethod.POST , "/api/restaurant/auth/register")
                        .file(file)
                        .param("firstName" , "restaurant")
                        .param("lastName" , "Toko")
                        .param("address" , "banyuwangi")
                        .param("password" , "rahasia")
                        .param("username" , "restaurant")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testRestaurantToken() throws Exception{
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(restaurantTokenResponse.getRefreshToken());
        mc.perform(post("/api/restaurant/auth/token").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk()).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getToken());
            assertEquals(200, response.getStatus());
        });
    }

    @Test
    void testRestaurantTokenBadRequest() throws Exception{
        RefreshTokenRequest request = new RefreshTokenRequest();
        mc.perform(post("/api/restaurant/auth/token").
                contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(request))).
                andExpectAll(status().isBadRequest()).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
        });
    }

    @Test
    void testRestaurantTokenNotFound()throws Exception{
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("wrong refresh token");
        mc.perform(post("/api/restaurant/auth/token").
                        contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpectAll(status().isUnauthorized()).andExpect(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                });
    }

    @Test
    void testUserLoginSuccess() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("rahasia");
        mc.perform(
                post("/api/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getRefreshToken());
            assertEquals(200 , response.getStatus());
        });
    }

    @Test
    void testUserLoginBadRequest()throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("zamz");
        mc.perform(
                post("/api/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testUserLoginUnauthorized()throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("zamz");
        loginRequest.setPassword("wrong");
        mc.perform(
                post("/api/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void testUserRegisterSuccess()throws Exception{

            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername(String.valueOf(System.currentTimeMillis()));
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setFirstName("zamz");
            request.setLastName("zami");
            request.setEmail(System.currentTimeMillis() + "@gmail.com");
            request.setPhone_number("02123212321");

        mc.perform(
                post("/api/user/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andExpect(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
            });
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getRefreshToken());
            assertEquals(201 , response.getStatus());
        });
    }

    @Test
    void testUserRegisterBadRequest()throws Exception{
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(String.valueOf(System.currentTimeMillis()));
        request.setPassword("rahasia");
        request.setFirstName("zamz");
        request.setLastName("zamz");

        mc.perform(
                post("/api/user/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testUserCreateNewTokenSuccess()throws Exception{
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(userTokenResponse.getRefreshToken());
        mc.perform(
                post("/api/user/auth/token")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andExpect(
                result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                    });
                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getRefreshToken());
                    assertEquals(200 , response.getStatus());
                }
        );
    }

    @Test
    void testUserCreateNewTokenBadRequest()throws Exception{
        RefreshTokenRequest request = new RefreshTokenRequest();
        mc.perform(
                post("/api/user/auth/token")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        );
    }




}