package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin , String> {


    Optional<Admin> findByUsername(String username);

    Optional<Admin> findByToken(String token);

    Optional<Admin> findByRefreshToken(String refreshToken);

}
