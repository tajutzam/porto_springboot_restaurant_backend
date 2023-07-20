package com.zam.dev.food_order.configuration;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import com.zam.dev.food_order.properties.ApplicationProperties;
import com.zam.dev.food_order.resolver.AdminArgumentResolver;
import com.zam.dev.food_order.resolver.RestaurantArgumentResolver;
import com.zam.dev.food_order.resolver.UserArgumentResolver;
import com.zam.dev.food_order.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
                .setConnectionTimeout(5000)
                .setWriteTimeout(5000)
                .setReadTimeout(5000)
                .build();

        return new ConfigFactory(coreApiConfigOptions).getCoreApi();
    }

}
