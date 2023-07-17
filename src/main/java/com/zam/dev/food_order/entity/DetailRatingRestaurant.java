package com.zam.dev.food_order.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detail_rating_restaurant")
public class DetailRatingRestaurant {

    @Id
    private String id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rating_restaurant_id")
    private RatingRestaurant ratingRestaurant;

    @Column(name = "rate")
    private int rate;


}
