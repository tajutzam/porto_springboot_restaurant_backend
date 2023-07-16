package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class JwtServiceImplTest {


    @Autowired
    private JwtService jwtService;

    String token;

    String refreshToken;


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("zam");
        token = jwtService.generateToken(user);
        refreshToken = jwtService.generateRefreshToken(user);
    }

    @Test
    void extractUsername() {
        assertEquals("zam" , jwtService.extractUsername(token));
    }

    @Test
    void generateToken() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("zam");
        String token = jwtService.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void testGenerateRefreshToken(){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("zam");
        String refreshToken = jwtService.generateRefreshToken(user);
        assertNotNull(refreshToken);
    }


    @Test
    void getSingleClaim() {
        String username = jwtService.getSingleClaim(token, Claims::getSubject);
        assertEquals("zam" , username);
    }

    @Test
    void getSingleClaimRefreshToken() {
        String username = jwtService.getSingleClaim(refreshToken, Claims::getSubject);
        assertEquals("zam" , username);
    }

    @Test
    void extractAllClaims() {
        Claims claims = jwtService.extractAllClaims(token);
        assertNotNull(claims);
    }

    @Test
    void getSigningKey() {
        assertNotNull(jwtService.getSigningKey());
    }

    @Test
    void testGenerateToken() {
        User user  = new User();
        user.setUsername("zam");
        user.setId(UUID.randomUUID().toString());
        String token = jwtService.generateToken(new HashMap<>(), user);
        assertNotNull(token);
    }

    @Test
    void isTokenValid() {
        User user  = new User();
        user.setUsername("zam");
        user.setId(UUID.randomUUID().toString());
        String token = jwtService.generateToken(new HashMap<>(), user);
        assertTrue(jwtService.isTokenValid(token , user));
    }

    @Test
    void isTokenNotExpired() {
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void extractExpiration() {
        assertNotNull(jwtService.extractExpiration(token));
    }
}