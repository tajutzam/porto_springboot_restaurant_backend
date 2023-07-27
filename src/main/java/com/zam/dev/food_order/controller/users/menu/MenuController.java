package com.zam.dev.food_order.controller.users.menu;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.RatingMenuRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.transaksi.CartDetailRequest;
import com.zam.dev.food_order.model.transaksi.CartDetailResponse;
import com.zam.dev.food_order.model.transaksi.CartRequest;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.MenuService;
import com.zam.dev.food_order.service.RatingMenuService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userMenu")
@RequestMapping("/api/user/menu")
@AllArgsConstructor
public class MenuController {

    private MenuService menuService;

    private CartDetailService cartDetailService;
    private RatingMenuService ratingMenuService;


    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ObjectPagingResponse<List<MenuResponse>> findAllMenu(User user, @RequestParam(name = "page", defaultValue = "0", required = false) int page, @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return menuService.findAll(page, size);
    }


    @PostMapping(path = "/order/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> orderMenu(User user, @RequestBody CartDetailRequest request) {
        CartDetailResponse response = cartDetailService.createOrder(request);
        return WebResponse.builder().data(response).status(HttpStatus.CREATED.value()).message("CREATED").build();
    }

    @PostMapping(path = "/rating/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> rating(@RequestBody RatingMenuRequest request, User user) {
        int rate = ratingMenuService.rate(user, request);
        return WebResponse.builder().data(rate).message("CREATED").status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/order/check/restaurant")
    public WebResponse<Object> checkCart(@RequestBody CartRequest cartRequest , User user , HttpServletResponse response){
        WebResponse<Object> objectWebResponse = cartDetailService.checkRestaurantInCart(cartRequest, user);
        response.setStatus(objectWebResponse.getStatus());
        return objectWebResponse;
    }

}
