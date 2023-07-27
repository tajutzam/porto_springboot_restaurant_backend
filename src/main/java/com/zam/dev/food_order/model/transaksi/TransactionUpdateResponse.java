package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.entity.STATUS_TRANSACTION;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionUpdateResponse {

    private String id;
    private STATUS_TRANSACTION status;
    private String created_at;
    private String updated_at;

}
