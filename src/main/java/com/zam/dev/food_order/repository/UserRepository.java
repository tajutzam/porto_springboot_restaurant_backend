package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username , String email);

    Optional<User> findByToken(String token);

    Page<User> findAll(Pageable pageable);

    Optional<User> findByRefreshToken(String token);

    @Query("select u from User u where u.id <> ?1")
    List<User> findAllByIdNot(String id);

}
