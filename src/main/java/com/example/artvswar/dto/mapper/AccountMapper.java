package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.AccountShippingAddress;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private void checkNotNull(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Failed to process: parameters should not be null");
            }
        }
    }

    private void trimAndSetPersonalInfo(Account account, AccountCreateUpdateRequestDto dto) {
        account.setFirstName(dto.getFirstName().trim());
        account.setLastName(dto.getLastName().trim());
        account.setPhone(dto.getPhone().trim());
    }

    public Account toModel(AccountCreateUpdateRequestDto dto, String cognitoSubject,
                           String cognitoUsername, String stripeCustomerId) {
        checkNotNull(dto, cognitoSubject, cognitoUsername, stripeCustomerId);
        Account account = new Account();
        account.setCognitoSubject(cognitoSubject);
        account.setCognitoUsername(cognitoUsername);
        trimAndSetPersonalInfo(account, dto);
        account.setStripeCustomerId(stripeCustomerId);
        AccountEmailData accountEmailData = new AccountEmailData();
        accountEmailData.setEmail(dto.getEmail().trim());
        account.addAccountEmailData(accountEmailData);
        return account;
    }

    public Account updateAccountModel(AccountCreateUpdateRequestDto dto, Account account) {
        checkNotNull(dto, account);
        trimAndSetPersonalInfo(account, dto);
        AccountEmailData accountEmailData = account.getAccountEmailData();
        accountEmailData.setEmail(dto.getEmail().trim());
        return account;
    }

    public AccountResponseDto toDto(Account account) {
        checkNotNull(account);
        return new AccountResponseDto(
                account.getId(),
                account.getCognitoSubject(),
                account.getCognitoUsername(),
                account.getFirstName(),
                account.getLastName(),
                account.getAccountEmailData().getEmail(),
                account.getPhone());
    }

    public AccountShippingAddress toAccountShippingModel(AccountShippingRequestDto dto) {
        checkNotNull(dto);
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
        checkNotNull(address);
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
