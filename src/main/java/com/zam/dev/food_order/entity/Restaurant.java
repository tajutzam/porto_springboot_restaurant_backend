package com.zam.dev.food_order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "restaurant")
@Entity
public class Restaurant {

    @Id
    @Column(name = "id" , length = 100)
    private String id;
    @Column(name = "username" , length = 100)
    private String username;
    @Column(name = "password" , length = 100)
    private String password;
    @Column(name = "last_name" , length = 100)
    private String lastName;
    @Column(name = "first_name" , length = 100)
    private String firstName;
    @Column(name = "address" , length = 100)
    private String address;
    @Column(name = "token" , length = 400)
    private String token;
    @Column(name = "refresh_token" , length = 400)
    private String refreshToken;
    @Column(name = "banner")
    private String banner;

}
