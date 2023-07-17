package com.zam.dev.food_order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "category")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(name = "id" , length = 100)
    private String id;

    @Column(name = "name" , length = 100)
    private String name;
    @Column(name = "images" , length = 100)
    private String images;

    @OneToMany(mappedBy = "category")
    private List<Menu> menus;

}
