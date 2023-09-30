package com.example.artvswar.service.impl;

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
    public StripeProfile create(StripeProfile stripeProfile) {
        return stripeProfileRepository.save(stripeProfile);
    }

    @Override
    public String getAccountIdByAuthor(String authorCognitoSubject) {
        return stripeProfileRepository.getAccountIdByAuthor(authorCognitoSubject);
    }
}
