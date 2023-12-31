package com.example.artvswar.util;

import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRateRequestDto;
import com.example.artvswar.dto.request.stripe.StripeCheckoutSessionRequestDto;
import com.example.artvswar.exception.PaintingNotAvailableException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enummodel.PaymentStatus;
import com.neovisionaries.i18n.CountryCode;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.LoginLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Payout;
import com.stripe.model.PayoutCollection;
import com.stripe.model.Refund;
import com.stripe.model.ShippingRate;
import com.stripe.model.Transfer;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.BalanceTransactionListParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PayoutListParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.ShippingRateCreateParams;
import com.stripe.param.TransferCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class StripeUtils {

    private static final long HALF_HOUR = 30L;
    private static final String CURRENCY_EURO = "eur";
    public static final String REFRESH_URL = "https://artvswar.gallery/profile/stripe-refresh";
    public static final String RETURN_URL = "https://artvswar.gallery/profile?tab=Payment";
    public static final String SUCCESS_URL = "https://artvswar.gallery/order-done";
    public static final String CANCEL_URL = "https://artvswar.gallery/cart";
    public static final String PAID = "paid";
    public static final String PRODUCT_DESCRIPTION = "paintings made by hand";
    public static final String PAINTING_NOT_AVAILABLE = "Painting '%s' by %s is not available for buying";


    public Account createExpressAccount(Author author) {
        AccountCreateParams.BusinessProfile paintingMadeByHand = AccountCreateParams.BusinessProfile.builder()
                .setProductDescription(PRODUCT_DESCRIPTION)
                .build();

        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.EXPRESS)
                        .setCountry(CountryCode.findByName(author.getCountry()).get(0).getAlpha2())
                        .setEmail(author.getEmail())
                        .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                        .setBusinessProfile(paintingMadeByHand)
                        .build();
        try {
            Account account = Account.create(params);
            log.info(String.format("Created Stripe account with id: '%s' for Author Cognito Subject '%s'",
                    account.getId(), author.getCognitoSubject()));
            return account;
        } catch (StripeException e) {
            throw new RuntimeException("Can't create Express account", e);
        }
    }

    public AccountLink createAccountLink(String expressAccountId) {
        AccountLinkCreateParams params =
                AccountLinkCreateParams.builder()
                        .setAccount(expressAccountId)
                        .setRefreshUrl(REFRESH_URL)
                        .setReturnUrl(RETURN_URL)
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        try {
            AccountLink accountLink = AccountLink.create(params);
            log.info(String.format("Created AccountLink %s for Stripe account id: '%s'", accountLink.getUrl(), expressAccountId));
            return accountLink;
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't create account link for accountId: %s", expressAccountId), e);
        }
    }

    public String deleteAccount(String accountId) {
        try {
            Account account = retrieveAccount(accountId);
            Account deletedAccount = account.delete();
            log.info(String.format("Stripe express account with id: '%s' and name: '%s' was deleted",
                    accountId, account.getBusinessProfile().getName()));
            if (Boolean.TRUE.equals(deletedAccount.getDeleted())) {
                return String.format("Account with id: %s was deleted", accountId);
            } else {
                return String.format("Account with id: %s was NOT deleted", accountId);
            }
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't delete account with id: %s",
                    accountId), e);
        }
    }

    public LoginLink createLoginLinkToAccount(String stripeAccountId) {
        try {
            LoginLink link = LoginLink.createOnAccount(
                    stripeAccountId,
                    (Map<String, Object>) null,
                    (RequestOptions) null);
            log.info(String.format("Link to Stripe dashboard was created: '%s' for Stripe accountId: '%s'",
                    link.getUrl(), stripeAccountId));
            return link;
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't create login link to accountId: %s", stripeAccountId), e);
        }
    }

    public boolean checkIfChargeEnabled(String accountId) {
        Account account = retrieveAccount(accountId);
        return account.getChargesEnabled();
    }

    public boolean checkIfDetailsSubmitted(String accountId) {
        Account account = retrieveAccount(accountId);
        return account.getDetailsSubmitted();
    }

    public Session getCheckoutSessionUrl(List<Painting> paintings,
                                         StripeCheckoutSessionRequestDto dto,
                                         String stripeCustomerId) {

        Customer customer = updateCustomerShipping(stripeCustomerId, dto.getShippingAddress());

        long expiresAt = Instant.now().plus(HALF_HOUR, ChronoUnit.MINUTES).getEpochSecond();
        Object[] idsArray = paintings.stream().map(Painting::getId).toArray();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .putExtraParam("metadata", idsArray)
                        .setExpiresAt(expiresAt)
                        .setLocale(SessionCreateParams.Locale.EN)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setAfterExpiration(SessionCreateParams.AfterExpiration.builder()
//                                .setRecovery(SessionCreateParams.AfterExpiration.Recovery.builder()
//                                        .setEnabled(Boolean.TRUE).build()).build())
                        .addAllPaymentMethodType(List.of(SessionCreateParams.PaymentMethodType.CARD,
                                SessionCreateParams.PaymentMethodType.PAYPAL,
                                SessionCreateParams.PaymentMethodType.EPS,
                                SessionCreateParams.PaymentMethodType.GIROPAY,
                                SessionCreateParams.PaymentMethodType.IDEAL,
                                SessionCreateParams.PaymentMethodType.BANCONTACT,
                                SessionCreateParams.PaymentMethodType.P24))
                        .setCustomer(customer.getId())
                        .setCustomerUpdate(SessionCreateParams.CustomerUpdate.builder()
                                .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO).build())
//                        .setConsentCollection(SessionCreateParams.ConsentCollection.builder()
//                                .setTermsOfService(SessionCreateParams.ConsentCollection.TermsOfService.REQUIRED)  // You cannot collect consent to your terms of service unless a URL is set in the Stripe Dashboard. Update your public business details in the Dashboard https://dashboard.stripe.com/settings/public with a Terms of service URL to collect terms of service consent.
//                                .build())
                        .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                        .addAllLineItem(prepareItems(paintings))
                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
//                                        .setTransferData(
//                                                SessionCreateParams.PaymentIntentData.TransferData.builder()
//                                                        .setDestination(stripeAccountId)
//                                                        .setAmount(sellerAmount.longValue())
//                                                        .build()
//                                        )
//                                        .setSetupFutureUsage(SessionCreateParams.PaymentIntentData.SetupFutureUsage.ON_SESSION)
//                                        .setTransferGroup(transferGroupName)
                                        .build()
                        )
                        .addAllShippingOption(prepareShippingOptions(dto.getShippingRates()))
                        .setSuccessUrl(SUCCESS_URL)
                        .setCancelUrl(CANCEL_URL)
                        .build();

        try {
            Session session = Session.create(params);
            log.info(String.format("Checkout session with id: '%s' was created for customer with id: '%s' and name: '%s'",
                    session.getId(), customer.getId(), customer.getName()));
            return session;
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't create Stripe checkoutSession for paintings with id : %s "
                            + "for customer with Stripe id : %s", Arrays.toString(idsArray), stripeCustomerId), e);
        }
    }

    public Customer createCustomer(String name, String email) {
        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setName(name)
                .setEmail(email)
                .build();
        try {
            Customer customer = Customer.create(createParams);
            log.info(String.format("Customer with id: '%s', name: '%s', email: '%s' was created",
                    customer.getId(), customer.getName(), customer.getEmail()));
            return customer;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create Stripe customer with "
                    + "email: %s and name: %s", email, name), e);
        }
    }

    public Customer retrieveCustomer(String id) {
        try {
            Customer customer = Customer.retrieve(id);
            log.info(String.format("Customer with id: '%s', name: '%s', email: '%s' was retrieved",
                    customer.getId(), customer.getName(), customer.getEmail()));
            return customer;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve Customer by id %s", id), e);
        }
    }

    public Customer updateCustomerShipping(String customerId, AccountShippingRequestDto dto) {
        try {
            Customer customer = retrieveCustomer(customerId);

            CustomerUpdateParams updateParams = CustomerUpdateParams.builder().setShipping(CustomerUpdateParams.Shipping.builder()
                            .setName(dto.getFirstName() + " " + dto.getLastName())
                            .setPhone(dto.getPhone())
                            .setAddress(CustomerUpdateParams.Shipping.Address.builder().setLine1(dto.getAddressLine1())
                                    .setLine2(dto.getAddressLine2())
                                    .setCity(dto.getCity())
                                    .setState(dto.getState())
                                    .setCountry(CountryCode.findByName(dto.getCountry()).get(0).name())
                                    .setPostalCode(dto.getPostalCode())
                                    .build())
                            .build())
                    .build();

            Customer updatedCustomer = customer.update(updateParams);
            log.info(String.format("Shipping info for customer with id: '%s' was updated: '%s'",
                    updatedCustomer.getId(), updatedCustomer.getShipping()));
            return updatedCustomer;

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Account retrieveAccount(String accountId) {
        try {
            Account account = Account.retrieve(accountId);
            log.info(String.format("Account with id: '%s' was retrieved", account.getId()));
            return account;
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't retrieve Account with id %s", accountId), e);
        }
    }

    public PaymentIntent retrievePaymentIntent(String id) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
            log.info(String.format("Payment intent with id: '%s' was retrieved", paymentIntent.getId()));
            return paymentIntent;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve PaymentIntent "
                    + "intent with id %s", id), e);
        }
    }

    public Charge retrieveCharge(String id) {
        try {
            Charge charge = Charge.retrieve(id);
            log.info(String.format("Charge with id: '%s' was retrieved", charge.getId()));
            return charge;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve Сharge "
                    + " with id %s", id), e);
        }
    }

    public BalanceTransaction retrieveBalanceTransaction(String id) {
        try {
            BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(id);
            log.info(String.format("Balance Transaction with id: '%s' and amount: '%s' was retrieved",
                    balanceTransaction.getId(), balanceTransaction.getAmount() / 100));
            return balanceTransaction;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve balanceTransaction "
                    + " with id %s", id), e);
        }
    }

    public Transfer createSeparateTransfer(String chargeId, String stripeProfileId, Long amount) {
        TransferCreateParams params =
                TransferCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setSourceTransaction(chargeId)
                        .setDestination(stripeProfileId)
                        .build();

        Transfer transfer = null;
        try {
            transfer = Transfer.create(params);
            log.info(String.format("Separate transfer with id: '%s', amount '%s' and destination '%s' was created",
                    transfer.getId(), transfer.getAmount() / 100, transfer.getDestination()));

        } catch (StripeException e) {
            log.error(String.format("Can't create transfer with charge: %s"
                    + "for connected account id: %s", chargeId, stripeProfileId), e);
        }
        return transfer;
    }

    public double getTotalBalanceAmount(RequestOptions requestOptions) {
        try {
            BalanceTransactionCollection collection = BalanceTransaction.list(BalanceTransactionListParams.builder().build(), requestOptions);
//            Balance balance = Balance.retrieve(requestOptions);
//            List<Balance.Available> availableBalance = balance.getAvailable();
//            long balanceAmount = availableBalance.stream()
//                    .mapToLong(Balance.Available::getAmount)
//                    .sum();
            return (double) collection.getData().stream()
                    .mapToLong(BalanceTransaction::getAmount)
                    .sum() / 100;

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public double getTotalEarnings(RequestOptions requestOptions) {
        try {
            PayoutCollection list = Payout.list(PayoutListParams.builder().build(), requestOptions);
            return (double) list.getData()
                    .stream()
                    .filter(payout -> payout.getStatus().equals(PAID))
                    .mapToLong(Payout::getAmount)
                    .sum() / 100;

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Refund createRefund(String chargeId) {
        RefundCreateParams params = RefundCreateParams.builder()
                .setCharge(chargeId)
                .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                .build();
        try {
            Refund refund = Refund.create(params);
            log.info(String.format("Refund with id: '%s', amount: '%s', chargeId: '%s' was created",
                    refund.getId(), refund.getAmount() / 100, refund.getCharge()));
            return refund;
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create Refund for chargeId: %s", chargeId), e);
        }
    }

    private List<SessionCreateParams.LineItem> prepareItems(List<Painting> paintings) {

        List<SessionCreateParams.LineItem> itemList = new ArrayList<>();
        List<Painting> paintingsInProcessingStatus = new ArrayList<>();
        List<Painting> paintingsInSoldStatus = new ArrayList<>();
        List<Painting> paintingsDeactivatedAuthorProfile = new ArrayList<>();

        for (Painting painting : paintings) {
            if (painting.getPaymentStatus().equals(PaymentStatus.PROCESSING)) {
                paintingsInProcessingStatus.add(painting);
            } else if (painting.getPaymentStatus().equals(PaymentStatus.SOLD)) {
                paintingsInSoldStatus.add(painting);
            }

            if (painting.getAuthor().isDeleted()) {
                paintingsDeactivatedAuthorProfile.add(painting);
            }
            BigDecimal price = painting.getPrice().multiply(BigDecimal.valueOf(100));

            SessionCreateParams.LineItem item = SessionCreateParams.LineItem.builder()
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(CURRENCY_EURO)
                                    .setUnitAmount(price.longValue())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName("Painting: '" + painting.getTitle() + "' by " + painting.getAuthor().getFullName())
                                                    .build())
                                    .build())
                    .setQuantity(1L)
                    .build();

            itemList.add(item);
        }

        if (!paintingsInProcessingStatus.isEmpty()
                || !paintingsInSoldStatus.isEmpty()
                || !paintingsDeactivatedAuthorProfile.isEmpty()) {
            String message = createErrorMessage(paintingsInProcessingStatus, paintingsInSoldStatus,
                    paintingsDeactivatedAuthorProfile);
            throw new PaintingNotAvailableException(message);
        }
        return itemList;
    }

    private List<SessionCreateParams.ShippingOption> prepareShippingOptions(
            List<ShippingRateRequestDto> shippingDtos) {
        List<SessionCreateParams.ShippingOption> shippingOptions = new ArrayList<>();
        for (ShippingRateRequestDto dto : shippingDtos) {
            SessionCreateParams.ShippingOption shippingOption = SessionCreateParams.ShippingOption.builder()
                    .setShippingRate(prepareShippingRate(dto.getTotalShippingPrice(),
                            dto.getTotalDeliveryMinDays(),
                            dto.getTotalDeliveryMaxDays())
                            .getId()).build();

            shippingOptions.add(shippingOption);
        }
        return shippingOptions;
    }

    private ShippingRate prepareShippingRate(Long price, Long minDays, Long maxDays) {
        ShippingRateCreateParams params =
                ShippingRateCreateParams.builder()
                        .setDisplayName("Ground shipping")
                        .setType(ShippingRateCreateParams.Type.FIXED_AMOUNT)
                        .setFixedAmount(
                                ShippingRateCreateParams.FixedAmount.builder()
                                        .setAmount(price * 100)
                                        .setCurrency(CURRENCY_EURO)
                                        .build())
                        .setDeliveryEstimate(
                                ShippingRateCreateParams.DeliveryEstimate.builder()
                                        .setMinimum(
                                                ShippingRateCreateParams.DeliveryEstimate.Minimum.builder()
                                                        .setUnit(ShippingRateCreateParams.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY)
                                                        .setValue(minDays)
                                                        .build())
                                        .setMaximum(
                                                ShippingRateCreateParams.DeliveryEstimate.Maximum.builder()
                                                        .setUnit(ShippingRateCreateParams.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY)
                                                        .setValue(maxDays)
                                                        .build())
                                        .build())
                        .build();

        try {
            return ShippingRate.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create shipping rate customer "
                    + "for price: %s", price), e);
        }
    }

    private String createErrorMessage(List<Painting> paintingsInProcessingStatus,
                                      List<Painting> paintingsInSoldStatus,
                                      List<Painting> paintingsDeactivatedAuthorProfile) {
        String paintingInProcessingMessage = "";
        String paintingInSoldMessage = "";
        String paintingOfDeactivatedProfile = "";
        if (paintingsInProcessingStatus.size() == 1) {
            Painting painting = paintingsInProcessingStatus.get(0);
            paintingInProcessingMessage = String.format(PAINTING_NOT_AVAILABLE + ". It is in process of payment. ",
                    painting.getTitle(), painting.getAuthor().getFullName());
        } else if (paintingsInProcessingStatus.size() > 1) {
            StringBuilder builder = getStringBuilderForSeveralPaintings(paintingsInProcessingStatus);
            builder.append(" are not available for buying. They are in process of payment. ");
            paintingInProcessingMessage = builder.toString();
        }

        if (paintingsInSoldStatus.size() == 1) {
            Painting painting = paintingsInSoldStatus.get(0);
            paintingInSoldMessage = String.format(PAINTING_NOT_AVAILABLE + ". It is already sold. ",
                    painting.getTitle(), painting.getAuthor().getFullName());
        } else if (paintingsInSoldStatus.size() > 1) {
            StringBuilder builder = getStringBuilderForSeveralPaintings(paintingsInSoldStatus);
            builder.append(" are not available for buying. They are already sold. ");
            paintingInSoldMessage = builder.toString();
        }

        if (paintingsDeactivatedAuthorProfile.size() == 1) {
            Painting painting = paintingsDeactivatedAuthorProfile.get(0);
            paintingOfDeactivatedProfile = String.format(PAINTING_NOT_AVAILABLE,
                    painting.getTitle(), painting.getAuthor().getFullName());
        } else if (paintingsDeactivatedAuthorProfile.size() > 1) {
            StringBuilder builder = getStringBuilderForSeveralPaintings(paintingsDeactivatedAuthorProfile);
            builder.append(" are not available for buying. ");
            paintingOfDeactivatedProfile = builder.toString();
        }

        return paintingInSoldMessage + System.lineSeparator()
                + paintingInProcessingMessage + System.lineSeparator()
                + paintingOfDeactivatedProfile;
    }

    private StringBuilder getStringBuilderForSeveralPaintings(List<Painting> paintings) {
        StringBuilder builder = new StringBuilder("Paintings ");
        for (Painting painting : paintings) {
            builder.append("'").append(painting.getTitle()).append("' by ").append(painting.getAuthor().getFullName()).append(", ");
        }
        builder.replace(builder.length() - 2, builder.length() - 1, "");
        return builder;
    }
}
