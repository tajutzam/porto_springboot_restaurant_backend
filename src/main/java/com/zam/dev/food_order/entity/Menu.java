package com.zam.dev.food_order.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    private String id;

    private String name;

    private String image;
    private int price;


    @Enumerated(EnumType.STRING)
    @Column(name = "status_menu")
    private STATUS_MENU statusMenu;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Menu menu = (Menu) o;
        return id != null && Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
