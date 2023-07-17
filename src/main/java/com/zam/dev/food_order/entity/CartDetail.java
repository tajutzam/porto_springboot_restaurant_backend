package com.zam.dev.food_order.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_detail")
public class CartDetail {

    @Id
    private String id;


    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "qty")
    private int qty;

    @Column(name = "sub_total")
    private int subTotal;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
