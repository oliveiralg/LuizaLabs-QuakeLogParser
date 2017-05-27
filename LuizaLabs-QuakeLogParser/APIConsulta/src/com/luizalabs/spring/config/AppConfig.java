package com.luizalabs.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/*
 * Classe de configuracao do spring
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.luizalabs.spring")
public class AppConfig {

}
