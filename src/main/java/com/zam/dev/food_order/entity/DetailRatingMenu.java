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
@Table(name = "detail_rating_menu")
public class DetailRatingMenu {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "rating_menu_id")
    private RatingMenu ratingMenu;

    @Column(name = "rate")
    private int rate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;


}
