package com.zam.dev.food_order.controller.admin.restaurant;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.model.CashRestaurantResponse;
import com.zam.dev.food_order.model.ObjectPagingResponse;
import com.zam.dev.food_order.model.RestaurantResponse;
import com.zam.dev.food_order.model.WebResponse;
import com.zam.dev.food_order.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminRestaurant")
@RequestMapping("/api/admin/restaurant")
@AllArgsConstructor
public class RestaurantController {

    private AdminService adminService;

    @GetMapping(path = "" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPagingResponse<List<RestaurantResponse>> restaurants(Admin admin , @RequestParam(name = "page" , defaultValue = "0") int page , @RequestParam(name = "size" , defaultValue = "10") int size){
        return adminService.restaurants(admin , page  , size);
    }

    @GetMapping(path = "/cash")
    public WebResponse<Object> incomeMoneyRestaurants(Admin admin){
        List<CashRestaurantResponse> balances = adminService.getBalances(admin);
        return WebResponse.builder().data(balances).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping(path = "/cash/{restaurantId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> payToRestaurant(Admin admin , @PathVariable("restaurantId") String restaurantId){
        int updated = adminService.pay(restaurantId);
        return WebResponse.builder().data(updated).status(HttpStatus.OK.value()).message("OK").build();
    }


}
