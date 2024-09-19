package com.example.artvswar.service;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingAddressRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingAddressResponseDto;
import com.example.artvswar.model.Account;
import org.springframework.security.oauth2.jwt.Jwt;
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

    List<AccountShippingAddressResponseDto> saveAccountShippingAddresses(List<AccountShippingAddressRequestDto> dto,
                                                                         String cognitoSubject);

    List<AccountShippingAddressResponseDto> getAccountShippingAddresses(String cognitoSubject);

    String getCognitoSubjectByStripeId(String stripeCustomerId);

    void changeUnsubscribeEmailStatus(String accountSubject, boolean unsubscribe);

    void delete(String cognitoSubject, Jwt jwt);
}
