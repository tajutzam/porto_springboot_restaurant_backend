package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_CART;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.model.transaksi.CartResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;

import java.util.List;

public interface CartService {

    CartResponse createCart(CartRequest cartRequest , User user);

    ObjectPagingResponse<List<CartResponse>> cartsUser(User user , int page , int size , STATUS_CART status_cart);

    CartResponse checkRestaurantCart(String restaurantId  , User user);

}
