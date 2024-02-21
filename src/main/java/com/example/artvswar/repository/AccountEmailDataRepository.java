package com.example.artvswar.repository;

import com.example.artvswar.model.AccountEmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AccountEmailDataRepository extends JpaRepository<AccountEmailData, Long> {

    @Query("select aed from AccountEmailData aed join fetch aed.account where aed.email = ?1")
    Optional<AccountEmailData> getAccountEmailDataByEmail(String email);
}
