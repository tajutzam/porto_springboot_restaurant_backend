package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.menu.MenuRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import org.springframework.data.domain.PageImpl;
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

    PageImpl<MenuResponse> findAllMenuByStatus(STATUS_MENU status_menu , Restaurant restaurant, int page , int size);

    int updateStatusMenu(STATUS_MENU status_menu , Restaurant restaurant , String menuId);


}
