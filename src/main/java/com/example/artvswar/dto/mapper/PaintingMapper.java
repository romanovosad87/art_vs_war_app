package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaintingMapper {
    private final ImageService imageService;
    private final StyleService styleService;
    private final MediumService mediumService;
    private final SupportService supportService;

    public PaintingResponseDto toPaintingResponseDto(Painting painting) {
        PaintingResponseDto dto = new PaintingResponseDto();
        dto.setId(painting.getId());
        dto.setTitle(painting.getTitle());
        dto.setDescription(painting.getDescription());
        dto.setYearOfCreation(painting.getYearOfCreation());
        dto.setPrice(painting.getPrice());
        dto.setHeight(painting.getHeight());
        dto.setWidth(painting.getWidth());

        AuthorForPaintingResponseDto authorDto = new AuthorForPaintingResponseDto();
        authorDto.setId(painting.getAuthor().getId());
        authorDto.setFullName(painting.getAuthor().getFullName());
        dto.setAuthor(authorDto);

        StyleResponseDto styleResponseDto = new StyleResponseDto();
        styleResponseDto.setId(painting.getStyle().getId());
        styleResponseDto.setName(painting.getStyle().getName());
        dto.setStyle(styleResponseDto);

        MediumResponseDto mediumResponseDto = new MediumResponseDto();
        mediumResponseDto.setId(painting.getMedium().getId());
        mediumResponseDto.setName(painting.getMedium().getName());
        dto.setMedium(mediumResponseDto);

        SupportResponseDto supportResponseDto = new SupportResponseDto();
        supportResponseDto.setId(painting.getSupport().getId());
        supportResponseDto.setName(painting.getSupport().getName());
        dto.setSupport(supportResponseDto);

        dto.setImageUrl(imageService.generateGetUrl(painting.getImageFileName()));
        dto.setEntityCreatedAt(painting.getEntityCreatedAt());

        return dto;
    }

    public Painting toPaintingModel(PaintingRequestDto dto) {
        Painting painting = new Painting();

        painting.setTitle(dto.getTitle());
        painting.setPrice(dto.getPrice());
        painting.setYearOfCreation(dto.getYearOfCreation());
        painting.setHeight(dto.getHeight());
        painting.setWidth(dto.getWidth());
        painting.setDescription(dto.getDescription());
        painting.setImageFileName(dto.getImageFileName());
        painting.setStyle(styleService.getReferenceById(dto.getStyleId()));
        painting.setMedium(mediumService.getReferenceById(dto.getMediumId()));
        painting.setSupport(supportService.getReferenceById(dto.getSupportId()));

        return painting;
    }
}
