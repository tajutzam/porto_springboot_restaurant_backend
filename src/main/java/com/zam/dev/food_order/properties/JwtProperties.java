package com.zam.dev.food_order.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        "jwt"
)
@Getter
@Setter
public class JwtProperties {

    private String key;

}
