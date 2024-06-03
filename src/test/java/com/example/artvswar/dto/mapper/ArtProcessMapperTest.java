package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.artprocess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artprocess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.request.image.FullImageCreateRequestDto;
import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import com.example.artvswar.dto.response.artprocess.ArtProcessResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.ArtProcess;
import com.example.artvswar.model.ArtProcessImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.util.ModerationMockImage;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Order(320)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArtProcessMapperTest {

    @Autowired
    private ArtProcessMapper artProcessMapper;

    @MockBean
    private CloudinaryClient cloudinaryClient;

    @MockBean
    private ImageTransformation imageTransformation;

    private ArtProcess artProcess;
    private ArtProcessCreateRequestDto createRequestDto;
    private ArtProcessUpdateRequestDto updateRequestDto;
    private Image image;
    private ArtProcessImage artProcessImage;
    private FullImageUpdateRequestDto updateImageDto;

    private static final Long ART_PROCESS_ID = 1L;
    private static final String DESCRIPTION = "Original Art";
    private static final String IMAGE_PUBLIC_ID = "img123";
    private static final String IMAGE_PUBLIC_ID_NEW = "newImage123";
    private static final String IMAGE_URL = "http://example.com/image.jpg";
    private static final Double IMAGE_WIDTH = 1024.0;
    private static final Double IMAGE_HEIGHT = 768.0;
    private static final Double WIDTH_NEW = 500.0;
    private static final Double HEIGHT_NEW = 400.0;
    private static final String VERSION = "v1";
    private static final String SIGNATURE = "signatureXYZ";
    private static final String SIGNATURE_INVALID = "invalidSignature";
    private static final ModerationStatus MODERATION_STATUS_APPROVED = ModerationStatus.APPROVED;
    private static final ModerationStatus MODERATION_STATUS_REJECTED = ModerationStatus.REJECTED;
    private static final String UPDATED_DESCRIPTION = "Updated Art Description";

    @BeforeEach
    public void setUp() {
        artProcess = new ArtProcess();
        artProcess.setId(ART_PROCESS_ID);
        artProcess.setDescription(DESCRIPTION);

        image = new Image();
        image.setPublicId(IMAGE_PUBLIC_ID);
        image.setUrl(IMAGE_URL);
        image.setModerationStatus(MODERATION_STATUS_APPROVED);

        artProcessImage = new ArtProcessImage();
        artProcessImage.setArtProcess(artProcess);
        artProcessImage.setImage(image);
        artProcessImage.setWidth(IMAGE_WIDTH);
        artProcessImage.setHeight(IMAGE_HEIGHT);
        artProcess.setArtProcessImage(artProcessImage);

        createRequestDto = new ArtProcessCreateRequestDto();
        createRequestDto.setDescription(DESCRIPTION);
        FullImageCreateRequestDto imageDto = new FullImageCreateRequestDto();
        imageDto.setPublicId(IMAGE_PUBLIC_ID);
        imageDto.setWidth(IMAGE_WIDTH);
        imageDto.setHeight(IMAGE_HEIGHT);
        imageDto.setSignature(SIGNATURE);
        imageDto.setVersion(VERSION);
        imageDto.setModerationStatus(MODERATION_STATUS_APPROVED.toString());
        createRequestDto.setImage(imageDto);

        updateRequestDto = new ArtProcessUpdateRequestDto();
        updateRequestDto.setDescription(UPDATED_DESCRIPTION);

        updateImageDto = new FullImageUpdateRequestDto();
        updateImageDto.setVersion(VERSION);
        updateImageDto.setWidth(WIDTH_NEW);
        updateImageDto.setHeight(HEIGHT_NEW);
        updateImageDto.setModerationStatus("APPROVED");

        updateRequestDto.setImage(updateImageDto);

        when(cloudinaryClient.verifySignature(anyString(), anyString(), anyString())).thenReturn(true);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(cloudinaryClient, imageTransformation);
    }

    @Test
    @DisplayName("toDto - Successfully transforms ArtProcess to DTO")
    @Order(10)
    void testToDto_Success() {
        // Act
        ArtProcessResponseDto dto = artProcessMapper.toDto(artProcess);

        // Assert
        assertNotNull(dto);
        assertEquals(ART_PROCESS_ID, dto.getId());
        assertEquals(DESCRIPTION, dto.getDescription());
        assertEquals(IMAGE_URL, dto.getArtProcessImageImageUrl());
    }

    @Test
    @DisplayName("toModel - Throws CloudinaryCredentialException when signature is invalid")
    @Order(20)
    void testToModel_InvalidSignature() {
        // Arrange
        when(cloudinaryClient.verifySignature(anyString(), anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(CloudinaryCredentialException.class, () -> artProcessMapper.toModel(createRequestDto));
    }

    @Test
    @DisplayName("toDto - Uses fallback when ModerationStatus is not APPROVED")
    @Order(30)
    void testToDto_NotApprovedModerationStatus() {
        // Arrange
        artProcess.getArtProcessImage().getImage().setModerationStatus(MODERATION_STATUS_REJECTED);

        // Act
        ArtProcessResponseDto dto = artProcessMapper.toDto(artProcess);

        // Assert
        assertNotNull(dto);
        assertEquals(ModerationMockImage.IMAGE_WIDTH, dto.getArtProcessImageWidth());
        assertEquals(ModerationMockImage.IMAGE_HEIGHT, dto.getArtProcessImageHeight());
        assertEquals(ModerationMockImage.PENDING_URL, dto.getArtProcessImageImageUrl());
    }

    @Test
    @DisplayName("toModel - Successfully creates ArtProcess from DTO")
    @Order(40)
    void testToModel_Success() {
        // Act
        ArtProcess result = artProcessMapper.toModel(createRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(DESCRIPTION.trim(), result.getDescription());
        assertEquals(IMAGE_PUBLIC_ID, result.getArtProcessImage().getImage().getPublicId());
    }

    @Test
    @DisplayName("toModel Update - Throws CloudinaryCredentialException when public ID matches but signature is invalid")
    @Order(50)
    void testToUpdateModel_PublicIdMatchSignatureInvalid() {
        // Arrange
        updateImageDto.setPublicId(IMAGE_PUBLIC_ID);
        updateImageDto.setSignature(SIGNATURE_INVALID);
        Mockito.when(cloudinaryClient.verifySignature(
                IMAGE_PUBLIC_ID, VERSION, SIGNATURE_INVALID)).thenReturn(false);

        // Act & Assert
        assertThrows(CloudinaryCredentialException.class,
                () -> artProcessMapper.toModel(updateRequestDto, artProcess),
                "Should throw CloudinaryCredentialException when the public ID matches but the signature verification fails.");
    }

    @Test
    @DisplayName("toModel Update - Successfully updates ArtProcess")
    @Order(60)
    void testToUpdateModel_Success() {
        // Arrange
        updateImageDto.setPublicId(IMAGE_PUBLIC_ID_NEW);
        updateImageDto.setSignature(SIGNATURE);

        // Act
        ArtProcess updatedArtProcess = artProcessMapper.toModel(updateRequestDto, artProcess);

        // Assert
        assertNotNull(updatedArtProcess, "ArtProcess should not be null after update.");
        assertEquals(UPDATED_DESCRIPTION, updatedArtProcess.getDescription(),
                "Expected description to be updated.");
        assertEquals(IMAGE_PUBLIC_ID_NEW, updatedArtProcess.getArtProcessImage().getImage().getPublicId(),
                "Expected public ID to be updated.");
        assertEquals(WIDTH_NEW, updatedArtProcess.getArtProcessImage().getWidth(),
                "Expected width to be updated.");
        assertEquals(HEIGHT_NEW, updatedArtProcess.getArtProcessImage().getHeight(),
                "Expected height to be updated.");
    }
}