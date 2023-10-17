package com.example.artvswar.controller;

import com.example.artvswar.dto.request.stripe.StripeCheckoutSessionRequestDto;
import com.example.artvswar.dto.response.stripe.StripeBalanceEarningsResponseDto;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.stripe.StripeService;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stripe")
public class StripeController {
    private static final String COGNITO_SUBJECT = "sub";
    private final StripeService stripeService;
    private final AccountService accountService;

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/createAccount")
    @ResponseStatus(HttpStatus.CREATED)
    public String createExpressAccount(@AuthenticationPrincipal Jwt jwt) {
        String authorCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        return stripeService.createExpressAccount(authorCognitoSubject);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/onboarding")
    public String getUrlForOnboarding(@AuthenticationPrincipal Jwt jwt) {
        String authorCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        return stripeService.getOnboardingUrl(authorCognitoSubject);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{accountId}")
    public String delete(@PathVariable String accountId) {
        return stripeService.deleteAccount(accountId);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/dashboard")
    public String getLoginLinkToDashboard(@AuthenticationPrincipal Jwt jwt) {
        String authorCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        return stripeService.createLoginLinkToAccount(authorCognitoSubject);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping()
    public StripeBalanceEarningsResponseDto getBalanceAndEarnings(@AuthenticationPrincipal Jwt jwt) {
        String authorCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        return stripeService.getBalanceAndEarnings(authorCognitoSubject);
    }

    @PostMapping("/checkout")
    public String getCheckOutUrl(@RequestParam Set<Long> paintingIds,
                                 @RequestBody @Valid StripeCheckoutSessionRequestDto dto,
                                 @AuthenticationPrincipal Jwt jwt) {
        String clientCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        accountService.saveAccountShippingAddresses(List.of(dto.getShippingAddress()), clientCognitoSubject);
        String stripeCustomerId = accountService.getStripeCustomerId(clientCognitoSubject);
        return stripeService.getCheckoutSessionUrl(paintingIds, dto, stripeCustomerId);
    }

    @GetMapping("/customer")
    public String createCustomer(@RequestParam String name, @RequestParam String email) {
        Customer customer = stripeService.createCustomer(name, email);
        return customer.getId();
    }

    @GetMapping("/intent/{id}")
    public PaymentIntent getPaymentIntent(@PathVariable String id) {
        return stripeService.retrievePaymentIntent(id);
    }

    @GetMapping("/refund")
    public Refund makeRefund(@RequestParam String chargeId) {
        return stripeService.createRefund(chargeId);
    }
}
