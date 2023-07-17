package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu , String> {


    @Query("select (count(m) > 0) from Menu m where m.name = ?1 and m.restaurant = ?2")
    boolean existsByNameAndRestaurant(String name , Restaurant restaurant);

    boolean existsByNameAndRestaurantAndIdNot(String name , Restaurant restaurant , String menuId);

    Page<Menu> findAllByRestaurant(Restaurant restaurant , Pageable pageable);

    Optional<Menu> findByRestaurantAndId(Restaurant restaurant , String id);

    @Modifying
    @Query(value = "delete from Menu m where m.id= :id")
    int customDeleteMenu(@Param("id") String id);


}
