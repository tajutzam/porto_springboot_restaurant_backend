package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.properties.JwtProperties;
import com.zam.dev.food_order.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {



    JwtProperties jwtProperties;

    @Override
    public String extractUsername(String token) {

        return getSingleClaim(token , Claims::getSubject);
    }

    @Override
    public String generateToken(Object object) {
        return generateToken(new HashMap<>() , object);
    }

    @Override
    public <T> T getSingleClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        log.info(jwtProperties.getKey());
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getSigningKey() {

        byte[] decoded = Decoders.BASE64.decode(jwtProperties.getKey());
        return Keys.hmacShaKeyFor(decoded) ;
    }

    @Override
    public String generateToken(Map<String, Object> payload, Object object) {
        Instant nowDate = Instant.now();
        JwtBuilder jwtBuilder = null;
        if(object instanceof User user){
             jwtBuilder = Jwts.builder().
                     signWith(getSigningKey()).
                    setSubject(user.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(1, ChronoUnit.HOURS)))
                    .setId(user.getId());
        }else if(object instanceof Restaurant restaurant){
            jwtBuilder = Jwts.builder().
                    signWith(getSigningKey()).
                    setSubject(restaurant.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(1, ChronoUnit.HOURS)))
                    .setId(restaurant.getId());
        } else if(object instanceof Admin admin){
            jwtBuilder = Jwts.builder().
                    signWith(getSigningKey()).
                    setSubject(admin.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(1, ChronoUnit.HOURS)))
                    .setId(admin.getId());
        }else{
            throw new RuntimeException("object not instance of admin , user , or restaurant");
        }
        return jwtBuilder.compact();
    }

    @Override
    public boolean isTokenValid(String token, Object object) {
        if(object instanceof  User user){
            String username = extractUsername(token);
            return (username.equals(user.getUsername()) && !isTokenExpired(token));
        }else if(object instanceof Restaurant restaurant){
            String username = extractUsername(token);
            return (username.equals(restaurant.getUsername()) && !isTokenExpired(token));
        }else if(object instanceof  Admin admin){
            String username = extractUsername(token);
            return (username.equals(admin.getUsername()) && !isTokenExpired(token));
        }else{
            throw new RuntimeException("your object not valid");
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return getSingleClaim(token , Claims::getExpiration);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> payload, Object object) {
        Instant nowDate = Instant.now();
        JwtBuilder jwtBuilder = null;
        if(object instanceof User user){
            jwtBuilder = Jwts.builder().
                    signWith(getSigningKey()).
                    setSubject(user.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(7, ChronoUnit.DAYS)))
                    .setId(user.getId());
        }else if(object instanceof Restaurant restaurant){
            jwtBuilder = Jwts.builder().
                    signWith(getSigningKey()).
                    setSubject(restaurant.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(7, ChronoUnit.DAYS)))
                    .setId(restaurant.getId());
        } else if(object instanceof Admin admin){
            jwtBuilder = Jwts.builder().
                    signWith(getSigningKey()).
                    setSubject(admin.getUsername())
                    .setIssuedAt(Date.from(nowDate))
                    .setExpiration(Date.from(nowDate.plus(7, ChronoUnit.DAYS)))
                    .setId(admin.getId());
        }else{
            throw new RuntimeException("object not instance of admin , user , or restaurant");
        }
        return jwtBuilder.compact();
    }

    @Override
    public String generateRefreshToken(Object object) {
        return generateRefreshToken(new HashMap<>() , object);
    }
}
