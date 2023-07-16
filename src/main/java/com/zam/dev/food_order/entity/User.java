package com.zam.dev.food_order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    @Column(name = "username"  , length = 100)
    private String username;
    @Column(name = "password" , length = 100)
    private String password;
    @Column(name = "first_name" , length = 100)
    private String firstName;
    @Column(name = "last_name" , length = 100)
    private String lastName;
    @Column(name = "address" , length = 100)
    private String address;
    @Column(name = "token" , length = 400)
    private String token;
    @Column(name = "refresh_token" , length = 400)
    private String refreshToken;

    @Column(name = "avatar")
    private String avatar;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "update_at")
    private  Instant updatedAt;

}
