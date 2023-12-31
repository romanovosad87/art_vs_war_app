package com.example.artvswar.controller;

import com.example.artvswar.dto.request.author.AuthorChangeUnsubscribeResponseDto;
import com.example.artvswar.dto.request.author.AuthorCheckInputDto;
import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.author.AuthorCheckStripeAndAddressPresenceResponseDto;
import com.example.artvswar.dto.response.author.AuthorProfileResponseDto;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private static final String SUBJECT = "sub";
    private static final String USERNAME = "username";
    private static final int DEFAULT_PAGE_SIZE = 8;
    public static final String CREATED_AT = "createdAt";
    private final AuthorService authorService;

    @GetMapping()
    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthorsByParams(
            @RequestParam Map<String, String> params,
            @SortDefault(value = CREATED_AT, direction = Sort.Direction.DESC)
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<AuthorResponseDto> allAuthors = authorService.getAll(params, pageable);
        return new ResponseEntity<>(allAuthors, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/profile")
    public ResponseEntity<AuthorProfileResponseDto> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        AuthorProfileResponseDto dto = authorService.getAuthorProfileDtoByCognitoSubject(subject);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{cognitoSubject}")
    public ResponseEntity<AuthorResponseDto> getByCognitoSubject(
            @PathVariable String cognitoSubject) {
        AuthorResponseDto authorResponseDto = authorService.getDtoByCognitoSubjectWithStyles(cognitoSubject);
        return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
    }

    @GetMapping("/v2/{prettyId}")
    public ResponseEntity<AuthorResponseDto> getByPrettyId(@PathVariable String prettyId) {
        AuthorResponseDto authorResponseDto = authorService.getDtoByPrettyIdWithStyles(prettyId);
        return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
    }

    @PostMapping("/checkInputAndGet")
    public FolderResponseDto checkInputParametersAndReturnCloudinaryFolder(
            @RequestBody @Valid AuthorCheckInputDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        return authorService.createCloudinaryFolder(subject);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorProfileResponseDto> create(
            @RequestBody @Valid AuthorCreateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        String username = jwt.getClaimAsString(USERNAME);
        AuthorProfileResponseDto responseDto = authorService.save(dto, subject, username);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<AuthorProfileResponseDto> update(
            @RequestBody @Valid AuthorUpdateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        AuthorProfileResponseDto responseDto = authorService.update(dto, subject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfAllAuthors() {
        return new ResponseEntity<>(authorService.getNumberOfAllAuthors(),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping
    public void delete(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        authorService.delete(subject);
    }

    @GetMapping("/check")
    public AuthorCheckStripeAndAddressPresenceResponseDto checkAuthorProfile(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        return authorService.checkAuthorProfile(subject);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PatchMapping("/unsubscribe")
    public ResponseEntity<Void> changeUnsubscribeEmail(@Valid @RequestBody
                                                    AuthorChangeUnsubscribeResponseDto dto,
                                                    @AuthenticationPrincipal Jwt jwt) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        authorService.changeUnsubscribeEmail(authorSubject, dto.isUnsubscribe());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
