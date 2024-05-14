package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SubjectService;
import com.example.artvswar.service.SupportService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaintingMapperTest {

    @Autowired
    private PaintingMapper paintingMapper;

    @MockBean
    private PaintingImageMapper paintingImageMapper;

    @MockBean
    private RoomViewManager roomViewManager;

    @MockBean
    private StyleService styleService;

    @MockBean
    private MediumService mediumService;

    @MockBean
    private SupportService supportService;

    @MockBean
    private SubjectService subjectService;

    @MockBean
    private CloudinaryClient cloudinaryClient;

    private Painting painting;
    private Author author;

    private static final Long PAINTING_ID = 1L;
    private static final String PAINTING_TITLE = "Starry Night";
    private static final String PAINTING_DESCRIPTION = "A famous starry night painting.";
    private static final int PAINTING_YEAR = 1889;
    private static final BigDecimal PAINTING_PRICE = new BigDecimal("100000.00");
    private static final Long AUTHOR_ID = 1L;
    private static final String AUTHOR_FULL_NAME = "Van Gogh";
    private static final String AUTHOR_PRETTY_ID = "van-gogh";
    private static final String AUTHOR_COUNTRY = "Netherlands";

    @BeforeEach
    public void setUp() {
        author = new Author();
        author.setId(AUTHOR_ID);
        author.setFullName(AUTHOR_FULL_NAME);
        author.setPrettyId(AUTHOR_PRETTY_ID);
        author.setCountry(AUTHOR_COUNTRY);

        painting = new Painting();
        painting.setId(PAINTING_ID);
        painting.setTitle(PAINTING_TITLE);
        painting.setDescription(PAINTING_DESCRIPTION);
        painting.setYearOfCreation(PAINTING_YEAR);
        painting.setPrice(PAINTING_PRICE);
        painting.setAuthor(author);

        PaintingResponseDto responseDto = new PaintingResponseDto();
        responseDto.setId(painting.getId());
        responseDto.setTitle(painting.getTitle());
        responseDto.setDescription(painting.getDescription());
        responseDto.setYearOfCreation(painting.getYearOfCreation());
        responseDto.setPrice(painting.getPrice());
    }

    @Test
    @Order(10)
    @DisplayName("toPaintingResponseDto - Successfully converts Painting to PaintingResponseDto")
    void testToPaintingResponseDto_Success() {
        // Arrange
        when(paintingImageMapper.toImageResponseDto(any(), any())).thenReturn(new PaintingImageResponseDto());

        // Act
        PaintingResponseDto dto = paintingMapper.toPaintingResponseDto(painting);

        // Assert
        assertNotNull(dto, "The DTO should not be null");
        assertEquals(PAINTING_ID, dto.getId(), "The painting ID should match");
        assertEquals(PAINTING_TITLE, dto.getTitle(), "The painting title should match");
        assertEquals(PAINTING_DESCRIPTION, dto.getDescription(), "The painting description should match");
        assertEquals(PAINTING_YEAR, dto.getYearOfCreation(), "The painting year of creation should match");
        assertEquals(PAINTING_PRICE, dto.getPrice(), "The painting price should match");
    }

    @Test
    @Order(11)
    @DisplayName("toPaintingResponseDto - Handles null Author gracefully")
    void testToPaintingResponseDto_NullAuthor() {
        // Arrange
        painting.setAuthor(null); // Explicitly setting the author to null

        // Act
        PaintingResponseDto dto = paintingMapper.toPaintingResponseDto(painting);

        // Assert
        assertNotNull(dto, "The DTO should not be null");
        assertNull(dto.getAuthor().getId(), "Author ID should be null");
        assertNull(dto.getAuthor().getPrettyId(), "Author Pretty ID should be null");
        assertNull(dto.getAuthor().getFullName(), "Author full name should be null");
        assertNull(dto.getAuthor().getCountry(), "Author country should be null");
    }

    @Test
    @Order(20)
    @DisplayName("toPaintingProfileResponseDto - Successfully converts Painting to PaintingProfileResponseDto")
    void testToPaintingProfileResponseDto_Success() {
        // Arrange
        when(paintingImageMapper.toImageResponseDto(any(), any())).thenReturn(new PaintingImageResponseDto());

        // Act
        PaintingProfileResponseDto dto = paintingMapper.toPaintingProfileResponseDto(painting);

        // Assert
        assertNotNull(dto, "The DTO should not be null");
        assertEquals(PAINTING_ID, dto.getId(), "The painting ID should match");
        assertEquals(PAINTING_TITLE, dto.getTitle(), "The painting title should match");
        assertEquals(PAINTING_DESCRIPTION, dto.getDescription(), "The painting description should match");
        assertEquals(PAINTING_YEAR, dto.getYearOfCreation(), "The painting year of creation should match");
        assertEquals(PAINTING_PRICE, dto.getPrice(), "The painting price should match");
    }

    @Test
    @Order(21)
    @DisplayName("toPaintingProfileResponseDto - throw exception if Author is null")
    void testToPaintingProfileResponseDto_NullAuthor() {
        // Arrange
        painting.setAuthor(null); // Set the author to null

        // Assert
        assertThrows(AppEntityNotFoundException.class, () -> paintingMapper.toPaintingProfileResponseDto(painting),
                "Should throw exception if painting don't have an author");
    }

    @Test
    @Order(30)
    @DisplayName("toPaintingModel - Successfully converts PaintingCreateRequestDto to Painting model")
    void testToPaintingModel_Success() {
        // Arrange
        PaintingCreateRequestDto createDto = new PaintingCreateRequestDto();
        createDto.setTitle("New Painting");
        createDto.setPrice(new BigDecimal("200000.00"));
        createDto.setYearOfCreation(2020);

        // Act
        Painting model = paintingMapper.toPaintingModel(createDto);

        // Assert
        assertNotNull(model, "The model should not be null");
        assertEquals("New Painting", model.getTitle().trim(), "The model title should match");
        assertEquals(new BigDecimal("200000.00"), model.getPrice(), "The model price should match");
        assertEquals(2020, model.getYearOfCreation(), "The model year of creation should match");
    }

    @Test
    @Order(40)
    @DisplayName("updatePaintingModel - Successfully updates existing Painting model from PaintingUpdateRequestDto")
    void testUpdatePaintingModel_Success() {
        // Arrange
        PaintingUpdateRequestDto updateDto = new PaintingUpdateRequestDto();
        updateDto.setTitle("Updated Painting");
        updateDto.setPrice(new BigDecimal("150000.00"));
        updateDto.setYearOfCreation(2021);

        // Act
        Painting updatedModel = paintingMapper.updatePaintingModel(updateDto, painting);

        // Assert
        assertNotNull(updatedModel, "The updated model should not be null");
        assertEquals("Updated Painting", updatedModel.getTitle(), "The updated model's title should match");
        assertEquals(new BigDecimal("150000.00"), updatedModel.getPrice(), "The updated model's price should match");
        assertEquals(2021, updatedModel.getYearOfCreation(), "The updated model's year of creation should match");
    }

    @Test
    @Order(41)
    @DisplayName("updatePaintingModel - Handles null image DTO gracefully")
    void testUpdatePaintingModel_NullImageDto() {
        // Arrange
        Painting paintingFromDB = new Painting(); // set up a dummy painting
        PaintingUpdateRequestDto updateDto = new PaintingUpdateRequestDto();
        updateDto.setImage(null); // explicitly setting image DTO to null

        // Act
        Painting updatedPainting = paintingMapper.updatePaintingModel(updateDto, paintingFromDB);

        // Assert
        assertNotNull(updatedPainting, "Updated painting should not be null");
        // further asserts can validate that no operations related to image processing were attempted
    }

    @Test
    @Order(42)
    @DisplayName("updatePaintingModel - Handles null width and height gracefully")
    void testUpdatePaintingModel_NullWidthHeight() {
        // Arrange
        Painting paintingFromDB = new Painting(); // Initialize with some default values
        paintingFromDB.setWidth(100.0);
        paintingFromDB.setHeight(200.0);

        PaintingUpdateRequestDto updateDto = new PaintingUpdateRequestDto();
        updateDto.setWidth(null);
        updateDto.setHeight(null);

        // Act
        Painting updatedPainting = paintingMapper.updatePaintingModel(updateDto, paintingFromDB);

        // Assert
        assertNotNull(updatedPainting, "Updated painting should not be null");
        assertEquals(100.0, updatedPainting.getWidth(), "Width should remain unchanged");
        assertEquals(200.0, updatedPainting.getHeight(), "Height should remain unchanged");
    }

    @Test
    @DisplayName("updatePaintingModel - Handles null style IDs gracefully")
    void testUpdatePaintingModel_NullStyleIds() {
        // Arrange
        Painting paintingFromDB = new Painting(); // Setup initial painting with styles
        paintingFromDB.addStyle(new Style());

        PaintingUpdateRequestDto updateDto = new PaintingUpdateRequestDto();
        updateDto.setStyleIds(null); // Explicitly setting style IDs to null

        // Act
        Painting updatedPainting = paintingMapper.updatePaintingModel(updateDto, paintingFromDB);

        // Assert
        assertNotNull(updatedPainting, "Updated painting should not be null");
        assertTrue(updatedPainting.getStyles().isEmpty(), "Styles should be cleared if input style IDs are null");
    }
}