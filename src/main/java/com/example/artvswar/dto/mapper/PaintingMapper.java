package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class PaintingMapper {
    private final AuthorService authorService;
    private final ImageService imageService;

    public PaintingMapper(AuthorService authorService, ImageService imageService) {
        this.authorService = authorService;
        this.imageService = imageService;
    }

    public PaintingResponseDto toPictureResponseDto(Painting painting) {
        PaintingResponseDto dto = new PaintingResponseDto();
        dto.setId(painting.getId());
        dto.setPaintingTitle(painting.getTitle());
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setId(painting.getAuthor().getId());
        authorResponseDto.setName(painting.getAuthor().getName());
        dto.setAuthor(authorResponseDto);
        dto.setPictureUrl(imageService.generateGetUrl(painting.getImageFileName()));
        return dto;
    }

    public Painting toPictureModel(PaintingRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getPaintingTitle());
        Author author = authorService.get(dto.getAuthorId());
        painting.setAuthor(author);
        painting.setImageFileName(dto.getImageFileName());
        return painting;
    }
}
