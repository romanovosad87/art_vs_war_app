package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountShippingAddress;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toModel(AccountCreateUpdateRequestDto dto, String cognitoSubject,
                           String cognitoUsername, String stripeCustomerId) {
        Account account = new Account();
        account.setCognitoSubject(cognitoSubject);
        account.setCognitoUsername(cognitoUsername);
        account.setFirstName(dto.getFirstName().trim());
        account.setLastName(dto.getLastName().trim());
        account.setEmail(dto.getEmail().trim());
        account.setPhone(dto.getPhone().trim());
        account.setStripeCustomerId(stripeCustomerId);
        return account;
    }

    public Account updateAccountModel(AccountCreateUpdateRequestDto dto, Account account) {
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        return account;
    }

    public AccountResponseDto toDto(Account account) {
        return new AccountResponseDto(
                account.getId(),
                account.getCognitoSubject(),
                account.getCognitoUsername(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getPhone());
    }

    public AccountShippingAddress toAccountShippingModel(AccountShippingRequestDto dto) {
        AccountShippingAddress address = new AccountShippingAddress();
        address.setFirstName(dto.getFirstName());
        address.setLastName(dto.getLastName());
        address.setPhone(dto.getPhone());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setCountryCode(CountryCode.findByName(dto.getCountry()).get(0).name());
        address.setPostalCode(dto.getPostalCode());
        return address;
    }

    public AccountShippingResponseDto toAccountShippingDto(AccountShippingAddress address) {
        return new AccountShippingResponseDto(
                address.getFirstName(),
                address.getLastName(),
                address.getPhone(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getCountryCode(),
                address.getPostalCode());
    }
}
