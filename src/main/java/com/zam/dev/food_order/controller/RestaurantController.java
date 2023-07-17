package com.zam.dev.food_order.controller;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.*;
import com.zam.dev.food_order.service.MenuService;
import com.zam.dev.food_order.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final MenuService menuService;

    @GetMapping(path = "/api/restaurant/" , produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> get(Restaurant restaurant){
        RestaurantResponse response = restaurantService.get(restaurant);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()
        ).build();
    }

    @GetMapping(path =  "/api/restaurant/menu/" ,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPagingResponse<List<MenuResponse>> findAllMenu(Restaurant restaurant , @RequestParam(name = "page" ,defaultValue = "0" , required = false) int page , @RequestParam(name = "size" ,defaultValue = "10" , required = false) int size ){
        return menuService.findAllByRestaurant(page, size, restaurant);
    }

    @PostMapping(path = "/api/restaurant/menu/" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> addMenu(@ModelAttribute MenuRequest request , Restaurant restaurant , @RequestPart("images")MultipartFile multipartFile){
        MenuResponse menuResponse = menuService.add(request, restaurant, multipartFile);
        return WebResponse.builder().data(menuResponse).message("CREATED").status(HttpStatus.CREATED.value()
        ).build();
    }

    @PutMapping(path = "/api/restaurant/menu/{menuId}" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateMenu(@ModelAttribute MenuRequest request , Restaurant restaurant , @RequestPart("images")MultipartFile multipartFile , @PathVariable("menuId") String menuId){
        MenuResponse menuResponse = menuService.update(request, restaurant, multipartFile, menuId);
        return WebResponse.builder().data(menuResponse).message("CREATED").status(HttpStatus.CREATED.value()
        ).build();
    }
}
