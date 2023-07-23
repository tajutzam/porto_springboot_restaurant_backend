package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Cart;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_CART;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.model.transaksi.CartResponse;
import com.zam.dev.food_order.model.other.ObjectPaging;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.repository.CartRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.service.CartService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private  CartRepository cartRepository;
    private RestaurantRepository restaurantRepository;
    private ValidationService validationService;





    @Override
    @Transactional
    public CartResponse createCart(CartRequest cartRequest , User user) {

        validationService.validate(cartRequest);
        Restaurant restaurant = restaurantRepository.findById(cartRequest.getRestaurant_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "restaurant not found"));

        List<Cart> cartList = cartRepository.findAllByRestaurantAndUser(restaurant, user);
        for (Cart cart : cartList) {
            if(cart.getStatusCart().equals(STATUS_CART.CHECKOUT) || cart.getStatusCart().equals(STATUS_CART.QUEUE)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "please complete your order first before");
            }
        }
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setStatusCart(STATUS_CART.QUEUE);
        cart.setTotalPrice(0);
        cart.setRestaurant(restaurant);
        cart.setUser(user);
        Cart response = cartRepository.save(cart);
        return castToCartResponse(response);
    }

    @Override
    public ObjectPagingResponse<List<CartResponse>> cartsUser(User user, int page , int size , STATUS_CART status_cart) {

        Pageable pageable = PageRequest.of(page ,size);
        Page<Cart> cartPage = cartRepository.findAllByUserAndStatusCart(user, status_cart , pageable);
        List<CartResponse> responseList = cartPage.getContent().stream().map(this::castToCartResponse).toList();
        ObjectPaging objectPaging = new ObjectPaging();
        objectPaging.setPage(cartPage.getNumber());
        objectPaging.setSize(responseList.size());
        ObjectPagingResponse<List<CartResponse>> response = new ObjectPagingResponse<>();
        response.setObjectPaging(objectPaging);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OK");
        response.setData(responseList);
        return response;
    }

    @Override
    public CartResponse checkRestaurantCart(String restaurantId , User user) {
        Cart cart = cartRepository.findByRestaurant_idAndUser(restaurantId , user).orElse(null);
        if(cart != null){
            return castToCartResponse(cart);
        }
        return null;
    }

    private CartResponse castToCartResponse(Cart cart){
        return CartResponse.builder()
                .status_cart(cart.getStatusCart().name())
                .created_at(cart.getCreatedAt())
                .total_price(cart.getTotalPrice())
                .id(cart.getId())
                .updated_at(cart.getUpdatedAt())
                .build();
    }

}
