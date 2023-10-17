package com.example.artvswar.repository;

import com.example.artvswar.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    <T> Optional<T> findByCognitoSubject(Class<T> type, String cognitoSubject);

    @Query("select ac.stripeCustomerId from Account ac where ac.cognitoSubject = ?1")
    Optional<String> getStripeCustomerId(String cognitoSubject);

    Optional<Account> findByStripeCustomerId(String stripeCustomerId);

    @Query("select ac.id from Account ac where ac.cognitoSubject = ?1")
    Long getIdByCognitoSubject(String cognitoSubject);

    @Query("select ac.cognitoSubject from Account ac where ac.stripeCustomerId = ?1")
    String getAccountCognitoSubjectByStripeCustomerId(String stripeCustomerId);
}
