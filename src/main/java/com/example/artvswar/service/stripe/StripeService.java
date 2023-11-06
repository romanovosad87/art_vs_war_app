package com.example.artvswar.service.stripe;

import com.example.artvswar.dto.request.stripe.StripeCheckoutSessionRequestDto;
import com.example.artvswar.dto.response.stripe.StripeBalanceEarningsResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Order;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.StripeProfile;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.OrderService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.StripeProfileService;
import com.example.artvswar.util.StripeUtils;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.LoginLink;
import com.stripe.model.Refund;
import com.stripe.model.Transfer;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class StripeService {
    private final StripeProfileService stripeProfileService;
    private final AuthorService authorService;
    private final PaintingService paintingService;
    private final OrderService orderService;
    private final StripeUtils stripeUtils;

    public String getOnboardingUrl(String authorCognitoSubject) {
        Author author = authorService.getAuthorByCognitoSubject(authorCognitoSubject);
        StripeProfile stripeProfile = author.getStripeProfile();
        String expressAccountId;
        if (stripeProfile == null) {
            Account account = stripeUtils.createExpressAccount(author);
            String stripeAccountId = account.getId();
            StripeProfile createdProfile = stripeProfileService.create(stripeAccountId, author.getCognitoSubject());
            expressAccountId = createdProfile.getAccountId();
        } else {
            expressAccountId = stripeProfile.getAccountId();
        }
        stripeUtils.createAccountLink(expressAccountId);
        AccountLink accountLink = stripeUtils.createAccountLink(authorCognitoSubject);
        return accountLink.getUrl();
    }

    public String deleteAccount(String accountId) {
        return stripeUtils.deleteAccount(accountId);
    }

    public String createLoginLinkToAccount(String authorCognitoSubject) {
        String stripeAccountId = stripeProfileService.
                getAccountIdByAuthor(authorCognitoSubject);
        LoginLink link = stripeUtils.createLoginLinkToAccount(stripeAccountId);
        return link.getUrl();
    }

    @Transactional
    public String getCheckoutSessionUrl(Set<Long> paintingIds,
                                        StripeCheckoutSessionRequestDto dto,
                                        String stripeCustomerId) {
        List<Painting> paintings = paintingIds.stream()
                .map(paintingService::get)
                .collect(Collectors.toList());
        Session session = stripeUtils.getCheckoutSessionUrl(paintings, dto, stripeCustomerId);
        paintingIds.stream()
                .map(paintingService::get)
                .forEach(painting -> paintingService.changePaymentStatus(painting,
                        PaymentStatus.PROCESSING));
        log.info(String.format("Created payment session with id: %s and metadata (painting ids: %s)",
                session.getId(), session.getMetadata()));
        return session.getUrl();
    }

    public Customer createCustomer(String name, String email) {
        return stripeUtils.createCustomer(name, email);
    }

    @Transactional
    @Scheduled(cron = "0 0 9,18 * * ?", zone = "ECT")
    public void provideTransferToAuthor() {
        LocalDateTime dayBefore = LocalDateTime.now()
                .minusDays(1L)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
        List<Order> orders = orderService.getOrdersDeliveredAtBeforeAndDoNotHasTransfer(dayBefore);
        log.info(String.format("List of orders to for proceeding transfer: %s", orders));
        for (var order : orders) {
            for (var painting : order.getPaintings()) {
                StripeProfile stripeProfile = painting.getAuthor().getStripeProfile();
                if (stripeProfile != null) {
                    String stripeAccountId = stripeProfile.getAccountId();
                    // Author amount is 40 % from the painting price (Stripe except long multiplied by 100)
                    long amount = painting.getPrice().multiply(BigDecimal.valueOf(0.4 * 100)).longValue();
                    String chargeId = order.getChargeId();
                    Transfer transfer = stripeUtils.createSeparateTransfer(chargeId, stripeAccountId, amount);
                    Long transferAmountLong = transfer.getAmount();
                    BigDecimal transferAmount = BigDecimal.valueOf((double) transferAmountLong / 100);
                    BigDecimal existingTransferAmount = Optional.ofNullable(order.getTransferAmount())
                            .orElse(BigDecimal.ZERO);
                    order.setTransferAmount(existingTransferAmount.add(transferAmount));
                    BigDecimal income = order.getNetAmount().subtract(transferAmount);
                    order.setIncome(income);
                }
            }
        }
    }

    public StripeBalanceEarningsResponseDto getBalanceAndEarnings(String authorCognitoSubject) {
        String stripeProfileId = stripeProfileService.getAccountIdByAuthor(authorCognitoSubject);
        RequestOptions requestOptions =
                RequestOptions.builder().setStripeAccount(stripeProfileId).build();
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(
                () -> stripeUtils.getTotalBalanceAmount(requestOptions));
        double totalBalanceAmount = completableFuture.join();
        double totalEarnings = stripeUtils.getTotalEarnings(requestOptions);

        return new StripeBalanceEarningsResponseDto(totalBalanceAmount, totalEarnings);
    }

    public Refund createRefund(String chargeId) {
        return stripeUtils.createRefund(chargeId);
    }
}
