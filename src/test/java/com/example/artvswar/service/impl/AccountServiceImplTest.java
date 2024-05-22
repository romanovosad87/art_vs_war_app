package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.AccountMapper;
import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.AccountShippingAddress;
import com.example.artvswar.repository.AccountRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ShoppingCartService;
import com.example.artvswar.util.AwsCognitoClient;
import com.example.artvswar.util.TimeZoneAPI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Order(710)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceImplTest {

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountMapper accountMapper;
    @MockBean
    private AwsCognitoClient awsCognitoClient;
    @MockBean
    private ShoppingCartService shoppingCartService;
    @MockBean
    private TimeZoneAPI timeZoneAPI;
    @MockBean
    private AuthorService authorService;

    @Autowired
    private AccountServiceImpl accountService;

    private Account account;
    private AccountResponseDto accountResponseDto;
    private AccountCreateUpdateRequestDto accountCreateUpdateRequestDto;


    private static final String COGNITO_SUBJECT = "cognitoSubject";
    private static final String COGNITO_USERNAME = "cognitoUsername";
    private static final String STRIPE_CUSTOMER_ID = "stripeCustomerId";
    private static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    private static final Long ACCOUNT_ID = 1L;
    private static final String ACCOUNT_SUBJECT = "accountSubject";
    private static final String TEST_STRIPE_CUSTOMER_ID = "stripeCustomerId";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(
                accountRepository,
                accountMapper,
                awsCognitoClient,
                shoppingCartService,
                timeZoneAPI,
                authorService
        );

        account = new Account();
        accountResponseDto = new AccountResponseDto();
        accountCreateUpdateRequestDto = new AccountCreateUpdateRequestDto();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(accountRepository, accountMapper, awsCognitoClient,
                shoppingCartService, timeZoneAPI, authorService);
    }

    @Test
    @DisplayName("save - Successfully saves an account")
    @Order(10)
    @Transactional
    void testSave_Success() {
        // Arrange
        when(accountMapper.toModel(any(AccountCreateUpdateRequestDto.class), anyString(), anyString(), anyString()))
                .thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountResponseDto);

        // Act
        AccountResponseDto responseDto = accountService
                .save(accountCreateUpdateRequestDto, COGNITO_SUBJECT, COGNITO_USERNAME, STRIPE_CUSTOMER_ID);

        // Assert
        assertNotNull(responseDto, "Response DTO should not be null");
        verify(accountRepository, times(1)).save(account);
        verify(shoppingCartService, times(1)).create(account);
        verify(awsCognitoClient, times(1)).addUserToGroup(COGNITO_USERNAME, ROLE_CUSTOMER);
    }

    @Test
    @DisplayName("update - Successfully updates an account")
    @Order(20)
    @Transactional
    void testUpdate_Success() {
        // Arrange
        when(accountRepository.findByCognitoSubject(Account.class, COGNITO_SUBJECT)).thenReturn(Optional.of(account));
        when(accountMapper.updateAccountModel(any(AccountCreateUpdateRequestDto.class), any(Account.class)))
                .thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountResponseDto);

        // Act
        AccountResponseDto responseDto = accountService.update(accountCreateUpdateRequestDto, COGNITO_SUBJECT);

        // Assert
        assertNotNull(responseDto, "Response DTO should not be null");
        verify(accountRepository, times(1))
                .findByCognitoSubject(Account.class, COGNITO_SUBJECT);
        verify(accountMapper, times(1))
                .updateAccountModel(accountCreateUpdateRequestDto, account);
    }

    @Test
    @Order(20)
    @DisplayName("get - Successfully retrieves an account by cognito subject")
    void testGet_Success() {
        // Arrange
        when(accountRepository.findByCognitoSubject(AccountResponseDto.class, COGNITO_SUBJECT))
                .thenReturn(Optional.of(accountResponseDto));

        // Act
        AccountResponseDto responseDto = accountService.get(COGNITO_SUBJECT);

        // Assert
        assertNotNull(responseDto, "Response DTO should not be null");
        verify(accountRepository, times(1))
                .findByCognitoSubject(AccountResponseDto.class, COGNITO_SUBJECT);
    }

    @Test
    @Order(30)
    @DisplayName("getAccountByCognitoSubject - Successfully retrieves account by cognito subject")
    void testGetAccountByCognitoSubject_Success() {
        // Arrange
        when(accountRepository.findByCognitoSubject(Account.class, COGNITO_SUBJECT)).thenReturn(Optional.of(account));

        // Act
        Account retrievedAccount = accountService.getAccountByCognitoSubject(COGNITO_SUBJECT);

        // Assert
        assertNotNull(retrievedAccount, "Retrieved account should not be null");
        verify(accountRepository, times(1)).findByCognitoSubject(Account.class, COGNITO_SUBJECT);
    }

    @Test
    @Order(40)
    @DisplayName("getAccountByReferenceId - Successfully retrieves account by reference ID")
    void testGetAccountByReferenceId_Success() {
        // Arrange
        when(accountRepository.getReferenceById(ACCOUNT_ID)).thenReturn(account);

        // Act
        Account retrievedAccount = accountService.getAccountByReferenceId(ACCOUNT_ID);

        // Assert
        assertNotNull(retrievedAccount, "Retrieved account should not be null");
        verify(accountRepository, times(1)).getReferenceById(ACCOUNT_ID);
    }

    @Test
    @Order(50)
    @DisplayName("getStripeCustomerId - Successfully retrieves stripe customer ID by cognito subject")
    void testGetStripeCustomerId_Success() {
        // Arrange
        when(accountRepository.getStripeCustomerId(COGNITO_SUBJECT)).thenReturn(Optional.of(STRIPE_CUSTOMER_ID));

        // Act
        String stripeCustomerId = accountService.getStripeCustomerId(COGNITO_SUBJECT);

        // Assert
        assertNotNull(stripeCustomerId,
                "Stripe Customer ID should not be null");
        assertEquals(STRIPE_CUSTOMER_ID, stripeCustomerId,
                "Stripe Customer ID should match the expected value");
        verify(accountRepository,
                times(1)).getStripeCustomerId(COGNITO_SUBJECT);
    }

    @Test
    @Order(60)
    @DisplayName("getAccountByStripeCustomerId - Successfully retrieves account by stripe customer ID")
    void testGetAccountByStripeCustomerId_Success() {
        // Arrange
        when(accountRepository.findByStripeCustomerId(STRIPE_CUSTOMER_ID)).thenReturn(Optional.of(account));

        // Act
        Account retrievedAccount = accountService.getAccountByStripeCustomerId(STRIPE_CUSTOMER_ID);

        // Assert
        assertNotNull(retrievedAccount, "Retrieved account should not be null");
        verify(accountRepository, times(1)).findByStripeCustomerId(STRIPE_CUSTOMER_ID);
    }

    @Test
    @Order(70)
    @DisplayName("saveAccountShippingAddresses - Successfully saves account shipping addresses")
    @Transactional
    void testSaveAccountShippingAddresses_Success() {
        // Arrange
        AccountShippingRequestDto shippingRequestDto = new AccountShippingRequestDto();
        AccountShippingAddress shippingAddress = new AccountShippingAddress();
        List<AccountShippingRequestDto> dtos = List.of(shippingRequestDto);

        when(accountRepository.findByCognitoSubject(Account.class, COGNITO_SUBJECT)).thenReturn(Optional.of(account));
        when(accountMapper.toAccountShippingModel(shippingRequestDto)).thenReturn(shippingAddress);
        when(accountMapper.toAccountShippingDto(any(AccountShippingAddress.class)))
                .thenReturn(new AccountShippingResponseDto());
        when(timeZoneAPI.getOffset(anyString(), anyString())).thenReturn(0);

        // Act
        List<AccountShippingResponseDto> responseDtos = accountService.saveAccountShippingAddresses(dtos,
                COGNITO_SUBJECT);

        // Assert
        assertNotNull(responseDtos, "Response DTO list should not be null");
        assertFalse(responseDtos.isEmpty(), "Response DTO list should not be empty");
        verify(accountRepository, times(1)).findByCognitoSubject(Account.class, COGNITO_SUBJECT);
        verify(accountMapper, times(1)).toAccountShippingModel(shippingRequestDto);
    }

    @Test
    @Order(80)
    @DisplayName("getCognitoSubjectByStripeId - Successfully retrieves cognito subject by stripe ID")
    void testGetCognitoSubjectByStripeId_Success() {
        // Arrange
        when(accountRepository.getAccountCognitoSubjectByStripeCustomerId(TEST_STRIPE_CUSTOMER_ID))
                .thenReturn(COGNITO_SUBJECT);

        // Act
        String cognitoSubject = accountService.getCognitoSubjectByStripeId(TEST_STRIPE_CUSTOMER_ID);

        // Assert
        assertNotNull(cognitoSubject,
                "Cognito Subject should not be null");
        assertEquals(COGNITO_SUBJECT,
                cognitoSubject, "Cognito Subject should match the expected value");
        verify(accountRepository,
                times(1)).getAccountCognitoSubjectByStripeCustomerId(TEST_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Order(90)
    @DisplayName("changeUnsubscribeEmailStatus - Successfully changes unsubscribe email status")
    @Transactional
    void testChangeUnsubscribeEmailStatus_Success() {
        // Arrange
        AccountEmailData accountEmailData = new AccountEmailData();
        account.setAccountEmailData(accountEmailData);

        when(accountRepository.findByCognitoSubject(Account.class, ACCOUNT_SUBJECT)).thenReturn(Optional.of(account));

        // Act
        accountService.changeUnsubscribeEmailStatus(ACCOUNT_SUBJECT, true);

        // Assert
        assertTrue(account.getAccountEmailData().isUnsubscribed(), "Account should be unsubscribed");
        verify(accountRepository, times(1)).findByCognitoSubject(Account.class, ACCOUNT_SUBJECT);
    }

    @Test
    @Order(100)
    @DisplayName("delete - Successfully deletes an account by cognito subject")
    @Transactional
    void testDelete_Success() {
        // Arrange
        Jwt jwt = mock(Jwt.class);
        when(accountRepository.findByCognitoSubject(Account.class, COGNITO_SUBJECT)).thenReturn(Optional.of(account));

        // Act
        accountService.delete(COGNITO_SUBJECT, jwt);

        // Assert
        assertTrue(account.isDeleted(), "Account should be marked as deleted");
        verify(accountRepository, times(1)).findByCognitoSubject(Account.class, COGNITO_SUBJECT);
        verify(awsCognitoClient, times(1)).deleteUser(jwt);
    }
}
