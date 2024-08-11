package com.retailer.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class RewardPointsCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardPointsCalculatorApplication.class, args);
	}

}
