package com.zam.dev.food_order.controller.restaurant;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.other.WebResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantUpdateRequest;
import com.zam.dev.food_order.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> get(Restaurant restaurant){
        RestaurantResponse response = restaurantService.get(restaurant);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()
        ).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Object> update(Restaurant restaurant , @RequestBody RestaurantUpdateRequest request){
        RestaurantResponse response = restaurantService.update(request, restaurant);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

    @PutMapping("/avatar")
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateAvatar(@RequestPart("banner") MultipartFile file , Restaurant restaurant){
        RestaurantResponse response = restaurantService.updateAvatar(file, restaurant);
        return WebResponse.builder().data(response).message("OK").status(HttpStatus.OK.value()).build();
    }

}
