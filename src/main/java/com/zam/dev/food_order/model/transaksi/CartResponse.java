package com.zam.dev.food_order.model.transaksi;


import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartResponse {

    private String id;
    private int total_price;
    private String status_cart;
    private Instant created_at;
    private Instant updated_at;

}
