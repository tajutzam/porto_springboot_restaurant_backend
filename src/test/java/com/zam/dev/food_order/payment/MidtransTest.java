package com.zam.dev.food_order.payment;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.zam.dev.food_order.model.midtrans.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class MidtransTest {

    @Autowired
    private MidtransCoreApi coreApi;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMidtrans() throws MidtransError {
        Midtrans.serverKey = "SB-Mid-server-OQVuVaKzhMvS2s7UfDqKyE5O";
        Midtrans.isProduction = false;

        UUID idRand = UUID.randomUUID();
        Map<String, Object> chargeParams = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", "265000");

        chargeParams.put("transaction_details", transactionDetails);
        Map<String , String> bank = new HashMap<>();
        bank.put("bank" , "bca");
        chargeParams.put("payment_type", "bank_transfer");
        chargeParams.put("bank_transfer" , bank);

        JSONObject jsonObject = CoreApi.chargeTransaction(chargeParams);
        System.out.println(jsonObject);
    }

    @Test
    void testCreateMidtrans() throws MidtransError {

        UUID idRand = UUID.randomUUID();
        Map<String, Object> chargeParams = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", "265000");

        chargeParams.put("transaction_details", transactionDetails);
        Map<String , String> bank = new HashMap<>();
        bank.put("bank" , "bca");
        chargeParams.put("payment_type", "bank_transfer");
        chargeParams.put("bank_transfer" , bank);

        JSONObject jsonObject = coreApi.chargeTransaction(chargeParams);
        System.out.println(jsonObject);
    }

    @Test
    void testParseObject() throws JsonProcessingException {
        MidtransPaymentRequest request = new MidtransPaymentRequest();
        request.setPayment_type("bank_transfer");
        request.setCustomer_details(new MidtransCustomerDetailRequest("email@gmail.com" , "zam" , "zam" , "09123123"));
        request.setBank_transfer(new MidtransBankTransferRequest("bca"));
        request.setItem_details(List.of(new MidtransItemDetailsRequest("1" ,1 , 1 , "1")));
        request.setTransaction_details(new MidtransTransactionDetailsRequest("1" , 2000));
        Map<String, Object> convertValue = objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(convertValue);
    }

    @Test
    void testCreateTransactionUsingObjectRequest() throws MidtransError {
        MidtransPaymentRequest request = new MidtransPaymentRequest();
        request.setPayment_type("bank_transfer");
        request.setCustomer_details(new MidtransCustomerDetailRequest("email@gmail.com" , "zam" , "zam" , "09123123"));
        request.setBank_transfer(new MidtransBankTransferRequest("bca"));
        request.setItem_details(List.of(new MidtransItemDetailsRequest("1" ,1 , 1 , "1")));
        request.setTransaction_details(new MidtransTransactionDetailsRequest("1" , 1));
        Map<String, Object> convertValue = objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
        });
        coreApi.chargeTransaction(convertValue);
    }


}
