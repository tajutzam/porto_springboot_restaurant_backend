package com.zam.dev.food_order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_TRANSACTION;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.midtrans.MidtransPaymentApiRequest;
import com.zam.dev.food_order.model.midtrans.NotificationMidtransRequest;
import com.zam.dev.food_order.model.transaksi.TransactionResponse;
import com.zam.dev.food_order.model.transaksi.TransactionCreateResponse;
import com.zam.dev.food_order.model.transaksi.TransactionUpdateRequest;
import com.zam.dev.food_order.model.transaksi.TransactionUpdateResponse;

import java.util.List;

public interface TransactionService {

    TransactionCreateResponse createTransaction(User user , MidtransPaymentApiRequest request) throws MidtransError, JsonProcessingException;

    TransactionResponse getRealtimeTransactionStatus(String transactionId) throws MidtransError;

    void updateTransaction(NotificationMidtransRequest request);

    List<TransactionResponse> findAllTransactionUser(User user , STATUS_TRANSACTION status_transaction);

    List<TransactionResponse> findAllTransactionRestaurant(Restaurant restaurant , STATUS_TRANSACTION status_transaction);

    TransactionUpdateResponse updateStatus(TransactionUpdateRequest request  , Restaurant restaurant , String id);






}
