package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant , String> {

    Optional<Restaurant> findByUsername(String username);

    Optional<Restaurant> findByToken(String token);

    Optional<Restaurant> findByRefreshToken(String refreshToken);


    @Query("select r from Restaurant r where r.id <> ?1")
    List<Restaurant> findAllByIdNot(String id);


    @Query("select r from Restaurant r where r.balance <> ?1")
    List<Restaurant> findAllByBalanceNot(long balance);


    @Query("update Restaurant r set r.balance= 0 where r.id= :id")
    @Transactional
    @Modifying
    int updateBalance(@Param("id") String id);

}
