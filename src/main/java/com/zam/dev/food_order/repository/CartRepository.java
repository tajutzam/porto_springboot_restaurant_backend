package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Cart;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_CART;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart , String> {

    List<Cart> findAllByRestaurantAndUser(Restaurant restaurant , User user);

    @Query("select c from Cart c where c.user = ?1 and c.statusCart = ?2")
    Page<Cart> findAllByUserAndStatusCart(User user , STATUS_CART status_cart ,  Pageable pageable);

    Optional<Cart> findByUserAndId(User user , String cartId);

    Optional<Cart> findByRestaurant_idAndUser(String restaurantId , User user);

}
