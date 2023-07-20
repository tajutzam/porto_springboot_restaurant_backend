package com.zam.dev.food_order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.midtrans.httpclient.error.MidtransError;
import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.midtrans.MidtransPaymentApiRequest;
import com.zam.dev.food_order.model.transaksi.TransactionByIdResponse;
import com.zam.dev.food_order.model.transaksi.TransactionResponse;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.TransactionService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionOperations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplTest
{

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    User user;
    @Autowired
    private CartRepository cartRepository;

    Cart cart;

    Restaurant restaurant;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    Category category;
    Menu menu;
    CartDetail cartDetail;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private CartDetailService cartDetailService;

    @Autowired
    private TransactionOperations transactionOperations;


    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        cartRepository.deleteAll();
        restaurantRepository.deleteAll();
        categoryRepository.deleteAll();
        menuRepository.deleteAll();
        cartDetailRepository.deleteAll();
        user = new User();
        user.setId("test");
        user.setUsername("test");
        user.setRefreshToken("refresh");
        user.setToken("token");
        user.setAvatar("avatar");
        user.setPassword("rahasia");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAddress("banyuwangi");
        user.setPhoneNumber("087623123123");
        user.setEmail("mohammadtajutzamzami07@gmail.com");
        userRepository.save(user);

        restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setUsername("test");
        restaurant.setBanner("banner");
        restaurant.setToken("token");
        restaurant.setRefreshToken("refresh");
        restaurant.setAddress("banyuwangi");
        restaurant.setPassword("rahasia");
        restaurant.setFirstName("zam");
        restaurant.setLastName("zami");
        restaurantRepository.save(restaurant);

        category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName("new");
        category.setImages("images");
        categoryRepository.save(category);


        menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        menu.setImage("image");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setName("name menu");
        menu.setPrice(20000);
        menu.setCategory(category);
        menu.setRestaurant(restaurant);
        menuRepository.save(menu);

        cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUser(user);
        cart.setRestaurant(restaurant);
        cart.setStatusCart(STATUS_CART.QUEUE);
        cartRepository.save(cart);


        CartDetailRequest request = new CartDetailRequest();
        request.setQty(2);
        request.setMenuId(menu.getId());
        request.setCartId(cart.getId());

        cartDetailService.createOrder(request);

    }

    @Test
    void testCreateTransactionSuccess() throws MidtransError, JsonProcessingException {
        MidtransPaymentApiRequest request = new MidtransPaymentApiRequest();
        request.setBank_transfer("bca");
        request.setCartId(cart.getId());
        TransactionResponse transaction = transactionService.createTransaction(user, request);
        System.out.println(transaction.getStatus_code());
        assertNotNull(transaction);
        System.out.println(transaction.toString());
    }

    @Test
    void testCreateTransactionConstraintViolation(){
        assertThrows(ConstraintViolationException.class , () -> {
            MidtransPaymentApiRequest request = new MidtransPaymentApiRequest();
            request.setBank_transfer("bni");
            request.setCartId(cart.getId());
            TransactionResponse transaction = transactionService.createTransaction(user, request);
            System.out.println(transaction.getStatus_code());
            assertNotNull(transaction);
            System.out.println(transaction.toString());
        });
    }

    @Test
    void testGetTransactionAndUpdateStatus() throws MidtransError, JsonProcessingException {
        MidtransPaymentApiRequest request = new MidtransPaymentApiRequest();
        request.setBank_transfer("bca");
        request.setCartId(cart.getId());
        TransactionResponse response = transactionOperations.execute(status -> {
            try {
                return transactionService.createTransaction(user, request);
            } catch (MidtransError | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            Thread.sleep(2000L);
            TransactionByIdResponse realtimeTransactionStatus = transactionService.getRealtimeTransactionStatus("eb8a6490-f44b-4eb0-add3-bfe30cbe1aca");
            assertNotNull(realtimeTransactionStatus);
            System.out.println(realtimeTransactionStatus.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}