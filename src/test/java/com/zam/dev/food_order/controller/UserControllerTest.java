package com.zam.dev.food_order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.user.UserResponse;
import com.zam.dev.food_order.model.user.UserUpdateRequest;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.JwtService;
import com.zam.dev.food_order.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    private MockMvc mc;

    @Autowired
    private ResourceLoader resourceLoader;

    private TokenResponse userTokenResponse;

    @Autowired
    private ObjectMapper objectMapper;


    private MockMultipartFile file;

    @Autowired
    private UserService userService;

    User user;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Bcrypt bcrypt;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp()throws Exception{
        file =  new MockMultipartFile("banner", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());

        user = new User();
        user.setPhoneNumber("asd");
        user.setEmail("test@gmail.com");
        user.setId("1");
        user.setAddress("bwi");
        user.setPassword(bcrypt.hashPw("rahasia"));
        user.setAvatar("ava");
        user.setFirstName("zam");
        user.setUsername("test");
        user.setLastName("zami");
        user.setToken(jwtService.generateToken(user));
        user.setRefreshToken(jwtService.generateRefreshToken(user));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("rahasia");
        loginRequest.setUsername("test");
        userTokenResponse = userService.login(loginRequest);
    }

    @Test
    void testUserUpdateAvatarSuccess()throws Exception{
        file =  new MockMultipartFile("avatar", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        mc.perform(
                multipart(HttpMethod.PUT , "/api/user/avatar")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + userTokenResponse.getToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)

        ).andExpectAll(
                status().isOk()
        ).andExpect(result ->{
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });
            assertNotNull(response.getData());
            assertNotNull(response.getData().getId());
        });
    }

    @Test
    void testUserUpdateAvatarBadRequest()throws Exception{
        mc.perform(
                multipart(HttpMethod.PUT , "/api/user/avatar")

                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + userTokenResponse.getToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)

        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testUserUpdateAvatarUnauthorized()throws Exception{
        file =  new MockMultipartFile("avatar", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        mc.perform(
                multipart(HttpMethod.PUT , "/api/user/avatar")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + "wrong token")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)

        ).andExpectAll(
                status().isUnauthorized()
        );
    }

    @Test
    void testGetUserLoginSuccess()throws Exception{
        mc.perform(
                get("/api/user/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + userTokenResponse.getToken())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andExpect(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });
            assertNotNull(response);
            assertNotNull(response.getData().getId());
            assertNotNull(response.getData().getUsername());
        });
    }

    @Test
    void testGetUserLoginUnauthorized()throws Exception{
        mc.perform(
                get("/api/user/")
                        .header(HttpHeaders.AUTHORIZATION , "Bearer " + "wrong token")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andExpect(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });
            assertNull(response.getData());
        });
    }

    @Test
    void testUserUpdateSuccess()throws Exception{
        UserUpdateRequest request = new UserUpdateRequest();
        request.setLastName("zami");
        request.setFirstName("zamz");
        request.setPassword("rahasia");
        request.setAddress("banyuwangi");
        request.setUsername("zamz");
        request.setEmail(System.currentTimeMillis() + "@gmail.com");
        request.setPhone_number("3433433433433");

        mc.perform(
                put("/api/user/")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer "+userTokenResponse.getToken())
        )  .andExpectAll(
                status().isOk()
        ).andExpect(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                    });
                    assertNotNull(response.getData());
                    assertNotNull(response.getData().getId());
                    assertEquals(200 , response.getStatus());
                }
        );
    }


    @Test
    void testUpdateUserBadRequest()throws Exception{
        UserUpdateRequest request = new UserUpdateRequest();
        request.setLastName("zami");
        request.setFirstName("zamz");
        request.setPassword("rahasia");
        mc.perform(
                put("/api/user/")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer "+userTokenResponse.getToken())
        )  .andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testUpdateUnAuthorized()throws Exception{
        UserUpdateRequest request = new UserUpdateRequest();
        request.setLastName("zami");
        request.setFirstName("zamz");
        request.setPassword("rahasia");
        request.setAddress("banyuwangi");
        request.setUsername("zamz");

        mc.perform(
                put("/api/user/")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION , "Bearer "+"wrong token")
        )  .andExpectAll(
                status().isUnauthorized()
        );
    }

}