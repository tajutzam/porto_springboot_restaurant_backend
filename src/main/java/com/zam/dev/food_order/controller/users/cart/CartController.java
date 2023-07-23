package com.zam.dev.food_order.controller.users.cart;

import com.zam.dev.food_order.entity.STATUS_CART;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.model.transaksi.CartResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userCart")
@RequestMapping("/api/user/cart")
@AllArgsConstructor
public class CartController {

    private CartService cartService;

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> addCart(@RequestBody CartRequest request, User user) {
        CartResponse cartResponse = cartService.createCart(request, user);
        return WebResponse.builder().
                data(cartResponse)
                .message("CREATED")
                .status(HttpStatus.CREATED.value()).
                build();
    }

    @GetMapping(path = "" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPagingResponse<List<CartResponse>> cartsUser(User user , @RequestParam(name = "page" , defaultValue = "0" , required = true) int page , @RequestParam(name = "size" , defaultValue = "10" , required = false) int size , @RequestParam(value = "status" , defaultValue = "QUEUE")STATUS_CART status_cart){
        return cartService.cartsUser(user , page , size , status_cart);
    }

}
