package com.example.artvswar.service;

import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    Author save(Author author);

    Author update(Author author);
    Author getAuthorByCognitoUsername(String cognitoUsername);

    AuthorResponseDto getDtoByCognitoUsername(String cognitoUsername);

    Author getReference(String id);

    Page<AuthorResponseDto> getAll(Pageable pageable);

    long getNumberOfAllAuthors();
}
