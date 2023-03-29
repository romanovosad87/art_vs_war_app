package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorResponseDto toAuthorResponseDto(Author author) {
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        return dto;
    }

    public Author toAuthorModel(AuthorRequestDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setCountry(dto.getCountry());
        author.setCity(dto.getCity());
        author.setShortStory(dto.getShortStory());
        return author;
    }
}
