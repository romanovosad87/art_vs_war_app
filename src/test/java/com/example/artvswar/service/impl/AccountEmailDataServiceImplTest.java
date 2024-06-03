package com.example.artvswar.service.impl;

import com.example.artvswar.dto.request.accountemaildata.AccountEmailDataRequestDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.enummodel.EmailNotificationType;
import com.example.artvswar.repository.AccountEmailDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Order(700)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountEmailDataServiceImplTest {

    @MockBean
    private AccountEmailDataRepository accountEmailDataRepository;

    @Autowired
    private AccountEmailDataServiceImpl accountEmailDataService;

    private AccountEmailDataRequestDto dto;
    private AccountEmailData accountEmailData;

    private static final String EMAIL = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        dto = new AccountEmailDataRequestDto();
        dto.setEmail(EMAIL);
        dto.setNotificationType(EmailNotificationType.COMPLAINT.name());

        accountEmailData = new AccountEmailData();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(accountEmailDataRepository);
    }

    @Test
    @Order(10)
    @DisplayName("updateEmailData - Should set complaint to true when notification type is COMPLAINT")
    void testUpdateEmailData_SetComplaintToTrue() {
        // Arrange
        when(accountEmailDataRepository.getAccountEmailDataByEmail(dto.getEmail()))
                .thenReturn(Optional.of(accountEmailData));

        // Act
        accountEmailDataService.updateEmailData(dto);

        // Assert
        verify(accountEmailDataRepository).getAccountEmailDataByEmail(dto.getEmail());
        assertTrue(accountEmailData.isComplaint(), "Complaint flag should be set to true");
        assertFalse(accountEmailData.isBounce(), "Bounce flag should be set to false");
    }

    @Test
    @Order(20)
    @DisplayName("updateEmailData - Should set bounce to true when notification type is BOUNCE")
    void testUpdateEmailData_SetBounceToTrue() {
        // Arrange
        dto.setNotificationType(EmailNotificationType.BOUNCE.name());
        when(accountEmailDataRepository.getAccountEmailDataByEmail(dto.getEmail()))
                .thenReturn(Optional.of(accountEmailData));

        // Act
        accountEmailDataService.updateEmailData(dto);

        // Assert
        verify(accountEmailDataRepository).getAccountEmailDataByEmail(dto.getEmail());
        assertTrue(accountEmailData.isBounce(), "Bounce flag should be set to true");
        assertFalse(accountEmailData.isComplaint(), "Complaint flag should be set to false");
    }

    @Test
    @Order(30)
    @DisplayName("updateEmailData - Should throw AppEntityNotFoundException when email not found")
    void testUpdateEmailData_EmailNotFound() {
        // Arrange
        when(accountEmailDataRepository.getAccountEmailDataByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AppEntityNotFoundException exception = assertThrows(AppEntityNotFoundException.class,
                () -> accountEmailDataService.updateEmailData(dto));
        assertEquals("Can't find AccountEmailData by email: " + "'" + EMAIL + "'",
                exception.getMessage(), "Exception message should match");
    }
}
