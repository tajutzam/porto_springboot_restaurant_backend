package com.zam.dev.food_order.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("file")
@Component
@Getter
@Setter
public class FileProperties {

    private String category;

    private String menu;

    private String user;

    private String restaurant;

}
