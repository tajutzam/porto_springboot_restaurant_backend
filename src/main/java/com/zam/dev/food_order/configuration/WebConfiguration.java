package com.zam.dev.food_order.configuration;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.resolver.AdminArgumentResolver;
import com.zam.dev.food_order.resolver.RestaurantArgumentResolver;
import com.zam.dev.food_order.resolver.UserArgumentResolver;
import com.zam.dev.food_order.security.BCrypt;
import io.swagger.v3.oas.models.ExternalDocumentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.*;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;
    @Autowired
    private AdminArgumentResolver adminArgumentResolver;

    @Autowired
    private RestaurantArgumentResolver restaurantArgumentResolver;

    @Autowired
    private ApplicationProperties properties;

    ExternalDocumentation documentation;

    List<Map<String  , String>> externalDoc = List.of(
            new HashMap<>() {{
                put("docName", "admin/admin.json");
            }},
            new HashMap<>() {{
                put("docName", "restaurant/menu_restaurant.json");
            }},
            new HashMap<>() {{
                put("docName", "restaurant/restaurant.json");
            }},
            new HashMap<>() {{
                put("docName", "restaurant/transaction_restaurant.json");
            }},
            new HashMap<>() {{
                put("docName", "user/cart_users.json");
            }},
            new HashMap<>() {{
                put("docName", "user/menu_users.json");
            }},
            new HashMap<>() {{
                put("docName", "user/transaction_users.json");
            }},
            new HashMap<>() {{
                put("docName", "user/users.json");
            }}
    );



    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
        resolvers.add(adminArgumentResolver);
        resolvers.add(restaurantArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://example.com", "http://localhost:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public MidtransCoreApi coreApi(){
        Config coreApiConfigOptions = Config.builder()
                .setServerKey(properties.getServerKey())
                .setClientKey(properties.getClientKey())
                .setIsProduction(properties.isProductionMode())
                .setConnectionTimeout(10000)
                .setWriteTimeout(10000)
                .setReadTimeout(10000)
                .build();

        return new ConfigFactory(coreApiConfigOptions).getCoreApi();
    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }




}
