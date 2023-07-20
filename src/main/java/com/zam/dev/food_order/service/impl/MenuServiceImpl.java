package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.admin.CategoryResponse;
import com.zam.dev.food_order.model.menu.MenuRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPaging;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.model.restaurant.RestaurantResponse;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.properties.FileProperties;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.service.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private MenuRepository menuRepository;
    private ValidationService validationService;
    private FileProperties fileProperties;
    private FileUploadService fileUploadService;
    private CategoryRepository categoryRepository;
    private ApplicationProperties applicationProperties;
    private RestaurantService restaurantService;
    private CategoryService categoryService;


    @Override
    @Transactional
    public MenuResponse add(MenuRequest menuRequest, Restaurant restaurant, MultipartFile multipartFile) {
        validationService.validate(menuRequest);
        Category category = categoryRepository.findById(menuRequest.getCategory_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category id not found"));
        if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please insert image");
        }
        if (menuRepository.existsByNameAndRestaurant(menuRequest.getName(), restaurant)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu name already defined");
        }

        String menuImage = fileUploadService.upload(multipartFile, "menu", fileProperties.getMenu());

        Menu menu = new Menu();
        menu.setId(UUID.randomUUID().toString());
        menu.setStatusMenu(menuRequest.getStatus_menu());
        menu.setName(menuRequest.getName());
        menu.setCategory(category);
        menu.setRestaurant(restaurant);
        menu.setImage(menuImage);
        Menu saved = menuRepository.save(menu);
        return castToMenuResponse(saved, category, restaurant);
    }

    @Override
    @Transactional
    public MenuResponse update(MenuRequest menuRequest, Restaurant restaurant, MultipartFile multipartFile , String menuId) {
        validationService.validate(menuRequest);

        Menu menu = menuRepository.findByRestaurantAndId(restaurant, menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "menu id not found"));

        Category category = categoryRepository.findById(menuRequest.getCategory_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category id not found"));
        if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please insert image");
        }
        if (menuRepository.existsByNameAndRestaurantAndIdNot(menuRequest.getName(), restaurant, menuId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu name already defined");
        }

        String menuImage = fileUploadService.upload(multipartFile, "menu", fileProperties.getMenu());

        menu.setStatusMenu(menuRequest.getStatus_menu());
        menu.setName(menuRequest.getName());
        menu.setCategory(category);
        menu.setRestaurant(restaurant);
        menu.setImage(menuImage);
        Menu saved = menuRepository.save(menu);
        return castToMenuResponse(saved, category, restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectPagingResponse<List<MenuResponse>> findAllByRestaurant(int page, int size, Restaurant restaurant) {
        Sort orders = Sort.by(Sort.Order.desc("name"));
        Pageable pageable = PageRequest.of(page, size , orders);
        Page<Menu> menuPage = menuRepository.findAllByRestaurant(restaurant, pageable);
        List<MenuResponse> menuResponses = menuPage.getContent().stream().map(menu -> castToMenuResponse(menu, menu.getCategory(), restaurant)).toList();
        PageImpl<MenuResponse> responses = new PageImpl<>(menuResponses, pageable, menuPage.getTotalElements());
        return castToObjectPaging(responses);
    }

    private ObjectPagingResponse<List<MenuResponse>> castToObjectPaging(PageImpl<MenuResponse> responses ){
        ObjectPaging objectPaging = ObjectPaging.builder()
                .page(responses.getNumber())
                .size(responses.getContent().size())
                .build();
        ObjectPagingResponse<List<MenuResponse>> response = new ObjectPagingResponse<>();
        response.setObjectPaging(objectPaging);
        response.setData(responses.getContent());
        response.setMessage("OK");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public ObjectPagingResponse<List<MenuResponse>> findAll(int page, int size) {
        if(size == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Page size must not be less than one");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuRepository.findAll(pageable);
        ObjectPagingResponse<List<MenuResponse>> pagingResponse = new ObjectPagingResponse<>();
        ObjectPaging objectPaging = new ObjectPaging();
        objectPaging.setPage(page);
        objectPaging.setSize(menuPage.getContent().size());
        pagingResponse.setObjectPaging(objectPaging);
        pagingResponse.setMessage("OK");
        pagingResponse.setStatus(HttpStatus.OK.value());
        List<MenuResponse> responseList = menuPage.getContent().stream().map(menu -> this.castToMenuResponse(menu, menu.getCategory(), menu.getRestaurant())).toList();
        pagingResponse.setData(responseList);
        return pagingResponse;
    }

    @Override
    public MenuResponse findById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "menu id not found"));
        return castToMenuResponse(menu, menu.getCategory(), menu.getRestaurant());
    }

    @Override
    public MenuResponse findByIdAndRestaurant(String id, Restaurant restaurant) {
        Menu menu = menuRepository.findByRestaurantAndId(restaurant, id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "menu id not found"));
        return castToMenuResponse(menu , menu.getCategory(), menu.getRestaurant());
    }

    @Override
    @Transactional
    public int deleteMenu(String id , Restaurant restaurant) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "menu id not found"));
        return menuRepository.deleteByIdAndRestaurant(menu.getId(), restaurant);
    }

    private MenuResponse castToMenuResponse(Menu menu, Category category, Restaurant restaurant) {
        RestaurantResponse restaurantResponse = restaurantService.castToRestaurantResponse(restaurant);
        CategoryResponse categoryResponse = categoryService.castToCategoryResponse(category);
        return MenuResponse.builder()
                .category(categoryResponse)
                .restaurant(restaurantResponse)
                .id(menu.getId())
                .status(menu.getStatusMenu())
                .name(menu.getName())
                .image("http://localhost:" + applicationProperties.getPort() + "/images/menu/" + menu.getImage())
                .created_at(menu.getCreatedAt())
                .updated_at(menu.getUpdatedAt())
                .build();
    }


    @Override
    public PageImpl<MenuResponse> findAllMenuByStatus(STATUS_MENU status_menu , Restaurant restaurant, int page , int size) {
        Pageable pageable = PageRequest.of(page , size);
        Page<Menu> menuPage = menuRepository.findAllByStatusMenuAndRestaurant(status_menu,restaurant, pageable);
        List<MenuResponse> menuResponseList = menuPage.getContent().stream().map(menu -> castToMenuResponse(menu, menu.getCategory(), menu.getRestaurant())).toList();
        return new PageImpl<>(menuResponseList , pageable , menuPage.getTotalElements());
    }

    @Override
    @Transactional
    public int updateStatusMenu(STATUS_MENU status_menu, Restaurant restaurant, String menuId) {
        menuRepository.findByRestaurantAndId(restaurant , menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST , "menu id not found"));
        return menuRepository.updateMenuStatus(status_menu, restaurant, menuId);
    }
}
