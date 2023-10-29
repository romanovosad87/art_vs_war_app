package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.StripeProfile;
import com.example.artvswar.repository.StripeProfileRepository;
import com.example.artvswar.service.StripeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StripeProfileServiceImpl implements StripeProfileService {

    private final StripeProfileRepository stripeProfileRepository;

    @Override
    @Transactional
    public StripeProfile create(String accountId, Author author) {
        StripeProfile stripeProfile = new StripeProfile();
        stripeProfile.setAccountId(accountId);
        stripeProfile.setAuthor(author);
        return stripeProfileRepository.save(stripeProfile);
    }

    @Override
    public String getAccountIdByAuthor(String authorCognitoSubject) {
        return stripeProfileRepository.getAccountIdByAuthor(authorCognitoSubject);
    }

    @Override
    public StripeProfile get(String accountId) {
        return stripeProfileRepository.getStripeProfileByAccountId(accountId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find Stripe profile by accountId: %s", accountId)));
    }

    @Override
    @Transactional
    public void changeDetailsSubmitted(String accountId, boolean isDetailedSubmitted) {
        StripeProfile stripeProfile = stripeProfileRepository.getStripeProfileByAccountId(accountId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find Stripe profile by accountId: %s", accountId)));

        stripeProfile.setDetailsSubmitted(isDetailedSubmitted);
    }

    @Override
    public boolean checkIfDetailsSubmitted(String authorCognitoSubject) {
        String accountId = stripeProfileRepository.getAccountIdByAuthor(authorCognitoSubject);
        if (accountId != null) {
            return stripeProfileRepository
                    .getStripeProfileByAccountId(accountId)
                    .stream()
                    .map(StripeProfile::isDetailsSubmitted)
                    .findFirst()
                    .orElse(false);
        }
        return false;
    }
}
