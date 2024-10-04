package com.example.artvswar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {
    @Value("${cors.add.mapping}")
    private String addMapping;
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;
    @Value("${cors.allowed.methods}")
    private String allowedMethods;
    @Value("${cors.allowed.headers}")
    private String allowedHeaders;
    @Value("${cors.max.age}")
    private int maxAge;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
               registry.addMapping(addMapping)
                       .allowedOrigins(allowedOrigins)
                       .allowedMethods(allowedMethods)
                       .allowedHeaders(allowedHeaders)
                       .maxAge(maxAge);
            }
        };
    }

    @Bean
    public WebClient buildWebClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RestClient buildRestCLient() {
        return RestClient.builder().build();
    }
}
