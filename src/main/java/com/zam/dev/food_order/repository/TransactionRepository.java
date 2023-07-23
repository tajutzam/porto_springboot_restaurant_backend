package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Transaction;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction , String> {

    List<Transaction> findAllByUser(User user);

}
