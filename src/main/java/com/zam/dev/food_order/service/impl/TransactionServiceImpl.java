package com.zam.dev.food_order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.midtrans.*;
import com.zam.dev.food_order.model.transaksi.*;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.repository.CartRepository;
import com.zam.dev.food_order.repository.TransactionRepository;
import com.zam.dev.food_order.service.TransactionService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private MidtransCoreApi coreApi;

    private TransactionRepository transactionRepository;

    private CartRepository cartRepository;
    private ObjectMapper objectMapper;

    private ValidationService validationService;
    private ApplicationProperties properties;
    @Override
    @Transactional
    public TransactionResponse createTransaction(User user, MidtransPaymentApiRequest request) throws MidtransError, JsonProcessingException {
        validationService.validate(request);
        Cart cart = cartRepository.findByUserAndId(user, request.getCartId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "cart id not found"));
        if(cart.getStatusCart().equals(STATUS_CART.CHECKOUT)||cart.getStatusCart().equals(STATUS_CART.DONE)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Cart already checkout");
        }
        MidtransPaymentRequest paymentRequest = new MidtransPaymentRequest();
        paymentRequest.setBank_transfer(new MidtransBankTransferRequest(request.getBank_transfer()));
        paymentRequest.setPayment_type("bank_transfer");
        MidtransCustomerDetailRequest customerRequest = new MidtransCustomerDetailRequest();
        customerRequest.setEmail(user.getEmail());
        customerRequest.setPhone(user.getPhoneNumber());
        customerRequest.setLast_name(user.getLastName());
        customerRequest.setFirst_name(user.getFirstName());
        paymentRequest.setCustomer_details(customerRequest);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        paymentRequest.setItem_details(new ArrayList<>());
        for (CartDetail cartDetail : cart.getCartDetails()) {
            MidtransItemDetailsRequest itemDetailsRequest = new MidtransItemDetailsRequest();
            itemDetailsRequest.setId(cartDetail.getMenu().getId());
            itemDetailsRequest.setName(cartDetail.getMenu().getName());
            itemDetailsRequest.setPrice(cartDetail.getMenu().getPrice());
            itemDetailsRequest.setQuantity(cartDetail.getQty());
            paymentRequest.getItem_details().add(itemDetailsRequest);
        }
        MidtransTransactionDetailsRequest transactionDetailsRequest = new MidtransTransactionDetailsRequest();
        transactionDetailsRequest.setGross_amount(cart.getTotalPrice());
        transactionDetailsRequest.setOrder_id(transaction.getId());
        paymentRequest.setTransaction_details(transactionDetailsRequest);
        Map<String, Object> jsonRequest = castToRequestJson(paymentRequest);
        JSONObject jsonObject = coreApi.chargeTransaction(jsonRequest);
        TransactionResponse transactionResponse = objectMapper.readValue(jsonObject.toString(), TransactionResponse.class);
        if(transactionResponse.getStatus_code().equals("201")){
            cart.setStatusCart(STATUS_CART.CHECKOUT);
            cartRepository.save(cart);

            transaction.setStatus_transaction(STATUS_TRANSACTION.WAITING_PAYMENT);
            transaction.setCart(cart);
            transaction.setCreatedPayment(stringToInstant(transactionResponse.getTransaction_time()));
            transaction.setUser(user);
            transaction.setCreatedAt(Instant.now());
            transaction.setUpdatedAt(Instant.now());
            transaction.setVaNumber(transactionResponse.getVa_numbers().get(0).getVa_number());
            if(request.getBank_transfer().equals("bca")){
                transaction.setBank_method(BANK_METHOD.BCA);
            }else{
                transaction.setBank_method(BANK_METHOD.BRI);
            }
            transaction.setRestaurant(cart.getRestaurant());
            transaction.setExpiredPayment(stringToInstant(transactionResponse.getExpiry_time()));
            transaction.setTotalPrice(cart.getTotalPrice());
            transactionRepository.save(transaction);
            return transactionResponse;
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(Integer.parseInt(transactionResponse.getStatus_code())) , transactionResponse.getStatus_message());
    }


    @Override
    @Transactional
    public TransactionByIdResponse getRealtimeTransactionStatus(String transactionId) throws MidtransError {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction not found"));
        JSONObject transactionResponse = coreApi.checkTransaction(transaction.getId());
        log.info(transactionResponse.toString());
        try {
            TransactionStatusResponse response = objectMapper.readValue(transactionResponse.toString(), TransactionStatusResponse.class);
            log.info(response.toString());
            log.info(response.getTransaction_status());
            switch (response.getTransaction_status()) {
                case "settlement" -> {
                    log.info(response.getTransaction_status());
                    transaction.setStatus_transaction(STATUS_TRANSACTION.PROCESS);
                    Cart cart = transaction.getCart();
                    cart.setStatusCart(STATUS_CART.DONE);
                    transaction = transactionRepository.save(transaction);
                    cartRepository.save(cart);
                    log.info("process update");
                    break;
                }
                case "cancel" -> {
                    transaction.setStatus_transaction(STATUS_TRANSACTION.CANCELED);
                    Cart cart = transaction.getCart();
                    cart.setStatusCart(STATUS_CART.DONE);
                    transaction = transactionRepository.save(transaction);
                    log.info(transaction.toString());
                    cartRepository.save(cart);
                    log.info("cancel update");
                    break;
                }
                case "expire" -> {
                    transaction.setStatus_transaction(STATUS_TRANSACTION.EXPIRE);
                    Cart cart = transaction.getCart();
                    cart.setStatusCart(STATUS_CART.DONE);
                    transaction = transactionRepository.save(transaction);
                    log.info(transaction.toString());
                    cartRepository.save(cart);
                    log.info("expire update");
                    break;
                }
                case "pending" ->{
                    transaction.setStatus_transaction(STATUS_TRANSACTION.WAITING_PAYMENT);
                    Cart cart = transaction.getCart();
                    cart.setStatusCart(STATUS_CART.CHECKOUT);
                    transaction = transactionRepository.save(transaction);
                    log.info("pending update");
                    log.info(transaction.toString());
                    cartRepository.save(cart);
                    break;
                }
            }
            return castToTransactionByIdResponse(response , transaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTransaction(NotificationMidtransRequest request) {
        Transaction transaction = transactionRepository.findById(request.getOrder_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction not found"));
        switch (request.getTransaction_status()) {
            case "settlement" -> {
                log.info(request.getTransaction_status());
                transaction.setStatus_transaction(STATUS_TRANSACTION.PROCESS);
                Cart cart = transaction.getCart();
                cart.setStatusCart(STATUS_CART.DONE);
                transaction = transactionRepository.save(transaction);
                cartRepository.save(cart);
                log.info("process update");
                break;
            }
            case "cancel" -> {
                transaction.setStatus_transaction(STATUS_TRANSACTION.CANCELED);
                Cart cart = transaction.getCart();
                cart.setStatusCart(STATUS_CART.DONE);
                transaction = transactionRepository.save(transaction);
                log.info(transaction.toString());
                cartRepository.save(cart);
                log.info("cancel update");
                break;
            }
            case "expire" -> {
                transaction.setStatus_transaction(STATUS_TRANSACTION.EXPIRE);
                Cart cart = transaction.getCart();
                cart.setStatusCart(STATUS_CART.DONE);
                transaction = transactionRepository.save(transaction);
                log.info(transaction.toString());
                cartRepository.save(cart);
                log.info("expire update");
                break;
            }
            case "pending" ->{
                transaction.setStatus_transaction(STATUS_TRANSACTION.WAITING_PAYMENT);
                Cart cart = transaction.getCart();
                cart.setStatusCart(STATUS_CART.CHECKOUT);
                transaction = transactionRepository.save(transaction);
                log.info("pending update");
                log.info(transaction.toString());
                cartRepository.save(cart);
                break;
            }
        }
    }


    private Map<String, Object> castToRequestJson(MidtransPaymentRequest request) {
        return objectMapper.convertValue(request, new TypeReference<Map<String, Object>>() {
        });
    }

    private Instant stringToInstant(String date){
        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private TransactionByIdResponse castToTransactionByIdResponse(TransactionStatusResponse response , Transaction transaction){
        CartTransactionResponse cartResponse = new CartTransactionResponse();
        cartResponse.setCreated_at(transaction.getCart().getCreatedAt().toString());
        cartResponse.setUpdated_at(transaction.getCart().getUpdatedAt().toString());
        cartResponse.setId(transaction.getCart().getId());
        cartResponse.setTotal_price(response.getGross_amount());
        cartResponse.setItem_details(new ArrayList<>());
        for (CartDetail cartDetail : transaction.getCart().getCartDetails()) {
            CartDetailTransactionResponse detailTransaction = new CartDetailTransactionResponse();
            detailTransaction.setId(cartDetail.getId());
            detailTransaction.setQty(cartDetail.getQty());
            detailTransaction.setSub_total(cartDetail.getSubTotal());
            MenuTransactionResponse menuResponse = new MenuTransactionResponse();
            menuResponse.setId(cartDetail.getMenu().getId());
            menuResponse.setImage("http://localhost:"+properties.getPort()+"/images/menu/"+cartDetail.getMenu().getImage());
            menuResponse.setPrice(cartDetail.getMenu().getPrice());
            menuResponse.setName(cartDetail.getMenu().getName());
            detailTransaction.setMenu(menuResponse);
        }
        TransactionByIdResponse transactionByIdResponse = new TransactionByIdResponse();
        transactionByIdResponse.setCreated_at(transaction.getCreatedAt().toString());
        transactionByIdResponse.setUpdated_at(transaction.getUpdatedAt().toString());
        transactionByIdResponse.setTotal_price(transaction.getTotalPrice());
        transactionByIdResponse.setCart(cartResponse);
        transactionByIdResponse.setId(transaction.getId());
        transactionByIdResponse.setPayment(new VaNumber(response.getVa_numbers().get(0).getBank() , response.getVa_numbers().get(0).getVa_number()));
        transactionByIdResponse.setStatus(transaction.getStatus_transaction().name());
        return transactionByIdResponse;
    }

}
