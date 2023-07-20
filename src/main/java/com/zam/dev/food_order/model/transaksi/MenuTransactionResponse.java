package com.zam.dev.food_order.model.transaksi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTransactionResponse {


    private String id;
    private String name;
    private int price;
    private String image;

}
