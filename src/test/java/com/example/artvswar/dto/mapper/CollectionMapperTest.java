package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.collection.CollectionCreateUpdateRequestDto;
import com.example.artvswar.dto.response.collection.CollectionShortResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Order(350)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CollectionMapperTest {

    @Autowired
    private CollectionMapper collectionMapper;

    private CollectionCreateUpdateRequestDto createUpdateDto;
    private Collection collection;
    private static final String TITLE = "Art Collection";
    private static final String DESCRIPTION = "A collection of modern art pieces.";
    private static final String UPDATED_TITLE = "Updated Art Collection";
    private static final String UPDATED_DESCRIPTION = "An updated collection of modern and contemporary art pieces.";

    @BeforeEach
    public void setUp() {
        createUpdateDto = new CollectionCreateUpdateRequestDto();
        createUpdateDto.setTitle(TITLE);
        createUpdateDto.setDescription(DESCRIPTION);

        collection = new Collection();
        collection.setId(1L);
        collection.setPrettyId("coll-001");
        collection.setTitle(TITLE);
        collection.setDescription(DESCRIPTION);
        Author author = new Author(); // Assuming an Author class with required fields
        author.setFullName("John Doe");
        author.setPrettyId("auth-001");
        collection.setAuthor(author);
    }

    @Test
    @Order(10)
    @DisplayName("toModel - Successfully creates Collection model from DTO")
    void testToModel_Success() {
        // Act
        Collection result = collectionMapper.toModel(createUpdateDto);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(TITLE, result.getTitle(), "The title should match the expected value");
        assertEquals(DESCRIPTION, result.getDescription(),
                "The description should match the expected value");
    }

    @Test
    @Order(20)
    @DisplayName("toModelUpdate - Successfully updates Collection model from DTO")
    void testToModelUpdate_Success() {
        // Arrange
        CollectionCreateUpdateRequestDto updateDto = new CollectionCreateUpdateRequestDto();
        updateDto.setTitle(UPDATED_TITLE);
        updateDto.setDescription(UPDATED_DESCRIPTION);

        // Act
        Collection updatedModel = collectionMapper.toModel(collection, updateDto);

        // Assert
        assertNotNull(updatedModel, "The updated model should not be null");
        assertEquals(UPDATED_TITLE, updatedModel.getTitle(),
                "The updated title should match the expected value");
        assertEquals(UPDATED_DESCRIPTION, updatedModel.getDescription(),
                "The updated description should match the expected value");
    }

    @Test
    @Order(30)
    @DisplayName("toDto - Successfully transforms Collection to DTO")
    void testToDto_Success() {
        // Act
        CollectionShortResponseDto dto = collectionMapper.toDto(collection);

        // Assert
        assertNotNull(dto, "The DTO should not be null");
        assertEquals(collection.getId(), dto.getId(),
                "DTO's id should match the Collection's id");
        assertEquals(collection.getPrettyId(), dto.getPrettyId(),
                "DTO's pretty id should match the Collection's pretty id");
        assertEquals(TITLE, dto.getTitle(),
                "DTO's title should match the Collection's title");
        assertEquals(DESCRIPTION, dto.getDescription(),
                "DTO's description should match the Collection's description");
        assertEquals("John Doe", dto.getAuthorFullName(),
                "DTO's author full name should match");
        assertEquals("auth-001", dto.getAuthorPrettyId(),
                "DTO's author pretty id should match");
    }
}