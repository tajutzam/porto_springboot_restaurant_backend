package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.RatingRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRestaurantRepository extends JpaRepository<RatingRestaurant , String> {
}
