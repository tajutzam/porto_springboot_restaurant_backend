package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.menu.MenuOnCartDetailResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailResponse {

    private String id;
    private int qty;
    private int subTotal;
    private CartOnCartDetailResponse cart;
    private MenuOnCartDetailResponse menu;

    @Override
    public String toString() {
        return "CartDetailResponse{" +
                "id='" + id + '\'' +
                ", qty=" + qty +
                ", subTotal=" + subTotal +
                ", cart=" + cart +
                ", menu=" + menu +
                '}';
    }
}
