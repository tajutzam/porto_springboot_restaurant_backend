package com.zam.dev.food_order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.midtrans.MidtransPaymentApiRequest;
import com.zam.dev.food_order.model.midtrans.NotificationMidtransRequest;
import com.zam.dev.food_order.model.transaksi.TransactionByIdResponse;
import com.zam.dev.food_order.model.transaksi.TransactionResponse;

public interface TransactionService {

    TransactionResponse createTransaction(User user , MidtransPaymentApiRequest request) throws MidtransError, JsonProcessingException;

    TransactionByIdResponse getRealtimeTransactionStatus(String transactionId) throws MidtransError;

    void updateTransaction(NotificationMidtransRequest request);

}
