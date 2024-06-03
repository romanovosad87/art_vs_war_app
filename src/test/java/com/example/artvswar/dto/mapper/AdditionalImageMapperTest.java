package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Order(310)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdditionalImageMapperTest {

    @Autowired
    private AdditionalImageMapper additionalImageMapper;

    @MockBean
    private CloudinaryClient cloudinaryClient;

    @MockBean
    private PaintingService paintingService;

    @MockBean
    private ImageTransformation imageTransformation;

    private AdditionalImage additionalImage;
    private ImageCreateRequestDto imageDto;
    private Image image;

    private static final Long ID = 1L;
    private static final String IMAGE_PUBLIC_ID = "img123";
    private static final String IMAGE_URL = "http://example.com/image.jpg";
    private static final String VERSION = "v1";
    private static final String SIGNATURE = "v1";
    private static final String MODERATION_STATUS = ModerationStatus.APPROVED.name();

    @BeforeEach
    public void setUp() {
        additionalImage = new AdditionalImage();
        image = new Image();
        image.setPublicId(IMAGE_PUBLIC_ID);
        image.setUrl(IMAGE_URL);
        image.setModerationStatus(ModerationStatus.valueOf(MODERATION_STATUS));
        additionalImage.setId(ID);
        additionalImage.setImage(image);

        imageDto = new ImageCreateRequestDto();
        imageDto.setPublicId(IMAGE_PUBLIC_ID);
        imageDto.setVersion(VERSION);
        imageDto.setSignature(SIGNATURE);
        imageDto.setModerationStatus(MODERATION_STATUS);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(cloudinaryClient, paintingService, imageTransformation);
    }

    @Test
    @DisplayName("toDto - Converts AdditionalImage to AdditionalImageResponseDto")
    @Order(10)
    void testToDto_Success() {
        // Act
        AdditionalImageResponseDto dto = additionalImageMapper.toDto(additionalImage);

        // Assert
        assertNotNull(dto, "The DTO should not be null.");
        assertEquals(ID, dto.getId(), "Expected ID to match.");
        assertEquals(IMAGE_PUBLIC_ID, dto.getImageId(), "Expected image public ID to match.");
        assertEquals(IMAGE_URL, dto.getImageUrl(), "Expected image URL to match.");
        assertEquals(ModerationStatus.valueOf(MODERATION_STATUS), dto.getImageModerationStatus(),
                "Expected moderation status to match.");
    }

    @Test
    @DisplayName("toModel - Throws CloudinaryCredentialException when invalid signature")
    @Order(20)
    void testToModel_InvalidSignature() {
        // Act & Assert
        assertThrows(CloudinaryCredentialException.class, () -> {
            additionalImageMapper.toModel(imageDto, ID);
        }, "Should throw CloudinaryCredentialException due to invalid signature.");
    }

    @Test
    @DisplayName("toModel - Successfully converts ImageCreateRequestDto to AdditionalImage")
    @Order(30)
    void testToModel_Success() {
        // Arrange
        when(cloudinaryClient.verifySignature(anyString(), anyString(), anyString())).thenReturn(true);
        when(paintingService.getReference(ID)).thenReturn(new Painting());  // Ensure this returns a valid object
        when(imageTransformation.paintingImageEagerTransformation(anyString())).thenReturn(IMAGE_URL);

        // Act
        AdditionalImage result = additionalImageMapper.toModel(imageDto, ID);

        // Assert
        assertNotNull(result, "The model should not be null.");
        assertNotNull(result.getPainting(), "Painting reference should not be null.");
        assertEquals(IMAGE_PUBLIC_ID, result.getImage().getPublicId(), "Expected public ID to match.");
        assertNotNull(result.getImage().getUrl(), "URL should not be null.");
        assertEquals(ModerationStatus.valueOf(MODERATION_STATUS), result.getImage().getModerationStatus(),
                "Expected moderation status to match.");
    }
}