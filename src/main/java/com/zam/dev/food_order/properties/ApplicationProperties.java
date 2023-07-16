package com.zam.dev.food_order.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {


    @Value("${server.port}")
    @Getter
    @Setter
    private int port;

}
