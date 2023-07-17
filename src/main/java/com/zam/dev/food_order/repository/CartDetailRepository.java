package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetail , String> {
}
