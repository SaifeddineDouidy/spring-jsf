package com.example.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example")
public class SpringContextConfig {
    // This configuration enables Spring to scan components in the specified package.
}

