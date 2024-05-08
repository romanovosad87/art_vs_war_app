package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.AccountShippingAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    private AccountCreateUpdateRequestDto dto;
    private Account account;
    private AccountShippingRequestDto shippingDto;
    private AccountEmailData accountEmailData;
    private AccountShippingAddress address;

    private static final String COGNITO_SUBJECT = "cognito-sub";
    private static final String COGNITO_USERNAME = "cognito-username";
    private static final String STRIPE_CUSTOMER_ID = "12345";
    private static final String FIRST_NAME = "John";
    private static final String UPDATED_FIRST_NAME = "Ringo";
    private static final String LAST_NAME = "Lennon";
    private static final String UPDATED_LAST_NAME = "Starr";
    private static final String PHONE_NUMBER = "1234567";
    private static final String UPDATED_PHONE_NUMBER = "7654321";
    private static final String EMAIL = "john.lennon@gmail.com";
    private static final String UPDATED_EMAIL = "ringo.starr@gmail.com";
    private static final String ADDRESS_LINE_1 = "123 Abbey Road";
    private static final String ADDRESS_LINE_2 = "Suite 1";
    private static final String CITY = "London";
    private static final String STATE = "Greater London";
    private static final String COUNTRY = "United Kingdom";
    private static final String POSTAL_CODE = "NW8 9AY";

    @BeforeEach
    public void setUp() {
        dto = new AccountCreateUpdateRequestDto();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setPhone(PHONE_NUMBER);
        dto.setEmail(EMAIL);

        shippingDto = new AccountShippingRequestDto();
        shippingDto.setFirstName(FIRST_NAME);
        shippingDto.setLastName(LAST_NAME);
        shippingDto.setPhone(PHONE_NUMBER);
        shippingDto.setAddressLine1(ADDRESS_LINE_1);
        shippingDto.setAddressLine2(ADDRESS_LINE_2);
        shippingDto.setCity(CITY);
        shippingDto.setState(STATE);
        shippingDto.setCountry(COUNTRY);
        shippingDto.setPostalCode(POSTAL_CODE);

        account = new Account();
        accountEmailData = new AccountEmailData();
        accountEmailData.setEmail(EMAIL);
        account.addAccountEmailData(accountEmailData);

        address = new AccountShippingAddress();
    }

    // Test cases for createCountryFromDto(CountryRequestDto dto)

    @Test
    @DisplayName("toModel - Throw IllegalArgumentException for null parameters on creation")
    @Order(10)
    public void testToModel_CheckNullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toModel(null, COGNITO_SUBJECT, COGNITO_USERNAME,
                        STRIPE_CUSTOMER_ID),
                "Should throw IllegalArgumentException when the DTO is null.");
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toModel(dto, null, COGNITO_USERNAME,
                        STRIPE_CUSTOMER_ID),
                "Should throw IllegalArgumentException when the COGNITO SUBJECT is null.");
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toModel(dto, COGNITO_SUBJECT, null,
                        STRIPE_CUSTOMER_ID),
                "Should throw IllegalArgumentException when the COGNITO USERNAME is null.");
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toModel(dto, COGNITO_SUBJECT, COGNITO_USERNAME,
                        null),
                "Should throw IllegalArgumentException when the STRIPE CUSTOMER ID is null.");
    }

    @Test
    @DisplayName("toModel - Successfully create Account")
    @Order(20)
    public void testToModel_Success() {
        // Act
        account = accountMapper.toModel(dto, COGNITO_SUBJECT, COGNITO_USERNAME, STRIPE_CUSTOMER_ID);

        // Assert
        assertNotNull(account, "The account object should not be null after conversion.");

        assertEquals(FIRST_NAME, account.getFirstName(),
                "Expected first name to match the FIRST_NAME constant.");
        assertEquals(LAST_NAME, account.getLastName(),
                "Expected last name to match the LAST_NAME constant.");
        assertEquals(PHONE_NUMBER, account.getPhone(),
                "Expected phone number to match the PHONE_NUMBER constant.");
    }

    @Test
    @DisplayName("updateAccountModel - Throw IllegalArgumentException for null parameters on creation")
    @Order(25)
    public void tesUpdateAccountModel_CheckNullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountMapper.updateAccountModel(null, account),
                "Should throw IllegalArgumentException when the DTO is null.");
        assertThrows(IllegalArgumentException.class, () -> accountMapper.updateAccountModel(dto, null),
                "Should throw IllegalArgumentException when the ACCOUNT is null.");

    }

    @Test
    @DisplayName("updateAccountModel - Successfully update Account Model")
    @Order(30)
    public void testUpdateAccountModel_Success() {
        // Arrange
        dto.setFirstName(UPDATED_FIRST_NAME);
        dto.setLastName(UPDATED_LAST_NAME);
        dto.setPhone(UPDATED_PHONE_NUMBER);
        dto.setEmail(UPDATED_EMAIL);

        // Act
        Account updatedAccount = accountMapper.updateAccountModel(dto, account);

        // Assert
        assertNotNull(updatedAccount, "Account should not be null after update.");
        assertEquals(UPDATED_FIRST_NAME, updatedAccount.getFirstName(), "Expected first name to be updated.");
        assertEquals(UPDATED_LAST_NAME, updatedAccount.getLastName(), "Expected last name to be updated.");
        assertEquals(UPDATED_PHONE_NUMBER, updatedAccount.getPhone(), "Expected phone number to be updated.");
        assertEquals(UPDATED_EMAIL, updatedAccount.getAccountEmailData().getEmail(), "Expected email to be updated.");
    }

    @Test
    @DisplayName("toDto - Throw IllegalArgumentException for null parameters on creation")
    @Order(35)
    public void tesToDto_CheckNullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toDto(null),
                "Should throw IllegalArgumentException when the ACCOUNT is null.");
    }

    @Test
    @DisplayName("toDto - Convert Account to AccountResponseDto successfully")
    @Order(40)
    public void testToDto_Success() {
        // Act
        AccountResponseDto accountDto = accountMapper.toDto(account);

        // Assert
        assertNotNull(accountDto, "DTO should not be null after conversion.");
        assertEquals(account.getFirstName(), accountDto.getFirstName(), "First name should match.");
        assertEquals(account.getLastName(), accountDto.getLastName(), "Last name should match.");
        assertEquals(account.getPhone(), accountDto.getPhone(), "Phone should match.");
    }

    @Test
    @DisplayName("toAccountShippingModel - Throw IllegalArgumentException for null parameters on creation")
    @Order(45)
    public void tesToAccountShippingModel_CheckNullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toAccountShippingModel(null),
                "Should throw IllegalArgumentException when the AccountShippingRequestDto is null.");
    }

    @Test
    @DisplayName("toAccountShippingModel - Successfully create AccountShippingAddress")
    @Order(50)
    public void testToAccountShippingModel_Success() {
        // Act
        AccountShippingAddress address = accountMapper.toAccountShippingModel(shippingDto);

        // Assert
        assertNotNull(address, "AccountShippingAddress should not be null.");
        assertEquals(FIRST_NAME, address.getFirstName(), "First name should match.");
        assertEquals(LAST_NAME, address.getLastName(), "Last name should match.");
        assertEquals(PHONE_NUMBER, address.getPhone(), "Phone should match.");
        assertEquals(ADDRESS_LINE_1, address.getAddressLine1(), "Address line 1 should match.");
        assertEquals(ADDRESS_LINE_2, address.getAddressLine2(), "Address line 2 should match.");
        assertEquals(CITY, address.getCity(), "City should match.");
        assertEquals(STATE, address.getState(), "State should match.");
        assertEquals(COUNTRY, address.getCountry(), "Country should match.");
        assertEquals(POSTAL_CODE, address.getPostalCode(), "Postal code should match.");
    }

    @Test
    @DisplayName("toAccountShippingDto - Throw IllegalArgumentException for null parameters on creation")
    @Order(55)
    public void tesToAccountShippingDto_CheckNullParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountMapper.toAccountShippingDto(null),
                "Should throw IllegalArgumentException when the AccountShippingAddress is null.");
    }

    @Test
    @DisplayName("Convert AccountShippingAddress to AccountShippingResponseDto successfully")
    @Order(60)
    public void testToAccountShippingDto_Success() {
        // Act
        AccountShippingResponseDto accountShippingDto = accountMapper.toAccountShippingDto(address);

        // Assert
        assertNotNull(accountShippingDto, "AccountShippingResponseDto should not be null after conversion.");
        assertEquals(address.getFirstName(), accountShippingDto.getFirstName(), "First name should match.");
        assertEquals(address.getLastName(), accountShippingDto.getLastName(), "Last name should match.");
        assertEquals(address.getPhone(), accountShippingDto.getPhone(), "Phone should match.");
        assertEquals(address.getAddressLine1(), accountShippingDto.getAddressLine1(), "Address Line 1 should match.");
        assertEquals(address.getAddressLine2(), accountShippingDto.getAddressLine2(), "Address Line 2 should match.");
        assertEquals(address.getCity(), accountShippingDto.getCity(), "City should match.");
        assertEquals(address.getState(), accountShippingDto.getState(), "State should match.");
        assertEquals(address.getCountry(), accountShippingDto.getCountry(), "Country should match.");
        assertEquals(address.getCountryCode(), accountShippingDto.getCountryCode(), "Country code should match.");
        assertEquals(address.getPostalCode(), accountShippingDto.getPostalCode(), "Postal code should match.");
    }
}