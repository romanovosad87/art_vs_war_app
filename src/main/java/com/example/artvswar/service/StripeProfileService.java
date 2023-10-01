package com.example.artvswar.service;

import com.example.artvswar.model.StripeProfile;

public interface StripeProfileService {
    StripeProfile create(StripeProfile stripeProfile);

    String getAccountIdByAuthor(String authorCognitoSubject);
}
