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

    private static final String ACCOUNT_ID_VALID = "acct_1OmdD4RaZbILOxz5";
    private static final String ACCOUNT_ID_INVALID = "acct_not_exist";

    @Autowired
    private StripeProfileRepository stripeProfileRepository;

    @Test
    @DisplayName("should find Stripe Profile by valid stripe account id")
    void shouldFindStripeProfileByValidAccountId() {
        //when
        var optionalStripeProfile = stripeProfileRepository.getStripeProfileByAccountId(ACCOUNT_ID_VALID);

        // then
        assertThat(optionalStripeProfile).isNotEmpty();
        assertThat(optionalStripeProfile.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should not find Stripe Profile by invalid stripe account id")
    void shouldNotFindStipeProfileByInvalidAccountId() {
        //when
        var optionalStripeProfile = stripeProfileRepository.getStripeProfileByAccountId(ACCOUNT_ID_INVALID);

        //then
        assertThat(optionalStripeProfile).isEmpty();
    }
}