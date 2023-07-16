package com.zam.dev.food_order.service;

import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    String generateToken(Object object);

    <T> T getSingleClaim(String token, Function<Claims, T> claimResolver);

    Claims extractAllClaims(String token);

    Key getSigningKey();

    String generateToken(Map<String, Object> payload, Object object);

    boolean isTokenValid(String token, Object user);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    String generateRefreshToken(Map<String, Object> payload, Object object);

    String generateRefreshToken(Object object);


}
