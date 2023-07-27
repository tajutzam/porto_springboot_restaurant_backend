package com.zam.dev.food_order.service.impl;

import com.zam.dev.food_order.entity.Menu;
import com.zam.dev.food_order.entity.RatingMenu;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.RatingMenuRequest;
import com.zam.dev.food_order.repository.MenuRepository;
import com.zam.dev.food_order.repository.RatingMenuRepository;
import com.zam.dev.food_order.service.RatingMenuService;
import com.zam.dev.food_order.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RatingMenuServiceImpl implements RatingMenuService {

    private RatingMenuRepository ratingMenuRepository;

    private MenuRepository menuRepository;
    private ValidationService validationService;


    @Override
    @Transactional
    public int rate(User user, RatingMenuRequest request) {
        validationService.validate(request);
        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "menu id not found"));
        RatingMenu ratingMenu = new RatingMenu();
        ratingMenu.setMenu(menu);
        ratingMenu.setId(UUID.randomUUID().toString());
        ratingMenu.setTotalRate(request.getRate());
        ratingMenu.setUser(user);
        ratingMenuRepository.save(ratingMenu);
        return 1;
    }

}
