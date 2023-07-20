package com.zam.dev.food_order.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {


    @Value("${server.port}")
    private int port;

    @Value("${midtrans.server.key}")
    private String serverKey;

    @Value("${midtrans.server.production}")
    private boolean productionMode;
    @Value("${midtrans.client.key}")
    private String clientKey;


}
