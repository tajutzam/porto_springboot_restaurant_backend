package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.*;
import com.zam.dev.food_order.model.menu.MenuOnCartDetailResponse;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.repository.CartDetailRepository;
import com.zam.dev.food_order.repository.CartRepository;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.CartService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CartDetailServiceImpl implements CartDetailService {

    private CartDetailRepository cartDetailRepository;

    private CartRepository cartRepository;

    private MenuRepository menuRepository;
    private ValidationService validationService;

    private TransactionOperations transactionOperations;
    private ApplicationProperties applicationProperties;
    private CartService cartService;


    @Override
    public CartDetailResponse createOrder(CartDetailRequest request) {

        validationService.validate(request);
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart not found"));
        if (cart.getStatusCart() == STATUS_CART.CHECKOUT || cart.getStatusCart() == STATUS_CART.DONE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart already checked out or done");
        }
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu ID not found"));
        if (menu.getStatusMenu() == STATUS_MENU.NOT_READY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu not ready");
        }

        checkMenuAlreadyAdd(cart, menu);

        if (!cart.getRestaurant().getId().equals(menu.getRestaurant().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cant order in different restaurant");
        }

        CartDetail responseDetail = transactionOperations.execute(status -> {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setId(UUID.randomUUID().toString());
            cartDetail.setCart(cart);
            cartDetail.setQty(request.getQty());
            cartDetail.setSubTotal(request.getQty() * menu.getPrice());
            cartDetail.setMenu(menu);
            log.info("create cart");
            return cartDetailRepository.save(cartDetail);
        });
        Cart cartResponse = transactionOperations.execute(transactionStatus -> {
            int oldPrice = cart.getTotalPrice();
            if (Objects.nonNull(responseDetail)) {
                cart.setTotalPrice(oldPrice + responseDetail.getSubTotal());
                return cartRepository.save(cart);
            }
            return null;
        });
        if (Objects.nonNull(responseDetail) && Objects.nonNull(cartResponse)) {
            return castToCartDetailResponse(responseDetail, cartResponse, menu);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "failed to create cart detail");
    }

    @Override
    public WebResponse<Object> checkRestaurantInCart(CartRequest request, User user) {
        validationService.validate(request);
        CartResponse cartResponse = cartService.checkRestaurantCart(request.getRestaurant_id(), user);
        if (cartResponse == null) {
            return WebResponse.builder().status(HttpStatus.OK.value()).message("OK").data(true).build();
        }
        return WebResponse.builder().status(HttpStatus.ACCEPTED.value()).message("ACCEPTED").data(cartResponse).build();
    }

    private void checkMenuAlreadyAdd(Cart cart, Menu menu) {
        for (CartDetail detail : cart.getCartDetails()) {
            if (detail.getMenu().equals(menu)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu already checkout");
            }
        }
    }

    private CartDetailResponse castToCartDetailResponse(CartDetail cartDetail, Cart cart, Menu menu) {
        CartOnCartDetailResponse cartOnCartDetailResponse = CartOnCartDetailResponse.builder().status(cart.getStatusCart()).id(cart.getId()).build();
        MenuOnCartDetailResponse menuOnCartDetailResponse = MenuOnCartDetailResponse.builder().images("http://localhost:" + applicationProperties.getPort() + "/images/menu/" + menu.getImage()).id(menu.getId()).build();
        return CartDetailResponse.
                builder()
                .id(cartDetail.getId())
                .qty(cartDetail.getQty())
                .subTotal(cartDetail.getSubTotal())
                .cart(cartOnCartDetailResponse)
                .menu(menuOnCartDetailResponse)
                .build();
    }

}
