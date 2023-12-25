package com.example.artvswar.service;

import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.author.AuthorCheckStripeAndAddressPresenceResponseDto;
import com.example.artvswar.dto.response.author.AuthorProfileResponseDto;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface AuthorService {
    AuthorProfileResponseDto save(AuthorCreateRequestDto dto, String cognitoSubject,
                String cognitoUsername);

    AuthorProfileResponseDto update(AuthorUpdateRequestDto dto, String cognitoSubject);

    Author getAuthorByCognitoSubject(String cognitoSubject);

    AuthorResponseDto getDtoByCognitoSubjectWithStyles(String cognitoSubject);
    AuthorProfileResponseDto getAuthorProfileDtoByCognitoSubject(String cognitoSubject);

    AuthorResponseDto getDtoByPrettyIdWithStyles(String prettyId);

    Author getReference(Long id);

    Page<AuthorResponseDto> getAll(Map<String, String> params, Pageable pageable);

    long getNumberOfAllAuthors();

    String createPrettyId(String fullName);
    FolderResponseDto createCloudinaryFolder(String cognitoSubject);

    void delete(String cognitoSubject);

    AuthorCheckStripeAndAddressPresenceResponseDto checkAuthorProfile(String authorCognitoSubject);

    void changeUnsubscribeEmail(String authorSubject, boolean unsubscribe);
}
