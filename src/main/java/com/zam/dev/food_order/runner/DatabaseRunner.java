package com.zam.dev.food_order.runner;

import com.zam.dev.food_order.entity.Admin;
import com.zam.dev.food_order.entity.Restaurant;
import com.zam.dev.food_order.entity.User;
import com.zam.dev.food_order.repository.AdminRepository;
import com.zam.dev.food_order.repository.CategoryRepository;
import com.zam.dev.food_order.repository.RestaurantRepository;
import com.zam.dev.food_order.repository.UserRepository;
import com.zam.dev.food_order.security.Bcrypt;
import com.zam.dev.food_order.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public class DatabaseRunner implements ApplicationRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Bcrypt bcrypt;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Admin admin = adminRepository.findByUsername("admin").orElse(null);
        if(admin == null){
            admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(bcrypt.hashPw("rahasia"));
            admin.setId(UUID.randomUUID().toString());
            admin.setFirstName("admin");
            admin.setLastName("zam");
            String token = jwtService.generateToken(admin);
            admin.setToken(token);
            adminRepository.save(admin);
        }

        User user = userRepository.findByUsername("zamz").orElse(null);
        if(user == null){
            user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setFirstName("zam");
            user.setUsername("zamz");
            user.setLastName("zami");
            user.setAddress("banyuwangi");
            user.setEmail("moh@gmail.com");
            user.setPhoneNumber("123123123123");
            user.setPassword(bcrypt.hashPw("rahasia"));
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            user.setToken(token);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

        restaurantRepository.deleteAll();
        Restaurant restaurant = restaurantRepository.findByUsername("restaurant").orElse(null);
        if(restaurant == null){
            log.info("restaurant null");
            restaurant = new Restaurant();
            restaurant.setBanner("banner");
            restaurant.setId(UUID.randomUUID().toString());
            restaurant.setFirstName("zam");
            restaurant.setUsername("restaurant");
            restaurant.setLastName("zami");
            restaurant.setAddress("banyuwangi");
            restaurant.setPassword(bcrypt.hashPw("rahasia"));
            restaurantRepository.save(restaurant);
        }

    }
}
