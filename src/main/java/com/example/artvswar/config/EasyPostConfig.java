package com.example.artvswar.config;

import com.easypost.exception.General.MissingParameterError;
import com.easypost.service.EasyPostClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyPostConfig {

    @Value("${easy.post.key}")
    private String easyPostKey;

    @Bean
    public EasyPostClient getEasyPostClient() throws MissingParameterError {
        return new EasyPostClient(easyPostKey);
    }
}
