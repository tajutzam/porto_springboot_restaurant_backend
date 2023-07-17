package com.zam.dev.food_order.controller.users.menu;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.service.CartDetailService;
import com.zam.dev.food_order.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController("userMenu")
@RequestMapping("/api/user/menu")
@AllArgsConstructor
public class MenuController {

    private MenuService menuService;

    private CartDetailService cartDetailService;


    @GetMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ObjectPagingResponse<List<MenuResponse>> findAllMenu(User user , @RequestParam(name = "page" , defaultValue = "0" , required = false) int page , @RequestParam(name = "size" , defaultValue = "10", required = false) int size
    ){
        return menuService.findAll(page ,size);
    }


    @PostMapping(path = "/order/add" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> orderMenu(User user , @RequestBody CartDetailRequest request){
        CartDetailResponse response = cartDetailService.createOrder(request);
        return WebResponse.builder().data(response).status(HttpStatus.CREATED.value()).message("CREATED").build();
    }

}
