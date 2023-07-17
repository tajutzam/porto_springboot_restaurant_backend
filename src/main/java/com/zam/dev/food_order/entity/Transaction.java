package com.zam.dev.food_order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaksi")
public class Transaction {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "cart_id" , nullable = true)
    private Cart cart;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_transaction")
    private STATUS_TRANSACTION status_transaction;

    @Column(name = "total_price")
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", cart=" + cart +
                ", status_transaction=" + status_transaction +
                ", totalPrice=" + totalPrice +
                ", restaurant=" + restaurant +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return totalPrice == that.totalPrice && Objects.equals(id, that.id) && Objects.equals(cart, that.cart) && status_transaction == that.status_transaction && Objects.equals(restaurant, that.restaurant) && Objects.equals(user, that.user) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cart, status_transaction, totalPrice, restaurant, user, createdAt, updatedAt);
    }
}
