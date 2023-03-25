package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.PictureRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.dto.response.PictureResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Picture;
import com.example.artvswar.service.AuthorService;
import org.springframework.stereotype.Component;

@Component
public class PictureMapper {
    private final AuthorService authorService;

    public PictureMapper(AuthorService authorService) {
        this.authorService = authorService;
    }

    public PictureResponseDto toPictureResponseDto(Picture picture) {
        PictureResponseDto dto = new PictureResponseDto();
        dto.setId(picture.getId());
        dto.setPictureTitle(picture.getTitle());
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setId(picture.getAuthor().getId());
        authorResponseDto.setName(picture.getAuthor().getName());
        dto.setAuthorResponseDto(authorResponseDto);
  //      dto.setPictureUrl(picture.getPictureUrl());
        return dto;
    }

    public Picture toPictureModel(PictureRequestDto dto) {
        Picture picture = new Picture();
        picture.setTitle(dto.getPictureTitle());
        Author author = authorService.get(dto.getAuthorId());
        picture.setAuthor(author);
 //       picture.setPictureUrl(dto.getPictureUrl());
        return picture;
    }
}
