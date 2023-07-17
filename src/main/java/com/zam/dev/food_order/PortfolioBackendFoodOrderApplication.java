package com.zam.dev.food_order;

import com.midtrans.Midtrans;
import com.zam.dev.food_order.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(
		JwtProperties.class
)
@EnableJpaAuditing
public class PortfolioBackendFoodOrderApplication {

	public static void main(String[] args) {


		SpringApplication.run(PortfolioBackendFoodOrderApplication.class, args);
	}

}
