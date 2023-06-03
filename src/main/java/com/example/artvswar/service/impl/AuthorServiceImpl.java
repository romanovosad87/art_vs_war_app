package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.repository.AuthorRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.AwsCognitoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AwsCognitoClient awsCognitoClient;

    @Override
    @Transactional
    public Author save(Author author) {
        Author savedUser = authorRepository.save(author);
        awsCognitoClient.addUserToGroup(author.getCognitoUsername(), "ROLE_AUTHOR");
        return savedUser;
    }


    @Override
    @Transactional
    public Author update(Author author) {
       return authorRepository.save(author);
    }

    @Override
    public Author getAuthorByCognitoUsername(String cognitoUsername) {
        return authorRepository.findById(cognitoUsername).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find author by id = %s", cognitoUsername)));
    }

    @Override
    public AuthorResponseDto getDtoByCognitoUsername(String cognitoUsername) {
        return authorRepository.findByCognitoUsername(AuthorResponseDto.class, cognitoUsername).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find author by id = %s", cognitoUsername)));
    }

    @Override
    public Author getReference(String id) {
        return authorRepository.getReferenceById(id);
    }


    @Override
    public Page<AuthorResponseDto> getAll(Pageable pageable) {
        return authorRepository.findAllBy(AuthorResponseDto.class, pageable);
    }


    @Override
    public long getNumberOfAllAuthors() {
        return authorRepository.count();
    }
}
