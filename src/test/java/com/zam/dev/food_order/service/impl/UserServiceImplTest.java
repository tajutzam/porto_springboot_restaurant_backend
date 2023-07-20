package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.LoginRequest;
import com.zam.dev.food_order.model.other.RefreshTokenRequest;
import com.zam.dev.food_order.model.other.TokenResponse;
import com.zam.dev.food_order.model.user.UserRegisterRequest;
import com.zam.dev.food_order.model.user.UserResponse;
import com.zam.dev.food_order.model.user.UserUpdateRequest;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    TokenResponse tokenResponse;

    private MockMultipartFile file;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Bcrypt bcrypt;

    @BeforeEach
    void setUp() throws Exception {

        file = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
        User user = new User();
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
        loginRequest.setUsername("test");
        loginRequest.setPassword("rahasia");
        tokenResponse = userService.login(loginRequest);
    }

    @Test
    void testFindAllUsers() {
        PageImpl<UserResponse> responses = userService.findAll(0, 1);
        List<UserResponse> responseList = responses.getContent();
        assertEquals(0, responses.getNumber());
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
    }


    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("rahasia");



        TokenResponse tokenResponse = userService.login(request);
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getToken());
        assertNotNull(tokenResponse.getRefreshToken());
    }

    @Test
    void testLoginConstraintViolation() {
        assertThrows(ConstraintViolationException.class, () -> {
            LoginRequest request = new LoginRequest();
            request.setUsername("zamz");
            TokenResponse tokenResponse = userService.login(request);
            assertNotNull(tokenResponse);
            assertNotNull(tokenResponse.getToken());
            assertNotNull(tokenResponse.getRefreshToken());
        });
    }

    @Test
    void testLoginResponseStatusCodeException() {
        assertThrows(ResponseStatusException.class, () -> {
            LoginRequest request = new LoginRequest();
            request.setUsername("zamz");
            request.setPassword("salah");
            TokenResponse tokenResponse = userService.login(request);
            assertNotNull(tokenResponse);
            assertNotNull(tokenResponse.getToken());
            assertNotNull(tokenResponse.getRefreshToken());
        });
    }

    @Test
    void testRegisterSuccess() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(String.valueOf(System.currentTimeMillis()));
        request.setPassword("rahasia");
        request.setAddress("banyuwangi");
        request.setFirstName("zamz");
        request.setLastName("zami");
        request.setEmail(System.currentTimeMillis() + "@gmail.com");
        request.setPhone_number("02123212321");
        log.info("email , {}", request.getEmail());
        log.info("user , {}" , request.toString());
        TokenResponse tokenResponse = userService.register(request);
        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getRefreshToken());
        assertNotNull(tokenResponse.getToken());
    }

    @Test
    void testRegisterDuplicateEmail(){

        User user = new User();
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

        assertThrows(ResponseStatusException.class, ()->{

            UserRegisterRequest request = new UserRegisterRequest();

            request.setUsername(String.valueOf(System.currentTimeMillis()));
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setFirstName("zamz");
            request.setLastName("zami");
            request.setEmail("test@gmail.com");
            request.setPhone_number("02123212321");
            userService.register(request);
        });
    }

    @Test
    void testRegisterConstraintViolations() {
        assertThrows(ConstraintViolationException.class, () -> {
            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername(String.valueOf(System.currentTimeMillis()));
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setFirstName("zamz");
            TokenResponse tokenResponse = userService.register(request);
            assertNotNull(tokenResponse);
            assertNotNull(tokenResponse.getRefreshToken());
            assertNotNull(tokenResponse.getToken());
        });
    }

    @Test
    void testResponseStatusCodeException() {
        assertThrows(ResponseStatusException.class, () -> {
            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername("test");
            request.setPassword("rahasia");
            request.setAddress("banyuwangi");
            request.setFirstName("zamz");
            request.setLastName("zami");
            request.setEmail(System.currentTimeMillis() + "@gmail.com");
            request.setPhone_number("08123123123");
            TokenResponse tokenResponse = userService.register(request);
            assertNotNull(tokenResponse);
            assertNotNull(tokenResponse.getRefreshToken());
            assertNotNull(tokenResponse.getToken());
        });
    }

    @Test
    void testGenerateTokenSuccess() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(tokenResponse.getRefreshToken());
        TokenResponse response = userService.createNewToken(request);
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    void testGenerateTokenConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            RefreshTokenRequest request = new RefreshTokenRequest();
            TokenResponse response = userService.createNewToken(request);
            assertNotNull(response);
            assertNotNull(response.getToken());
        });
    }

    @Test
    void testGenerateTokenResponseStatusException(){
        assertThrows(ResponseStatusException.class , () ->{
            RefreshTokenRequest request = new RefreshTokenRequest();
            request.setRefreshToken("wrong");
            TokenResponse response = userService.createNewToken(request);
            assertNotNull(response);
            assertNotNull(response.getToken());
        });
    }

    @Test
    void testUpdateAvatarUserSuccess(){
        User user = userRepository.findByUsername("test").orElse(null);
        UserResponse userResponse = userService.updateAvatar(file, user);
        assertNotNull(userResponse);
        assertNotNull(userResponse.getId());

    }


    @Test
    void testUpdateUserSuccess(){
        User user = userRepository.findByUsername("test").orElse(null);
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUsername("zamz");
        request.setPassword("rahasia");
        request.setAddress("banyuwangi baru");
        request.setFirstName("zamz");
        request.setLastName("zami");
        request.setEmail(System.currentTimeMillis()+"@gmail.com");
        request.setPhone_number("085607185972");
        UserResponse userResponse = userService.updateUser(request, user);
        assertNotNull(userResponse);
        assertNotNull(userResponse.getId());
    }

    @Test
    void testUpdateUserConstraintViolationException(){
        assertThrows(ConstraintViolationException.class , ()->{
            User user = userRepository.findByUsername("zamz").orElse(null);
            UserUpdateRequest request = new UserUpdateRequest();
            request.setUsername("zamz");
            request.setPassword("rahasia");
            request.setAddress("banyuwangi baru");
            request.setLastName("zami");
            UserResponse userResponse = userService.updateUser(request, user);
            assertNotNull(userResponse);
            assertNotNull(userResponse.getId());
        });
    }

    @Test
    void testUpdateUserResponseStatusException(){
        assertThrows(NullPointerException.class , ()->{// unauthorized
            User user = userRepository.findByUsername("null").orElse(null);
            UserUpdateRequest request = new UserUpdateRequest();
            request.setUsername("zamz");
            request.setPassword("rahasia");
            request.setAddress("banyuwangi baru");
            request.setLastName("zami");
            request.setFirstName("zamz");
            request.setEmail("moh@gmail.com");
            request.setPhone_number("034323432312");
            UserResponse userResponse = userService.updateUser(request, user);
            assertNotNull(userResponse);
            assertNotNull(userResponse.getId());
        });
    }




    @AfterEach
    void after() {
        userRepository.deleteAll();
    }
}