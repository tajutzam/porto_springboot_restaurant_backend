package com.zam.dev.food_order.controller;

import com.zam.dev.food_order.model.midtrans.NotificationMidtransRequest;
import com.zam.dev.food_order.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationMidtransController {

    private TransactionService transactionService;


    @PostMapping("/api/transaction")
    public void notification(@RequestBody NotificationMidtransRequest request){
        transactionService.updateTransaction(request);
    }

}
