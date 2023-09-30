package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.author.AuthorProfileResponseDto;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.repository.author.AuthorRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.AwsCognitoClient;
import com.example.artvswar.util.CloudinaryFolderCreator;
import com.example.artvswar.util.PrettyIdCreator;
import com.example.artvswar.util.UrlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {
    private final static String ROLE_AUTHOR = "ROLE_AUTHOR";
    private final AuthorRepository authorRepository;
    private final AwsCognitoClient awsCognitoClient;
    private final AuthorMapper authorMapper;
    private final PrettyIdCreator prettyIdCreator;
    private final UrlParser urlParser;
    private final CloudinaryFolderCreator cloudinaryFolderCreator;

    @Override
    @Transactional
    public AuthorProfileResponseDto save(AuthorCreateRequestDto dto, String cognitoSubject, String cognitoUsername) {
        Author author = authorMapper.toAuthorModel(dto);
        author.setCognitoSubject(cognitoSubject);
        author.setCognitoUsername(cognitoUsername);
        author.setPrettyId(createPrettyId(dto.getFullName()));
        Author savedUser = authorRepository.save(author);
        awsCognitoClient.addUserToGroup(cognitoUsername, ROLE_AUTHOR);
        return authorMapper.toDto(savedUser);
    }


    @Override
    @Transactional
    public AuthorProfileResponseDto update(AuthorUpdateRequestDto dto, String cognitoSubject) {
        Author authorFromDb = authorRepository.findByCognitoSubject(Author.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find author by cognito subject = %s", cognitoSubject)));
        if (!dto.getFullName().equals(authorFromDb.getFullName())) {
            authorFromDb.setPrettyId(createPrettyId(dto.getFullName()));
        }
        Author author = authorMapper.updateAuthorModel(dto, authorFromDb);
        return authorMapper.toDto(author);
    }

    @Override
    public Author getAuthorByCognitoSubject(String cognitoSubject) {
        return authorRepository.findByCognitoSubject(Author.class, cognitoSubject).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find author by cognito subject = %s", cognitoSubject)));
    }

    @Override
    public AuthorResponseDto getDtoByCognitoSubjectWithStyles(String cognitoSubject) {
        AuthorResponseDto dto = authorRepository.findByCognitoSubject(AuthorResponseDto.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find author by cognito subject = %s", cognitoSubject)));
        return transformWithStyles(dto);
    }

    @Override
    public AuthorProfileResponseDto getAuthorProfileDtoByCognitoSubject(String cognitoSubject) {
        AuthorProfileResponseDto dto = authorRepository.findByCognitoSubject(
                AuthorProfileResponseDto.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find author by cognito subject = %s", cognitoSubject)));
        return transformWithStyles(dto);
    }

    @Override
    public AuthorResponseDto getDtoByPrettyIdWithStyles(String prettyId) {
        AuthorResponseDto dto = authorRepository.findByPrettyId(AuthorResponseDto.class, prettyId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find author by prettyId = %s", prettyId)));
        return transformWithStyles(dto);
    }

    @Override
    public Author getReference(Long id) {
        return authorRepository.getReferenceById(id);
    }


    @Override
    public Page<AuthorResponseDto> getAll(Map<String, String> params, Pageable pageable) {
        Specification<Author> authorSpecification = urlParser.getAuthorSpecification(params);
        Page<AuthorResponseDto> authorsDtos = authorRepository
                .findAllBySpecification(authorSpecification, pageable);
        return transformWithStyles(authorsDtos);
    }


    @Override
    public long getNumberOfAllAuthors() {
        return authorRepository.count();
    }

    @Override
    public String createPrettyId(String fullName) {
        String proposedPrettyId = prettyIdCreator.create(fullName);
        int repentance = authorRepository.checkIfExistPrettyId(proposedPrettyId);
        if (repentance == 0) {
            return proposedPrettyId;
        } else {
            return proposedPrettyId + "-" + new Random().nextInt(100000);
        }
    }

    @Override
    public FolderResponseDto createCloudinaryFolder(String cognitoSubject) {
        return cloudinaryFolderCreator.createForAuthorPhoto(cognitoSubject);
    }

    @Override
    @Transactional
    public void delete(String cognitoSubject, Jwt jwt) {
        authorRepository.deleteByCognitoSubject(cognitoSubject);
        awsCognitoClient.deleteUser(jwt);
    }

    private Page<AuthorResponseDto> transformWithStyles(Page<AuthorResponseDto> authorsDtos) {
        List<String> authorsCognitoSubjects = authorsDtos.stream()
                .map(AuthorResponseDto::getCognitoSubject)
                .collect(Collectors.toList());

        Map<String, Set<String>> authorsAllStyleMap = authorRepository
                .authorsAllStyleMap(authorsCognitoSubjects);

        authorsDtos.forEach(dto -> {
            Set<String> styles = authorsAllStyleMap.get(dto.getCognitoSubject());
            dto.setStyles(Objects.requireNonNullElseGet(styles, HashSet::new));
        });
        return authorsDtos;
    }

    private AuthorResponseDto transformWithStyles(AuthorResponseDto dto) {
        Map<String, Set<String>> authorsAllStyleMap = authorRepository
                .authorsAllStyleMap(Collections.singletonList(dto.getCognitoSubject()));
        Set<String> styles = authorsAllStyleMap.get(dto.getCognitoSubject());
        dto.setStyles(Objects.requireNonNullElseGet(styles, HashSet::new));
        return dto;
    }

    private AuthorProfileResponseDto transformWithStyles(AuthorProfileResponseDto dto) {
        Map<String, Set<String>> authorsAllStyleMap = authorRepository
                .authorsAllStyleMap(Collections.singletonList(dto.getCognitoSubject()));
        Set<String> styles = authorsAllStyleMap.get(dto.getCognitoSubject());
        dto.setStyles(Objects.requireNonNullElseGet(styles, HashSet::new));
        return dto;
    }
}
