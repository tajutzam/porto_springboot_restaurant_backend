package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.CartRequest;
import com.zam.dev.food_order.model.CartResponse;
import com.zam.dev.food_order.model.ObjectPagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {

    CartResponse createCart(CartRequest cartRequest , User user);

    ObjectPagingResponse<List<CartResponse>> cartsUser(User user , int page , int size);

}
