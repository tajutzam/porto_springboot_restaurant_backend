package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.model.MenuRequest;
import com.zam.dev.food_order.model.MenuResponse;
import com.zam.dev.food_order.model.ObjectPagingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService {

    MenuResponse add(MenuRequest menuRequest , Restaurant restaurant , MultipartFile multipartFile);

    MenuResponse update(MenuRequest menuRequest , Restaurant restaurant , MultipartFile multipartFile , String menuId);

    ObjectPagingResponse<List<MenuResponse>> findAllByRestaurant(int page , int size , Restaurant restaurant);

    ObjectPagingResponse<List<MenuResponse>> findAll(int page , int size);

    MenuResponse findById(String id);

    MenuResponse findByIdAndRestaurant(String id , Restaurant restaurant);

    int deleteMenu(String id , Restaurant restaurant);



}
