package com.zam.dev.food_order.service;

import com.zam.dev.food_order.model.CartDetailRequest;
import com.zam.dev.food_order.model.CartDetailResponse;

public interface CartDetailService {

    CartDetailResponse createOrder(CartDetailRequest cartDetailRequest);

}
