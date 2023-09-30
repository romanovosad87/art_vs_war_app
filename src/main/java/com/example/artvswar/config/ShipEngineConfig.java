package com.example.artvswar.config;

import com.shipengine.ShipEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

@Configuration
public class ShipEngineConfig {
    @Value("${ship.engine.key}")
    private String apiKey;

    @Bean
    public ShipEngine getShipEngine() {
        return new ShipEngine(new HashMap<>() {{
            put("apiKey", apiKey);
            put("pageSize", 75);
            put("retries", 3);
            put("timeout", 8000);
        }});
    }
}
