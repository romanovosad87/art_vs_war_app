package com.example.artvswar.service.impl;

import com.example.artvswar.dto.request.accountemaildata.AccountEmailDataRequestDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.enummodel.EmailNotificationType;
import com.example.artvswar.repository.AccountEmailDataRepository;
import com.example.artvswar.service.AccountEmailDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountEmailDataServiceImpl implements AccountEmailDataService {
    private final AccountEmailDataRepository accountEmailDataRepository;

    @Transactional
    @Override
    public void updateEmailData(AccountEmailDataRequestDto dto) {
        String email = dto.getEmail();
        AccountEmailData accountEmailData = accountEmailDataRepository.getAccountEmailDataByEmail(email)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find AccountEmailData by email: '%s'", email)));
        String notificationType = dto.getNotificationType();
        if (notificationType.equals(EmailNotificationType.COMPLAINT.name())) {
            accountEmailData.setComplaint(true);
        } else if (notificationType.equals(EmailNotificationType.BOUNCE.name())) {
            accountEmailData.setBounce(true);
        }
    }
}
