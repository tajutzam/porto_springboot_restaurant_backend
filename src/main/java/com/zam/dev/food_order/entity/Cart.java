package com.zam.dev.food_order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
@Entity
public class Cart {

    @Id
    private String id;
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_cart")
    private STATUS_CART statusCart;

    @OneToOne
    @JoinColumn(name = "transaction_id" , nullable = true)
    private Transaction transaction;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(
            mappedBy = "cart", fetch = FetchType.EAGER
    )
    private List<CartDetail> cartDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cart cart = (Cart) o;
        return id != null && Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
