package com.example.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.example")
@EntityScan(basePackages = "com.example.model")
@EnableJpaRepositories("com.example.repository")

public class SpringContextConfig {
    // This configuration enables Spring to scan components in the specified package.
}

