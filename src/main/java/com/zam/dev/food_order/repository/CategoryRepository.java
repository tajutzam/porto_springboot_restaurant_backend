package com.zam.dev.food_order.repository;


import com.zam.dev.food_order.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category , String> {

    Optional<Category> findByName(String name);
    Page<Category> findAll(Pageable pageable);

    @Modifying
    @Query(value = "delete from Category c where c.id= :id")
    int customDeleteCategory(@Param("id") String id);

}
