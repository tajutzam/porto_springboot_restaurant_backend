package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.entity.STATUS_CART;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartOnCartDetailResponse {


    private String id;
    private STATUS_CART status;

}
