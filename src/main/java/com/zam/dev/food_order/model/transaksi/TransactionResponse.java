package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.midtrans.VaNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private String status_code;
    private String status_message;
    private String transaction_id;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String currency;
    private String payment_type;
    private String transaction_time;
    private String transaction_status;
    private String fraud_status;
    private List<VaNumber> va_numbers;
    private String expiry_time;

}
