package com.davinchicoder.springbank.common.insfrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@Configuration
public class ApplicationConfig {
}
