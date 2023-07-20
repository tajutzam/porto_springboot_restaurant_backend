package com.zam.dev.food_order.model.midtrans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NotificationMidtransRequest {


    private List<VaNumber> va_numbers;
    private String transaction_time;
    private String transaction_status;
    private String transaction_id;
    private String status_message;
    private String status_code;
    private String signature_key;
    private String settlement_time;
    private String payment_type;
    private List<Object> payment_amounts;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String fraud_status;
    private String currency;

}
