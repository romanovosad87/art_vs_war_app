package com.example.artvswar.config;

import com.example.artvswar.util.gson.PageAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    @Bean
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new PageAdapterFactory())
                .create();
    }
}
