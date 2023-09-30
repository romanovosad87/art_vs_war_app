package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.AccountMapper;
import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountShippingAddress;
import com.example.artvswar.repository.AccountRepository;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.ShoppingCartService;
import com.example.artvswar.util.AwsCognitoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
    private static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AwsCognitoClient awsCognitoClient;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public AccountResponseDto save(AccountCreateUpdateRequestDto dto, String cognitoSubject,
                                   String cognitoUsername, String stripeCustomerId) {
        Account account = accountMapper.toModel(dto, cognitoSubject, cognitoUsername, stripeCustomerId);
        Account savedAccount = accountRepository.save(account);
        shoppingCartService.create(account);
        awsCognitoClient.addUserToGroup(cognitoUsername, ROLE_CUSTOMER);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountResponseDto update(AccountCreateUpdateRequestDto dto, String cognitoSubject) {
        Account account = accountRepository.findByCognitoSubject(Account.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find account by cognito subject : %s", cognitoSubject)));
        Account updatedAccount = accountMapper.updateAccountModel(dto, account);
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    public AccountResponseDto get(String cognitoSubject) {
        return accountRepository.findByCognitoSubject(AccountResponseDto.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find account by cognito subject : %s", cognitoSubject)));
    }

    @Override
    public Account getAccountByReferenceId(Long id) {
        return accountRepository.getReferenceById(id);
    }

    @Override
    public String getStripeCustomerId(String cognitoSubject) {
        return accountRepository.getStripeCustomerId(cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find stripe customer id by cognito subject : %s",
                                cognitoSubject)));
    }

    @Override
    public Account getAccountByStripeCustomerId(String stripeCustomerId) {
        return accountRepository.findByStripeCustomerId(stripeCustomerId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find Account by Stripe CustomerId : %s",
                                stripeCustomerId)));
    }

    @Override
    @Transactional
    public List<AccountShippingResponseDto> saveAccountShippingAddresses(List<AccountShippingRequestDto> dtos,
                                                                         String cognitoSubject) {
        Account account = accountRepository.findByCognitoSubject(Account.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find account by Cognito Subject : %s", cognitoSubject)));

        List<AccountShippingAddress> addresses = dtos.stream()
                .map(accountMapper::toAccountShippingModel)
                .collect(Collectors.toList());

        account.setShippingAddresses(addresses);

        return addresses.stream()
                .map(accountMapper::toAccountShippingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountShippingResponseDto> getAccountShippingAddresses(String cognitoSubject) {
        Account account = accountRepository.findByCognitoSubject(Account.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find account by Cognito Subject : %s", cognitoSubject)));
        return account.getShippingAddresses()
                .stream()
                .map(accountMapper::toAccountShippingDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getIdByCognitoSubject(String cognitoSubject) {
        return accountRepository.getIdByCognitoSubject(cognitoSubject);
    }

    @Override
    public String getCognitoSubjectByStripeId(String stripeCustomerId) {
        return null;
    }
}
