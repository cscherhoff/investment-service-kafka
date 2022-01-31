package com.exxeta.investmentservicems.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.exxeta.investmentservice"})
@EnableJpaRepositories(basePackages = {"com.exxeta.investmentservice"})
public class SpringConfiguration {

}
