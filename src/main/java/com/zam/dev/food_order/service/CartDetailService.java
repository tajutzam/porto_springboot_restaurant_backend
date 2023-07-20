package com.zam.dev.food_order.service;

import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.transaksi.CartDetailResponse;

public interface CartDetailService {

    CartDetailResponse createOrder(CartDetailRequest cartDetailRequest);

}
