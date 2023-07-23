package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.midtrans.VaNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private String id;
    private int total_price;
    private String created_at;
    private String updated_at;
    private String status;
    private VaNumber payment;
    private CartTransactionResponse cart;

}
