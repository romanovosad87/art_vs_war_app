package com.example.artvswar.service;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.model.Account;
import java.util.List;

public interface AccountService {
    AccountResponseDto save(AccountCreateUpdateRequestDto dto,
                            String cognitoSubject,
                            String cognitoUsername,
                            String stripeCustomerId);
    AccountResponseDto update(AccountCreateUpdateRequestDto dto, String cognitoSubject);

    AccountResponseDto get(String cognitoSubject);

    Account getAccountByCognitoSubject(String cognitoSubject);

    Account getAccountByReferenceId(Long id);

    String getStripeCustomerId(String cognitoSubject);

    Account getAccountByStripeCustomerId(String stripeCustomerId);

    List<AccountShippingResponseDto> saveAccountShippingAddresses(List<AccountShippingRequestDto> dto,
                                                                  String cognitoSubject);

    List<AccountShippingResponseDto> getAccountShippingAddresses(String cognitoSubject);

    Long getIdByCognitoSubject(String cognitoSubject);

    String getCognitoSubjectByStripeId(String stripeCustomerId);

    void changeUnsubscribeEmailStatus(String accountSubject, boolean unsubscribe);
}
