package com.zam.dev.food_order.repository;


import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.RatingMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingMenuRepository extends JpaRepository<RatingMenu, String> {
}
