package com.example.artvswar.service;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.StripeProfile;

public interface StripeProfileService {
    StripeProfile create(String accountId, Author author);

    String getAccountIdByAuthor(String authorCognitoSubject);

    StripeProfile get(String accountId);

    void changeDetailsSubmitted(String accountId, boolean isDetailedSubmitted);

    boolean checkIfDetailsSubmitted(String authorCognitoSubject);
}
