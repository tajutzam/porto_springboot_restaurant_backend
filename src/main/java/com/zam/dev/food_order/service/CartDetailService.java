package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.transaksi.CartDetailResponse;
import com.zam.dev.food_order.model.transaksi.CartRequest;

public interface CartDetailService {

    CartDetailResponse createOrder(CartDetailRequest cartDetailRequest);

    WebResponse<Object> checkRestaurantInCart(CartRequest request , User user);

}
