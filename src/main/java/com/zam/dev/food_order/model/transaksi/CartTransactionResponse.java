package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.transaksi.CartDetailTransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartTransactionResponse {

    private String total_price;
    private String id;
    private String created_at;
    private String updated_at;
    private List<CartDetailTransactionResponse> item_details;

}
