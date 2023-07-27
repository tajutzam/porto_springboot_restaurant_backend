package com.zam.dev.food_order.controller.users.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.zam.dev.food_order.entity.STATUS_TRANSACTION;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.midtrans.MidtransPaymentApiRequest;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.TransactionCreateResponse;
import com.zam.dev.food_order.model.transaksi.TransactionResponse;
import com.zam.dev.food_order.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user/transaction")
@RestController("userTransaction")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> createTransaction(User user , @RequestBody MidtransPaymentApiRequest request) throws MidtransError, JsonProcessingException {

        TransactionCreateResponse response = transactionService.createTransaction(user, request);
        return WebResponse.builder().message("CREATED").status(HttpStatus.CREATED.value()).data(response).build();

    }

    @GetMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> findAllTransactionUser(User user , @RequestParam(name = "status" , defaultValue = "WAITING_PAYMENT")STATUS_TRANSACTION status_transaction){
        List<TransactionResponse> responses = transactionService.findAllTransactionUser(user , status_transaction);
        return WebResponse.builder().message("OK").data(responses).status(HttpStatus.OK.value()).build();
    }

    @GetMapping(path = "/{transactionId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> findByTransactionId(User user , @PathVariable("transactionId") String transactionId) throws MidtransError {
        TransactionResponse response = transactionService.getRealtimeTransactionStatus(transactionId);
        return WebResponse.builder().message("OK").data(response).status(HttpStatus.OK.value()).build();
    }




}
