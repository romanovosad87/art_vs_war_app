package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toAuthorModel(AuthorCreateRequestDto dto) {
        Author author = new Author();
        author.setFullName(dto.getFullName());
        author.setCountry(dto.getCountry());
        author.setCity(dto.getCity());
        author.setAboutMe(dto.getAboutMe());
        author.setImageFileName(dto.getImageFileName());
        author.setEmail(dto.getEmail());
        return author;
    }

    public Author toAuthorModel(AuthorUpdateRequestDto dto) {
        Author author = new Author();
        author.setFullName(dto.getFullName());
        author.setCountry(dto.getCountry());
        author.setCity(dto.getCity());
        author.setAboutMe(dto.getAboutMe());
        author.setImageFileName(dto.getImageFileName());
        author.setEmail(dto.getEmail());
        return author;
    }
}
