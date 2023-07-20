package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Cart;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart , String> {

    List<Cart> findAllByRestaurantAndUser(Restaurant restaurant , User user);

    Page<Cart> findAllByUser(User user , Pageable pageable);

    Optional<Cart> findByUserAndId(User user , String cartId);

}
