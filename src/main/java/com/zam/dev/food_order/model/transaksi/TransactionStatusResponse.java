package com.zam.dev.food_order.model.transaksi;

import com.zam.dev.food_order.model.midtrans.VaNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusResponse {

    private String status_code;
    private String transaction_id;
    private String gross_amount;
    private String currency;
    private String order_id;
    private String payment_type;
    private String signature_key;
    private String transaction_status;
    private String fraud_status;
    private String status_message;
    private String merchant_id;
    private List<VaNumber> va_numbers;
    private List<Object> payment_amounts; // The data type of payment_amounts is not clear in the JSON
    private String transaction_time;
    private String settlement_time;
    private String expiry_time;

}
