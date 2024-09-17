package com.example.artvswar.service.impl;

import java.util.Optional;
import com.example.artvswar.dto.mapper.AccountMapper;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.repository.AccountRepository;
import com.example.artvswar.service.ShoppingCartService;
import com.example.artvswar.util.AwsCognitoClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private AwsCognitoClient awsCognitoClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Successfully created a new account")
    void save_WithValidRequestDto_Success() {
        String cognitoSubject = "cognitoSubject";
        String cognitoUsername = "cognitoUsername";
        String stripeCustomerId = "stripeCustomerId";

        AccountCreateUpdateRequestDto requestDto = new AccountCreateUpdateRequestDto();
        requestDto.setFirstName("Jeremy");
        requestDto.setLastName("Clarkson");
        requestDto.setEmail("jezza@mail.com");
        requestDto.setPhone("12345678");

        AccountEmailData accountEmailData = new AccountEmailData();
        accountEmailData.setEmail(requestDto.getEmail());

        Account account = new Account();
        account.setFirstName(requestDto.getFirstName());
        account.setLastName(requestDto.getLastName());
        account.addAccountEmailData(accountEmailData);
        account.setPhone(requestDto.getPhone());
        account.setCognitoSubject(cognitoSubject);
        account.setCognitoUsername(cognitoUsername);
        account.setStripeCustomerId(stripeCustomerId);

        Account savedAccount;
        savedAccount = account;
        savedAccount.setId(1L);

        AccountResponseDto expected = new AccountResponseDto(
                savedAccount.getId(),
                savedAccount.getCognitoSubject(),
                savedAccount.getCognitoUsername(),
                savedAccount.getFirstName(),
                savedAccount.getLastName(),
                savedAccount.getAccountEmailData().getEmail(),
                savedAccount.getPhone()
        );

        when(accountMapper.toModel(requestDto, cognitoSubject, cognitoUsername, stripeCustomerId)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(savedAccount);
        when(accountMapper.toDto(savedAccount)).thenReturn(expected);

        AccountResponseDto actual = accountService.save(requestDto, cognitoSubject, cognitoUsername, stripeCustomerId);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Successfully updating account")
    void update_WithValidRequestDto_Success() {
        String cognitoSubject = "cognitoSubject";
        String cognitoUsername = "cognitoUsername";
        String stripeCustomerId = "stripeCustomerId";

        AccountCreateUpdateRequestDto requestDto = new AccountCreateUpdateRequestDto();
        requestDto.setFirstName("Jeremy");
        requestDto.setLastName("Clarkson");
        requestDto.setEmail("jezza@mail.com");
        requestDto.setPhone("12345678");

        AccountEmailData accountEmailData = new AccountEmailData();
        accountEmailData.setEmail("may@mail.uk");

        Account account = new Account();
        account.setId(123L);
        account.setFirstName("James");
        account.setLastName("May");
        account.addAccountEmailData(accountEmailData);
        account.setPhone("2222222222");
        account.setCognitoSubject(cognitoSubject);
        account.setCognitoUsername(cognitoUsername);
        account.setStripeCustomerId(stripeCustomerId);

        AccountEmailData updatedAccountEmailData = new AccountEmailData();
        accountEmailData.setEmail(requestDto.getEmail());

        Account updatedAccount;
        updatedAccount = account;
        updatedAccount.setAccountEmailData(updatedAccountEmailData);
        updatedAccount.setFirstName(requestDto.getFirstName());
        updatedAccount.setLastName(requestDto.getLastName());
        updatedAccount.setPhone(requestDto.getPhone());

        AccountResponseDto expected = new AccountResponseDto(
                updatedAccount.getId(),
                updatedAccount.getCognitoSubject(),
                updatedAccount.getCognitoUsername(),
                updatedAccount.getFirstName(),
                updatedAccount.getLastName(),
                updatedAccount.getAccountEmailData().getEmail(),
                updatedAccount.getPhone()
        );
        when(accountRepository.findByCognitoSubject(Account.class, cognitoSubject))
                .thenReturn(Optional.of(account));
        when(accountMapper.updateAccountModel(requestDto, account)).thenReturn(updatedAccount);
        when(accountMapper.toDto(updatedAccount)).thenReturn(expected);

        AccountResponseDto actual = accountService.update(requestDto, cognitoSubject);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Unsuccessful attempt to update account by not an existing cognitoSubject")
    void update_WithNotValidRequestDto_NotSuccess() {
        String notAvailableCognitoSubject = "notAvailableCognitoSubject";

        assertThrows(AppEntityNotFoundException.class, () -> {
            accountService.update(new AccountCreateUpdateRequestDto(), notAvailableCognitoSubject);
        });
    }
}
