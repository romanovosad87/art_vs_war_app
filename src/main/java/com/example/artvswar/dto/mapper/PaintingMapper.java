package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import org.springframework.stereotype.Component;

@Component
public class PaintingMapper {
    private final AuthorService authorService;
    private final ImageService imageService;
    private final StyleService styleService;
    private final MediumService mediumService;

    public PaintingMapper(AuthorService authorService, ImageService imageService,
                          StyleService styleService, MediumService mediumService) {
        this.authorService = authorService;
        this.imageService = imageService;
        this.styleService = styleService;
        this.mediumService = mediumService;
    }

    public PaintingResponseDto toPaintingResponseDto(Painting painting) {
        PaintingResponseDto dto = new PaintingResponseDto();
        dto.setId(painting.getId());
        dto.setTitle(painting.getTitle());
        dto.setDescription(painting.getDescription());
        dto.setPrice(painting.getPrice());
        dto.setHeight(painting.getHeight());
        dto.setWidth(painting.getWidth());

        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setId(painting.getAuthor().getId());
        authorResponseDto.setName(painting.getAuthor().getName());
        authorResponseDto.setCountry(painting.getAuthor().getCountry());
        authorResponseDto.setCity(painting.getAuthor().getCity());
        authorResponseDto.setShortStory(painting.getAuthor().getShortStory());
        dto.setAuthor(authorResponseDto);

        StyleResponseDto styleResponseDto = new StyleResponseDto();
        styleResponseDto.setId(painting.getStyle().getId());
        styleResponseDto.setName(painting.getStyle().getName());
        dto.setStyle(styleResponseDto);

        MediumResponseDto mediumResponseDto = new MediumResponseDto();
        mediumResponseDto.setId(painting.getMedium().getId());
        mediumResponseDto.setName(painting.getMedium().getName());
        dto.setMedium(mediumResponseDto);

        dto.setImageUrl(imageService.generateGetUrl(painting.getImageFileName()));

        return dto;
    }

    public Painting toPaintingModel(PaintingRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getTitle());
        painting.setPrice(dto.getPrice());
        painting.setDescription(dto.getDescription());
        painting.setAuthor(authorService.get(dto.getAuthorId()));
        painting.setImageFileName(dto.getImageFileName());
        painting.setStyle(styleService.get(dto.getStyleId()));
        painting.setMedium(mediumService.get(dto.getMediumId()));
        painting.setHeight(dto.getHeight());
        painting.setWidth(dto.getWidth());
        return painting;
    }
}
