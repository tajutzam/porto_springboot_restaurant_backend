package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction , String> {
}
