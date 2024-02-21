package com.example.artvswar.service.impl;

import com.example.artvswar.exception.SendingMailjetEmailException;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.Order;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.EmailExternalService;
import com.example.artvswar.service.stripe.StripeService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import com.stripe.model.Address;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailExternalServiceImpl implements EmailExternalService {
    private static final String EMAIL = "Email";
    private static final String NAME = "Name";
    private static final int MAILJET_TEMPLATE_ORDER = 5653057;
    private static final int MAILJET_TEMPLATE_PURCHASE = 5653835;
    public static final String BASIC_EMAIL = "info@artvswar.gallery";
    public static final String FROM_ORDER_EMAIL = "order@artvswar.gallery";
    public static final String FROM_PURCHASE_EMAIL = "purchase@artvswar.gallery";
    public static final String MAIL_FROM_NAME = "Art vs War";
    public static final String ORDER_EMAIL_SUBJECT = "Order information";
    public static final String PURCHASE_EMAIL_SUBJECT = "Purchase";
    public static final String PAINTING_PAGE = "https://artvswar.gallery/profile/%s";
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final int ESTIMATED_DELIVERY_DAYS = 5;
    private static final String TRACKING_NUMBER = "";

    @Value("${mailjet.api.key}")
    private String mailjetApiKey;
    @Value("${mailjet.api.secret}")
    private String mailjetApiSecret;

    private final StripeService stripeService;

    @Override
    @Async
    public void purchasePaintingToCustomerEmail(Order order, ShippingDetails shippingDetails, Account account,
                                                String titleWithAuthor) {

        AccountEmailData accountEmailData = account.getAccountEmailData();
        boolean isUnsubscribed = accountEmailData.isUnsubscribed();
        boolean isBounce = accountEmailData.isBounce();
        boolean isComplaint = accountEmailData.isComplaint();
        if (!isUnsubscribed && !isBounce && !isComplaint) {
            String email = accountEmailData.getEmail();
            MailjetResponse response;
            ClientOptions clientOptions = ClientOptions.builder()
                    .apiKey(mailjetApiKey)
                    .apiSecretKey(mailjetApiSecret)
                    .build();
            MailjetClient client = new MailjetClient(clientOptions);
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put(EMAIL, FROM_ORDER_EMAIL)
                                            .put(NAME, MAIL_FROM_NAME))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put(EMAIL, email)))
                                    .put(Emailv31.Message.TEMPLATEID, MAILJET_TEMPLATE_ORDER)
                                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                    .put(Emailv31.Message.SUBJECT, ORDER_EMAIL_SUBJECT)
                                    .put(Emailv31.Message.VARIABLES,
                                            new JSONObject(prepareTemplateDataForOrderEmail(order, shippingDetails, account, titleWithAuthor)))));
            try {
                response = client.post(request);
               log.info(String.format("Sending order email to customer '%s' with status '%s' and response data '%s'",
                        email, response.getStatus(), response.getData()));
            } catch (MailjetException e) {
                throw new SendingMailjetEmailException(String.format("Can't send email to '%s'", email), e);
            }
        }
    }

    @Override
    @Async
    public void purchasePaintingToAuthorEmail(ShippingDetails shippingDetails, AccountEmailData accountEmailData,
                                              String authorFullName, Painting painting) {

        boolean isUnsubscribed = accountEmailData.isUnsubscribed();
        boolean isBounce = accountEmailData.isBounce();
        boolean isComplaint = accountEmailData.isComplaint();
        if (!isUnsubscribed && !isBounce && !isComplaint) {
            String email = accountEmailData.getEmail();
            MailjetResponse response;
            ClientOptions clientOptions = ClientOptions.builder()
                    .apiKey(mailjetApiKey)
                    .apiSecretKey(mailjetApiSecret)
                    .build();
            MailjetClient client = new MailjetClient(clientOptions);
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put(EMAIL, FROM_PURCHASE_EMAIL)
                                            .put(NAME, MAIL_FROM_NAME))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put(EMAIL, email)))
                                    .put(Emailv31.Message.TEMPLATEID, MAILJET_TEMPLATE_PURCHASE)
                                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                    .put(Emailv31.Message.SUBJECT, PURCHASE_EMAIL_SUBJECT)
                                    .put(Emailv31.Message.VARIABLES,
                                            new JSONObject(prepareTemplateDataForPurchaseEmail(shippingDetails, authorFullName, painting)))));
            try {
                response = client.post(request);
                log.info(String.format("Sending purchase email to author '%s' with status '%s' and response data '%s'",
                        email, response.getStatus(), response.getData()));
            } catch (MailjetException e) {
                throw new SendingMailjetEmailException(String.format("Can't send email to '%s'", email), e);
            }
        }

    }

    private String prepareTemplateDataForPurchaseEmail(ShippingDetails shippingDetails,
                                                       String authorFullName, Painting painting) {
        String phone = shippingDetails.getPhone();
        Address shippingAddress = shippingDetails.getAddress();
        return String.format(
                "{"
                        + "\"artistName\":\"%s\", "
                        + "\"artworkTitle\":\"%s\", "
                        + "\"street1\":\"%s\", "
                        + "\"street2\":\"%s\", "
                        + "\"city\":\"%s\", "
                        + "\"state\":\"%s\", "
                        + "\"country\":\"%s\", "
                        + "\"postcode\":\"%s\", "
                        + "\"phone\":\"%s\", "
                        + "}",
                getNonNullValue(authorFullName),
                getNonNullValue(painting.getTitle()),
                getNonNullValue(shippingAddress.getLine1()),
                getNonNullValue(shippingAddress.getLine2()),
                getNonNullValue(shippingAddress.getCity()),
                getNonNullValue(shippingAddress.getState()),
                getNonNullValue(shippingAddress.getCountry()),
                getNonNullValue(shippingAddress.getPostalCode()),
                getNonNullValue(phone)
        );
    }

    private String prepareTemplateDataForOrderEmail(Order order, ShippingDetails shippingDetails,
                                                    Account account, String titleWithAuthor) {
        String phone = shippingDetails.getPhone();
        Address shippingAddress = shippingDetails.getAddress();
        PaymentIntent paymentIntent = stripeService.getPaymentIntent(order.getPaymentIntentId());
        Charge charge = stripeService.getCharge(order.getChargeId());
        String receiptUrl = charge.getReceiptUrl();
        return String.format(
                "{"
                        + "\"customerName\":\"%s\", "
                        + "\"orderNumber\":\"%s\", "
                        + "\"orderDate\":\"%s\", "
                        + "\"artworkTitle\":\"%s\", "
                        + "\"netAmount\":\"%s\", "
                        + "\"shippingAmount\":\"%s\", "
                        + "\"totalAmount\":\"%s\", "
                        + "\"receiptLink\":\"%s\", "
                        + "\"street1\":\"%s\", "
                        + "\"street2\":\"%s\", "
                        + "\"city\":\"%s\", "
                        + "\"state\":\"%s\", "
                        + "\"country\":\"%s\", "
                        + "\"postcode\":\"%s\", "
                        + "\"phone\":\"%s\", "
                        + "\"estimatedDeliveryDate\":\"%s\", "
                        + "\"trackingNumber\":\"%s\", "
                        + "\"paymentMethod\":\"%s\", "
                        + "\"paymentIntentId\":\"%s\""
                        + "}",
                getNonNullValue(account.getFirstName()),
                getNonNullValue(order.getId()),
                getNonNullValue(order.getCreatedAt().format(formatter)),
                getNonNullValue(titleWithAuthor),
                getNonNullValue(order.getSubtotalAmount()),
                getNonNullValue(order.getShippingAmount()),
                getNonNullValue(order.getTotalAmount()),
                getNonNullValue(receiptUrl),
                getNonNullValue(shippingAddress.getLine1()),
                getNonNullValue(shippingAddress.getLine2()),
                getNonNullValue(shippingAddress.getCity()),
                getNonNullValue(shippingAddress.getState()),
                getNonNullValue(shippingAddress.getCountry()),
                getNonNullValue(shippingAddress.getPostalCode()),
                getNonNullValue(phone),
                getNonNullValue(order.getCreatedAt().plusDays(ESTIMATED_DELIVERY_DAYS).format(formatter)),
                getNonNullValue(TRACKING_NUMBER),
                getNonNullValue(String.join(", ", paymentIntent.getPaymentMethodTypes())),
                getNonNullValue(paymentIntent.getId())
        );
    }

    private String getNonNullValue(Object value) {
        return value == null ? "" : value.toString();
    }
}
