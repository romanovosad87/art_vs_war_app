package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
    private final ImageService imageService;
    public AuthorResponseDto toAuthorResponseDto(Author author) {
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.setId(author.getId());
        dto.setFullName(author.getFullName());
        dto.setCity(author.getCity());
        dto.setCountry(author.getCountry());
        dto.setAboutMe(author.getAboutMe());
        dto.setPhotoUrl(imageService.generateGetUrl(author.getImageFileName()));
        return dto;
    }

    public Author toAuthorModel(AuthorRequestDto dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setFullName(dto.getFullName());
        author.setCountry(dto.getCountry());
        author.setCity(dto.getCity());
        author.setAboutMe(dto.getAboutMe());
        author.setImageFileName(dto.getImageFileName());
        return author;
    }
}
