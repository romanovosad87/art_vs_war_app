package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.request.image.ImageUpdateRequestDto;
import com.example.artvswar.dto.response.author.AuthorProfileResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorPhoto;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enummodel.ModerationStatus;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Order(330)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorMapperTest {

    @Autowired
    private AuthorMapper authorMapper;

    @MockBean
    private CloudinaryClient cloudinaryClient;

    @MockBean
    private ImageTransformation imageTransformation;

    private Author existingAuthor;
    private AuthorCreateRequestDto createRequestDto;
    private AuthorPhoto authorPhoto;
    private AuthorUpdateRequestDto updateRequestDto;
    private Image image;
    private ImageCreateRequestDto imageDto;
    private ImageUpdateRequestDto imageUpdateRequestDto;

    private static final String FULL_NAME = "John Doe";
    private static final String FULL_NAME_UPDATED = "Paul McCartney";
    private static final String COUNTRY = "USA";
    private static final String COUNTRY_UPDATED = "United Kingdom";
    private static final String CITY = "New York";
    private static final String CITY_UPDATED = "Liverpool";
    private static final String ABOUT_ME = "About John Doe";
    private static final String ABOUT_ME_UPDATED = "About Paul McCartney";
    private static final String EMAIL = "john.doe@example.com";
    private static final String IMAGE_URL = "http://example.com/image.jpg";
    private static final String IMAGE_URL_UPDATED = "http://example.com/updated_photo.jpg";
    private static final String PUBLIC_ID = "publicId123";
    private static final String PUBLIC_ID_UPDATED = "updatedId123";
    private static final String SIGNATURE = "validSignature";
    private static final String SIGNATURE_INVALID = "invalidSignature";
    private static final String VERSION = "1";
    private static final ModerationStatus MODERATION_STATUS_APPROVED = ModerationStatus.APPROVED;

    @BeforeEach
    public void setUp() {
        createRequestDto = new AuthorCreateRequestDto();
        createRequestDto.setFullName(FULL_NAME);
        createRequestDto.setCountry(COUNTRY);
        createRequestDto.setCity(CITY);
        createRequestDto.setAboutMe(ABOUT_ME);
        createRequestDto.setEmail(EMAIL);

        imageDto = new ImageCreateRequestDto();
        imageDto.setPublicId(PUBLIC_ID);
        imageDto.setSignature(SIGNATURE);
        imageDto.setVersion(VERSION);
        imageDto.setModerationStatus(MODERATION_STATUS_APPROVED.toString());
        createRequestDto.setImage(imageDto);

        existingAuthor = new Author();
        existingAuthor.setFullName(FULL_NAME);
        existingAuthor.setCountry(COUNTRY);
        existingAuthor.setCity(CITY);
        existingAuthor.setAboutMe(ABOUT_ME);
        existingAuthor.setEmail(EMAIL);

        authorPhoto = new AuthorPhoto();

        image = new Image();
        image.setPublicId(PUBLIC_ID);
        image.setUrl(IMAGE_URL);
        image.setModerationStatus(ModerationStatus.APPROVED);
        authorPhoto.setImage(image);
        existingAuthor.setAuthorPhoto(authorPhoto);

        updateRequestDto = new AuthorUpdateRequestDto();
        updateRequestDto.setFullName(FULL_NAME_UPDATED);
        updateRequestDto.setCountry(COUNTRY_UPDATED);
        updateRequestDto.setCity(CITY_UPDATED);
        updateRequestDto.setAboutMe(ABOUT_ME_UPDATED);
        updateRequestDto.setDeactivated(false);

        imageUpdateRequestDto = new ImageUpdateRequestDto();
        imageUpdateRequestDto.setPublicId(PUBLIC_ID_UPDATED);
        imageUpdateRequestDto.setModerationStatus(MODERATION_STATUS_APPROVED.toString());
        imageUpdateRequestDto.setVersion(VERSION);
        imageUpdateRequestDto.setSignature(SIGNATURE);

        updateRequestDto.setImage(imageUpdateRequestDto);

        when(imageTransformation.photoImageEagerTransformation(PUBLIC_ID))
                .thenReturn(IMAGE_URL_UPDATED);
        when(cloudinaryClient.verifySignature(anyString(), anyString(), anyString())).thenReturn(true);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(cloudinaryClient, imageTransformation);
    }

    @Test
    @DisplayName("toAuthorModel - Successfully creates Author model from DTO")
    @Order(10)
    void testToAuthorModel_Success() {
        // Act
        Author result = authorMapper.toAuthorModel(createRequestDto);

        // Assert
        assertNotNull(result, "The result should not be null");

        assertEquals(FULL_NAME, result.getFullName(),
                "The full name of the author should match the expected value");
        assertEquals(COUNTRY, result.getCountry(),
                "The country of the author should match the expected value");
        assertEquals(CITY, result.getCity(), "The city of the author should match the expected value");
        assertEquals(ABOUT_ME, result.getAboutMe(),
                "The about me text of the author should match the expected value");
        assertEquals(EMAIL, result.getEmail(), "The email of the author should match the expected value");

        assertNotNull(result.getAuthorPhoto(), "The author photo should not be null");
        assertEquals(PUBLIC_ID, result.getAuthorPhoto().getImage().getPublicId(),
                "The public ID of the author's photo should match the expected value");
        assertEquals(IMAGE_URL_UPDATED, result.getAuthorPhoto().getImage().getUrl(),
                "The URL of the author's photo should match the transformed photo URL");
    }

    @Test
    @DisplayName("toAuthorModel - Throws CloudinaryCredentialException when signature is invalid")
    @Order(20)
    void testToAuthorModel_InvalidSignature() {
        // Arrange
        when(cloudinaryClient.verifySignature(PUBLIC_ID, VERSION, SIGNATURE)).thenReturn(false);

        // Act & Assert
        assertThrows(CloudinaryCredentialException.class, () -> authorMapper.toAuthorModel(createRequestDto),
                "Should throw CloudinaryCredentialException when the signature verification" +
                        " fails due to invalid credentials");
    }

    @Test
    @DisplayName("updateAuthorModel - Successfully updates Author model")
    @Order(30)
    void testUpdateAuthorModel_Success() {
        // Act
        Author updatedAuthor = authorMapper.updateAuthorModel(updateRequestDto, existingAuthor);

        // Assert
        assertNotNull(updatedAuthor, "The updated author object should not be null.");

        assertEquals(FULL_NAME_UPDATED, updatedAuthor.getFullName(),
                "The updated author's full name should match the expected value.");
        assertEquals(COUNTRY_UPDATED, updatedAuthor.getCountry(),
                "The updated author's country should match the expected value.");
        assertEquals(CITY_UPDATED, updatedAuthor.getCity(),
                "The updated author's city should match the expected value.");
        assertEquals(ABOUT_ME_UPDATED, updatedAuthor.getAboutMe(),
                "The updated author's about me description should match the expected value.");

        assertFalse(updatedAuthor.isDeleted(), "The author should not be marked as deleted after the update.");
    }

    @Test
    @DisplayName("toDto - Successfully transforms Author to DTO")
    @Order(40)
    void testToDto_Success() {
        // Act
        AuthorProfileResponseDto dto = authorMapper.toDto(existingAuthor);

        // Assert
        assertNotNull(dto, "The DTO should not be null.");

        assertEquals(FULL_NAME, dto.getFullName(),
                "Expected DTO's full name to match the input full name.");
        assertEquals(COUNTRY, dto.getCountry(),
                "Expected DTO's country to match the input country.");
        assertEquals(CITY, dto.getCity(),
                "Expected DTO's city to match the input city.");
        assertEquals(ABOUT_ME, dto.getAboutMe(),
                "Expected DTO's about me text to match the input about me text.");

        assertEquals(PUBLIC_ID, dto.getAuthorPhotoImagePublicId(),
                "Expected DTO's author photo public ID to match the input public ID.");
        assertEquals(IMAGE_URL, dto.getAuthorPhotoImageUrl(),
                "Expected DTO's author photo URL to match the input photo URL.");
    }

    @Test
    @DisplayName("updateAuthorModel - Successfully updates Author image when public ID changes and signature is valid")
    @Order(50)
    void testUpdateAuthorModel_ImageUpdateSuccess() {
        // Arrange
        when(cloudinaryClient.verifySignature(PUBLIC_ID_UPDATED, VERSION, SIGNATURE)).thenReturn(true);
        when(imageTransformation.photoImageEagerTransformation(PUBLIC_ID_UPDATED)).thenReturn(IMAGE_URL_UPDATED);

        // Act
        Author updatedAuthor = authorMapper.updateAuthorModel(updateRequestDto, existingAuthor);

        // Assert
        assertNotNull(updatedAuthor, "The updated author should not be null after processing.");
        assertEquals(PUBLIC_ID_UPDATED, updatedAuthor.getAuthorPhoto().getImage().getPublicId(),
                "The public ID of the author's photo should be updated to the new public ID.");
        assertEquals(IMAGE_URL_UPDATED, updatedAuthor.getAuthorPhoto().getImage().getUrl(),
                "The URL of the author's photo should reflect the updated transformation result.");
        verify(cloudinaryClient).delete(PUBLIC_ID);
    }

    @Test
    @DisplayName("updateAuthorModel - Throws CloudinaryCredentialException when public ID changes and signature is invalid")
    @Order(60)
    void testUpdateAuthorModel_InvalidSignatureThrowsException() {
        // Arrange
        ImageUpdateRequestDto imageDto = new ImageUpdateRequestDto();
        imageDto.setPublicId(PUBLIC_ID_UPDATED);
        imageDto.setSignature(SIGNATURE_INVALID);
        imageDto.setVersion(VERSION);
        imageDto.setModerationStatus(MODERATION_STATUS_APPROVED.toString());

        updateRequestDto.setImage(imageDto);

        when(cloudinaryClient.verifySignature(PUBLIC_ID_UPDATED, VERSION, SIGNATURE_INVALID)).thenReturn(false);

        // Act & Assert
        assertThrows(CloudinaryCredentialException.class,
                () -> authorMapper.updateAuthorModel(updateRequestDto, existingAuthor),
                "Should throw CloudinaryCredentialException when the signature is invalid.");
    }
}