package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant , String> {

    Optional<Restaurant> findByUsername(String username);

    Optional<Restaurant> findByToken(String token);

    Optional<Restaurant> findByRefreshToken(String refreshToken);

}
