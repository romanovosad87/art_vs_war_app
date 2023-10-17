package com.example.artvswar.service.stripe;

import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRateRequestDto;
import com.example.artvswar.dto.request.stripe.StripeCheckoutSessionRequestDto;
import com.example.artvswar.dto.response.stripe.StripeBalanceEarningsResponseDto;
import com.example.artvswar.exception.PaintingNotAvailableException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.StripeProfile;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.StripeProfileService;
import com.neovisionaries.i18n.CountryCode;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
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
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PayoutListParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.ShippingRateCreateParams;
import com.stripe.param.ShippingRateCreateParams.DeliveryEstimate;
import com.stripe.param.TransferCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StripeService {
    private static final long HALF_HOUR = 30L;
    private static final String CURRENCY_EURO = "eur";
    private final StripeProfileService stripeProfileService;
    private final AuthorService authorService;
    private final PaintingService paintingService;

    @Transactional
    public String createExpressAccount(String cognitoSubject) {
        AccountCreateParams.BusinessProfile paintingMadeByHand = AccountCreateParams.BusinessProfile.builder()
                .setProductDescription("paintings made by hand")
                .build();

        Author author = authorService.getAuthorByCognitoSubject(cognitoSubject);
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
            String stripeAccountId = account.getId();
            StripeProfile stripeProfile = new StripeProfile();
            stripeProfile.setAccountId(stripeAccountId);
            stripeProfile.setAuthor(author);
            StripeProfile savedStripeProfile = stripeProfileService.create(stripeProfile);
            return savedStripeProfile.getAccountId();
        } catch (StripeException e) {
            throw new RuntimeException("Can't create Express account", e);
        }
    }

    public AccountLink createAccountLink(String authorCognitoSubject) {
        String expressAccountId = createExpressAccount(authorCognitoSubject);
        AccountLinkCreateParams params =
                AccountLinkCreateParams.builder()
                        .setAccount(expressAccountId)
                        .setRefreshUrl("https://www.albedosunrise.com/stripe/onboarding")
                        .setReturnUrl("https://develop.artvswar.gallery")
                        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                        .build();
        try {
            return AccountLink.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't create account link for accountId: %s", expressAccountId), e);
        }
    }

    public String getOnboardingUrl(String authorCognitoSubject) {
        AccountLink accountLink = createAccountLink(authorCognitoSubject);
        return accountLink.getUrl();
    }

    public String deleteAccount(String accountId) {
        try {
            Account account = retrieveAccount(accountId);
            Account deletedAccount = account.delete();
            if (deletedAccount.getDeleted()) {
                return String.format("Account with id: %s was deleted", accountId);
            } else {
                return String.format("Account with id: %s was NOT deleted", accountId);
            }
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't delete account with id: %s",
                    accountId), e);
        }
    }

    public String createLoginLinkToAccount(String authorCognitoSubject) {
        String stripeAccountId = stripeProfileService.
                getAccountIdByAuthor(authorCognitoSubject);
        try {
            LoginLink loginLink = LoginLink.createOnAccount(
                    stripeAccountId,
                    (Map<String, Object>) null,
                    (RequestOptions) null
            );
            return loginLink.getUrl();
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

    @Transactional
    public String getCheckoutSessionUrl(Set<Long> paintingIds,
                                        StripeCheckoutSessionRequestDto dto,
                                        String stripeCustomerId) {
//        Painting painting = paintingService.get(paintingId);
//        BigDecimal price = painting.getPrice().multiply(BigDecimal.valueOf(100));
//        String stripeAccountId = painting.getAuthor().getStripeProfile().getAccountId();
//
//        BigDecimal sellerAmount = price.multiply(BigDecimal.valueOf(0.4),
//                new MathContext(2, RoundingMode.FLOOR));
//        BigDecimal fundsAmount = price.multiply(BigDecimal.valueOf(0.5),
//                new MathContext(2, RoundingMode.FLOOR));

        Customer customer = updateCustomerShipping(stripeCustomerId, dto.getShippingAddress());

        long expiresAt = Instant.now().plus(HALF_HOUR, ChronoUnit.MINUTES).getEpochSecond();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .putExtraParam("metadata", paintingIds.toArray())
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
                        .addAllLineItem(prepareItems(paintingIds))
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
                        .setSuccessUrl("https://artvswar.gallery/order-done")
                        .setCancelUrl("https://artvswar.gallery/cart")
                        .build();

        try {
            Session session = Session.create(params);
            paintingIds.stream()
                    .map(paintingService::get)
                    .forEach(painting -> paintingService.changePaymentStatus(painting,
                            PaymentStatus.PROCESSING));
            System.out.println(session.getMetadata());
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't create Stripe checkoutSession for paintings with id : %s "
                            + "for customer with Stripe id : %s", paintingIds, stripeCustomerId), e);
        }
    }

    public Customer createCustomer(String name, String email) {
        CustomerCreateParams createParams = CustomerCreateParams.builder()
                .setName(name)
                .setEmail(email)
                .build();
        try {
            return Customer.create(createParams);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create Stripe customer with "
                    + "email: %s and name: %s", email, name), e);
        }
    }

    public Customer retrieveCustomer(String id) {
        try {
            return Customer.retrieve(id);
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

            return customer.update(updateParams);

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Account retrieveAccount(String accountId) {
        try {
            return Account.retrieve(accountId);
        } catch (StripeException e) {
            throw new RuntimeException(
                    String.format("Can't retrieve Account with id %s", accountId), e);
        }
    }

    public PaymentIntent retrievePaymentIntent(String id) {
        try {
            return PaymentIntent.retrieve(id);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve PaymentIntent "
                    + "intent with id %s", id), e);
        }
    }

    public Charge retrieveCharge(String id) {
        try {
            return Charge.retrieve(id);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't retrieve Сharge "
                    + " with id %s", id), e);
        }
    }

    public BalanceTransaction retrieveBalanceTransaction(String id) {
        try {
            return BalanceTransaction.retrieve(id);
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

        try {
            return Transfer.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create transfer with charge: %s"
                    + "for connected account id: %s", chargeId, stripeProfileId), e);
        }
    }

    public StripeBalanceEarningsResponseDto getBalanceAndEarnings(String authorCognitoSubject) {
        String stripeProfileId = stripeProfileService.getAccountIdByAuthor(authorCognitoSubject);
        RequestOptions requestOptions =
                RequestOptions.builder().setStripeAccount(stripeProfileId).build();

        try {
            Balance balance = Balance.retrieve(requestOptions);
            List<Balance.Available> availableBalance = balance.getAvailable();
            long balanceAmount = availableBalance.stream()
                    .mapToLong(Balance.Available::getAmount)
                    .sum() / 100;

            PayoutCollection list = Payout.list(PayoutListParams.builder().build(), requestOptions);
            long totalEarnings = list.getData()
                    .stream()
                    .mapToLong(Payout::getAmount)
                    .sum() / 100;
            return new StripeBalanceEarningsResponseDto(balanceAmount, totalEarnings);

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
            return Refund.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(String.format("Can't create Refund for chargeId: %s", chargeId), e);
        }
    }

    private List<SessionCreateParams.LineItem> prepareItems(Set<Long> paintingsIds) {

        List<SessionCreateParams.LineItem> itemList = new ArrayList<>();
        List<Painting> paintingsInProcessingStatus = new ArrayList<>();
        List<Painting> paintingsInSoldStatus = new ArrayList<>();

        for (Long paintingId : paintingsIds) {
            Painting painting = paintingService.get(paintingId);
            if (painting.getPaymentStatus().equals(PaymentStatus.PROCESSING)) {
                paintingsInProcessingStatus.add(painting);
            } else if (painting.getPaymentStatus().equals(PaymentStatus.SOLD)) {
                paintingsInSoldStatus.add(painting);
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

        if (!paintingsInProcessingStatus.isEmpty() || !paintingsInSoldStatus.isEmpty()) {
            String message = createErrorMessage(paintingsInProcessingStatus, paintingsInSoldStatus);
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
                                DeliveryEstimate.builder()
                                        .setMinimum(
                                                DeliveryEstimate.Minimum.builder()
                                                        .setUnit(DeliveryEstimate.Minimum.Unit.BUSINESS_DAY)
                                                        .setValue(minDays)
                                                        .build())
                                        .setMaximum(
                                                DeliveryEstimate.Maximum.builder()
                                                        .setUnit(DeliveryEstimate.Maximum.Unit.BUSINESS_DAY)
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
                                      List<Painting> paintingsInSoldStatus) {
        String paintingInProcessingMessage = "";
        String paintingInSoldMessage = "";
        if (paintingsInProcessingStatus.size() == 1) {
            Painting painting = paintingsInProcessingStatus.get(0);
            paintingInProcessingMessage = String.format("Painting '%s' by %s is not "
                            + "available for buying. It is in process of payment. ",
                    painting.getTitle(), painting.getAuthor().getFullName());
        } else if (paintingsInProcessingStatus.size() > 1) {
            StringBuilder builder = getStringBuilderForSeveralPaintings(paintingsInProcessingStatus);
            builder.append(" are not available for buying. They are in process of payment. ");
            paintingInProcessingMessage = builder.toString();
        }

        if (paintingsInSoldStatus.size() == 1) {
            Painting painting = paintingsInSoldStatus.get(0);
            paintingInSoldMessage = String.format("Painting '%s' by %s is not "
                            + "available for buying. It is already sold. ",
                    painting.getTitle(), painting.getAuthor().getFullName());
        } else if (paintingsInSoldStatus.size() > 1) {
            StringBuilder builder = getStringBuilderForSeveralPaintings(paintingsInSoldStatus);
            builder.append(" are not available for buying. They are already sold. ");
            paintingInSoldMessage = builder.toString();
        }
        return paintingInSoldMessage + System.lineSeparator() + paintingInProcessingMessage;
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
