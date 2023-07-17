package com.zam.dev.food_order.controller.restaurant.menu;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.MenuRequest;
import com.zam.dev.food_order.model.MenuResponse;
import com.zam.dev.food_order.model.ObjectPagingResponse;
import com.zam.dev.food_order.model.WebResponse;
import com.zam.dev.food_order.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("restaurantMenu")
@AllArgsConstructor
@RequestMapping("/api/restaurant/menu")
public class MenuController {

    private MenuService menuService;


    @GetMapping(path =  "/" ,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectPagingResponse<List<MenuResponse>> findAllMenu(Restaurant restaurant , @RequestParam(name = "page" ,defaultValue = "0" , required = false) int page , @RequestParam(name = "size" ,defaultValue = "10" , required = false) int size ){
        return menuService.findAllByRestaurant(page, size, restaurant);
    }

    @PostMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<Object> addMenu(@ModelAttribute MenuRequest request , Restaurant restaurant , @RequestPart("images") MultipartFile multipartFile){
        MenuResponse menuResponse = menuService.add(request, restaurant, multipartFile);
        return WebResponse.builder().data(menuResponse).message("CREATED").status(HttpStatus.CREATED.value()
        ).build();
    }

    @PutMapping(path = "/{menuId}" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> updateMenu(@ModelAttribute MenuRequest request , Restaurant restaurant , @RequestPart("images")MultipartFile multipartFile , @PathVariable("menuId") String menuId){
        MenuResponse menuResponse = menuService.update(request, restaurant, multipartFile, menuId);
        return WebResponse.builder().data(menuResponse).message("OK").status(HttpStatus.OK.value()
        ).build();
    }

    @GetMapping(path = "/{menuId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> findMenuById(Restaurant restaurant , @PathVariable("menuId") String menuId){
        MenuResponse menuResponse = menuService.findByIdAndRestaurant(menuId, restaurant);
        return WebResponse.builder().data(menuResponse).message("CREATED").status(HttpStatus.CREATED.value()
        ).build();
    }

    @DeleteMapping(value = "/{menuId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<Object> deleteMenu(Restaurant restaurant , @PathVariable("menuId") String id){
        int deleteMenu = menuService.deleteMenu(id, restaurant);
        return WebResponse.builder().message("OK").data(deleteMenu).status(HttpStatus.OK.value()).build();
    }

}
