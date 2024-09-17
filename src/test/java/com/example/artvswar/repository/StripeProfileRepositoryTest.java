package com.example.artvswar.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "db/creation/stripeprofile/V1__populate-for-stripe-profile.sql")
class StripeProfileRepositoryTest extends GenericRepositoryContainer {

    private static final String STRIPE_ACCOUNT_ID_VALID = "acct_1OmdD4RaZbILOxz5";
    private static final String STRIPE_ACCOUNT_ID_INVALID = "acct_not_exist";
    private static final String AUTHOR_COGNITO_SUBJECT = "231c6372-b42a-41b0-8264-dd208f724b68";

    @Autowired
    private StripeProfileRepository stripeProfileRepository;

    @Test
    @DisplayName("should find Stripe Profile by valid stripe account id")
    void shouldFindStripeProfileByValidAccountId() {
        //when
        var optionalStripeProfile = stripeProfileRepository.getStripeProfileByAccountId(STRIPE_ACCOUNT_ID_VALID);

        // then
        assertThat(optionalStripeProfile).isNotEmpty();
        assertThat(optionalStripeProfile.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should not find Stripe Profile by invalid stripe account id")
    void shouldNotFindStipeProfileByInvalidAccountId() {
        //when
        var optionalStripeProfile = stripeProfileRepository.getStripeProfileByAccountId(STRIPE_ACCOUNT_ID_INVALID);

        //then
        assertThat(optionalStripeProfile).isEmpty();
    }

    @Test
    @DisplayName("should find Stripe Account ID by Author Cognito Subject")
    void shouldFindStripeAccountIdByCognitoSubject() {
        // when
        String stripeAccountId = stripeProfileRepository.getAccountIdByAuthor(AUTHOR_COGNITO_SUBJECT);

        // then
        assertThat(stripeAccountId).endsWith(STRIPE_ACCOUNT_ID_VALID);
    }

    @Test
    @DisplayName("should not find Stripe Account ID by not existing Author Cognito Subject")
    void shouldNotFindStripeAccountIdByNotExistingCognitoSubject() {
        // when
        String stripeAccountId = stripeProfileRepository.getAccountIdByAuthor("not exist");

        // then
        assertThat(stripeAccountId).isNull();
    }
}
