package com.zam.dev.food_order.service;

import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.model.RatingMenuRequest;

public interface RatingMenuService {

    int rate( User user , RatingMenuRequest ratingMenuRequest);

}
