package com.zam.dev.food_order.controller.restaurant.transaction;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.entity.STATUS_TRANSACTION;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.TransactionResponse;
import com.zam.dev.food_order.model.transaksi.TransactionUpdateRequest;
import com.zam.dev.food_order.model.transaksi.TransactionUpdateResponse;
import com.zam.dev.food_order.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("restaurantTransaction")
@RequestMapping("/api/restaurant/transaction")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;


    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> findAllByStatus(@RequestParam(name = "status", defaultValue = "WAITING_PAYMENT") STATUS_TRANSACTION status_transaction, Restaurant restaurant) {
        List<TransactionResponse> responses = transactionService.findAllTransactionRestaurant(restaurant, status_transaction);
        return WebResponse.builder().data(responses).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping(path = "/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> updateStatusTransaction(Restaurant restaurant, @PathVariable("transactionId") String transactionId, @RequestBody TransactionUpdateRequest request) {
        TransactionUpdateResponse response = transactionService.updateStatus(request, restaurant, transactionId);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

}
