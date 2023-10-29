package com.example.artvswar.repository;

import com.example.artvswar.model.StripeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface StripeProfileRepository extends JpaRepository<StripeProfile, Long> {

    @Query("select sp.accountId from StripeProfile sp join sp.author a where a.cognitoSubject = ?1")
    String getAccountIdByAuthor(String authorCognitoSubject);

    Optional<StripeProfile> getStripeProfileByAccountId(String accountId);
}
