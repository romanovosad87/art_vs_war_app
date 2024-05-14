package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.authorshippingaddress.AuthorShippingAddressRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingAddressResponseDto;
import com.example.artvswar.model.AuthorShippingAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorShippingAddressMapperTest {

    @InjectMocks
    private AuthorShippingAddressMapper authorShippingAddressMapper;

    private AuthorShippingAddressRequestDto requestDto;
    private AuthorShippingAddress existingAddress;

    private static final String INITIAL_ADDRESS_LINE1 = "1234 Main St";
    private static final String INITIAL_ADDRESS_LINE2 = "Apt 101";
    private static final String INITIAL_CITY = "Anytown";
    private static final String INITIAL_STATE = "Anystate";
    private static final String INITIAL_COUNTRY = "United States";
    private static final String INITIAL_COUNTRY_CODE = "US";
    private static final String INITIAL_POSTAL_CODE = "12345";
    private static final String INITIAL_PHONE = "123-456-7890";

    private static final String UPDATED_ADDRESS_LINE1 = "4321 Side St";
    private static final String UPDATED_ADDRESS_LINE2 = "Apt 202";
    private static final String UPDATED_CITY = "Othertown";
    private static final String UPDATED_STATE = "Otherstate";
    private static final String UPDATED_COUNTRY = "Canada";
    private static final String UPDATED_POSTAL_CODE = "54321";
    private static final String UPDATED_PHONE = "987-654-3210";

    @BeforeEach
    public void setUp() {
        requestDto = new AuthorShippingAddressRequestDto();
        requestDto.setAddressLine1(INITIAL_ADDRESS_LINE1);
        requestDto.setAddressLine2(INITIAL_ADDRESS_LINE2);
        requestDto.setCity(INITIAL_CITY);
        requestDto.setState(INITIAL_STATE);
        requestDto.setCountry(INITIAL_COUNTRY);
        requestDto.setPostalCode(INITIAL_POSTAL_CODE);
        requestDto.setPhone(INITIAL_PHONE);

        existingAddress = new AuthorShippingAddress();
        existingAddress.setAddressLine1(INITIAL_ADDRESS_LINE1);
        existingAddress.setAddressLine2(INITIAL_ADDRESS_LINE2);
        existingAddress.setCity(INITIAL_CITY);
        existingAddress.setState(INITIAL_STATE);
        existingAddress.setCountry(INITIAL_COUNTRY);
        existingAddress.setCountryCode(INITIAL_COUNTRY_CODE);
        existingAddress.setPostalCode(INITIAL_POSTAL_CODE);
        existingAddress.setPhone(INITIAL_PHONE);
    }

    @Test
    @Order(10)
    @DisplayName("toModel - Successfully creates model from DTO")
    void testToModel_Success() {
        // Act
        AuthorShippingAddress model = authorShippingAddressMapper.toModel(requestDto);

        // Assert
        assertNotNull(model, "Model should not be null");
        assertEquals(INITIAL_ADDRESS_LINE1, model.getAddressLine1(), "Address line 1 should match");
        assertEquals(INITIAL_ADDRESS_LINE2, model.getAddressLine2(), "Address line 2 should match");
        assertEquals(INITIAL_CITY, model.getCity(), "City should match");
        assertEquals(INITIAL_STATE, model.getState(), "State should match");
        assertEquals(INITIAL_COUNTRY, model.getCountry(), "Country should match");
        assertEquals(INITIAL_COUNTRY_CODE, model.getCountryCode(), "Country code should match");
        assertEquals(INITIAL_POSTAL_CODE, model.getPostalCode(), "Postal code should match");
        assertEquals(INITIAL_PHONE, model.getPhone(), "Phone should match");
    }

    @Test
    @Order(20)
    @DisplayName("toModelUpdate - Successfully updates model from DTO")
    void testToModelUpdate_Success() {
        // Arrange
        AuthorShippingAddressRequestDto updateDto = new AuthorShippingAddressRequestDto();
        updateDto.setAddressLine1(UPDATED_ADDRESS_LINE1);
        updateDto.setAddressLine2(UPDATED_ADDRESS_LINE2);
        updateDto.setCity(UPDATED_CITY);
        updateDto.setState(UPDATED_STATE);
        updateDto.setCountry(UPDATED_COUNTRY);
        updateDto.setPostalCode(UPDATED_POSTAL_CODE);
        updateDto.setPhone(UPDATED_PHONE);

        // Act
        AuthorShippingAddress updatedModel = authorShippingAddressMapper.toModelUpdate(updateDto, existingAddress);

        // Assert
        assertNotNull(updatedModel, "Updated model should not be null");
        assertEquals(UPDATED_ADDRESS_LINE1, updatedModel.getAddressLine1(),
                "Updated address line 1 should match");
        assertEquals(UPDATED_ADDRESS_LINE2, updatedModel.getAddressLine2(),
                "Updated address line 2 should match");
        assertEquals(UPDATED_CITY, updatedModel.getCity(),
                "Updated city should match");
        assertEquals(UPDATED_STATE, updatedModel.getState(),
                "Updated state should match");
        assertEquals(UPDATED_COUNTRY, updatedModel.getCountry(),
                "Updated country should match");
        assertEquals(UPDATED_POSTAL_CODE, updatedModel.getPostalCode(),
                "Updated postal code should match");
        assertEquals(UPDATED_PHONE, updatedModel.getPhone(),
                "Updated phone should match");
    }

    @Test
    @Order(30)
    @DisplayName("toDto - Successfully transforms model to DTO")
    void testToDto_Success() {
        // Act
        ShippingAddressResponseDto dto = authorShippingAddressMapper.toDto(existingAddress);

        // Assert
        assertNotNull(dto, "DTO should not be null");
        assertEquals(INITIAL_ADDRESS_LINE1, dto.getAddressLine1(), "DTO address line 1 should match");
        assertEquals(INITIAL_ADDRESS_LINE2, dto.getAddressLine2(), "DTO address line 2 should match");
        assertEquals(INITIAL_CITY, dto.getCity(), "DTO city should match");
        assertEquals(INITIAL_STATE, dto.getState(), "DTO state should match");
        assertEquals(INITIAL_COUNTRY, dto.getCountry(), "DTO country should match");
        assertEquals(INITIAL_COUNTRY_CODE, dto.getCountryCode(), "DTO country code should match");
        assertEquals(INITIAL_POSTAL_CODE, dto.getPostalCode(), "DTO postal code should match");
        assertEquals(INITIAL_PHONE, dto.getPhone(), "DTO phone should match");
    }
}
