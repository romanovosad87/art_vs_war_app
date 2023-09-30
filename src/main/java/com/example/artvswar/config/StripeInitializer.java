package com.example.artvswar.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class StripeInitializer {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void setup() {
        Stripe.apiKey = apiKey;
    }
}
