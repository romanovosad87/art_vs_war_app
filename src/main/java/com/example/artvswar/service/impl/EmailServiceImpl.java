package com.example.artvswar.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;
import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.Order;
import com.example.artvswar.service.AdminService;
import com.example.artvswar.service.EmailService;
import com.example.artvswar.service.stripe.StripeService;
import com.stripe.model.Address;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    public static final String CLOUDINARY_LINK = "https://console.cloudinary.com/console"
            + "/c-1e98a800837a96a82947e8ca3b3fd0/media_library/search?q=&view_mode=grid";
    public static final String MANUAL_MODERATION_LINK = "https://artvswar.gallery/images-validation";
    public static final String ORDER_EMAIL_SUBJECT = "Order information";
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final int ESTIMATED_DELIVERY_DAYS = 5;
    public static final String TRACKING_NUMBER = "";
    public static final String PURCHASE_EMAIL_TEMPLATE = "Order-1705157871158";
    public static final String BASIC_EMAIL = "info@artvswar.gallery";
    private final MailSender mailSender;
    private final AdminService adminService;
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final StripeService stripeService;

    @Override
    public void sendImageRejectionMail(String publicId, String moderationResponse) {
        String[] adminEmails = adminService.getAll()
                .stream()
                .map(AdminResponseDto::getEmail)
                .toArray(String[]::new);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Art vs War (moderation) <moderation@artvswar.gallery>");
        simpleMailMessage.setTo(adminEmails);
        simpleMailMessage.setSubject("AWS_REK moderation rejection");
        simpleMailMessage.setText(getMessage(publicId, moderationResponse));
        mailSender.send(simpleMailMessage);
    }

    @Override
    public String sendTestEmail(String email) {
        log.info("Current thread test Email: {}", Thread.currentThread().getName());
        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();

        sendTemplatedEmailRequest.setTemplate("Test_Email-1695674011980");
        sendTemplatedEmailRequest.setSource("Art vs War <info@artvswar.gallery>");
        sendTemplatedEmailRequest.setDestination(new Destination(List.of(email)));
        sendTemplatedEmailRequest.setReturnPath(BASIC_EMAIL);
        sendTemplatedEmailRequest.setReplyToAddresses(List.of(BASIC_EMAIL));
        sendTemplatedEmailRequest.setTemplateData("{\"subject\":\"First Test Email\"}");

        SendTemplatedEmailResult sendTemplatedEmailResult = amazonSimpleEmailService
                .sendTemplatedEmail(sendTemplatedEmailRequest);
        return sendTemplatedEmailResult.getMessageId();
    }

    @Override
    public void contactUsEmail(String email, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("contact_us@artvswar.gallery");
        simpleMailMessage.setTo(BASIC_EMAIL);
        simpleMailMessage.setSubject("Email from: " + email);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }

    @Async
    @Override
    public void purchasePaintingToCustomerEmail(Order order, Account account,
                                                String titleWithAuthor) {
        if (!account.isUnsubscribedEmail()) {
            SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
            sendTemplatedEmailRequest.setTemplate(PURCHASE_EMAIL_TEMPLATE);
            sendTemplatedEmailRequest.setSource("Art vs War <order@artvswar.gallery>");
            sendTemplatedEmailRequest.setReturnPath(BASIC_EMAIL);
            sendTemplatedEmailRequest.setReplyToAddresses(List.of(BASIC_EMAIL));
            Destination destination = new Destination()
                    .withToAddresses(account.getEmail());
            sendTemplatedEmailRequest.setDestination(destination);
            String templateData = prepareTemplateData(order, account, titleWithAuthor);

            sendTemplatedEmailRequest.setTemplateData(templateData);

            SendTemplatedEmailResult emailResult = amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);
            String messageId = emailResult.getMessageId();
            log.info("Sending email to {} with id {} to customer for order with id {}", account.getEmail(), messageId, order.getId());
        }
    }

    private String prepareTemplateData(Order order, Account account, String titleWithAuthor) {
        ShippingDetails shippingDetails = stripeService.getShippingDetailsOfOrder(order.getCheckoutSessionId());
        Address shippingAddress = shippingDetails.getAddress();
        PaymentIntent paymentIntent = stripeService.getPaymentIntent(order.getPaymentIntentId());
        Charge charge = stripeService.getCharge(order.getChargeId());
        String receiptUrl = charge.getReceiptUrl();
        return String.format(
                "{"
                        + "\"subject\":\"%s\", "
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
                        + "\"estimatedDeliveryDate\":\"%s\", "
                        + "\"trackingNumber\":\"%s\", "
                        + "\"paymentMethod\":\"%s\", "
                        + "\"paymentIntentId\":\"%s\""
                        + "}",
                ORDER_EMAIL_SUBJECT,
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
                getNonNullValue(order.getCreatedAt().plusDays(ESTIMATED_DELIVERY_DAYS).format(formatter)),
                getNonNullValue(TRACKING_NUMBER),
                getNonNullValue(String.join(", ", paymentIntent.getPaymentMethodTypes())),
                getNonNullValue(paymentIntent.getId())
        );
    }

    private String getNonNullValue(Object value) {
        return value == null ? "" : value.toString();
    }

    private String getMessage(String publicId, String moderationResponse) {
        return String.format("Image was rejected by AWS-Rekognition."
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "public_id:  %s"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "Moderation response:  %s."
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "The link to manual moderation:"
                        + System.lineSeparator()
                        + "%s"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "The link to Cloudinary dashboard:"
                        + System.lineSeparator()
                        + "%s",
                publicId, moderationResponse, MANUAL_MODERATION_LINK, CLOUDINARY_LINK);
    }
}
