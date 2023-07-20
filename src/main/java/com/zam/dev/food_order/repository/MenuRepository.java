package com.zam.dev.food_order.repository;

import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu , String> {


    @Query("select (count(m) > 0) from Menu m where m.name = ?1 and m.restaurant = ?2")
    boolean existsByNameAndRestaurant(String name , Restaurant restaurant);

    boolean existsByNameAndRestaurantAndIdNot(String name , Restaurant restaurant , String menuId);

    Page<Menu> findAllByRestaurant(Restaurant restaurant , Pageable pageable);

    Optional<Menu> findByRestaurantAndId(Restaurant restaurant , String id);


    @Transactional
    @Modifying
    @Query("delete from Menu m where m.id = :id and m.restaurant = :restaurant")
    int deleteByIdAndRestaurant(@Param("id") String id , @Param("restaurant") Restaurant restaurant);


    @Query("select m from Menu m where m.statusMenu = ?1 and m.restaurant = ?2")
    Page<Menu> findAllByStatusMenuAndRestaurant(STATUS_MENU status_menu , Restaurant restaurant , Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            "update Menu m set m.statusMenu= :statusMenu where m.restaurant= :restaurant and m.id= :menuId"
    )
    int updateMenuStatus(@Param("statusMenu") STATUS_MENU status_menu , @Param("restaurant") Restaurant restaurant , @Param("menuId") String menuId);

}
