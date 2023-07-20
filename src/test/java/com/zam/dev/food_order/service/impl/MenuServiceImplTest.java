package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Category;
import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.STATUS_MENU;
import com.zam.dev.food_order.model.menu.MenuRequest;
import com.zam.dev.food_order.model.menu.MenuResponse;
import com.zam.dev.food_order.model.other.ObjectPagingResponse;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.service.MenuService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuServiceImplTest {

    @Autowired
    private MenuService menuService;

    private MockMultipartFile file;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private Restaurant restaurant;

    private Category category;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionOperations transactionOperations;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() throws Exception{

       transactionOperations.executeWithoutResult(transactionStatus -> {
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
       });

        file = new MockMultipartFile("images", "code.png", "image/png", resourceLoader.getResource("classpath:code.png").getInputStream());
    }

    @Test
    void testAddMenuSuccess() throws Exception{
        MenuRequest request = new MenuRequest();
        request.setPrice(20000);
        request.setStatus_menu(STATUS_MENU.READY);
        request.setName("Bokake");
        request.setCategory_id(category.getId());
        MenuResponse response = menuService.add(request, restaurant, file);
        assertNotNull(response);
        assertNotNull(response.getCategory().getId());
        assertNotNull(response.getRestaurant().getId());
        assertNotNull(response.getImage());
    }


    @Test
    void testAddMenuConstraintViolationExceptions() throws Exception{
       assertThrows(ConstraintViolationException.class , ()->{
           MenuRequest request = new MenuRequest();
           request.setStatus_menu(STATUS_MENU.READY);
           request.setName("Bokake");
           request.setCategory_id(category.getId());
           MenuResponse response = menuService.add(request, restaurant, file);
           assertNotNull(response);
           assertNotNull(response.getCategory().getId());
           assertNotNull(response.getRestaurant().getId());
           assertNotNull(response.getImage());
       });
    }


    @Test
    void testAddMenuDuplicateName(){
        Menu menu = new Menu();
        menu.setId("1");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);

        assertThrows(ResponseStatusException.class , ()->{
            MenuRequest request = new MenuRequest();
            request.setPrice(20000);
            request.setStatus_menu(STATUS_MENU.READY);
            request.setName("menu");
            request.setCategory_id(category.getId());
            MenuResponse response = menuService.add(request, restaurant, file);
        });
    }

    @Test
    void testGetMenuRestaurantSuccess(){
        menuRepository.deleteAll();
        for (int i = 0; i <5 ; i++) {
            Menu menu = new Menu();
            menu.setId(String.valueOf(i));
            menu.setName("menu , " + i);
            menu.setStatusMenu(STATUS_MENU.READY);
            menu.setImage("image");
            menu.setPrice(1000);
            menu.setRestaurant(restaurant);
            menu.setCategory(category);
            menuRepository.save(menu);
        }

        ObjectPagingResponse<List<MenuResponse>> response = menuService.findAllByRestaurant(0, 2, restaurant);
        assertNotNull(response.getData());
        assertEquals(2 , response.getData().size());
        assertEquals(0 , response.getObjectPaging().getPage());
        assertEquals(2 , response.getObjectPaging().getSize());

    }

    @Test
    void testGetMenuRestaurantNull(){

        ObjectPagingResponse<List<MenuResponse>> response = menuService.findAllByRestaurant(0, 2, null);
        assertNotNull(response.getData());
            assertEquals(0 , response.getData().size());

    }


    @Test
    void testUpdateMenuSuccess(){
        Menu menu = new Menu();
        menu.setId("idmenu");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);
        MenuRequest request = new MenuRequest();
        request.setPrice(20000);
        request.setStatus_menu(STATUS_MENU.READY);
        request.setName("new menu name");
        request.setCategory_id(category.getId());
        MenuResponse menuResponse = menuService.update(request, restaurant, file, menu.getId());
        assertNotNull(menuResponse.getRestaurant());
        assertNotNull(menuResponse.getCategory());
        assertEquals("new menu name" , menuResponse.getName());

    }

    @Test
    void testAddMenuUserOtherMenu(){
        assertThrows(ResponseStatusException.class , ()->{
            Menu menu1 = new Menu();
            menu1.setId("idmenu1");
            menu1.setName("menu1");
            menu1.setStatusMenu(STATUS_MENU.READY);
            menu1.setImage("image");
            menu1.setPrice(1000);
            menu1.setRestaurant(restaurant);
            menu1.setCategory(category);
            menuRepository.save(menu1);

            Menu menu2 = new Menu();
            menu2.setId("idmenu2");
            menu2.setName("menu2");
            menu2.setStatusMenu(STATUS_MENU.READY);
            menu2.setImage("image");
            menu2.setPrice(1000);
            menu2.setRestaurant(restaurant);
            menu2.setCategory(category);
            menuRepository.save(menu2);

            MenuRequest request = new MenuRequest();
            request.setPrice(20000);
            request.setStatus_menu(STATUS_MENU.READY);
            request.setName("menu2");
            request.setCategory_id(category.getId());

            MenuResponse menuResponse = menuService.update(request, restaurant, file, menu1.getId());

        });
    }

    @Test
    void testGetMenuByIdSuccess(){
        Menu menu = new Menu();
        menu.setId("idmenu");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);
        MenuResponse response = menuService.findById(menu.getId());
        assertNotNull(response.getId());
        assertNotNull(response.getRestaurant().getId());
        assertNotNull(response.getCategory().getId());
    }

    @Test
    void testGetMenuNotFound(){
        assertThrows(ResponseStatusException.class , () -> {
           menuService.findById("not found");
        });
    }

    @Test
    void testDeleteMenuSuccess(){
        Menu menu = new Menu();
        menu.setId("idmenu");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);
        int deletedMenu = menuService.deleteMenu(menu.getId(), restaurant);
        assertEquals(1 , deletedMenu);
    }

    @Test
    void testFindAllMenu(){
        ObjectPagingResponse<List<MenuResponse>> pagingResponse = menuService.findAll(0, 1);
        assertNotNull(pagingResponse);
        assertNotNull(pagingResponse.getMessage());
        assertEquals(0 , pagingResponse.getObjectPaging().getPage());
        assertEquals(0 , pagingResponse.getObjectPaging().getSize());
    }

    @Test
    void testGetMenuByStatus(){
        menuRepository.deleteAll();
        Menu menu = new Menu();
        menu.setId("idmenu");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        PageImpl<MenuResponse> menuResponses = menuService.findAllMenuByStatus(STATUS_MENU.READY, restaurant, 0, 1);
        assertNotNull(menuResponses.getContent());
        assertEquals(0 , menuResponses.getNumber());
        assertEquals(0 , menuResponses.getContent().size());
    }

    @Test
    void testUpdateStatusMenu(){
        menuRepository.deleteAll();
        Menu menu = new Menu();
        menu.setId("idmenu");
        menu.setName("menu");
        menu.setStatusMenu(STATUS_MENU.READY);
        menu.setImage("image");
        menu.setPrice(1000);
        menu.setRestaurant(restaurant);
        menu.setCategory(category);
        menuRepository.save(menu);
        int deletedMenu = menuService.updateStatusMenu(STATUS_MENU.NOT_READY, restaurant, menu.getId());
        assertEquals(1 , deletedMenu);
    }

    @AfterEach
    void destroy(){
        categoryRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

}