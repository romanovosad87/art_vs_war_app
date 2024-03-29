package com.example.artvswar.service.stripe;

import com.example.artvswar.dto.mapper.OrderMapper;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Donate;
import com.example.artvswar.model.Order;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enummodel.PaymentStatus;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.DonateService;
import com.example.artvswar.service.EmailExternalService;
import com.example.artvswar.service.OrderService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.ShoppingCartPaintingService;
import com.example.artvswar.service.StripeProfileService;
import com.example.artvswar.util.StripeUtils;
import com.stripe.model.Account;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StripeWebhookService {
    private final PaintingService paintingService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final AccountService accountService;
    private final ShoppingCartPaintingService shoppingCartPaintingService;
    private final StripeUtils stripeUtils;
    private final DonateService donateService;
    private final StripeProfileService stripeProfileService;
    private final EmailExternalService emailExternalService;
    private final StripeService stripeService;

    @Transactional
    public void handleCheckOutSessionEvent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        StripeObject stripeObject = dataObjectDeserializer.getObject().orElseThrow(
                () -> new RuntimeException(
                        String.format("Can't deserialize event with id: %s", event.getId())));

        Long eventCreatedAt = event.getCreated();
        LocalDateTime timeOfEvent = LocalDateTime.ofEpochSecond(eventCreatedAt, 0, ZoneOffset.UTC);

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                handleCheckoutSessionCompleted(session, timeOfEvent);
                log.info("Checkout session completed for id = {}, with price {}",
                        session.getId(), session.getAmountTotal());
                break;
            case "checkout.session.async_payment_succeeded":
                Session sessionForPaymentSucceeded = (Session) stripeObject;
                log.info("Checkout session payment succeeded for id = {}, with price {}",
                        sessionForPaymentSucceeded.getId(), (sessionForPaymentSucceeded.getAmountTotal()) / 100);
                break;
            case "checkout.session.async_payment_failed":
                Session sessionForPaymentFailed = (Session) stripeObject;
                log.info("Checkout session payment failed for id = {}, with price {}",
                        sessionForPaymentFailed.getId(), (sessionForPaymentFailed.getAmountTotal()) / 100);
                break;
            case "checkout.session.expired":
                Session sessionExpired = (Session) stripeObject;
                handleCheckoutSessionExpired(sessionExpired);
                log.info("Checkout session expired for id = {}, with price {}",
                        sessionExpired.getId(), (sessionExpired.getAmountTotal()) / 100);
                break;
            default:
                log.warn("Unhandled event type: {}", event.getType());
                break;
        }
    }


    @Transactional
    public void handleCheckoutSessionCompleted(Session session, LocalDateTime timeOfEvent) {
        if (session.getSubmitType() != null && session.getSubmitType().equals("donate")) {
            handleDonation(session);
            return;
        }
        String customerId = session.getCustomer();
        Map<String, String> metadata = session.getMetadata();
        log.info("Metadata {}", metadata);

        PaymentIntent paymentIntent = stripeUtils.retrievePaymentIntent(session.getPaymentIntent());
        log.info("Payment intent {}", paymentIntent);

        List<Painting> paintings = metadata.values().stream()
                .map(id -> paintingService.get(Long.parseLong(id)))
                .collect(Collectors.toList());
        log.info("After parsing metadata, paintings: {}", paintings);

        var customerAccount = accountService.getAccountByStripeCustomerId(customerId);
        Order order = createOrder(session, customerAccount, paymentIntent, paintings, timeOfEvent);

        ShippingDetails shippingDetails = stripeService.getShippingDetailsOfOrder(order.getCheckoutSessionId());

        paintings.forEach(painting -> {
            paintingService.changePaymentStatus(painting, PaymentStatus.SOLD);
            Author author = painting.getAuthor();
            String fullName = author.getFullName();
            AccountEmailData accountEmailData = author.getAccount().getAccountEmailData();
            emailExternalService.purchasePaintingToAuthorEmail(shippingDetails, accountEmailData, fullName, painting);
        });

        String titleWithAuthor = paintings.stream().map(painting -> {
            String authorFullName = painting.getAuthor().getFullName();
            String paintingTitle = painting.getTitle();
            return "'" + paintingTitle + "' by " + authorFullName;
        }).collect(Collectors.joining(", "));



        emailExternalService.purchasePaintingToCustomerEmail(order, shippingDetails, customerAccount, titleWithAuthor);

        String accountCognitoSubject = accountService.getCognitoSubjectByStripeId(customerId);
        paintings.forEach(painting -> shoppingCartPaintingService.remove(painting.getId(), accountCognitoSubject));
    }

    @Transactional
    public void handleCheckoutSessionExpired(Session session) {
        Map<String, String> metadata = session.getMetadata();
        log.info("Metadata {}", metadata);
        metadata.values().stream()
                .map(id -> paintingService.get(Long.parseLong(id)))
                .forEach(painting -> paintingService.changePaymentStatus(painting, PaymentStatus.AVAILABLE));
    }

    public void handleExpressAccountEvent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        StripeObject stripeObject = dataObjectDeserializer.getObject().orElseThrow(
                () -> new RuntimeException(
                        String.format("Can't deserialize event with id: %s", event.getId())));

        if (event.getType().equals("account.updated")) {
            Account account = (Account) stripeObject;
            handleExpressAccountUpdate(account);
            log.info("Express account updated for id = {}",
                    account.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }

    public void handleExpressAccountUpdate(Account account) {
        String accountId = account.getId();
        boolean isDetailsSubmitted = account.getDetailsSubmitted();
        if (isDetailsSubmitted) {
            log.info("Details Submitted: {}", account.getDetailsSubmitted());
            stripeProfileService.changeDetailsSubmitted(accountId, isDetailsSubmitted);
        }
    }

    private void handleDonation(Session session) {
        Donate donate = new Donate();
        donate.setAmount(BigDecimal.valueOf((double) session.getAmountTotal() / 100));
        Session.CustomerDetails customerDetails = session.getCustomerDetails();
        donate.setName(customerDetails.getName());
        donate.setEmail(customerDetails.getEmail());
        donate.setMessage(session.getCustomFields().get(0).getText().getValue());
        String paymentIntentId = session.getPaymentIntent();
        BigDecimal netAmount = getNetAmount(paymentIntentId);
        donate.setNetAmount(netAmount);
        donateService.save(donate);
    }

    private Order createOrder(Session session, com.example.artvswar.model.Account account,
                              PaymentIntent paymentIntent, List<Painting> paintings,
                              LocalDateTime timeOfEvent) {
        Order order = orderMapper.toModel(session, timeOfEvent);
        order.setAccount(account);
        paintings.forEach(order::addPainting);
        String latestCharge = paymentIntent.getLatestCharge();
        order.setChargeId(latestCharge);
        BigDecimal netAmount = getNetAmount(paymentIntent.getId());
        order.setNetAmount(netAmount);
        return orderService.save(order);
    }

    private BigDecimal getNetAmount(String paymentIntentId) {
        PaymentIntent paymentIntent = stripeUtils.retrievePaymentIntent(paymentIntentId);
        String latestChargeId = paymentIntent.getLatestCharge();
        Charge charge = stripeUtils.retrieveCharge(latestChargeId);
        String balanceTransactionId = charge.getBalanceTransaction();
        BalanceTransaction balanceTransaction = stripeUtils.retrieveBalanceTransaction(balanceTransactionId);
        return BigDecimal.valueOf((double) balanceTransaction.getNet() / 100);
    }
}
