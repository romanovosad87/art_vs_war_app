package com.example.artvswar.controller;

import com.example.artvswar.service.stripe.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/stripeWebhook")
public class StripeWebhookController {
    public static final String STRIPE_SIGNATURE = "Stripe-Signature";
    private final StripeWebhookService stripeWebhookService;
    @Value("${stripe.webhook.secret.checkout}")
    private String endpointSecretCheckout;

    @Value("${stripe.webhook.secret.expressAccount}")
    private String endpointSecretExpressAccount;



    @PostMapping("/checkOut")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleCheckOutSessionEvent(@RequestHeader(STRIPE_SIGNATURE) String sigHeader,
                                            @RequestBody String payload) {
        if (sigHeader == null) {
            return new ResponseEntity<>("Webhook error, sigHeader == null",
                    HttpStatus.BAD_REQUEST);
        }

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecretCheckout);
        } catch (SignatureVerificationException e) {
            log.info("⚠️ Webhook error while validating signature");
            return new ResponseEntity<>("Webhook error while validating signature",
                    HttpStatus.BAD_REQUEST);
        }

        stripeWebhookService.handleCheckOutSessionEvent(event);
        return new ResponseEntity<>("",
                HttpStatus.OK);
    }

    @PostMapping("/expressAccount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleUpdateAccountEvent(@RequestHeader(STRIPE_SIGNATURE) String sigHeader,
                                           @RequestBody String payload) {

        if (sigHeader == null) {
            return new ResponseEntity<>("Webhook error, sigHeader == null",
                    HttpStatus.BAD_REQUEST);
        }

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecretExpressAccount);
        } catch (SignatureVerificationException e) {
            log.info("⚠️ Webhook error while validating signature");
            return new ResponseEntity<>("Webhook error while validating signature",
                    HttpStatus.BAD_REQUEST);
        }

        stripeWebhookService.handleExpressAccountEvent(event);
        return new ResponseEntity<>("",
                HttpStatus.OK);


    }
}
