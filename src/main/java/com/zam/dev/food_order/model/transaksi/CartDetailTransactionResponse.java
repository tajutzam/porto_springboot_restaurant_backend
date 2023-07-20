package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.menu.MenuResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailTransactionResponse {

    private String id;
    private int sub_total;
    private int qty;
    private MenuTransactionResponse menu;

}
