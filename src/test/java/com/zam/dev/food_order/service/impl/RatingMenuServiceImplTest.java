package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.*;
import com.zam.dev.food_order.model.RatingMenuRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.repository.*;
import com.zam.dev.food_order.service.MenuService;
import com.zam.dev.food_order.service.RatingMenuService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class RatingMenuServiceImplTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionOperations transactionOperations;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RatingMenuService ratingMenuService;
    Restaurant restaurant;
    Category category;
    User user;

    Menu menu;
    @BeforeEach
    void setUp(){

        transactionOperations.executeWithoutResult(transactionStatus -> {
            userRepository.deleteAll();
            categoryRepository.deleteAll();
            restaurantRepository.deleteAll();
            menuRepository.deleteAll();
            restaurant = new Restaurant();
            restaurant.setId("2");
            restaurant.setUsername("test");
            restaurant.setBanner("banner");
            restaurant.setToken("token");
            restaurant.setRefreshToken("refresh");
            restaurant.setAddress("banyuwangi");
            restaurant.setPassword("rahasia");
            restaurant.setFirstName("zam");
            restaurant.setLastName("zami");
            restaurantRepository.save(restaurant);
            category = new Category();
            category.setId("2");
            category.setName("test");
            category.setImages("image.png");
            categoryRepository.save(category);
            user = new User();
            user.setEmail("email");
            user.setAvatar("avatar");
            user.setAddress("banyuwangi");
            user.setPhoneNumber("123123123");
            user.setPassword("rahasia");
            user.setFirstName("firstname");
            user.setLastName("lastname");
            user.setToken("token");
            user.setRefreshToken("rerfreshtoken");
            user.setId(UUID.randomUUID().toString());
            user.setUsername("zamz");
            userRepository.save(user);
            menu = new Menu();
            menu.setId(UUID.randomUUID().toString());
            menu.setStatusMenu(STATUS_MENU.READY);
            menu.setRestaurant(restaurant);
            menu.setCategory(category);
            menu.setPrice(123123);
            menu.setImage("image");
            menu.setName("burger");
            menuRepository.save(menu);
        });
    }

    @Test
    void testCreateRating(){
        RatingMenuRequest ratingMenuRequest  = new RatingMenuRequest();
        ratingMenuRequest.setRate(5);
        ratingMenuRequest.setMenuId(menu.getId());
        int rate = ratingMenuService.rate(user , ratingMenuRequest);
        ratingMenuRequest.setRate(4);
        ratingMenuService.rate(user , ratingMenuRequest );
        ObjectPagingResponse<List<MenuResponse>> response = menuService.findAll(0, 1);
        assertEquals(4.5 ,response.getData().get(0).getRate());
    }

    @Test
    void testCreateRateConstraintViolationException(){
        assertThrows(ConstraintViolationException.class , () -> {
            RatingMenuRequest ratingMenuRequest  = new RatingMenuRequest();
            ratingMenuRequest.setRate(0);
            ratingMenuRequest.setMenuId(menu.getId());
            int rate = ratingMenuService.rate(user , ratingMenuRequest);
            ratingMenuRequest.setRate(4);
            ratingMenuService.rate(user , ratingMenuRequest );

        });
    }


}