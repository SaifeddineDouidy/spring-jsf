package com.example.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.example")
@EnableJpaRepositories("com.example.repository")

public class SpringContextConfig {
    // This configuration enables Spring to scan components in the specified package.
}

