package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.image.FullImageCreateRequestDto;
import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.util.ModerationMockImage;
import com.example.artvswar.util.RatioHelper;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class PaintingImageMapperTest {

    @InjectMocks
    private PaintingImageMapper paintingImageMapper;

    @Mock
    private RatioHelper ratioHelper;
    @Mock
    private CloudinaryClient cloudinaryClient;
    @Mock
    private ImageTransformation imageTransformation;

    private static final String PUBLIC_ID = "publicId123";
    private static final String PUBLIC_ID_UPDATED = "publicIdUpdated";
    private static final String SIGNATURE = "validSignature";
    private static final String SIGNATURE_INVALID = "invalidSignature";
    private static final String VERSION = "1";
    private static final String MODERATION_STATUS_APPROVED = ModerationStatus.APPROVED.name();

    private FullImageCreateRequestDto createRequestDto;
    private FullImageUpdateRequestDto updateRequestDto;
    private PaintingImage existingPaintingImage;
    private List<AdditionalImage> additionalImages;

    @BeforeEach
    public void setUp() {
        createRequestDto = new FullImageCreateRequestDto();
        createRequestDto.setPublicId(PUBLIC_ID);
        createRequestDto.setSignature(SIGNATURE);
        createRequestDto.setVersion(VERSION);
        createRequestDto.setHeight(800.00);
        createRequestDto.setWidth(600.00);
        createRequestDto.setModerationStatus(MODERATION_STATUS_APPROVED);

        updateRequestDto = new FullImageUpdateRequestDto();
        updateRequestDto.setPublicId(PUBLIC_ID_UPDATED);
        updateRequestDto.setSignature(SIGNATURE);
        updateRequestDto.setVersion(VERSION);
        updateRequestDto.setHeight(900.00);
        updateRequestDto.setWidth(700.00);
        updateRequestDto.setModerationStatus(MODERATION_STATUS_APPROVED);

        existingPaintingImage = new PaintingImage();
        Image image = new Image();
        image.setPublicId(PUBLIC_ID);
        image.setUrl("https://example.com/original.jpg");
        image.setModerationStatus(ModerationStatus.APPROVED);
        existingPaintingImage.setImage(image);

        additionalImages = new ArrayList<>();
        AdditionalImage additionalImage = new AdditionalImage();
        additionalImage.setImage(image);
        additionalImages.add(additionalImage);

        when(ratioHelper.getTransformedRatio(anyDouble())).thenReturn(1.5);
        when(cloudinaryClient.verifySignature(anyString(), anyString(), anyString())).thenReturn(true);
        when(imageTransformation.paintingImageEagerTransformation(anyString()))
                .thenReturn("https://example.com/transformed.jpg");
    }

    @Test
    @Order(10)
    @DisplayName("toImageResponseDto - Successfully transforms to DTO")
    void testToImageResponseDto_Success() {
        // Act
        PaintingImageResponseDto dto = paintingImageMapper.toImageResponseDto(existingPaintingImage, additionalImages);

        // Assert
        assertNotNull(dto, "DTO should not be null");
        assertEquals(PUBLIC_ID, dto.getImagePublicId(),
                "Public ID should match the expected value");
        assertEquals(ModerationStatus.APPROVED, dto.getImageModerationStatus(),
                "Moderation status should be APPROVED");
        assertTrue(dto.getViews().contains("https://example.com/original.jpg"),
                "Views should contain the original image URL");
    }

    @Test
    @Order(11)
    @DisplayName("toImageResponseDto - Handles PENDING Moderation Status")
    void testToImageResponseDto_Pending() {
        // Arrange
        existingPaintingImage.getImage().setModerationStatus(ModerationStatus.PENDING);

        // Act
        PaintingImageResponseDto dto = paintingImageMapper.toImageResponseDto(existingPaintingImage, additionalImages);

        // Assert
        assertNotNull(dto, "DTO should not be null");
        assertEquals(ModerationStatus.PENDING, dto.getImageModerationStatus(), "Moderation status should be PENDING");
        assertEquals(ModerationMockImage.PENDING_URL, dto.getImageUrl(), "Image URL should be the predefined PENDING URL");
        assertTrue(dto.getViews().contains(ModerationMockImage.PENDING_URL), "Views should contain the PENDING image URL");
    }

    @Test
    @Order(12)
    @DisplayName("toImageResponseDto - Handles REJECTED Moderation Status")
    void testToImageResponseDto_Rejected() {
        // Arrange
        existingPaintingImage.getImage().setModerationStatus(ModerationStatus.REJECTED);

        // Act
        PaintingImageResponseDto dto = paintingImageMapper.toImageResponseDto(existingPaintingImage, additionalImages);

        // Assert
        assertNotNull(dto, "DTO should not be null");
        assertEquals(ModerationStatus.REJECTED, dto.getImageModerationStatus(), "Moderation status should be REJECTED");
        assertEquals(ModerationMockImage.REJECTED_URL, dto.getImageUrl(), "Image URL should be the predefined REJECTED URL");
        assertTrue(dto.getViews().contains(ModerationMockImage.REJECTED_URL), "Views should contain the REJECTED image URL");
    }

    @Test
    @Order(20)
    @DisplayName("toImageModel - Successfully creates image model from DTO with valid signature")
    void testToImageModel_Success() {
        // Act
        PaintingImage result = paintingImageMapper.toImageModel(createRequestDto);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(800, result.getHeight(), "Height should match");
        assertEquals(600, result.getWidth(), "Width should match");
        assertEquals(1.5, result.getTransformedRatio(),
                "Transformed ratio should match expected value");
        assertEquals(ModerationStatus.APPROVED, result.getImage().getModerationStatus(),
                "Moderation status should be APPROVED");
        assertEquals("https://example.com/transformed.jpg", result.getImage().getUrl(),
                "URL should match the transformed image URL");
    }

    @Test
    @Order(30)
    @DisplayName("toImageModel - Throws CloudinaryCredentialException when signature is invalid")
    void testToImageModel_InvalidSignature() {
        // Arrange
        when(cloudinaryClient.verifySignature(PUBLIC_ID, VERSION, SIGNATURE_INVALID)).thenReturn(false);
        createRequestDto.setSignature(SIGNATURE_INVALID);

        // Act & Assert
        CloudinaryCredentialException exception = assertThrows(CloudinaryCredentialException.class,
                () -> paintingImageMapper.toImageModel(createRequestDto),
                "Should throw CloudinaryCredentialException when" +
                        " the signature verification fails due to invalid credentials");
        assertTrue(exception.getMessage().contains("not valid for image public_id = " + PUBLIC_ID),
                "Exception message should indicate invalid credentials");
    }

    @Test
    @Order(31)
    @DisplayName("toImageModel with Update DTO - Successfully updates image model from DTO with valid signature")
    void testToImageModelWithUpdate_Success() {
        // Arrange
        when(cloudinaryClient.verifySignature(PUBLIC_ID_UPDATED, VERSION, SIGNATURE)).thenReturn(true);
        when(imageTransformation.paintingImageEagerTransformation(PUBLIC_ID_UPDATED)).thenReturn("http://example.com/updated.jpg");

        // Act
        PaintingImage result = paintingImageMapper.toImageModel(updateRequestDto, existingPaintingImage);

        // Assert
        assertNotNull(result, "The updated painting image should not be null");
        assertEquals(PUBLIC_ID_UPDATED, result.getImage().getPublicId(), "Public ID should be updated");
        assertEquals("http://example.com/updated.jpg", result.getImage().getUrl(), "Image URL should be updated to the transformed URL");
        assertEquals(900, result.getHeight(), "Height should be updated to match DTO");
        assertEquals(700, result.getWidth(), "Width should be updated to match DTO");
        assertEquals(ModerationStatus.APPROVED, result.getImage().getModerationStatus(), "Moderation status should be updated to APPROVED");
        assertEquals(1.5, result.getTransformedRatio(), "Transformed ratio should be recalculated");
    }

    @Test
    @Order(32)
    @DisplayName("toImageModel with Update DTO - Throws CloudinaryCredentialException when signature is invalid")
    void testToImageModelWithUpdate_InvalidSignature() {
        // Arrange
        when(cloudinaryClient.verifySignature(PUBLIC_ID_UPDATED, VERSION, SIGNATURE_INVALID)).thenReturn(false);
        updateRequestDto.setSignature(SIGNATURE_INVALID);

        // Act & Assert
        CloudinaryCredentialException exception = assertThrows(CloudinaryCredentialException.class,
                () -> paintingImageMapper.toImageModel(updateRequestDto, existingPaintingImage),
                "Should throw CloudinaryCredentialException when the signature verification fails due to invalid credentials");
        assertTrue(exception.getMessage().contains("not valid for image public_id = " + PUBLIC_ID_UPDATED), "Exception message should indicate invalid credentials for the updated public ID");
    }

    @Test
    @Order(40)
    @DisplayName("toImageModelSameImage - Updates model without changing image details")
    void testToImageModelSameImage() {
        // Act
        PaintingImage result = paintingImageMapper.toImageModelSameImage(updateRequestDto, existingPaintingImage);

        // Assert
        assertNotNull(result, "Updated painting image should not be null");
        assertEquals(900, result.getHeight(), "Height should be updated to 900");
        assertEquals(700, result.getWidth(), "Width should be updated to 700");
    }
}