package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_TRANSACTION;
import com.zam.dev.food_order.entity.Transaction;
import com.zam.dev.food_order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction , String> {

    @Query("select t from Transaction t where t.user = ?1 and t.statusTransaction = ?2")
    List<Transaction> findAllByUserAndStatusTransaction(User user , STATUS_TRANSACTION status_transaction);

    @Query("select t from Transaction t where t.restaurant = ?1 and t.statusTransaction = ?2")
    List<Transaction> findAllByRestaurantAndStatusTransaction(Restaurant restaurant , STATUS_TRANSACTION status_transaction);

    Optional<Transaction> findByIdAndRestaurant(String id , Restaurant restaurant);

}
