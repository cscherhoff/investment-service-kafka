package com.exxeta.investmentservicems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.exxeta")
@EnableFeignClients
public class InvestmentServiceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentServiceMsApplication.class, args);
	}

}
